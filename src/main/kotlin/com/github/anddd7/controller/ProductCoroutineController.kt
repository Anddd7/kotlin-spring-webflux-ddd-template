package com.github.anddd7.controller

import com.github.anddd7.entity.Product
import com.github.anddd7.service.ProductCoroutineService
import com.github.anddd7.service.dto.ProductStockDTO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.random.Random

@RestController
@RequestMapping("/coroutine/product")
class ProductCoroutineController(private val productService: ProductCoroutineService) {
  @GetMapping
  @FlowPreview
  fun products(): Flow<Product> = productService.findAll()

  @GetMapping("/{id}")
  suspend fun productStock(@PathVariable id: Long): ProductStockDTO = productService.getProductStock(id)

  @GetMapping("/{id}/stock")
  fun stock(@PathVariable id: Long): BigDecimal = Random(id).nextFloat().let(::abs).toBigDecimal()
}
