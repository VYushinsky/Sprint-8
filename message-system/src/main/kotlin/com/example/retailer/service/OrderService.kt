package com.example.retailer.service

import com.example.retailer.adapter.DistributorPublisher
import com.example.retailer.api.distributor.Order
import com.example.retailer.api.distributor.OrderInfo
import com.example.retailer.storage.OrderStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalStateException

/**
 * Сервис с бизнес-логикой обработки заказов
 */
@Service
class OrderService {

    @Autowired
    lateinit var distributorPublisher: DistributorPublisher

    @Autowired
    lateinit var orderStorage: OrderStorage

    /**
     * Размещение заказа
     * 1) Сохранение в БД и получение orderId
     * 2) Отправка заказа дистрибьютору
     * Возврат сохраненного заказа вместе с id
     */
    fun placeOrder(orderDraft: Order): OrderInfo {
        val data = orderStorage.createOrder(orderDraft)
        if (!distributorPublisher.placeOrder(data.order)) {
            throw IllegalStateException("Publishing failed")
        }
        return data.info
    }

    /**
     * Поиск заказа в БД
     */
    fun getOrderInfo(id: String): OrderInfo? = orderStorage.getOrderInfo(id)

    /**
     * Обновление заказа
     * Должно быть вызвано после получения каждого уведомления от дистрибьютора
     */
    fun updateOrderInfo(orderInfo: OrderInfo) : Boolean {
        return orderStorage.updateOrder(orderInfo)
    }
}