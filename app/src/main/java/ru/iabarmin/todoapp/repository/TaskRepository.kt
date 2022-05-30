package ru.iabarmin.todoapp.repository

import androidx.lifecycle.LiveData
import ru.iabarmin.todoapp.data.Task
import ru.iabarmin.todoapp.data.TaskDao

class TaskRepository(val taskDao: TaskDao) {

    suspend fun insert(task: Task) = taskDao.insert(task)

    suspend fun updateData(task: Task) = taskDao.update(task)

    suspend fun deleteItem(task: Task) = taskDao.delete(task)

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }

    fun getAllTasks() : LiveData<List<Task>> = taskDao.getAllTasks()

    fun getAllPriorityTasks() : LiveData<List<Task>> = taskDao.getAllPriorityTasks()

    fun searchDatabase(searchQuery: String): LiveData<List<Task>> {
        return taskDao.searchDatabase(searchQuery)
    }

    //suspend fun insertMany(tasks: LiveData<List<Task>>) = taskDao.insertMany(tasks)
}