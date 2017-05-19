package net.mycode.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by YanCheng on 2017/5/9.
 */
@Configuration
open class MyAsyncTaskExecutor {

    /** Set the ThreadPoolExecutor's core pool size.  */
    private val corePoolSize = 20
    /** Set the ThreadPoolExecutor's maximum pool size.  */
    private val maxPoolSize = 50
    /** Set the capacity for the ThreadPoolExecutor's BlockingQueue.  */
    private val queueCapacity = 100

    @Bean
    open fun executor(): Executor {
        var executor = ThreadPoolTaskExecutor()
        //设置核心线程池数量
        executor.corePoolSize = corePoolSize
        //设置最大线程池数量
        executor.maxPoolSize = maxPoolSize
        //设置队列数量
        executor.setQueueCapacity(queueCapacity)
        executor.threadNamePrefix = "MyExecutor-"
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        executor.initialize()
        return executor
    }

    @Bean
    open fun schedulerExecutor(): Executor {
        var executor = ThreadPoolTaskScheduler()
        executor.poolSize = 10
        executor.threadNamePrefix = "MySchedulerExecutor-"
        return executor
    }

}