package com.example.mvvm_practise.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import com.example.mvvm_practise.repository.UserRepository
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import com.example.mvvm_practise.viewModel.UserViewModel
import com.example.mvvm_practise.viewModel.UserViewModelFactory
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private  val viewModel: UserViewModel by viewModels{
        val repository = UserRepository(this)
        UserViewModelFactory(repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataScreen(viewModel)
        }
        }
    }

@Composable
fun DataScreen(viewModel: UserViewModel) {
    val data by viewModel.data.collectAsState(initial = emptyList())

    LazyColumn {
        items(data) { item ->
            BasicText(text = "${item}: ${item.description}")
        }
    }
}

