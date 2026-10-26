package com.example.librarymanagement.model

// 1. TÍNH TRỪU TƯỢNG (Abstraction): Abstract class định nghĩa cấu trúc chung cho các mục trong thư viện
abstract class LibraryItem(
    val id: String,
    protected var titleName: String
) {
    abstract fun getDetails(): String
    
    open fun getTitle(): String = titleName
}

// 2. TÍNH KẾ THỪA (Inheritance) & ĐÓNG GÓI (Encapsulation): 
abstract class Book(
    id: String,
    title: String,
    private var _author: String,
    private var _year: Int,
    var isBorrowed: Boolean = false
) : LibraryItem(id, title) {

    var author: String
        get() = _author
        set(value) {
            if (value.isNotBlank()) _author = value
        }

    var year: Int
        get() = _year
        set(value) {
            if (value > 0) _year = value
        }

    // 3. TÍNH ĐA HÌNH (Polymorphism)
    abstract override fun getDetails(): String
}

// Lớp con 1: Sách giáo khoa (TextBook)
class TextBook(
    id: String,
    title: String,
    author: String,
    year: Int,
    val subject: String,
    val grade: Int
) : Book(id, title, author, year) {
    
    override fun getDetails(): String {
        return "Sách giáo khoa: $titleName (Tác giả: $author, Năm: $year, Môn: $subject, Lớp: $grade)"
    }
}

// Lớp con 2: Tiểu thuyết (Novel)
class Novel(
    id: String,
    title: String,
    author: String,
    year: Int,
    val genre: String,
    val chapters: Int
) : Book(id, title, author, year) {
    
    override fun getDetails(): String {
        return "Tiểu thuyết: $titleName (Tác giả: $author, Năm: $year, Thể loại: $genre, Số chương: $chapters)"
    }
}

// Lớp Sinh viên (Student)
class Student(
    val id: String,
    private var _name: String,
    private var _studentCode: String
) {
    var name: String
        get() = _name
        set(value) {
            if (value.isNotBlank()) _name = value
        }

    var studentCode: String
        get() = _studentCode
        set(value) {
            if (value.isNotBlank()) _studentCode = value
        }

    private val _borrowedBooks = mutableListOf<Book>()
    val borrowedBooks: List<Book> get() = _borrowedBooks

    fun borrowBook(book: Book): Boolean {
        if (!book.isBorrowed) {
            book.isBorrowed = true
            _borrowedBooks.add(book)
            return true
        }
        return false
    }

    fun returnBook(book: Book): Boolean {
        if (_borrowedBooks.contains(book)) {
            book.isBorrowed = false
            _borrowedBooks.remove(book)
            return true
        }
        return false
    }
}
