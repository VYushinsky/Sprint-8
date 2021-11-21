package com.example.retailer

import com.example.retailer.api.distributor.Item
import com.example.retailer.api.distributor.Order
import com.example.retailer.api.distributor.OrderInfo
import com.example.retailer.api.distributor.OrderStatus
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RetailerApplicationTests {

    @LocalServerPort
    private var port: Int = 8080

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    private fun url(s: String): String {
        return "http://localhost:${port}/${s}"
    }

    /**
     * Интеграционный тест:
     * 1) Размещение заказа, должна произойти отправка заказа дистрибьютору
     * 2) Проверяем, что при размещении был статус SENT
     * 3) Ждем, пока статус заказа сменится на CREATED
     * 4) Ждем, пока статус заказа сменится на DELIVERED
     */
    @Test
    fun `distributor integration test`() {
        val draftOrder =
            Order(
                null,
                "sample address",
                "sample recipient",
                listOf(Item(1L, "Sample item"), Item(2L, "Another item"))
            )

        val placeOrder = placeOrder(draftOrder)

        //Исходное состояние заказа SENT
        assertThat(placeOrder.status, equalTo(OrderStatus.SENT))

        //После получения уведомления от дистрибьютора должен быть статус "СОЗДАН"
        checkStatus(placeOrder.orderId, OrderStatus.CREATED, 20)

        //После следующего уведомления от дистрибьютора должен быть статус "ДОСТАВЛЕН"
        checkStatus(placeOrder.orderId, OrderStatus.DELIVERED, 20)


    }

    private fun checkStatus(id: String, expectedStatus: OrderStatus, retryCount: Int) {
        var retry = retryCount
        while (retry-- > 0) {
            try {
                assertThat(getOrderInfo(id).status, equalTo(expectedStatus))
                return
            } catch (e: Throwable) {
                if (retry <= 0) {
                    throw e
                } else {
                    Thread.sleep(300)
                }
            }
        }
    }

    private fun placeOrder(order: Order): OrderInfo =
        assertResp(restTemplate.postForEntity(url("/placeOrder"), order, OrderInfo::class.java))


    private fun getOrderInfo(id: String): OrderInfo =
        assertResp(restTemplate.getForEntity(url("/view/" + id), OrderInfo::class.java))


    private fun assertResp(resp: ResponseEntity<OrderInfo>): OrderInfo {
        assertThat(resp.statusCode, equalTo(HttpStatus.OK))
        return resp.body!!
    }
}
