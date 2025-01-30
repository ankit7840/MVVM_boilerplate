package com.example.mvvm_practise.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val product_image: String,
    val product_price: Int,
    val product_name: String,
    val product_type: String,
    val product_tax: Int
)

data class Product(
    val image: String,
    val price: Int,
    val product_name: String,
    val product_type: String,
    val tax: Int
)

data class ApiResponse(val products: List<Product>)

