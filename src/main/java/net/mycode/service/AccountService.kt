package net.mycode.service

import net.mycode.dao.AccountRepository
import net.mycode.entity.Account
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by MAC on 2017/5/21.
 */

@Service
open class AccountService {

    @Autowired
    lateinit var accountRepository: AccountRepository

    fun save(account: Account): Account {
       return accountRepository.save(account)
    }

    fun get(id: Int): Account {
        return accountRepository.getOne(id)
    }
}