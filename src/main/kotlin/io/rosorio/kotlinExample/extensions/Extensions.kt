package io.rosorio.kotlinExample.extensions

import io.rosorio.kotlinExample.task.Task
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import java.text.SimpleDateFormat
import java.util.*

fun Date.format(format: String): String = SimpleDateFormat(format).format(this)

fun String.toDate(format: String): Date = SimpleDateFormat(format).parse(this)

fun JsonObject.toTask(): Task = Task(
  this.getString("_id"),
  this.getString("name"),
  this.getString("createdAt").toDate(DATE_FORMAT),
  this.getString("updatedAt")?.toDate(DATE_FORMAT),
  this.getBoolean("isDone")
)

fun Task.toJson(): JsonObject = json {
  return if (this@toJson.id == null) {
    obj(
      "name" to this@toJson.name,
      "createdAt" to this@toJson.createdAt.format(DATE_FORMAT),
      "updatedAt" to this@toJson.updatedAt?.format(DATE_FORMAT),
      "isDone" to this@toJson.isDone
    )
  } else {
    obj(
      "_id" to this@toJson.id,
      "name" to this@toJson.name,
      "createdAt" to this@toJson.createdAt.format(DATE_FORMAT),
      "updatedAt" to this@toJson.updatedAt?.format(DATE_FORMAT),
      "isDone" to this@toJson.isDone
    )
  }

}

fun List<Task>.toJson() = json {
  array(this@toJson.map { it.toJson() })
}
