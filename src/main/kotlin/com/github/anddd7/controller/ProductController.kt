package com.github.anddd7.controller

import com.github.anddd7.entity.Product
import com.github.anddd7.service.ProductService
import com.github.anddd7.service.dto.ProductStockDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.random.Random

@RestController
@RequestMapping("/product")
class ProductController(private val productService: ProductService) {
  @GetMapping
  fun products(): Flux<Product> = productService.findAll()

  @GetMapping("/{id}")
  fun productStock(@PathVariable id: Long): Mono<ProductStockDTO> =
      productService.getProductStock(id)
          .switchIfEmpty(Mono.error(NoSuchElementException()))

  @GetMapping("/{id}/stock")
  fun stock(@PathVariable id: Long): Mono<BigDecimal> = Mono.just(
      Random(id).nextFloat().let(::abs).toBigDecimal()
  )
}
