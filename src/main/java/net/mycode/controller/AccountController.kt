package net.mycode.controller

import net.mycode.entity.Account
import net.mycode.entity.Book
import net.mycode.service.AccountService
import net.mycode.service.BookService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * Created by MAC on 2017/5/21.
 */

@RestController
@RequestMapping("/account")
open class AccountController {

    private val logger: Logger = LoggerFactory.getLogger(UserController::class.java)


    @Autowired
    lateinit var accountService: AccountService
    @Autowired
    lateinit var bookService: BookService

    @RequestMapping("/index")
    fun index(): String {
        var account = Account(null, "wuzihe", "520code.net", Date(), Date())
        accountService.save(account)
        var book = Book(null, "三国演义", "四大名著之一", "罗贯中", Date(), account)
        bookService.save(book)
        return "你访问了AccountController控制器的index方法,${book.toString()}"
    }

    @RequestMapping(value = "/book/{accountId}")
    fun account(@PathVariable("accountId") accountId: Int): Account {
        /*var account = Account(null, "wuzihe", "520code.net", Date(), Date(),null)
        accountService.save(account)
        var book = Book(null, "三国演义", "四大名著之一", "罗贯中", Date(), account)
        bookService.save(book)*/

        var account = accountService.get(accountId)



        //  println("$$account")
        //  var book=bookService.get(accountId)
           return account
        //return "查询某个账户下的所有书籍, \n\n  "
    }

}