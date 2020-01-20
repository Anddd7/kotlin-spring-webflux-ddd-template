package com.github.anddd7.adapter.inbound.product

import com.github.anddd7.adapter.EndPoint
import com.github.anddd7.application.product.ProductCoroutineUserCase
import com.github.anddd7.application.product.dto.ProductStockDTO
import com.github.anddd7.domain.product.Product
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
class ProductCoroutineController(private val productUserCase: ProductCoroutineUserCase) : EndPoint {
  @GetMapping
  @FlowPreview
  fun products(): Flow<Product> = productUserCase.findAll()

  @GetMapping("/{id}")
  suspend fun productStock(@PathVariable id: Long): ProductStockDTO = productUserCase.getProductStock(id)

  @GetMapping("/{id}/stock")
  fun stock(@PathVariable id: Long): BigDecimal = Random(id).nextFloat().let(::abs).toBigDecimal()
}
