package com.github.anddd7.application.product.dto

import com.github.anddd7.application.DTO
import com.github.anddd7.domain.product.Product
import java.math.BigDecimal

data class ProductStockDTO(
    val id: Int = 0,
    val name: String,
    val price: BigDecimal,
    val stock: BigDecimal
) : DTO {
  constructor(product: Product, stock: BigDecimal) : this(product.id, product.name, product.price, stock)
}

