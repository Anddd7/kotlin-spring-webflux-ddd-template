package com.github.anddd7.adapter.outbound.stock

import com.github.anddd7.adapter.RepositoryImpl
import com.github.anddd7.domain.stock.StockRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Repository
class StockClient(private val webClient: WebClient) : StockRepository, RepositoryImpl {
  override fun getStock(productId: Int): Mono<BigDecimal> = webClient
      .get()
      .uri("/product/$productId/stock")
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToMono()
}
