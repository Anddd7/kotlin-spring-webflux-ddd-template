package com.github.anddd7.service

import com.github.anddd7.client.StockCoroutineClient
import com.github.anddd7.entity.Product
import com.github.anddd7.repository.ProductCoroutineRepository
import com.github.anddd7.service.dto.ProductStockDTO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class ProductCoroutineService(
    private val productRepository: ProductCoroutineRepository,
    private val stockClient: StockCoroutineClient
) {
  @FlowPreview
  fun findAll(): Flow<Product> = productRepository.findAll()

  suspend fun getProductStock(id: Long): ProductStockDTO = coroutineScope {
    val product = async { productRepository.getOne(id) }
    val stock = async { stockClient.getStock(id) }

    ProductStockDTO(product.await(), stock.await())
  }
}
