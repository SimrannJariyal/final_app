package com.example.uff.pages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.uff.R
import com.example.uff.viewmodels.NotificationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Get the screen width in dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = screenWidth * 0.6f // 60% of the screen width

    // ModalNavigationDrawer with content constrained to 60% width
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(0.dp, drawerWidth) // Set width to 60% of the screen width
            )
        }
    ) {
        // Content of the HomeScreen
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFE1D9D9))
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Carousel slider (unchanged)
            val carouselItems = listOf(
                R.drawable.slider1,
                R.drawable.slider2,
                R.drawable.slider3,
                R.drawable.slider4
            )

            val listState = rememberLazyListState()

            LazyRow(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                items(carouselItems.size) { index ->
                    val imageIndex = index % carouselItems.size
                    CarouselItem(imageResId = carouselItems[imageIndex])
                }
            }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(100)  // Auto-scroll delay
                    val nextIndex = (listState.firstVisibleItemIndex + 1) % carouselItems.size
                    coroutineScope.launch {
                        listState.animateScrollToItem(nextIndex)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Notes" card (unchanged)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEB3B)),
                shape = RoundedCornerShape(19.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Notes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display subjects in a vertical list with icons on the left
            val viewModel: NotificationViewModel = viewModel()
            val subjects = viewModel.subjects.value // Extracting the list of subjects from the ViewModel

            if (subjects.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(subjects) { subject ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Navigate to UnitScreen with the selected subject ID
                                    navController.navigate("unitscreen/${subject.id}")
                                }
                                .padding(8.dp)
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),
                            verticalAlignment = Alignment.CenterVertically // Align the icon and text vertically
                        ) {
                            // Load and display the sub_icon (either URL or resource ID)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp) // Icon size
                                    .background(Color.LightGray, shape = RoundedCornerShape(50)) // Circular background
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = subject.sub_icon // Assuming it's a URL
                                    ),
                                    contentDescription = subject.name,
                                    modifier = Modifier
                                        .size(30.dp) // Size of the icon
                                        .padding(4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp)) // Space between the icon and text

                            // Column for the subject's name and description
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = subject.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = subject.description,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "No subjects available.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}


@Composable
fun CarouselItem(imageResId: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

