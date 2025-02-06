package com.example.mvvm_practise.service

import com.example.mvvm_practise.model.AddProductResponse
import com.example.mvvm_practise.model.Product
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    //  for adding extra part of the url after base url
    @GET("get")
    suspend fun fetchData(): List<Product>

    @Multipart
    @POST("add")
    suspend fun addProduct(
        @Part("product_name") name: String,
        @Part("product_type") type: String,
        @Part("price") price: String,
        @Part("tax") tax: String,
        @Part files: List<MultipartBody.Part> = emptyList()
    ): Response<AddProductResponse>
}

