package com.github.anddd7.adapter.config.filter

import com.github.anddd7.utils.CORRELATION_ID_KEY
import net.logstash.logback.argument.StructuredArguments.entries
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.time.Duration.between
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.UUID.randomUUID

@Component
class RequestResponseLoggingFilter : WebFilter {
  private val log = LoggerFactory.getLogger(this.javaClass)

  override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
    exchange.retrieveCorrelationId()

    val next = chain.filter(exchange)

    exchange.response.beforeCommit {
      exchange.logResponse()
      Mono.empty()
    }

    return exchange.attachRequestToContext(next)
  }

  private fun ServerWebExchange.retrieveCorrelationId() {
    val correlationId = request.headers[CORRELATION_ID_KEY]
        ?.firstOrNull()
        ?: randomUUID().toString().replace("-", "")

    attributes["timestamp"] = now()
    attributes[ServerWebExchange.LOG_ID_ATTRIBUTE] = correlationId
    response.headers.set(CORRELATION_ID_KEY, correlationId)
  }

  private fun ServerWebExchange.logId() =
      attributes.getValue(ServerWebExchange.LOG_ID_ATTRIBUTE) as String

  private fun ServerWebExchange.attachRequestToContext(next: Mono<Void>) =
      next.subscriberContext { it.put(CORRELATION_ID_KEY, logId()) }

  private fun ServerWebExchange.logResponse() {
    if (isLogEnabled(request) && log.isInfoEnabled) {
      val properties = mapOf(
          "clientIP" to request.getClientIP(),
          "method" to request.method,
          "uri" to request.uri.path,
          "protocol" to request.uri.scheme,
          "status" to response.statusCode?.value(),
          "duration" to
              between(attributes["timestamp"] as LocalDateTime, now()).toMillis()
      )
      MDC.putCloseable(CORRELATION_ID_KEY, logId()).use {
        log.info("Response <<< {}", entries(properties))
      }
    }
  }

  private val ignoredEndpoints = listOf("/ping", "/hello")

  private fun isLogEnabled(request: ServerHttpRequest) =
      request.uri.path !in ignoredEndpoints

  private fun ServerHttpRequest.getClientIP() =
      remoteAddress?.address?.hostAddress
          ?.let { if ("0:0:0:0:0:0:0:1" == it) "127.0.0.1" else it }
          ?: "unknown"
}


