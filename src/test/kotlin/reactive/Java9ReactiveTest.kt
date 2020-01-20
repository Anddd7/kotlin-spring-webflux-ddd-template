package reactive

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.Flow

internal class SimpleReactiveTest {
  @Test
  fun `should transfer data from publisher to subscriber`() {
    SimplePublisher(10).subscribe(SimpleSubscriber())
  }

  internal class SimplePublisher(count: Int) : Flow.Publisher<Int> {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val iterator = (1..count).iterator()

    override fun subscribe(subscriber: Flow.Subscriber<in Int>) {
      log.info("start subscribing and send to subscriber")
      iterator.forEachRemaining(subscriber::onNext)

      log.info("notify subscriber it's completed")
      subscriber.onComplete()
    }
  }

  internal class SimpleSubscriber : Flow.Subscriber<Int> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun onNext(item: Int) {
      log.info("onNext: $item")
    }

    override fun onComplete() {
      log.info("onComplete")
    }

    override fun onSubscribe(subscription: Flow.Subscription) {
      log.info("onSubscribe (won't print)")
    }

    override fun onError(throwable: Throwable) {
      log.error("onError", throwable)
    }
  }
}

internal class GraduateReactiveTest {
  @Test
  fun `should transfer data from publisher to subscriber`() {
    GraduatePublisher(10).subscribe(GraduateSubscriber(5))
  }

  @Test
  fun `should got error when subscribe empty element`() {
    GraduatePublisher(10).subscribe(GraduateSubscriber(0))
  }

  internal class GraduatePublisher(count: Int) : Flow.Publisher<Int> {
    private val iterator = (1..count).iterator()

    override fun subscribe(subscriber: Flow.Subscriber<in Int>) {
      val subscription = GraduateSubscription(iterator, subscriber)
      subscriber.onSubscribe(subscription)
    }
  }

  internal class GraduateSubscription(
      private val iterator: IntIterator,
      private val subscriber: Flow.Subscriber<in Int>
  ) : Flow.Subscription {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun cancel() {
      log.info("cancel")
    }

    override fun request(n: Long) {
      if (n < 1) {
        subscriber.onError(IllegalArgumentException())
      }

      var demand = n
      while ((--demand) > 0 && iterator.hasNext()) {
        subscriber.onNext(iterator.next())
      }

      if (!iterator.hasNext()) {
        subscriber.onComplete()
      }
    }
  }

  internal class GraduateSubscriber(private val demand: Long) : Flow.Subscriber<Int> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun onNext(item: Int) {
      log.info("onNext: $item")
    }

    override fun onComplete() {
      log.info("onComplete")
    }

    override fun onSubscribe(subscription: Flow.Subscription) {
      log.info("onSubscribe")
      subscription.request(demand)
    }

    override fun onError(throwable: Throwable) {
      log.error("onError", throwable)
    }
  }
}
