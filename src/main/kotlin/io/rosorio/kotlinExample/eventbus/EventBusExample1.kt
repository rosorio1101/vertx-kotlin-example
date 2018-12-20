package io.rosorio.kotlinExample.eventbus

import io.vertx.core.AbstractVerticle

class EventBusExample1: AbstractVerticle() {

  private val ADDRESS = "example.eb.1"

  override fun start() {
    val eventBus = vertx.eventBus()

    eventBus.consumer<Any>(ADDRESS) {message ->
      println("Message receive: ${message.body()}")
    }

    eventBus.publish(ADDRESS, "Hello World")
  }
}
