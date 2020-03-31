package com.github.anddd7.adapter.config

import com.github.anddd7.utils.CORRELATION_ID_KEY
import com.github.anddd7.utils.getOrNull
import com.github.anddd7.utils.logOnNext
import net.logstash.logback.argument.StructuredArguments.entries
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.builder
import reactor.core.publisher.Mono
import java.time.Duration.between
import java.time.LocalDateTime
import java.time.LocalDateTime.now

object WebClientFactory {
  fun build(baseUrl: String): WebClient =
      builder().filter(TracingFilterFunction).baseUrl(baseUrl).build()
}

object TracingFilterFunction : ExchangeFilterFunction {
  private val log = LoggerFactory.getLogger(this.javaClass)

  override fun filter(request: ClientRequest, next: ExchangeFunction) =
      Mono.subscriberContext().flatMap {
        val correlationId = it.getOrNull<String>(CORRELATION_ID_KEY)
        val requestWrapper = ClientRequest.from(request)
            .header(CORRELATION_ID_KEY, correlationId)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .build()
        val timestamp = now()

        next.exchange((requestWrapper))
            .logOnNext { response -> logResponse(request, response, timestamp) }
      }

  private fun logResponse(request: ClientRequest, response: ClientResponse, timestamp: LocalDateTime?) {
    val properties = mapOf(
        "method" to request.method(),
        "uri" to request.url(),
        "protocol" to request.url().scheme,
        "status" to response.rawStatusCode(),
        "duration" to between(timestamp, now()).toMillis()
    )
    log.info("Client Response <<< {}", entries(properties))
  }
}
