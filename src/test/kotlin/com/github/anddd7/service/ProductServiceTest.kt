package com.github.anddd7.service

import com.github.anddd7.client.StockClient
import com.github.anddd7.entity.Product
import com.github.anddd7.repository.ProductRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockKExtension::class)
internal class ProductServiceTest {
  @MockK
  private lateinit var productRepository: ProductRepository
  @MockK
  private lateinit var stockClient: StockClient
  @InjectMockKs
  private lateinit var productService: ProductService

  @Test
  fun `should find all products from db`() {
    val expect = mockk<Product>()
    every { productRepository.findAll() } returns Flux.just(expect)

    StepVerifier.create(productService.findAll())
        .expectNext(expect)
        .verifyComplete()

    verify(exactly = 1) { productRepository.findAll() }
  }

  @Test
  fun `should get product and stock info together`() {
    every { productRepository.getOne(any()) } returns Mono.just(mockk(relaxed = true))
    every { stockClient.getStock(any()) } returns Mono.just(mockk())

    StepVerifier.create(productService.getProductStock(1))
        .expectNextCount(1)
        .verifyComplete()

    verify(exactly = 1) {
      productRepository.getOne(any())
      stockClient.getStock(any())
    }
  }
}
