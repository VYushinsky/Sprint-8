package com.example.retailer.storage

import com.example.retailer.api.distributor.Order
import com.example.retailer.api.distributor.OrderInfo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderInfoCrud : CrudRepository<OrderInfo, String>

@Repository
interface OrderRepositoryCrud : CrudRepository<Order, String>