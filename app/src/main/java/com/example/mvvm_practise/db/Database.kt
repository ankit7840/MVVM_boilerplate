package com.example.mvvm_practise.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvvm_practise.model.DateConverter
import com.example.mvvm_practise.model.TaskDao
import com.example.mvvm_practise.model.TaskEntity

//database setup
@Database(
    entities = [TaskEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
