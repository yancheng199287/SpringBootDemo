package net.mycode.controller

import net.mycode.entity.User
import net.mycode.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.Timestamp
import java.util.*
import com.oracle.util.Checksums.update
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMethod



/**
 * Created by MAC on 2017/5/7.
 */

@Controller
class UserController {

    private val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @Autowired
    lateinit var userService: UserService


    @RequestMapping("/index")
    @ResponseBody
    fun index(): String {
        logger.info("正在访问我的首页....")
        var user = User()
        user.name = "小明"
        user.age = 19
        user.addTime = Timestamp(Date().time)

        userService.save(user)
        return "你访问了我的首页！"
    }

    @RequestMapping("/default")
    @ResponseBody
    fun default(): User {
        logger.info("正在访问我的首页....")
        var user = User()
        user.name = "小明"
        user.age = 19
        user.addTime = Timestamp(Date().time)

        userService.save(user)
        return user
    }

    @RequestMapping(value = "/UpdateMember", method = arrayOf(RequestMethod.POST), produces = arrayOf("application/json"))
    @ResponseBody
    fun UpdateMember(user: User): Map<*, *> {
        logger.info("adminMember:=========" + user.toString())

        val  dataMap= mapOf<String,Int>("a" to 1,"b" to 2,"c" to 3)

        return dataMap
    }





    @RequestMapping("/user")
    @ResponseBody
    fun getUser(): String {
        logger.info("正在访问我的首页....")
        var user = userService.get(7)
        var s = user.toString()
        logger.info("正在访问我的首页....getUsergetUser")

        print("哈哈，为什么 logger打印出来乱码呢。======>"+s)
        println("打印出来吧 。。。")
        return "你访问了getUser方法！ $s"
    }


}