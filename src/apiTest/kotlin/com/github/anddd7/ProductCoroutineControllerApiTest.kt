package com.github.anddd7

import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.reactive.server.WebTestClient

@Suppress("FunctionName")
@EnableApiTest
@AutoConfigureWebTestClient
@Sql("classpath:fixture/product_api.sql")
internal class ProductCoroutineControllerApiTest {

  @Autowired
  private lateinit var webClient: WebTestClient

  @Test
  internal fun `should find all products`() {
    webClient.get().uri("/coroutine/product").exchange().expectStatus().isOk
  }

  @Test
  internal fun `should get the product by id`() {
    stubFor(
        get("/stock/coroutine/product/1/stock")
            .willReturn(okJson("9999.99"))
    )

    webClient.get().uri("/coroutine/product/1").exchange().expectStatus().isOk
  }

  @Test
  internal fun `should get stock of product`() {
    webClient.get().uri("/coroutine/product/1/stock").exchange().expectStatus().isOk
  }
}
