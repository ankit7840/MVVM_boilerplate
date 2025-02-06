package com.example.mvvm_practise.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mvvm_practise.model.AddProductResponse
import com.example.mvvm_practise.model.DataEntity
import com.example.mvvm_practise.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableStateFlow<List<DataEntity>>(emptyList())
    val data: StateFlow<List<DataEntity>> = _data


    private val _addProductResult = MutableLiveData<Result<AddProductResponse>>()
    val addProductResult: LiveData<Result<AddProductResponse>> get() = _addProductResult


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchDataAndUpdate()
    }

    private fun fetchDataAndUpdate() {
        viewModelScope.launch {
            repository.fetchDataFromApi()
            _data.value = repository.getAllData().first()
        }
    }

    fun addProduct(name: String, price: Float, type: String, tax: Float) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val result = repository.addProduct(name, price, type, tax)
                _addProductResult.postValue(result)
            } catch (e: Exception) {
                _addProductResult.postValue(Result.failure(e))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}


class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

