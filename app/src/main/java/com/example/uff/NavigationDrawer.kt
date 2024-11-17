package com.example.uff

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Drawer Header Composable
@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "LearnHub", fontSize = 30.sp, fontWeight = FontWeight.Bold)
    }
}

// Drawer Body Composable - Displays a list of nav items
@Composable
fun DrawerBody(
    items: List<NavItem>,
    onItemClick: (Int) -> Unit, // Callback for item click
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        items.forEachIndexed { index, navItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(index) } // Handle click to navigate
                    .padding(16.dp), // Optional: Padding to make the item look good
                verticalAlignment = Alignment.CenterVertically // Align icon and text vertically
            ) {
                // Icon
                Icon(
                    imageVector = navItem.icon,
                    contentDescription = navItem.label,
                    modifier = Modifier.size(24.dp) // Set the size of the icon
                )
                Spacer(modifier = Modifier.width(16.dp)) // Spacer between icon and text
                // Text
                Text(
                    text = navItem.label,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)
                )
            }
            Divider() // Optional: Divider between items
        }
    }
}

// This is the full NavigationDrawer composable that combines the header and body
@Composable
fun NavigationDrawer(
    items: List<NavItem>,
    onItemClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        DrawerHeader() // Header of the drawer
        DrawerBody(items = items, onItemClick = onItemClick) // Body of the drawer
    }
}

//implementation ("androidx.compose.material3:material3:<latest_version>")