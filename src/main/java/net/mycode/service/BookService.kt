package net.mycode.service

import net.mycode.dao.BookRepository
import net.mycode.entity.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by MAC on 2017/5/21.
 */

@Service
open class BookService {

    @Autowired
    lateinit var bookRepository: BookRepository

    fun save(book: Book): Book {
        return bookRepository.save(book)
    }

    fun get(id: Int): Book {
        return bookRepository.getOne(id)
    }

    fun getName(id: Int): String {
        return bookRepository.getName(id)
    }
}