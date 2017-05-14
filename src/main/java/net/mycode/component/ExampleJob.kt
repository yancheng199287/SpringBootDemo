package net.mycode.component

import net.mycode.service.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.quartz.QuartzJobBean

/**
 * Created by MAC on 2017/5/14.
 */

/**
 *   实现 QuartzJobBean抽象类
 *
 *   重写executeInternal方法就可以执行 定时任务
 *
 *   仅仅适合 JobDetailFactoryBean（查看QuartzConifg）  这个工厂管理  属于简单的第一种方法 不太方便扩展
 *
 *   ！！！ 在这个类里面是不能直接用注解的方式 去初始化bean， 必须先获取ApplicationContext 然后获取对应的bean
 *
 * */

open class ExampleJob : QuartzJobBean() {

    override fun executeInternal(jobExecutionContext: org.quartz.JobExecutionContext?) {
        println("开始执行ExampleJob的  executeInternal（）方法。。。。。。。 ")

       var ack : ApplicationContext = jobExecutionContext!!.jobDetail.jobDataMap["ack"] as ApplicationContext

        var redisService : RedisService= ack.getBean("redisService") as RedisService
        redisService.setKeyValue("love","ccx")
    }

}