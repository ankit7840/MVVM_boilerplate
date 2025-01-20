package com.example.mvvm_practise.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mvvm_practise.model.DataEntity
import com.example.mvvm_practise.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableStateFlow<List<DataEntity>>(emptyList())
    val data: StateFlow<List<DataEntity>> = _data
    init {
        fetchDataAndUpdate()
    }
    private fun fetchDataAndUpdate() {
        viewModelScope.launch {
            repository.fetchDataFromApi()
            _data.value = repository.getAllData().first()
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

