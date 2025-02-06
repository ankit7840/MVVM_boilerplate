package com.example.mvvm_practise.repository

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mvvm_practise.db.AppDatabase
import com.example.mvvm_practise.model.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date


class UserRepository(context: Context) {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Define the migration strategy here
            // For example, if you added a new column:
            // database.execSQL("ALTER TABLE DataEntity ADD COLUMN new_column INTEGER NOT NULL DEFAULT 0")
        }
    }

    //  defining Database and DataDao for database operations
    // Define the Room database with migration
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app_database"
    ).addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigration()
        .build()

    private val addTaskDao = database.taskDao()

    // main function for CRUD operations
    suspend fun isDataPresent(): Boolean {
        return withContext(Dispatchers.IO) {
            addTaskDao.getTaskCount() > 0 // ✅ Correct coroutine usage
        }
    }

    suspend fun fetchTaskandStore() {
        val priorities = listOf("High", "Medium", "Low")
        val dummyTasks = List(20) { index ->
            TaskEntity(
                title = "Task $index",
                description = "Description for Task $index",
                dueDate = getRandomDate(),
                priority = priorities.random()
            )
        }
        withContext(Dispatchers.IO) {
            addTaskDao.insertTasks(dummyTasks)
        }
    }

    fun getAllTasks(): Flow<List<TaskEntity>> {
        return addTaskDao.getAllTasks() // ✅ No need for `suspend`
    }

    suspend fun removeTask(id: Int) {
        withContext(Dispatchers.IO) {
            addTaskDao.deleteTask(id)
        }
    }

    suspend fun updateTask(taskEntity: TaskEntity) {
        withContext(Dispatchers.IO) {
            addTaskDao.updateTaskById(
                taskEntity.id,
                taskEntity.title,
                taskEntity.description,
                taskEntity.dueDate.time,
                taskEntity.priority
            )
        }
    }
    suspend fun addTask(title: String, description: String, priority: String) {
        val task = TaskEntity(
            title = title,
            description = description,
            dueDate = getCurrentDate(), // want current date
            priority = priority
        )
        withContext(Dispatchers.IO) {
            addTaskDao.insertTask(task)
        }
    }


    //auxilary functions
    fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

    fun getRandomDate(): Date {
        val calendar = Calendar.getInstance()
        val year = (2020..2023).random()
        val month = (0..11).random()
        val day = (1..28).random() // To avoid issues with February
        calendar.set(year, month, day)
        return calendar.time
    }

}