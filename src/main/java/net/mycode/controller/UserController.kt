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
import net.mycode.service.EhcacheService
import net.mycode.service.RedisService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.apache.shiro.web.servlet.SimpleCookie
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.cache.jcache.JCacheCacheManager
import org.springframework.context.annotation.Scope
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.RequestMethod
import java.util.concurrent.Future
import org.springframework.web.bind.annotation.PathVariable
import javax.annotation.Resource
import javax.cache.annotation.CachePut
import javax.cache.annotation.CacheResult
import javax.cache.annotation.CacheValue
import javax.persistence.Cacheable


/**
 * Created by MAC on 2017/5/7.
 */

@Controller
/***
 *   @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
 *   spring中默认所有的bean都是单例  通过 @Scope注解可以指定多例或者单例
 *
 *   如果一个bean是单例， 那么这个bean中的成员变量只会初始化一次， 它将会被缓存起来
 *
 *   在控制层中，如果是单例模式bean，定义了成员变量，如下面的 temp变量，在多个action中互相调用 会有线程安全问题，产生脏数据
 *
 *   建议是 如果你的Controller 不存在成员变量被共享使用的情况  可默认为单例模式，性能会更好
 *   否则定义为多例
 * */
open class UserController {

    private var temp: Int = 10

    private val logger: Logger = LoggerFactory.getLogger(UserController::class.java)


    @Autowired
    lateinit var ehcacheService: EhcacheService

    // @Autowired
    // var jCacheCacheManager: JCacheCacheManager

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var asynctask: AsyncTask

    @Autowired
    lateinit var redisService: RedisService

    @Autowired
    lateinit var okHttpClient: OkHttpClient

    @Autowired
    lateinit var myAuthorizationFilter: MyAuthorizationFilter

    @Autowired
    lateinit var myRealm: MyRealm

    @Autowired
    lateinit var redisSessionDao: RedisSessionDao

    @Resource(name = "rememberMeCookie")
    lateinit var simpleCookie: SimpleCookie


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
            asynctask.doTask2()
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
        var sss = simpleCookie.maxAge
        redisService.setKeyValue("test", "5201314")
        logger.info("获取key  :test=====" + redisService.getValue("test"))
        var log = " myAuthorizationFilter:${myAuthorizationFilter} myRealm:${myRealm} redisSessionDao:${redisSessionDao}"
        return "ok==maxAge:${sss}========${log}"
    }


    @RequestMapping(value = "/testrequest", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun doRequest(): String {
        var data: Int = 23
        data += 15
        temp += 12
        var request: Request = Request.Builder().url("http://baike.baidu.com/api/openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_key=关键字&bk_length=600").build()
        var response: Response = okHttpClient.newCall(request).execute()
        var result = response.body()!!.string()
        println("${okHttpClient.hashCode()}   result: temp: $temp   data: $data =======>:" + result)
        return result
    }


    @RequestMapping(value = "/testcache", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun doRequest01(): String {
        println("测试echcache 缓存。。。。")
        return ehcacheService.saveUser("孙尚香").toString()
    }

    @RequestMapping(value = "/testcache001", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun doRequest02(): String {
        println("测试echcache 缓存。。。。")
        return ehcacheService.saveUser001("周瑜").toString()
    }

}