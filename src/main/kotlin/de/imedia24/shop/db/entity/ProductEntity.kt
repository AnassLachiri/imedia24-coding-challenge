package de.imedia24.shop.db.entity

import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "products")
class ProductEntity {
    @Id
    @Column(name = "sku", nullable = false)
    lateinit var sku: String;

    @Column(name = "name", nullable = false)
    lateinit var name: String;

    @Column(name = "description")
    var description: String? = null;

    @Column(name = "price", nullable = false)
    lateinit var price: BigDecimal;

    @UpdateTimestamp
    @Column(name = "created_at", nullable = false)
    lateinit var createdAt: ZonedDateTime;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: ZonedDateTime;
}
