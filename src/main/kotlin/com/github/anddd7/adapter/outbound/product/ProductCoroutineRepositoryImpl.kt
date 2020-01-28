package com.github.anddd7.adapter.outbound.product

import com.github.anddd7.adapter.RepositoryImpl
import com.github.anddd7.adapter.outbound.product.po.ProductPO
import com.github.anddd7.adapter.outbound.product.po.toProduct
import com.github.anddd7.domain.product.Product
import com.github.anddd7.domain.product.ProductCoroutineRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductCoroutineRepositoryImpl(private val productDAO: ProductDAO) : ProductCoroutineRepository, RepositoryImpl {
  @FlowPreview
  override fun findAll(): Flow<Product> =
      productDAO.findAll().asFlow().map { it.toProduct() }

  override suspend fun getOne(id: Int): Product =
      productDAO.findById(id).awaitFirstOrNull()?.let(ProductPO::toProduct)
          ?: throw IllegalArgumentException()
}
