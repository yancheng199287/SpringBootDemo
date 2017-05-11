package net.mycode.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

/**
 * Created by YanCheng on 2017/5/11.
 */
@Service
open class RedisRepository {


    @Autowired
    lateinit var stringRedisTemplate: StringRedisTemplate

    fun setKeyValue(key: String, value: String) {
        stringRedisTemplate.opsForValue().set(key, value, 60 * 10, TimeUnit.SECONDS)//向redis里存入数据和设置缓存时间
    }

    fun getValue(key: String): String{
       return stringRedisTemplate.opsForValue().get(key)//根据key获取缓存中的val
    }

    fun delValue(key: String) {
        stringRedisTemplate.delete(key)//根据key删除缓存
    }

    fun hasKey(key: String): Boolean {
        return stringRedisTemplate.hasKey(key)//检查key是否存在，返回boolean值
    }

    fun addSetValue(key: String, value: ArrayList<String>) {
        var values = value.toTypedArray()
        stringRedisTemplate.opsForSet().add(key, *values);//向指定key中存放set集合
    }

    fun setExpire(key: String, time: Long): Boolean {
        return stringRedisTemplate.expire("red_123", time, TimeUnit.MILLISECONDS)//设置过期时间
    }

    fun haskeyFromList(key: String, element: String): Boolean {
        return stringRedisTemplate.opsForSet().isMember(key, element)//根据key查看集合中是否存在指定数据
    }

    fun getList(key: String, element: String): Set<String> {
        return stringRedisTemplate.opsForSet().members("red_123")//根据key获取set集合
    }


}
