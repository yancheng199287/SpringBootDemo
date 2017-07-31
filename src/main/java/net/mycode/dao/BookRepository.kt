package net.mycode.dao

import net.mycode.entity.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

/**
 * Created by MAC on 2017/5/21.
 */

@Transactional
interface BookRepository : JpaRepository<Book, Int> {

    @Query("select name from Book where id=:id order by id desc ")
    fun getName(@Param("id") id: Int): String

    @Query("select name from Book where id=:id order by id desc")
    fun search(pageable: Pageable): Page<Book>
}