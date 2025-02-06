package com.example.mvvm_practise.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mvvm_practise.model.TaskEntity
import com.example.mvvm_practise.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableStateFlow<List<TaskEntity>>(emptyList())
    val data: StateFlow<List<TaskEntity>> = _data


    init {
        checkDataPresence()
    }

    private fun checkDataPresence() {
        viewModelScope.launch {
            val isDataPresent = repository.isDataPresent()
            // Handle the result as needed
            if (!isDataPresent) {
                fetchDataAndUpdateTask()
            }
            repository.getAllTasks().collect { tasks -> // ✅ Use `collect`
                _data.value = tasks
            }
        }
    }

    private fun fetchDataAndUpdateTask() {
        viewModelScope.launch {
            repository.fetchTaskandStore()
        }
    }

    fun addTask(title: String, title_description: String, priority: String) {
        viewModelScope.launch {
            repository.addTask(title, title_description, priority)
            _data.value = repository.getAllTasks().first() // Refresh the task list
        }
    }

    fun removeTask(taskEntity: TaskEntity) {
        viewModelScope.launch {
            repository.removeTask(taskEntity.id)
            repository.getAllTasks().collect { updatedList ->
                _data.value = updatedList
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

