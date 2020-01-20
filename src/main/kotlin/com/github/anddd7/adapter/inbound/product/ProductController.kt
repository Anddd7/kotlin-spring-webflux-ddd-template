package com.github.anddd7.adapter.inbound.product

import com.github.anddd7.adapter.EndPoint
import com.github.anddd7.application.product.ProductUserCase
import com.github.anddd7.application.product.dto.ProductStockDTO
import com.github.anddd7.domain.product.Product
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
class ProductController(private val productUserCase: ProductUserCase) : EndPoint {
  @GetMapping
  fun products(): Flux<Product> = productUserCase.findAll()

  @GetMapping("/{id}")
  fun productStock(@PathVariable id: Long): Mono<ProductStockDTO> =
      productUserCase.getProductStock(id)
          .switchIfEmpty(Mono.error(NoSuchElementException()))

  @GetMapping("/{id}/stock")
  fun stock(@PathVariable id: Long): Mono<BigDecimal> = Mono.just(
      Random(id).nextFloat().let(::abs).toBigDecimal()
  )
}
