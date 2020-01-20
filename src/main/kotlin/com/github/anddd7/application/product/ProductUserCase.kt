package com.github.anddd7.application.product

import com.github.anddd7.application.UserCase
import com.github.anddd7.application.product.dto.ProductStockDTO
import com.github.anddd7.domain.product.Product
import com.github.anddd7.domain.product.ProductRepository
import com.github.anddd7.domain.product.ProductService
import com.github.anddd7.domain.stock.StockRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductUserCase(
    productRepository: ProductRepository,
    private val stockRepository: StockRepository
) : UserCase {
  private val productService: ProductService = ProductService(productRepository)

  fun findAll(): Flux<Product> = productService.findAll()
  fun getProductStock(id: Long): Mono<ProductStockDTO> {
    val product = productService.getOne(id)
    val stock = stockRepository.getStock(id)

    return Mono.zip(product, stock, ::ProductStockDTO)
  }
}
