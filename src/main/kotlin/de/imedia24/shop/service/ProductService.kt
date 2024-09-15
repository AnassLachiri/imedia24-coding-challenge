package de.imedia24.shop.service

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun findProductBySku(sku: String): ProductResponse? {
        return productRepository.findBySku(sku)?.toProductResponse();
    }

    fun findProductsBySkus(skus: List<String>): List<ProductResponse> {
        val productEntities = productRepository.findBySkuIn(skus)
        return productEntities.map { it.toProductResponse() }
    }

    fun createProduct(productRequest: ProductRequest): ProductResponse {
        val productEntity = ProductEntity().apply {
            sku = productRequest.sku
            name = productRequest.name
            description = productRequest.description
            price = productRequest.price
            stock = productRequest.stock
            createdAt = ZonedDateTime.now()
            updatedAt = ZonedDateTime.now()
        }

        val savedProduct = productRepository.save(productEntity)

        return savedProduct.toProductResponse()
    }
}
