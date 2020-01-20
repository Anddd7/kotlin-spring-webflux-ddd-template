package com.github.anddd7.adapter.outbound.product.po

import com.github.anddd7.adapter.PersistObject
import com.github.anddd7.domain.product.Product
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("products")
data class ProductPO(
    @Id
    val id: Long = 0,
    val name: String,
    val price: BigDecimal
) : PersistObject

fun ProductPO.toProduct() = Product(id, name, price)
