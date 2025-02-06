package com.example.mvvm_practise.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.mvvm_practise.model.TaskEntity
import com.example.mvvm_practise.repository.UserRepository
import com.example.mvvm_practise.viewModel.UserViewModel
import com.example.mvvm_practise.viewModel.UserViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataScreen(viewModel: UserViewModel) {
    val context = LocalContext.current
    val data by viewModel.data.collectAsState(initial = emptyList())
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 56.dp)
        ) {
            val filteredData = data.sortedBy { it.dueDate }

            LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                items(filteredData, key = { it.id }) { item ->
                    var dismissed by remember { mutableStateOf(false) }

                    if (!dismissed) {
                        DataEntityItem(
                            taskEntity = item,
                            onDismiss = {
                                dismissed = true
                                scope.launch {
                                    viewModel.removeTask(item)
                                }
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                val activity = context as? FragmentActivity
                activity?.let {
                    AddProductBottomSheetDialogFragment().show(
                        it.supportFragmentManager,
                        "AddProductBottomSheetDialogFragment"
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000080)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(13.dp)
        ) {
            Text("ADD TASK", color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataEntityItem(taskEntity: TaskEntity, onDismiss: () -> Unit) {
    var isVisible by remember { mutableStateOf(true) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                isVisible = false  // Start animation
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(isVisible) {
        if (!isVisible) {
            delay(300) // Adjust delay to match animation duration
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        exit = fadeOut() + shrinkVertically(), // Fade out + Collapse upwards
        modifier = Modifier.animateContentSize()
    ) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Deleting...",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            },
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White)
                        .wrapContentHeight()
                        .animateContentSize() // Ensures smooth collapse animation
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Task Title: ${taskEntity.title}",
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Task Description: ${taskEntity.description}",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF006400)
                        )
                        Text(
                            text = "Due Date: ${taskEntity.dueDate}",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontWeight = FontWeight.Light,
                            color = Color.Blue
                        )
                        Text(
                            text = "Priority: ${taskEntity.priority}",
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontWeight = FontWeight.Normal,
                            color = when (taskEntity.priority) {
                                "High" -> Color.Red
                                "Medium" -> Color.Yellow
                                "Low" -> Color.Green
                                else -> Color.Black
                            }
                        )
                    }
                }
            }
        )
    }
}


