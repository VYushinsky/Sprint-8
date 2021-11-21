
package com.example.retailer

import com.example.retailer.adapter.DistributorPublisher
import com.example.retailer.adapter.DistributorPublisherImpl
import com.example.retailer.adapter.Consumer
import com.example.retailer.adapter.ConsumerImpl
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class RetailerApplication {

	@Bean
	fun exchange(): TopicExchange {
		return TopicExchange("distributor_exchange")
	}
	@Bean
	fun publisher(): DistributorPublisher {
		return DistributorPublisherImpl()
	}
	@Bean
	fun queue(): Queue {
		return Queue("consumer")
	}
	@Bean
	fun consumer(): Consumer {
		return ConsumerImpl()
	}
	@Bean
	fun bindingRetailer(exchange: TopicExchange, queue: Queue
	): Binding {
		return BindingBuilder.bind(queue).to(exchange).with("retailer.VYushinsky.#")
	}
}

fun main(args: Array<String>) {
	runApplication<RetailerApplication>(*args)
}