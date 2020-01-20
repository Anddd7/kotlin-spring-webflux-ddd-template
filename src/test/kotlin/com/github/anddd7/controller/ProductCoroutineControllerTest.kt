package com.github.anddd7.controller

import com.github.anddd7.Application
import com.github.anddd7.entity.Product
import com.github.anddd7.service.ProductCoroutineService
import com.github.anddd7.service.dto.ProductStockDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList
import java.math.BigDecimal

@ContextConfiguration(classes = [Application::class])
@WebFluxTest(ProductCoroutineController::class)
internal class ProductCoroutineControllerTest {

  @Autowired
  private lateinit var webClient: WebTestClient

  @MockkBean
  private lateinit var productService: ProductCoroutineService

  @Test
  internal fun `should find all products`() {
    val expect = Product(name = "test", price = BigDecimal.ONE)
    coEvery { productService.findAll() } returns flowOf(expect)

    webClient.get().uri("/coroutine/product")
        .exchange()
        .expectStatus().isOk
        .expectBodyList<Product>()
        .hasSize(1)
        .contains(expect)
  }

  @Test
  internal fun `should get the product by id`() {
    val expect = ProductStockDTO(name = "test", price = BigDecimal.ONE, stock = BigDecimal.ONE)
    coEvery { productService.getProductStock(any()) } returns expect

    webClient.get().uri("/coroutine/product/1")
        .exchange()
        .expectStatus().isOk
        .expectBody<ProductStockDTO>()
        .isEqualTo(expect)
  }

  @Test
  internal fun `should get stock of product`() {
    webClient.get().uri("/coroutine/product/1/stock")
        .exchange()
        .expectStatus().isOk
  }
}
