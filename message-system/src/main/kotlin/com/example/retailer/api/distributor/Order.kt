package com.example.retailer.api.distributor

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

/**
 * Описание заказа
 */
@Entity
data class Order(
    /**
     * Уникальный идентификатор заказа на стороне ретейлера
     */
    @Id
    @GeneratedValue
    val id: String?,

    /**
     * Произвольный адрес доставки
     */
    @Column
    val address: String,

    /**
     * Произвольный получатель доставки
     */
    @Column
    val recipient: String,

    /**
     * Список заказанных товаров
     */
    @OneToMany
    @Cascade(CascadeType.ALL)
    val items: List<Item>
)