package com.example.retailer.storage

import com.example.retailer.api.distributor.Order
import com.example.retailer.api.distributor.OrderInfo
import com.example.retailer.api.distributor.OrderStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class OrderStorageService : OrderStorage {
    @Autowired
    lateinit var orderInfoCrud: OrderInfoCrud
    @Autowired
    lateinit var orderRepositoryCrud: OrderRepositoryCrud


    override fun createOrder(draftOrder: Order): PlaceOrderData {
        val order = orderRepositoryCrud.save(draftOrder)
        val info = orderInfoCrud.save(OrderInfo(order.id!!, OrderStatus.SENT, ""))
        return PlaceOrderData(order, info)
    }

    override fun updateOrder(order: OrderInfo): Boolean {
        return if (orderInfoCrud.existsById(order.orderId)){
            orderInfoCrud.save(order)
            true
        } else return false
    }

    override fun getOrderInfo(id: String): OrderInfo? = orderInfoCrud.findByIdOrNull(id)
}