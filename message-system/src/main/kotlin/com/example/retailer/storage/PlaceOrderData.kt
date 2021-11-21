package com.example.retailer.storage

import com.example.retailer.api.distributor.Order
import com.example.retailer.api.distributor.OrderInfo

/**
 * Контейнер для сущности заказа и сущности информации о заказе
 */
data class PlaceOrderData(val order: Order, val info: OrderInfo)