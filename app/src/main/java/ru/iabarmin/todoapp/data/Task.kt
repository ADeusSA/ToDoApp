package ru.iabarmin.todoapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: Int,
    var timestamp: Long,
): Parcelable
