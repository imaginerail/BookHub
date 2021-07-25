package com.aneeq.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.aneeq.bookhub.model.Book

@Dao
interface BookDao {
    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT * FROM book")
    fun getAllBooks():List<BookEntity>
    @Query("SELECT * FROM book WHERE bookId=:bookId ")
    fun getBookById(bookId:String):BookEntity

}