package com.github.anddd7.domain.product

import com.github.anddd7.domain.Entity
import java.math.BigDecimal

data class Product(
    val id: Long = 0,
    val name: String,
    val price: BigDecimal
) : Entity
