package com.example.mvvm_practise.repository
import android.content.Context
import androidx.room.Room
import com.example.mvvm_practise.db.AppDatabase
import com.example.mvvm_practise.model.ApiResponse
import com.example.mvvm_practise.model.DataEntity
import com.example.mvvm_practise.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserRepository(context : Context) {
    //  defining Database and DataDao for database operations
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app_database"
    ).build()
    private val dataDao = database.dataDao()

    //  implementing api service  interface with retrofit builder for HTTP operations
    private val apiService: ApiService = Retrofit.Builder()
        .baseUrl("https://api.example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    // function that will be called from the viewmodel fetching data and posting it to the database
    suspend fun fetchDataFromApi() {
//        val response = apiService.fetchData()
        delay(500)
        //  dummy data for testing
        val response = listOf(
            ApiResponse(1, "Item 1", "Description 1"),
            ApiResponse(2, "Item 2", "Description 2"),
            ApiResponse(3, "Item 3", "Description 3")
        )
        val entities = response.map { ApiResponse ->
            DataEntity(id = 0, ApiResponse.name, ApiResponse.description)
        }
        withContext(Dispatchers.IO) {
            dataDao.insertData(entities)
        }
    }
    // this fucntions will provide the data after fetching and storing it into the database
    fun getAllData(): Flow<List<DataEntity>> = dataDao.getAllData()
}