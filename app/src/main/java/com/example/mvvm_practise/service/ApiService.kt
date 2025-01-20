package com.example.mvvm_practise.service
import com.example.mvvm_practise.model.ApiResponse
import retrofit2.http.GET

interface ApiService {
    //  for adding extra part of the url after base url
    @GET("endpoint")
    suspend fun fetchData(): List<ApiResponse>
}

