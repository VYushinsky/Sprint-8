package com.example.retailer.api.distributor

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
    val items: List<Item>
)