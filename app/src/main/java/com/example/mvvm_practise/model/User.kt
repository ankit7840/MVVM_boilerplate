package com.example.mvvm_practise.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val name: String,
    val description: String
)

data class ApiResponse(val id: Int, val name: String, val description: String)

