package net.mycode.component

import org.ehcache.event.CacheEvent
import org.ehcache.event.CacheEventListener
import org.slf4j.LoggerFactory


/**
 * Created by MAC on 2017/5/15.
 */

open class EhcacheEventLogger : CacheEventListener<Any, Any> {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)


    override fun onEvent(event: CacheEvent<out Any, out Any>) {
        println("--------------出现缓存监听事件状态日志信息----------------------")
        LOGGER.info("\n\n==========Event: " + event.type + " Key: " + event.key + " old value: " + event.oldValue + " new value: " + event.newValue)
    }


}