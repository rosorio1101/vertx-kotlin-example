package io.rosorio.kotlinExample.eventbus

import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.DeliveryOptions

private const val ADDRESS5 = "verticle.eventbus.delay"

class ConsumerDelay : AbstractVerticle() {

  override fun start() {
    vertx.eventBus().consumer<Any>(ADDRESS5) { message ->
      println("Message receive: ${message.body()}")
      if(!message.headers().isEmpty) {
        message.headers().forEach { header ->
          println("Header ${header.key} : ${header.value}")
        }
      }
      vertx.setTimer(1000) {
        message.reply("Hello World from Consumer")
      }
    }
  }
}

class PublisherDelay : AbstractVerticle() {

  override fun start() {
    vertx.setTimer(5000) {
      println("Send message after 5 seconds")

      val options= DeliveryOptions()
      options.addHeader("x-my-header", "MyHeader")

      vertx.eventBus().send<Any>(ADDRESS5, "Hello World from Publisher!!", options) {asyncResult ->
        if(asyncResult.succeeded()) {
          println("Reply: ${asyncResult.result().body()}")
        }
      }
    }
  }
}
