package com.github.anddd7.client

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Component
class StockClient(private val webClient: WebClient) {
  fun getStock(productId: Long): Mono<BigDecimal> = webClient
      .get()
      .uri("/product/$productId/stock")
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToMono()
}
