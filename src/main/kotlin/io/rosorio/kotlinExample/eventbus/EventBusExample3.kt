package io.rosorio.kotlinExample.eventbus

import io.vertx.core.AbstractVerticle

val ADDRESS2 = "verticle.eventbus.2"

class Consumer2 : AbstractVerticle() {

  override fun start() {
    vertx.eventBus().consumer<Any>(ADDRESS2) { message ->
      println("Message receive: ${message.body()}")
      vertx.setTimer(1000) {
        message.reply("Hello World from Consumer")
      }
    }
  }
}

class Publisher2: AbstractVerticle() {


  override fun start() {
    vertx.eventBus().send<Any>(ADDRESS2, "Hello World from Publisher!!") {asyncResult ->
      if(asyncResult.succeeded()) {
        println("Reply: ${asyncResult.result().body()}")
      }
    }
  }
}
