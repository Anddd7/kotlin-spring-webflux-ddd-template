package com.github.anddd7.domain.product

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ProductService(
    private val productRepository: ProductRepository
) {
  fun findAll(): Flux<Product> = productRepository.findAll()
  fun getOne(id: Long): Mono<Product> = productRepository.getOne(id)
}
