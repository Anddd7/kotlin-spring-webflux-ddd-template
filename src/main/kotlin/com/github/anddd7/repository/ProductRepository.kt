package com.github.anddd7.repository

import com.github.anddd7.entity.Product
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.from
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ProductRepository(private val databaseClient: DatabaseClient) {
  fun findAll(): Flux<Product> = databaseClient.select().from<Product>().fetch().all()
  fun getOne(id: Long): Mono<Product> = databaseClient.select().from<Product>()
      .matching(
          where("id").`is`(id)
      )
      .fetch().first()
}
