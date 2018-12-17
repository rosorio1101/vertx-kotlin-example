package io.rosorio.kotlinExample.task

import java.util.*

data class Task(
  var id: String? = null,
  var name: String,
  var createdAt: Date = Date(),
  var updatedAt: Date? = null,
  var isDone: Boolean? = false
)
