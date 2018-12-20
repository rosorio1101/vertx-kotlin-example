package io.rosorio.kotlinExample.eventbus

import io.vertx.core.AbstractVerticle

private const val ADDRESS = "verticle.eventbus.2"

class Consumer : AbstractVerticle() {

  override fun start() {
    println("Start Consumer")
    vertx.eventBus().consumer<Any>(ADDRESS) { message ->
      println("Message receive: ${message.body()}")
      vertx.close()
    }
  }
}

class Publisher: AbstractVerticle() {

  override fun start() {
    println("Start publisher")
    vertx.eventBus().publish(ADDRESS, "Hello World from Publisher!!")
  }
}
