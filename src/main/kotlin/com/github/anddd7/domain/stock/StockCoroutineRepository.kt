package com.github.anddd7.domain.stock

import com.github.anddd7.domain.Repository
import java.math.BigDecimal

interface StockCoroutineRepository : Repository {
  suspend fun getStock(productId: Int): BigDecimal
}
