package ru.iabarmin.todoapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.iabarmin.todoapp.data.Task
import ru.iabarmin.todoapp.data.TaskDatabase
import ru.iabarmin.todoapp.repository.TaskRepository

class TaskViewModel(application: Application): AndroidViewModel(application) {

    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository: TaskRepository

    val getAllTasks: LiveData<List<Task>>
    val getAllPriorityTasks: LiveData<List<Task>>

    init {
        repository = TaskRepository(taskDao)
        getAllTasks = repository.getAllTasks()
        getAllPriorityTasks = repository.getAllPriorityTasks()
    }

    fun insert(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(task)
        }
    }

    fun delete(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteItem(task)
        }
    }
    fun update(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateData(task)
        }
    }
    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAll()
        }
    }
    fun searchDatabase(searchQuery: String): LiveData<List<Task>> {
        return repository.searchDatabase(searchQuery)
    }
}