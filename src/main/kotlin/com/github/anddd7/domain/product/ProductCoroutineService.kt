package com.github.anddd7.domain.product

import com.github.anddd7.domain.Service
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

class ProductCoroutineService(
    private val productRepository: ProductCoroutineRepository
) : Service {
  @FlowPreview
  fun findAll(): Flow<Product> = productRepository.findAll()

  suspend fun getOne(id: Int): Product = productRepository.getOne(id)
}
