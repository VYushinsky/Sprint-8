package com.example.retailer

import com.example.retailer.adapter.Consumer
import com.example.retailer.adapter.ConsumerImpl
import com.example.retailer.adapter.DistributorPublisher
import com.example.retailer.adapter.DistributorPublisherImpl
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
	fun queue(): Queue {
		return Queue("consumer")
	}

	@Bean
	fun exchange(): TopicExchange {
		return TopicExchange("distributor_exchange")
	}

	@Bean
	fun binding(queue: Queue, exchange: TopicExchange): Binding? {
		return BindingBuilder.bind(queue).to(exchange).with("retailer.VYushinsky.#")
	}

	@Bean
	fun receiver(): Consumer {
		return ConsumerImpl()
	}

	@Bean
	fun sender(): DistributorPublisher {
		return DistributorPublisherImpl()
	}
}

fun main(args: Array<String>) {
	runApplication<RetailerApplication>(*args)
}