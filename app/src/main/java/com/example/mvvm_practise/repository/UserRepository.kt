package com.example.mvvm_practise.repository
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mvvm_practise.db.AppDatabase
import com.example.mvvm_practise.model.AddProductResponse
import com.example.mvvm_practise.model.DataEntity
import com.example.mvvm_practise.model.ErrorResponse
import com.example.mvvm_practise.service.ApiService
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserRepository(context : Context) {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Define the migration strategy here
            // For example, if you added a new column:
            // database.execSQL("ALTER TABLE DataEntity ADD COLUMN new_column INTEGER NOT NULL DEFAULT 0")
        }
    }
    //  defining Database and DataDao for database operations
    // Define the Room database with migration
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app_database"
    ).addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigration()
        .build()


    private val dataDao = database.dataDao()
    private val apiService: ApiService = Retrofit.Builder()
        .baseUrl("https://app.getswipe.in/api/public/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    // function that will be called from the viewmodel fetching data and posting it to the database
    suspend fun fetchDataFromApi() {
        val response = apiService.fetchData()
        val defaultImageUrl =
            "https://thumbs.dreamstime.com/z/flat-isolated-vector-eps-illustration-icon-minimal-design-long-shadow-product-not-available-icon-117825338.jpg"
        val entities = response.map { product ->
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


    suspend fun addProduct(
        name: String,
        price: Float,
        type: String,
        tax: Float
    ): Result<AddProductResponse> {
        return withContext(Dispatchers.IO) {
            val response: Response<AddProductResponse> = apiService.addProduct(
                name = name,
                type = type,
                price = price,
                tax = tax
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        Result.failure(Exception(errorResponse.message))
                    } catch (e: JsonSyntaxException) {
                        Result.failure(Exception("JSON parsing error"))
                    }
                } else {
                    Result.failure(Exception("Unknown error"))
                }
            }
        }
    }


}