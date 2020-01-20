package reactive

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Duration

internal class ReactorTest {
  private val log = LoggerFactory.getLogger(this.javaClass)

  @Test
  fun `mono operations`() {
    Mono.fromCallable { 1 }
        .log()
        .map(Int::toString)
        .subscribe(log::info)
  }

  @Test
  fun `mono parallel`() {
    val delay: Long = 100
    val first = Mono.just(1).delayElement(Duration.ofMillis(delay)).log()
    val second = Mono.just(2).delayElement(Duration.ofMillis(delay)).log()

    Mono.zip(first, second) { f, s -> f + s }
        .map(Int::toString)
        .subscribeOn(Schedulers.parallel())
        .subscribe(log::info)

    log.info("finished")
    Thread.sleep(delay * 2)
  }


  @Test
  fun `flux operations`() {
    Flux.just(1, 2, 3, 4)
        .map(Int::toString)
        .subscribe(log::info)

    log.info("finished")
  }

  @Test
  fun `flux parallel`() {
    Flux.just(1, 2, 3, 4)
        .map(Int::toString)
        .subscribeOn(Schedulers.parallel())
        .subscribe(log::info)

    log.info("finished")
  }
}
