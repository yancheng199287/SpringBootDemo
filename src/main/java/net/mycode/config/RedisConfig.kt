package net.mycode.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnection
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPoolConfig

/**
 * Created by YanCheng on 2017/5/11.
 * 相关文档资料 http://docs.spring.io/spring-data/redis/docs/current/reference/html/
 */
@Configuration
//@ConditionalOnClass(JedisConnection::class, RedisOperations::class, Jedis::class)  //配置的条件是存在相关的redis jar包
open class RedisConfig {

    @Value("\${net.mycode.config.redisConfig.maxTotal}")
    private var maxTotal: Int = 30
    @Value("\${net.mycode.config.redisConfig.maxIdle}")
    private var maxIdle: Int = 10
    @Value("\${net.mycode.config.redisConfig.maxWaitMillis}")
    private var maxWaitMillis: Long = 10000
    @Value("\${net.mycode.config.redisConfig.minIdle}")
    private var minIdle: Int = 1

    @Value("\${net.mycode.config.redisConfig.port}")
    private var port: Int = 6379
    @Value("\${net.mycode.config.redisConfig.hostName}")
    private var hostName: String = "localhost"
    @Value("\${net.mycode.config.redisConfig.password}")
    private var password: String = ""
    @Value("\${net.mycode.config.redisConfig.timeout}")
    private var timeout: Int = 150000


    @Bean
    @ConditionalOnMissingBean //判断是否执行初始化代码，即如果用户未创建则执行创建bean，如果已经创建了bean，则相关的初始化代码不再执行
    open fun getJedisConnectionFactory(): JedisConnectionFactory {
        var pool = JedisPoolConfig()
        pool.maxTotal = maxTotal
        pool.maxIdle = maxIdle
        pool.maxWaitMillis = maxWaitMillis
        pool.minIdle = minIdle

        //保证连接的有效性，即使redis.conf 配置了timeout关闭连接， 也不会自动报错,会尝试自动获取有效连接
        pool.testOnBorrow = true
        pool.testOnReturn = true

        var factory = JedisConnectionFactory()
        factory.port = port
        factory.hostName = hostName
        factory.password = password
        factory.timeout = timeout
        factory.usePool = true
        factory.poolConfig = pool
        return factory
    }


    @Bean
    @ConditionalOnMissingBean
    open fun getRedisTemplate(): StringRedisTemplate {
        //使用字符串模版，内置还有其他的，也可以通过RedisTemplate定制
        var template = StringRedisTemplate()
        //当@Bean对彼此的依赖，表达这种依赖很简单，只要有一个Bean的方法调用另一个如下
        template.connectionFactory = getJedisConnectionFactory()
        return template
    }


}