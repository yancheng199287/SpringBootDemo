package net.mycode.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by YanCheng on 2017/5/9.
 */
@Configuration
@EnableAsync  //表示 xml中的 <task>  告诉spring我要开启自定义异步线程， 如果不开启，默认会有一个简单的异步线程池
open class MyAsyncTaskExecutor {

    /** Set the ThreadPoolExecutor's core pool size.  */
    private val corePoolSize = 20
    /** Set the ThreadPoolExecutor's maximum pool size.  */
    private val maxPoolSize = 50
    /** Set the capacity for the ThreadPoolExecutor's BlockingQueue.  */
    private val queueCapacity = 100

    @Bean
    open fun executorBean(): Executor {
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
        executor.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize()
        return executor
    }

}