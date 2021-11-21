
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
	fun topic(): TopicExchange {
		return TopicExchange("distributor_exchange", true, false)
	}
	@Bean
	fun autoDeleteRetailerQueue(): Queue {
		return Queue("consumer", false, false, true)
	}
	@Bean
	fun bindingRetailer(
		topic: TopicExchange,
		autoDeleteRetailerQueue: Queue
	): Binding {
		return BindingBuilder.bind(autoDeleteRetailerQueue)
			.to(topic)
			.with("retailer.VYushinsky.#")
	}
	@Bean
	fun consumer(): Consumer {
		return ConsumerImpl()
	}
	@Bean
	fun publisher(): DistributorPublisher {
		return DistributorPublisherImpl()
	}
}

fun main(args: Array<String>) {
	runApplication<RetailerApplication>(*args)
}