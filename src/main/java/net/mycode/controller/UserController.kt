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
import net.mycode.component.AsyncTask
import net.mycode.component.shiro.MyAuthorizationFilter
import net.mycode.component.shiro.MyRealm
import net.mycode.component.shiro.RedisSessionDao
import net.mycode.service.RedisService
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.RequestMethod
import java.util.concurrent.Future
import org.springframework.web.bind.annotation.PathVariable


/**
 * Created by MAC on 2017/5/7.
 */

@Controller
class UserController {

    private val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var asynctask: AsyncTask

    @Autowired
    lateinit var redisService: RedisService



    @Autowired
    lateinit var myAuthorizationFilter: MyAuthorizationFilter

    @Autowired
    lateinit var myRealm: MyRealm

    @Autowired
    lateinit var redisSessionDao: RedisSessionDao




    @RequestMapping("/index")
    @ResponseBody
    fun index(): String {
        logger.info("正在访问我的首页....")
        var user = User()
        user.name = "小明"
        user.age = 19
        user.addTime = Timestamp(Date().time)

        //  userService.save(user)
        return "你访问了我的首页！"
    }

    @RequestMapping("/default")
    @ResponseBody
    fun default(): User {
//        logger.info("正在访问我的首页....")
        var user = User()
        user.name = "小明"
        user.age = 19
        user.addTime = Timestamp(Date().time)

        userService.save(user)
        return user
    }

    //, produces = arrayOf("application/json") 可以省略
    @RequestMapping(value = "/UpdateMember", method = arrayOf(RequestMethod.POST), produces = arrayOf("application/json"))
    @ResponseBody
    fun UpdateMember(user: User): Map<*, *> {
        logger.info("adminMember:=========" + user.toString())

        val dataMap = mapOf<String, Any>("a" to 1, "王子个" to 2, "李宇春" to 3, "user" to user)
        //dataMap.plus()

        return dataMap
    }


    @RequestMapping("/user")
    @ResponseBody
    fun getUser(): String {
        logger.info("正在访问我的首页....")
        var user = userService.get(7)
        var s = user.toString()
        logger.info("正在访问我的首页....getUsergetUser")

        print("哈哈，为什么 logger打印出来乱码呢。======>" + s)
        println("打印出来吧 。。。")
        return "你访问了getUser方法！ $s"
    }


    @RequestMapping(value = "/task1", method = arrayOf(RequestMethod.GET), produces = arrayOf("text/html;charset=UTF-8"))
    @ResponseBody
    fun task1(): String {
        println("正在访问我的首页....")
        for (i in 0..2) {
            //如果直接调用，不需要返回值，当前线程不会等待异步线程的结果
            asynctask.doTask1()
            //如果需要异步处理方法的返回结果   当前线程会阻塞等待异步线程的结果
            //  var result: Future<String> = asynctask.doTask1()
            //var s = "isDone:" + result.isDone + ",  isCancelled: " + result.isCancelled + ",  get()" + result.get()
            // println(s)
        }

        return "哈哈哈，不会是乱码吧，我正在处理异步任务，你可以选择阻塞等待异步结果，也可以不用等待，这取决你怎么调用方法！ task1"
    }


    /**
     * 查询单个用户   restful风格   如果userId是字符串类型，可以是任何字符串，包括json {userId}  可以放置在url上

     * @param userId
     * *
     * @return
     */
    @RequestMapping(value = "/user/{userId}", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    operator fun get(@PathVariable("userId") userId: Int): User {
        logger.info("获取用户userId=" + userId)
        var user = userService.get(userId)
        return user
    }

    @RequestMapping(value = "/testredis", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun addValue(): String {
        redisService.setKeyValue("test", "5201314")
        logger.info("获取key  :test=====" + redisService.getValue("test"))
        var log=" myAuthorizationFilter:${myAuthorizationFilter} myRealm:${myRealm} redisSessionDao:${redisSessionDao}"
        return "ok===>${log}"
    }


}