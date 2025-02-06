package com.example.mvvm_practise.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvm_practise.model.AddProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: AddProductEntity)

    @Query("SELECT * FROM addProductTable")
    fun getAllProducts(): Flow<List<AddProductEntity>>
}