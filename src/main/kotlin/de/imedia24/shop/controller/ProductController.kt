package de.imedia24.shop.controller

import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

@RestController
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @GetMapping("/products/{sku}", produces = ["application/json;charset=utf-8"])
    fun findProductsBySku(
        @PathVariable("sku") sku: String
    ): ResponseEntity<ProductResponse> {
        logger.info("Request for product $sku")

        val product = productService.findProductBySku(sku)
        return if(product == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(product)
        }
    }

    @GetMapping("/products", produces = ["application/json;charset=utf-8"])
    fun findProductsBySkus(
            @RequestParam("skus") skus: List<String>
    ): ResponseEntity<List<ProductResponse>> {
        logger.info("Request for products with SKUs: $skus")

        val products = productService.findProductsBySkus(skus)
        return if (products.isEmpty()) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(products)
        }
    }

    @PostMapping("/products", consumes = ["application/json"], produces = ["application/json;charset=utf-8"])
    fun createProduct(
            @RequestBody productRequest: ProductRequest
    ): ResponseEntity<ProductResponse> {
        logger.info("Creating product with SKU: ${productRequest.sku}")

        val createdProduct = productService.createProduct(productRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
    }
}
