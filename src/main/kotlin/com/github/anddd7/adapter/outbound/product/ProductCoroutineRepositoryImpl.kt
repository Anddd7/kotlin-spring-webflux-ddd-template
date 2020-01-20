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
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.awaitOne
import org.springframework.data.r2dbc.core.from
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class ProductCoroutineRepositoryImpl(private val databaseClient: DatabaseClient) : ProductCoroutineRepository, RepositoryImpl {
  @FlowPreview
  override fun findAll(): Flow<Product> = databaseClient
      .select().from<ProductPO>()
      .fetch().all()
      .asFlow()
      .map { it.toProduct() }

  override suspend fun getOne(id: Long): Product = databaseClient
      .select().from<ProductPO>()
      .matching(
          where("id").`is`(id)
      )
      .fetch()
      .awaitOne()
      .let(ProductPO::toProduct)
}
