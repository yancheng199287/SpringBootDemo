package net.mycode.dao

import net.mycode.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

/**
 * Created by MAC on 2017/5/21.
 */

@Transactional
interface BookRepository : JpaRepository<Book, Int> {
}