package com.github.anddd7.service.dto

import com.github.anddd7.entity.Product
import java.math.BigDecimal

data class ProductStockDTO(
    val id: Long = 0,
    val name: String,
    val price: BigDecimal,
    val stock: BigDecimal
) {
  constructor(product: Product, stock: BigDecimal) : this(product.id, product.name, product.price, stock)
}

