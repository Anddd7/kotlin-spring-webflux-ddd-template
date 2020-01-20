package com.github.anddd7.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("products")
data class Product(
    @Id
    val id: Long = 0,
    val name: String,
    val price: BigDecimal
)
