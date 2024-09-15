package de.imedia24.shop.service

import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun findProductBySku(sku: String): ProductResponse? {
        return productRepository.findBySku(sku)?.toProductResponse();
    }

    fun findProductsBySkus(skus: List<String>): List<ProductResponse> {
        val productEntities = productRepository.findBySkuIn(skus)
        return productEntities.map { it.toProductResponse() }
    }
}
