package com.github.anddd7.adapter.outbound.product

import com.github.anddd7.adapter.RepositoryImpl
import com.github.anddd7.adapter.outbound.product.po.ProductPO
import com.github.anddd7.adapter.outbound.product.po.toProduct
import com.github.anddd7.domain.product.Product
import com.github.anddd7.domain.product.ProductRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ProductRepositoryImpl(private val productDAO: ProductDAO) : ProductRepository, RepositoryImpl {
  override fun findAll(): Flux<Product> = productDAO.findAll().map(ProductPO::toProduct)
  override fun getOne(id: Int): Mono<Product> = productDAO.findById(id).map(ProductPO::toProduct)
}

@Component
interface ProductDAO : ReactiveCrudRepository<ProductPO, Int>
