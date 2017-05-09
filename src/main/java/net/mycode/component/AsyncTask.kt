package net.mycode.component

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Future


/**
 * Created by YanCheng on 2017/5/9.
 */

@Component
open class AsyncTask {
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


}