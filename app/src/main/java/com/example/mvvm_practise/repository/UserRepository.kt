package com.example.mvvm_practise.repository
import android.content.Context
import androidx.room.Room
import com.example.mvvm_practise.db.AppDatabase
import com.example.mvvm_practise.model.DataEntity
import com.example.mvvm_practise.service.ApiService
import kotlinx.coroutines.Dispatchers
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
    private val apiService: ApiService = Retrofit.Builder()
        .baseUrl("https://app.getswipe.in/api/public/get")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    // function that will be called from the viewmodel fetching data and posting it to the database
    suspend fun fetchDataFromApi() {
        val response = apiService.fetchData()
        val defaultImageUrl =
            "https://p.kindpng.com/picc/s/325-3256258_purchase-svg-free-product-icon-png-transparent-png.png"

        val entities = response.products.map { product ->
            DataEntity(
                id = 0,
                product_image = if (product.image.isEmpty()) defaultImageUrl else product.image,
                product_price = product.price,
                product_name = product.product_name,
                product_type = product.product_type,
                product_tax = product.tax
            )
        }
        withContext(Dispatchers.IO) {
            dataDao.insertData(entities)
        }
    }


    // this fucntions will provide the data after fetching and storing it into the database
    fun getAllData(): Flow<List<DataEntity>> = dataDao.getAllData()
}