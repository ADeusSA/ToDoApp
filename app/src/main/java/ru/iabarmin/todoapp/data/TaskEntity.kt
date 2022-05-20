package ru.iabarmin.todoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    var taskId: Int = 0,

    @ColumnInfo(name = "title")
    var mTitle: String,

    @ColumnInfo(name = "description")
    var mDescription: String,

    @ColumnInfo(name = "user_name")
    var userName: String,

    @ColumnInfo(name = "password_text")
    var passwrd: String
)
