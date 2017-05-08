package net.mycode.service

import net.mycode.dao.UserRepository
import net.mycode.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by MAC on 2017/5/7.
 */

@Service
class UserService {

    @Autowired
    lateinit  var userRepository : UserRepository


    fun  save(user: User): User {
        return userRepository.save(user)
    }

    fun get(id: Int): User {

        return userRepository.getOne(id)
    }



}