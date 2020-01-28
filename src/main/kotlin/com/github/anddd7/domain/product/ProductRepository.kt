package com.github.anddd7.domain.product

import com.github.anddd7.domain.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductRepository : Repository {
  fun findAll(): Flux<Product>
  fun getOne(id: Int): Mono<Product>
}
