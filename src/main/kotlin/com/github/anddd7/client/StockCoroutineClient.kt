package com.github.anddd7.client

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import java.math.BigDecimal

@Component
class StockCoroutineClient(private val webClient: WebClient) {
  suspend fun getStock(productId: Long): BigDecimal =
      webClient.get().uri("/coroutine/product/$productId/stock").accept(MediaType.APPLICATION_JSON)
          .awaitExchange().awaitBody()
}
