package io.rosorio.kotlinExample.task

import io.rosorio.kotlinExample.extensions.toJson
import io.rosorio.kotlinExample.extensions.toTask
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.mongo.BulkOperation
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.ext.mongo.*


class MongoTaskRepository(
  private val client: MongoClient
) : TaskRepository {

  private val collection = "Tasks"
  private val logger = LoggerFactory.getLogger(MongoTaskRepository::class.java)

  override suspend fun findOne(id: String): Task? =
    client.findOneAwait(collection,
      query = json {
        obj {
          "_id" to id
        }
      },
      fields = json {
        obj {
          "_id" to "id"
          "name" to "name"
          "createdAt" to "createdAt"
          "updatedAt" to "updatedAt"
          "isDone" to "isDone"
        }
      }
    ).let {
      return it.toTask()
    }

  override suspend fun findAll(): List<Task> =
    client.findAwait(
      collection,
      query = json { obj() }
    ).map {
      it.toTask()
    }


  override suspend fun save(task: Task): Task = client.saveAwait(
    collection,
    document = task.toJson()
  ).let {
    task.id = it
    return@let task
  }

  override suspend fun deleteAll(): Long = client.removeDocumentsAwait(collection, json { obj { } }).removedCount

  override suspend fun delete(id: String): Task =
    client.findOneAndDeleteAwait(
      collection,
      query = json { obj { "_id" to id } }
    ).toTask()


  override suspend fun createAll(tasks: List<Task>): Long =
    tasks.map {
      BulkOperation.createInsert(it.toJson())
    }.run {
      client.bulkWriteAwait(collection, this).insertedCount
    }
}
