package de.imedia24.shop.service

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
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

    fun updateProduct(sku: String, updateRequest: ProductUpdateRequest): ProductResponse? {
        val existingProduct = productRepository.findBySku(sku) ?: return null

        updateRequest.name?.let { existingProduct.name = it }
        updateRequest.description?.let { existingProduct.description = it }
        updateRequest.price?.let { existingProduct.price = it }

        existingProduct.updatedAt = ZonedDateTime.now()

        val updatedProduct = productRepository.save(existingProduct)

        return updatedProduct.toProductResponse()
    }
}
