package net.mycode.component

import net.mycode.service.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Created by MAC on 2017/5/14.
 */

/*
*
*  仅仅适合 MethodInvokingJobDetailFactoryBean（查看QuartzConifg）  这个工厂管理  第二种方法
*  非常方便 灵活易用  推荐这种
* **/

@Component
open class ExampleBusinessObject {

    @Autowired
    lateinit var redisService: RedisService

    fun doIt() {
        println("开始执行ExampleBusinessObject对象中的doIt()方法。。。。。。。 ")
        redisService.setKeyValue("friend", "wms")
    }

    /**
     * initialDelay=5000 初始化5s执行方法
     * fixedDelay=2000 该方法的执行周期为2s
     *
     *  注意调度任务本身是异步任务，在AsyncTaskExecutorConfig 配置了 ThreadPoolTaskScheduler 线程池
     *
     *  不需要再去调用 @Async注解的方法，最后结果就是在spring的默认SimpleAsyncTaskExecutor线程运行
     *
     *  我们直接在 @Scheduled注解的方法里调用普通方法 执行代码块 就是异步的 用的线程是ThreadPoolTaskScheduler线程池的
     */
    //@Scheduled(cron = "*/1 * * * * ?")
    /* fun doTimerTask001() {
         var t=Thread.currentThread().name
         println("$t 开始执行ExampleBusinessObject对象中的 doTimerTask001 ()方法。。。。。。。 ")

        // doasynctak()
     }


     @Async
     open fun doasynctak(){
         var t=Thread.currentThread().name
         println("$t 开始执行ExampleBusinessObject对象中的 doasynctak ()方法。。。。。。。 ")
     }*/

}