package net.mycode.service

import net.mycode.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.jcache.JCacheCacheManager
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

/**
 * Created by MAC on 2017/5/19.
 */

@Service
open class EhcacheService {

    /***
     * 这个jCacheCacheManager不能在controller里面去注入，会导致所有在controller的依赖全部失败，但是如果这样配置
     * @EnableCaching(proxyTargetClass = true,mode=AdviceMode.ASPECTJ) 则可以在controller里注入，但是这样的话 我们的缓存注解全部失效，必须手动用cache.put存入 和cache.get获取缓存值
     *  为了保证 手动 通过jCacheCacheManager对象 自己存值取值和注解同时生效， @EnableCaching这样就可以，但是jCacheCacheManager注入不能在controller里面！！
     *  最好写到这里Service层， 具体原因与代理有关系。
     *  jCacheCacheManager是spring自动发现声明的bean，idea在我的项目src源码里找不到所以报错。这个声明bean在jar包中的JCacheCacheConfiguration类里面
     * */
    @Autowired
    lateinit var jCacheCacheManager: JCacheCacheManager

    // 注解自动开启，如果不存在缓存中直接保存然后返回，存在则立即从缓存返回
    @Cacheable("people")
    open fun saveUser(name: String): User {
        var cache = jCacheCacheManager.getCache("people")
        return User(100, " 我的名字叫：$name ${cache.name}", 33, Timestamp(Date().time))
    }

    // 手动自动开启，如果不存在缓存中直接保存然后返回，存在则立即从缓存返回
    open fun saveUser001(name: String): User {
        var cache = jCacheCacheManager.getCache("people")
        var u = User(100, " 我的名字叫：$name ${cache.name}", 39, Timestamp(Date().time))
        cache.putIfAbsent("user", u)
        return cache.get("user", User::class.java)
    }

}