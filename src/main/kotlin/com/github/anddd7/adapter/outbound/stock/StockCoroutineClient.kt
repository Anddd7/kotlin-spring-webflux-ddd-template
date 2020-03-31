package com.github.anddd7.adapter.outbound.stock

import com.github.anddd7.adapter.RepositoryImpl
import com.github.anddd7.domain.stock.StockCoroutineRepository
import com.github.anddd7.utils.logOnNext
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.math.BigDecimal

@Repository
class StockCoroutineClient(private val webClient: WebClient) : StockCoroutineRepository, RepositoryImpl {
  private val log = LoggerFactory.getLogger(this.javaClass)

  override suspend fun getStock(productId: Int): BigDecimal = webClient
      .get()
      .uri("/coroutine/product/$productId/stock")
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .onStatus(HttpStatus::is4xxClientError, this::handle4xxError)
      .onStatus(HttpStatus::is5xxServerError, this::handle5xxError)
      .awaitBody()

  private fun handle4xxError(it: ClientResponse): Mono<ResponseStatusException> =
      // error response body
      it.bodyToMono<String>()
          .logOnNext { log.error(it) }
          .flatMap { ResponseStatusException(HttpStatus.NOT_FOUND, it).toMono<ResponseStatusException>() }

  private fun handle5xxError(it: ClientResponse): Mono<ResponseStatusException> =
      // error response body
      it.bodyToMono<String>()
          .logOnNext { log.error(it) }
          .flatMap { ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, it).toMono<ResponseStatusException>() }
}
