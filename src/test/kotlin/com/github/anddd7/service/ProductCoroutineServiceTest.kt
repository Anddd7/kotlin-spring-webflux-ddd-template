package com.github.anddd7.service

import com.github.anddd7.client.StockCoroutineClient
import com.github.anddd7.entity.Product
import com.github.anddd7.repository.ProductCoroutineRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ProductCoroutineServiceTest {
  @MockK
  private lateinit var productRepository: ProductCoroutineRepository
  @MockK
  private lateinit var stockClient: StockCoroutineClient
  @InjectMockKs
  private lateinit var productService: ProductCoroutineService

  @Test
  fun `should find all products from db`() = runBlocking {
    val expect = mockk<Product>()
    coEvery { productRepository.findAll() } returns flowOf(expect)

    productService.findAll()

    verify(exactly = 1) { productRepository.findAll() }
  }

  @Test
  fun `should get product and stock info together`() = runBlocking {
    coEvery { productRepository.getOne(any()) } returns mockk(relaxed = true)
    coEvery { stockClient.getStock(any()) } returns mockk()

    productService.getProductStock(1)

    coVerify(exactly = 1) {
      productRepository.getOne(any())
      stockClient.getStock(any())
    }
  }
}
