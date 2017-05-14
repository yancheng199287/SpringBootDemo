package net.mycode.component

import net.mycode.component.shiro.MyRealm
import org.apache.shiro.web.servlet.SimpleCookie
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.DependsOn
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.Future
import javax.annotation.Resource


/**
 * Created by YanCheng on 2017/5/9.
 */

@Component

open class AsyncTask {



    //可以注入到任何对象中，调用executor.execute｛｝ 方法  在代码块区中执行你要的任务
    @Autowired
    lateinit var executor: Executor


    //可以在任何对象中，使用这种@Async注解某方法，自动会从线程池获取线程对象去执行这个方法
    @Async
    open fun doTask1(): Future<String> {
        var logger: Logger = LoggerFactory.getLogger(AsyncTask::class.java)
        println(logger)
        logger.info("\n\n  =====================Task1 started." + Thread.currentThread().name)
        val start = System.currentTimeMillis()
        Thread.sleep(3000)
        val end = System.currentTimeMillis()
        logger.info("Task1 finished, time elapsed: {} ms.", end - start)
        return AsyncResult("Task1 accomplished!")
    }


    open fun doTask2() {
        for (i in 1..5) {
            executor.execute {
                println("执行第:$i 次,使用配置好的线程池注入到当前对象去执行一个异步Runnable 的run方法，当前线程名字：${Thread.currentThread().name}")
            }
        }
    }


}