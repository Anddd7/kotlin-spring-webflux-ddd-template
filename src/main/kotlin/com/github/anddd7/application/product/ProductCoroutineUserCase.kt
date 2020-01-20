package com.github.anddd7.application.product

import com.github.anddd7.application.UserCase
import com.github.anddd7.application.product.dto.ProductStockDTO
import com.github.anddd7.domain.product.Product
import com.github.anddd7.domain.product.ProductCoroutineRepository
import com.github.anddd7.domain.product.ProductCoroutineService
import com.github.anddd7.domain.stock.StockCoroutineRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class ProductCoroutineUserCase(
    productRepository: ProductCoroutineRepository,
    private val stockRepository: StockCoroutineRepository
) : UserCase {

  private val productService: ProductCoroutineService = ProductCoroutineService(productRepository)

  @FlowPreview
  fun findAll(): Flow<Product> = productService.findAll()

  suspend fun getProductStock(id: Long): ProductStockDTO = coroutineScope {
    val product = async { productService.getOne(id) }
    val stock = async { stockRepository.getStock(id) }

    ProductStockDTO(product.await(), stock.await())
  }
}
