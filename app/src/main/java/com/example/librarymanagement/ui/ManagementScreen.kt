package com.example.librarymanagement.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.librarymanagement.model.Book
import com.example.librarymanagement.viewmodel.LibraryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementScreen(viewModel: LibraryViewModel) {
    var studentNameInput by remember(viewModel.currentStudent) {
        mutableStateOf(viewModel.currentStudent?.name ?: "")
    }
    
    var showStudentDialog by remember { mutableStateOf(false) }
    var showAddStudentDialog by remember { mutableStateOf(false) }
    
    var newStudentName by remember { mutableStateOf("") }
    var newStudentCode by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Tiêu đề hệ thống
            Text(
                text = "Hệ thống\nQuản lý Thư viện",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Phần Sinh viên
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sinh viên mượn sách",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )
                TextButton(onClick = { showAddStudentDialog = true }) {
                    Text("+ Thêm SV mới", fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = studentNameInput,
                    onValueChange = { studentNameInput = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    label = { Text("Tên sinh viên") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (studentNameInput.isNotBlank()) {
                            viewModel.updateCurrentStudentName(studentNameInput)
                            scope.launch {
                                snackbarHostState.showSnackbar("Đã thay đổi tên sinh viên thành: $studentNameInput")
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Thay đổi")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nút chuyển sinh viên khác
            OutlinedButton(
                onClick = { showStudentDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("📋 Chọn sinh viên khác (${viewModel.currentStudent?.name ?: "Chưa chọn"} - Mã: ${viewModel.currentStudent?.studentCode ?: ""})")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Danh sách sách
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Danh sách sách",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )
                Text(
                    text = "Đang mượn: ${viewModel.currentStudent?.borrowedBooks?.size ?: 0} cuốn",
                    fontSize = 13.sp,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.books) { book ->
                        BookItemRow(
                            book = book,
                            onBorrowToggle = { isChecked ->
                                val studentName = viewModel.currentStudent?.name ?: "Sinh viên"
                                if (isChecked) {
                                    viewModel.borrowBook(book)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("$studentName đã mượn: ${book.getTitle()}")
                                    }
                                } else {
                                    viewModel.returnBook(book)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("$studentName đã trả: ${book.getTitle()}")
                                    }
                                }
                            },
                            isCurrentStudentBorrowed = viewModel.currentStudent?.borrowedBooks?.contains(book) == true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nút Xác nhận mượn / Thêm
            Button(
                onClick = {
                    val count = viewModel.currentStudent?.borrowedBooks?.size ?: 0
                    val name = viewModel.currentStudent?.name ?: "Sinh viên"
                    scope.launch {
                        snackbarHostState.showSnackbar("Xác nhận: $name đang mượn tổng cộng $count cuốn sách.")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Xác nhận mượn sách (${viewModel.currentStudent?.borrowedBooks?.size ?: 0})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    // Dialog chọn sinh viên
    if (showStudentDialog) {
        AlertDialog(
            onDismissRequest = { showStudentDialog = false },
            title = { Text("Chọn sinh viên mượn sách") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    viewModel.students.forEach { student ->
                        OutlinedButton(
                            onClick = {
                                viewModel.selectStudent(student)
                                studentNameInput = student.name
                                showStudentDialog = false
                                scope.launch {
                                    snackbarHostState.showSnackbar("Đã chuyển sang sinh viên: ${student.name}")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("${student.name} (${student.studentCode})")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showStudentDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }

    // Dialog thêm sinh viên mới
    if (showAddStudentDialog) {
        AlertDialog(
            onDismissRequest = { showAddStudentDialog = false },
            title = { Text("Thêm sinh viên mới") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newStudentName,
                        onValueChange = { newStudentName = it },
                        label = { Text("Họ tên sinh viên") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newStudentCode,
                        onValueChange = { newStudentCode = it },
                        label = { Text("Mã sinh viên (VD: SV003)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (newStudentName.isNotBlank() && newStudentCode.isNotBlank()) {
                        viewModel.addStudent(newStudentName, newStudentCode)
                        // Tự động chọn sinh viên mới thêm
                        viewModel.students.lastOrNull()?.let {
                            viewModel.selectStudent(it)
                            studentNameInput = it.name
                        }
                        showAddStudentDialog = false
                        newStudentName = ""
                        newStudentCode = ""
                        scope.launch {
                            snackbarHostState.showSnackbar("Đã thêm và chuyển sang sinh viên mới thành công!")
                        }
                    }
                }) {
                    Text("Thêm & Chọn")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddStudentDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun BookItemRow(
    book: Book,
    onBorrowToggle: (Boolean) -> Unit,
    isCurrentStudentBorrowed: Boolean
) {
    var checked by remember(isCurrentStudentBorrowed) { mutableStateOf(isCurrentStudentBorrowed) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = { newChecked ->
                    if (!book.isBorrowed || isCurrentStudentBorrowed) {
                        checked = newChecked
                        onBorrowToggle(newChecked)
                    }
                },
                enabled = !book.isBorrowed || isCurrentStudentBorrowed
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.getTitle(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = book.getDetails(), // Đa hình (Polymorphism)
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (book.isBorrowed && !isCurrentStudentBorrowed) {
                    Text(
                        text = "(Đã có người mượn)",
                        fontSize = 11.sp,
                        color = Color.Red
                    )
                }
            }
        }
    }
}
