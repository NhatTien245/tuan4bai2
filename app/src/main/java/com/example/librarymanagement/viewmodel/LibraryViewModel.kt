package com.example.librarymanagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.librarymanagement.model.Book
import com.example.librarymanagement.model.Novel
import com.example.librarymanagement.model.Student
import com.example.librarymanagement.model.TextBook

class LibraryViewModel : ViewModel {
    // Danh sách sinh viên
    val students = mutableStateListOf<Student>()
    
    // Sinh viên đang được chọn thao tác
    var currentStudent by mutableStateOf<Student?>(null)
        private set

    // Danh sách sách trong thư viện
    val books = mutableStateListOf<Book>()

    constructor() {
        // Dữ liệu mẫu (Sample data)
        val s1 = Student("S1", "Nguyen Van A", "SV001")
        val s2 = Student("S2", "Tran Thi B", "SV002")
        students.add(s1)
        students.add(s2)
        currentStudent = s1

        // Tạo danh sách sách đa dạng (thể hiện tính kế thừa và đa hình)
        books.add(TextBook("B1", "Sách 01", "Bộ Giáo Dục", 2023, "Toán học", 10))
        books.add(TextBook("B2", "Sách 02", "Bộ Giáo Dục", 2023, "Vật lý", 11))
        books.add(Novel("B3", "Nhà Giả Kim", "Paulo Coelho", 1988, "Phiêu lưu", 25))
        books.add(Novel("B4", "Dế Mèn Phiêu Lưu Ký", "Tô Hoài", 1941, "Thiếu nhi", 10))
    }

    fun selectStudent(student: Student) {
        currentStudent = student
    }

    fun updateCurrentStudentName(newName: String) {
        currentStudent?.let {
            it.name = newName
            // trigger state change by re-assigning or copying if needed
            currentStudent = it
        }
    }

    fun addStudent(name: String, code: String) {
        if (name.isNotBlank() && code.isNotBlank()) {
            val newS = Student("S${students.size + 1}", name, code)
            students.add(newS)
            if (currentStudent == null) {
                currentStudent = newS
            }
        }
    }

    fun addBook(book: Book) {
        books.add(book)
    }

    fun borrowBook(book: Book): Boolean {
        val student = currentStudent ?: return false
        return student.borrowBook(book)
    }

    fun returnBook(book: Book): Boolean {
        val student = currentStudent ?: return false
        return student.returnBook(book)
    }
}
