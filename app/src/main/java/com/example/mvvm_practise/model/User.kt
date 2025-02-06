package com.example.mvvm_practise.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val product_image: String,
    val product_name: String,
    val product_price: Float,
    val product_type: String,
    val product_tax: Float // Change from Int to Float
)

@Entity(tableName = "addProductTable")
data class AddProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val product_name: String,
    val product_type: String,
    val price: Float,
    val tax: Float
)


data class Product(
    val image: String,
    val price: Float,
    val product_name: String,
    val product_type: String,
    val tax: Float
)


data class AddProductResponse(
    val message: String,
    val product_details: String,
    val product_id: Int,
    val success: Boolean
)

data class ErrorResponse(
    val error_code: String,
    val message: String,
    val request_id: String,
    val success: Boolean
)

