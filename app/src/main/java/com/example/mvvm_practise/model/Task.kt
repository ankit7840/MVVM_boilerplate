package com.example.mvvm_practise.model

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import kotlinx.coroutines.flow.Flow
import java.util.Date


data class Task(
    val title: String,
    val description: String,
    val dueDate: Date,
    val priority: String
)


@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Date,
    val priority: String
)

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(tasks: List<TaskEntity>)

    @Query("SELECT COUNT(*) FROM task_table")
    fun getTaskCount(): Int


    @Query("SELECT * FROM task_table")
    fun getAllTasks(): Flow<List<TaskEntity>>


    @Query("DELETE FROM task_table WHERE id = :taskId")
    fun deleteTask(taskId: Int)


    @Query("UPDATE task_table SET title = :title, description = :description, dueDate = :dueDate, priority = :priority WHERE id = :id")
    fun updateTaskById(id: Int, title: String, description: String, dueDate: Long, priority: String)
}


class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

