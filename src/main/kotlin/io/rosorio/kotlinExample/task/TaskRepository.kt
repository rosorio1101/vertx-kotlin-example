package io.rosorio.kotlinExample.task

interface TaskRepository {
  suspend fun findOne(id: String): Task?
  suspend fun findAll(): List<Task>
  suspend fun save(task: Task): Task
  suspend fun delete(id: String): Task
  suspend fun deleteAll(): Long
  suspend fun createAll(tasks: List<Task>): Long
}
