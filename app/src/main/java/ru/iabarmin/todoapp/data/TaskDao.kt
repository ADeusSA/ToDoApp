package ru.iabarmin.todoapp.data

import androidx.room.*
import androidx.lifecycle.LiveData

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_table ORDER BY timestamp DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("select * from task_table order by priority asc")
    fun getAllPriorityTasks(): LiveData<List<Task>>

    @Query("select * from task_table where title like :searchQuery order by timestamp desc")
    fun searchDatabase(searchQuery: String): LiveData<List<Task>>

//    @Insert
//    suspend fun insertMany(tasks: LiveData<List<Task>>)
}