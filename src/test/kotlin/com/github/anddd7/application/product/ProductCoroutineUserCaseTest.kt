package com.github.anddd7.application.product

import com.github.anddd7.domain.product.Product
import com.github.anddd7.domain.product.ProductCoroutineRepository
import com.github.anddd7.domain.stock.StockCoroutineRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@FlowPreview
@ExtendWith(MockKExtension::class)
internal class ProductCoroutineUserCaseTest {
  @MockK
  private lateinit var productRepository: ProductCoroutineRepository
  @MockK
  private lateinit var stockRepository: StockCoroutineRepository
  @InjectMockKs
  private lateinit var productUserCase: ProductCoroutineUserCase

  @Test
  fun `should find all products from db`() = runBlocking {
    val expect = mockk<Product>()
    coEvery { productRepository.findAll() } returns flowOf(expect)

    productUserCase.findAll()

    verify(exactly = 1) { productRepository.findAll() }
  }

  @Test
  fun `should get product and stock info together`() = runBlocking {
    coEvery { productRepository.getOne(any()) } returns mockk(relaxed = true)
    coEvery { stockRepository.getStock(any()) } returns mockk()

    productUserCase.getProductStock(1)

    coVerify(exactly = 1) {
      productRepository.getOne(any())
      stockRepository.getStock(any())
    }
  }
}
