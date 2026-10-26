package com.example.librarymanagement.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.librarymanagement.viewmodel.LibraryViewModel

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Management : Screen("management", "Quản lý", Icons.Default.Home)
    object BookList : Screen("book_list", "DS Sách", Icons.Default.Book)
    object StudentList : Screen("student_list", "Sinh viên", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryApp(viewModel: LibraryViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf<Screen>(Screen.Management) }

    val items = listOf(
        Screen.Management,
        Screen.BookList,
        Screen.StudentList
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFF1976D2)
            ) {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selectedTab == screen,
                        onClick = { selectedTab = screen },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF1976D2),
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color(0xFF1976D2),
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                Screen.Management -> ManagementScreen(viewModel)
                Screen.BookList -> BookListScreen(viewModel)
                Screen.StudentList -> StudentListScreen(viewModel)
            }
        }
    }
}
