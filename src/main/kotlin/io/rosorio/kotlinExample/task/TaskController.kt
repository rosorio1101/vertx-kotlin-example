package io.rosorio.kotlinExample.task

import io.rosorio.kotlinExample.extensions.toJson
import io.rosorio.kotlinExample.extensions.toTask
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class TaskController(
  private val repository: TaskRepository
) {
  private val logger = LoggerFactory.getLogger(TaskController::class.java)

  fun createTask(routingContext: RoutingContext) {
    GlobalScope.launch {
      val task = routingContext.bodyAsJson.mapTo(Task::class.java)
      logger.info("create new task $task")

      repository.save(task)
      routingContext.response().setStatusCode(201).end()
    }
  }

  fun findAll(routingContext: RoutingContext) {
    GlobalScope.launch {
      logger.info("find all data")
      repository.findAll().also {
        routingContext.response()
          .setStatusCode(200)
          .end(
            it.toJson().encodePrettily()
          )
      }

    }
  }

  fun findById(routingContext: RoutingContext) {
    GlobalScope.launch {
      val id = routingContext.request().params().get("id")

      logger.info("find data for id : $id")

      val response = routingContext.response()

      repository.findOne(id).also {
        if (it == null) {
          response.setStatusCode(404).end()
        } else {
          response.setStatusCode(200)
            .end(
              it.toJson().encodePrettily()
            )
        }
      }
    }
  }

  fun delete(routingContext: RoutingContext) {
    GlobalScope.launch {
      val id = routingContext.request().params().get("id")
      val response = routingContext.response()
      logger.info("delete data for id: $id")
      repository.delete(id).also {
        response.setStatusCode(200).end(it.toJson().encodePrettily())
      }
    }
  }

  fun update(routingContext: RoutingContext) {
    GlobalScope.launch {
      val id = routingContext.request().params().get("id")
      val body = routingContext.bodyAsJson
      val response = routingContext.response()

      val existingTask = repository.findOne(id)
      logger.info("updating $existingTask")

      if (existingTask != null) {

        val jsonTask = existingTask.toJson()

        jsonTask.forEach {
          if (body.containsKey(it.key) && it.value != null && (
              body.getValue(it.key) != it.value
              )) {
            jsonTask.put(it.key, body.getValue(it.key))
          }
        }
        val updatedTask = repository.save(jsonTask.toTask().apply {
          updatedAt = Date()
        })

        response.setStatusCode(200).end(updatedTask.toJson().encodePrettily())
      } else {
        response.setStatusCode(404).end()
      }

    }
  }
}
