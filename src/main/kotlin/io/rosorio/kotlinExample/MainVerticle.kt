package io.rosorio.kotlinExample

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.rosorio.kotlinExample.task.*
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle

class MainVerticle : CoroutineVerticle() {

  private lateinit var taskRepository: TaskRepository
  private lateinit var taskController: TaskController
  private lateinit var taskRouter: TaskRouter

  private val logger = LoggerFactory.getLogger(MainVerticle::class.java)

  override suspend fun start() {
    Json.mapper.apply {
      registerKotlinModule()
    }

    val mongoClient = MongoClient.createShared(vertx, json {
      obj(
        "host" to "127.0.0.1",
        "port" to 27017,
        "db_name" to "Tasks"

      )
    })

    taskRepository = MongoTaskRepository(mongoClient)
    taskController = TaskController(taskRepository)
    taskRouter = TaskRouter(taskController)

    //deleteData()
    //createFakeData()

    val server = vertx.createHttpServer()
    val mainRouter = createMainRouter(vertx)

    server
      .requestHandler(mainRouter)
      .listen(8080) { http ->
        if (http.succeeded()) {
          println("HTTP server started on port 8080")
        }
      }
  }

  private fun createMainRouter(vertx: Vertx): Router {
    val mainRouter = Router.router(vertx)
    val apiRouter = Router.router(vertx)

    apiRouter.route().handler(BodyHandler.create())
    apiRouter.mountSubRouter(TaskRouter.PATH, taskRouter.route(vertx))

    mainRouter.mountSubRouter("/api/v1", apiRouter)

    return mainRouter
  }

  private suspend fun createFakeData() {
    logger.info("create fake data")
    val tasks = mutableListOf<Task>()
    for(i in 1..100000) {
      tasks.add(Task(
        name = "MyTask$i"
      ))
    }
    taskRepository.createAll(tasks)
    logger.info("fake data created!!")
  }

  private suspend fun deleteData() {
    logger.info("delete all data")
    taskRepository.deleteAll()
    logger.info("all data deleted")
  }
}
