package com.example.mvvm_practise.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.mvvm_practise.model.DataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertData(data: List<DataEntity>)

    @Query("SELECT * FROM data_table")
    fun getAllData(): Flow<List<DataEntity>>
}

@Database(entities = [DataEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}
