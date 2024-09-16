package de.imedia24.shop.controller

import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
import de.imedia24.shop.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

@RestController
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @Operation(summary = "Get product by SKU", description = "Fetch a product by its SKU")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successful retrieval of product"),
        ApiResponse(responseCode = "404", description = "Product not found")
    ])
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

    @Operation(summary = "Get products by list of SKUs", description = "Fetch multiple products by their SKUs")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successful retrieval of products"),
        ApiResponse(responseCode = "404", description = "Products not found")
    ])
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

    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Product created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid product data")
    ])
    @PostMapping("/products", consumes = ["application/json"], produces = ["application/json;charset=utf-8"])
    fun createProduct(
            @RequestBody productRequest: ProductRequest
    ): ResponseEntity<ProductResponse> {
        logger.info("Creating product with SKU: ${productRequest.sku}")

        val createdProduct = productService.createProduct(productRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
    }

    @Operation(summary = "Update product details", description = "Partially update a product by SKU")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Product updated successfully"),
        ApiResponse(responseCode = "404", description = "Product not found")
    ])
    @PatchMapping("/products/{sku}", consumes = ["application/json"], produces = ["application/json;charset=utf-8"])
    fun updateProduct(
            @PathVariable("sku") sku: String,
            @RequestBody updateRequest: ProductUpdateRequest
    ): ResponseEntity<ProductResponse> {
        logger.info("Updating product with SKU: $sku")

        val updatedProduct = productService.updateProduct(sku, updateRequest)
        return if (updatedProduct == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(updatedProduct)
        }
    }
}
