package com.github.anddd7.repository

import com.github.anddd7.entity.Product
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.awaitOne
import org.springframework.data.r2dbc.core.from
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class ProductCoroutineRepository(private val databaseClient: DatabaseClient) {
  @FlowPreview
  fun findAll(): Flow<Product> =
      databaseClient.select().from<Product>().fetch().all().asFlow()

  suspend fun getOne(id: Long): Product =
      databaseClient.select().from<Product>()
          .matching(
              where("id").`is`(id)
          )
          .fetch()
          .awaitOne()
}
