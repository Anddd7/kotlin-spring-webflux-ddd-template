package com.github.anddd7.adapter.outbound.product

import com.github.anddd7.adapter.RepositoryImpl
import com.github.anddd7.adapter.outbound.product.po.ProductPO
import com.github.anddd7.adapter.outbound.product.po.toProduct
import com.github.anddd7.domain.product.Product
import com.github.anddd7.domain.product.ProductRepository
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.from
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ProductRepositoryImpl(private val databaseClient: DatabaseClient) : ProductRepository, RepositoryImpl {
  override fun findAll(): Flux<Product> = databaseClient
      .select().from<ProductPO>()
      .fetch().all()
      .map(ProductPO::toProduct)

  override fun getOne(id: Long): Mono<Product> = databaseClient
      .select().from<ProductPO>()
      .matching(
          where("id").`is`(id)
      )
      .fetch().first()
      .map(ProductPO::toProduct)
}
