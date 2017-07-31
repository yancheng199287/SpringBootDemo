package net.mycode.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.common.collect.Lists
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

/**
 * Created by YanCheng on 2017/7/31.
 */
@Configuration
open class CaffeineCacheConfig {

    @Bean
    open fun caffeineCacheManager(): CacheManager {
        val cacheManager = CaffeineCacheManager()
        val cacheBuilder: Caffeine<Any, Any> = Caffeine.newBuilder()
                .initialCapacity(10)   //设置缓存容器的初始容量为10
                .maximumSize(100)//缓存大小   超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .weakKeys()  //弱引用键值，当键没有其它（强或软）引用时，缓存项可以被垃圾回收。因为垃圾回收仅依赖恒等式（==），使用弱引用键的缓存用==而不是 equals 比较键
                .softValues()//软引用值，使用软引用存储值。软引用只有在响应内存需要时，才按照全局最近最少使用的顺序回收。考虑到使用软引用的性能影响，我们通常建议使用更有性能预测性的缓存大小限定（见上文，基于容量回收）。使用软引用值的缓存同样用==而不是 equals 比较值。
                .refreshAfterWrite(120, TimeUnit.SECONDS)//距离上一次写入的刷新时间
                .expireAfterWrite(10, TimeUnit.MINUTES)//距离上一次写入的过期时间
                .expireAfterAccess(30, TimeUnit.MINUTES)//距离上一次访问的过期时间
                .executor(Runnable::run)
        cacheManager.setCaffeine(cacheBuilder)
        return cacheManager
    }

}