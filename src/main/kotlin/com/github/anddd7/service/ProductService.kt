package com.github.anddd7.service

import com.github.anddd7.client.StockClient
import com.github.anddd7.entity.Product
import com.github.anddd7.repository.ProductRepository
import com.github.anddd7.service.dto.ProductStockDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val stockClient: StockClient
) {
  fun findAll(): Flux<Product> = productRepository.findAll()
  fun getProductStock(id: Long): Mono<ProductStockDTO> {
    val product = productRepository.getOne(id)
    val stock = stockClient.getStock(id)

    return Mono.zip(product, stock, ::ProductStockDTO)
  }
}
