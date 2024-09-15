package de.imedia24.shop.domain.product

import java.math.BigDecimal

data class ProductUpdateRequest(
        val name: String? = null,
        val description: String? = null,
        val price: BigDecimal? = null
)
