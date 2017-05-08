package net.mycode.dao

import net.mycode.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

/**
 * Created by MAC on 2017/5/7.
 */



@Transactional
interface UserRepository : JpaRepository<net.mycode.entity.User, Int> {
    fun save(user: User): User
}