package com.github.anddd7.adapter.outbound.stock

import com.github.anddd7.adapter.RepositoryImpl
import com.github.anddd7.domain.stock.StockCoroutineRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import java.math.BigDecimal

@Repository
class StockCoroutineClient(private val webClient: WebClient) : StockCoroutineRepository, RepositoryImpl {
  override suspend fun getStock(productId: Long): BigDecimal = webClient
      .get()
      .uri("/coroutine/product/$productId/stock")
      .accept(MediaType.APPLICATION_JSON)
      .awaitExchange().awaitBody()
}
