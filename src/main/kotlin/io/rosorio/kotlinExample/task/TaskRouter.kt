package io.rosorio.kotlinExample.task

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class TaskRouter(
  private val controller: TaskController
) {
  companion object {
    @JvmStatic
    val PATH = "/tasks"
  }

  fun route(vertx: Vertx) = Router.router(vertx).apply {
    post("/").handler(controller::createTask)
    put("/:id").handler(controller::update)
    patch("/:id").handler(controller::update)
    get("/").handler(controller::findAll)
    get("/:id").handler(controller::findById)
    delete("/:id").handler(controller::delete)
  }
}
