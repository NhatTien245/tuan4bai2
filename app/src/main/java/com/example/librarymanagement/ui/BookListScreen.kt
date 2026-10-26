package com.example.librarymanagement.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.librarymanagement.model.Novel
import com.example.librarymanagement.model.TextBook
import com.example.librarymanagement.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(viewModel: LibraryViewModel) {
    var showAddDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var isTextBook by remember { mutableStateOf(true) }
    var extra1 by remember { mutableStateOf("") } // Subject / Genre
    var extra2 by remember { mutableStateOf("") } // Grade / Chapters

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh Sách Sách (OOP Polymorphism)") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1976D2), titleContentColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Thêm sách")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(viewModel.books) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = book.getTitle(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1976D2)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Gọi phương thức getDetails() đa hình (Polymorphism)
                        Text(
                            text = book.getDetails(),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (book.isBorrowed) "Trạng thái: Đã mượn" else "Trạng thái: Có sẵn",
                            fontSize = 12.sp,
                            color = if (book.isBorrowed) Color.Red else Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Thêm sách mới") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Tên sách") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Tác giả") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it },
                        label = { Text("Năm xuất bản") },
                        singleLine = true
                    )
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        RadioButton(selected = isTextBook, onClick = { isTextBook = true })
                        Text("Sách giáo khoa")
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(selected = !isTextBook, onClick = { isTextBook = false })
                        Text("Tiểu thuyết")
                    }
                    if (isTextBook) {
                        OutlinedTextField(
                            value = extra1,
                            onValueChange = { extra1 = it },
                            label = { Text("Môn học") },
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = extra2,
                            onValueChange = { extra2 = it },
                            label = { Text("Lớp") },
                            singleLine = true
                        )
                    } else {
                        OutlinedTextField(
                            value = extra1,
                            onValueChange = { extra1 = it },
                            label = { Text("Thể loại") },
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = extra2,
                            onValueChange = { extra2 = it },
                            label = { Text("Số chương") },
                            singleLine = true
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val y = year.toIntOrNull() ?: 2024
                    if (isTextBook) {
                        val grade = extra2.toIntOrNull() ?: 10
                        val newBook = TextBook("B${viewModel.books.size + 1}", title, author, y, extra1, grade)
                        viewModel.addBook(newBook)
                    } else {
                        val ch = extra2.toIntOrNull() ?: 1
                        val newBook = Novel("B${viewModel.books.size + 1}", title, author, y, extra1, ch)
                        viewModel.addBook(newBook)
                    }
                    showAddDialog = false
                    title = ""
                    author = ""
                    year = ""
                    extra1 = ""
                    extra2 = ""
                }) {
                    Text("Thêm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
