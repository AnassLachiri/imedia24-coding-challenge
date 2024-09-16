package de.imedia24.shop

import com.fasterxml.jackson.databind.ObjectMapper
import de.imedia24.shop.controller.ProductController
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
import de.imedia24.shop.service.ProductService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@WebMvcTest(ProductController::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return list of products for given SKUs`() {
        // Given
        val skus = listOf("SKU12345", "SKU67890")
        val productResponses = listOf(
                ProductResponse("SKU12345", "Product 1", "", BigDecimal(99.99), 10),
                ProductResponse("SKU67890", "Product 2", "Description 2", BigDecimal(59.99), 5)
        )

        `when`(productService.findProductsBySkus(skus)).thenReturn(productResponses)

        // When & Then
        mockMvc.perform(
                get("/products")
                        .param("skus", "SKU12345,SKU67890")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].sku").value("SKU12345"))
                .andExpect(jsonPath("$[1].name").value("Product 2"))
    }


    @Test
    fun `should update product and return updated product`() {
        // Given
        val sku = "SKU12345"
        val updateRequest = ProductUpdateRequest(name = "Updated Name", description = "Updated Description", price = BigDecimal(199.99))
        val updatedProductResponse = ProductResponse(sku, "Updated Name", "Updated Description", BigDecimal(199.99), 100)

        `when`(productService.updateProduct(sku, updateRequest)).thenReturn(updatedProductResponse)

        // When & Then
        mockMvc.perform(
            patch("/products/$sku")
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.sku").value(sku))
            .andExpect(jsonPath("$.name").value("Updated Name"))
            .andExpect(jsonPath("$.description").value("Updated Description"))
            .andExpect(jsonPath("$.price").value(199.99))
            .andExpect(jsonPath("$.stock").value(100))
    }
}
