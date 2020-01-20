package com.github.anddd7.domain.product

import com.github.anddd7.domain.Repository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

interface ProductCoroutineRepository : Repository {
  @FlowPreview
  fun findAll(): Flow<Product>

  suspend fun getOne(id: Long): Product
}
