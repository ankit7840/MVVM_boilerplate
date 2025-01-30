package com.example.mvvm_practise.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.mvvm_practise.R
import com.example.mvvm_practise.model.DataEntity
import com.example.mvvm_practise.repository.UserRepository
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataScreen(viewModel: UserViewModel) {
    val data by viewModel.data.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)) // Light grayish-blue background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 56.dp) // Add padding to avoid overlap with the button
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF0077BE), // Ocean blue border color
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        Color(0xFF00BFFF),
                        shape = RoundedCornerShape(8.dp)
                    ), // Ocean blue background color
                placeholder = {
                    Text(
                        "Search by product name",
                        color = Color.White
                    )
                }, // White placeholder text
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.Gray,
                    errorTextColor = Color.Red,
                    focusedContainerColor = Color(0xFF00BFFF), // Ocean blue for focused state
                    unfocusedContainerColor = Color(0xFF00BFFF), // Ocean blue for unfocused state
                    disabledContainerColor = Color(0xFF00BFFF),
                    errorContainerColor = Color(0xFFFFCDD2),
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            val filteredData =
                data.filter { it.product_name.contains(searchQuery, ignoreCase = true) }

            LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                items(filteredData) { item ->
                    val index = filteredData.indexOf(item)
                    val offset = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.offset ?: 0
                    val scale = 1f - (index - offset / 1000f).coerceIn(0f, 0.5f)

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        DataEntityItem(
                            dataEntity = item,
                            modifier = Modifier.graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Add space between items
                }
            }
        }

        Button(
            onClick = { /* Handle button click */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000080)), // Navy blue color
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(13.dp)
        ) {
            Text(" Add Product ", color = Color.White)
        }
    }
}


@Composable
fun DataEntityItem(dataEntity: DataEntity, modifier: Modifier = Modifier) {

    val specificUrl =
        "https://thumbs.dreamstime.com/z/flat-isolated-vector-eps-illustration-icon-minimal-design-long-shadow-product-not-available-icon-117825338.jpg"
    var imageUrl by remember { mutableStateOf(dataEntity.product_image) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)) // Add shadow effect
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.Red, Color.Blue, Color.Green)
                ),
                shape = RoundedCornerShape(8.dp)
            ) // Add a mixed color border
            .clip(RoundedCornerShape(8.dp)) // Set rounded corners
            .background(Color(0xFFFFFFF0)) // Set the background color to ivory
            .height(250.dp) // Set fixed height
            .animateContentSize() // Animate size changes

    ) {

        val painter = if (imageUrl == specificUrl) {
            painterResource(id = R.drawable.error)
        } else {
            rememberImagePainter(data = imageUrl)
        }

        Image(
            painter = painter,
            contentDescription = "Product Image",
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(2.dp)
                .onSizeChanged { size ->
                    if (size.width < 100 || size.height < 100) {
                        imageUrl = specificUrl
                    }
                },
            // Add padding to separate the border from the image
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                text = "Name: ${dataEntity.product_name}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
            Text(
                text = "Price: ${dataEntity.product_price}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF006400), // Dark green color
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
            Text(
                text = "Type: ${dataEntity.product_type}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Blue,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
            Text(
                text = "Tax: ${dataEntity.product_tax}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Red,
                modifier = Modifier
                    .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
        }
    }
}

