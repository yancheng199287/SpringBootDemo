package net.mycode.config

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.cache.CacheManager
import javax.cache.configuration.MutableConfiguration
import javax.cache.expiry.Duration
import javax.cache.expiry.TouchedExpiryPolicy

/**
 * Created by MAC on 2017/5/15.
 */

@Component
open class EhcacheConfig : JCacheManagerCustomizer {

    override fun customize(cacheManager: CacheManager?) {
        //cahche 名字 是xml 中定义好的 保持一致，则启用相关cache中定义的模版
        // 不一致，则使用自己定义好的配置

        //创建一个别名为“people”的缓存。
        cacheManager!!.createCache("people", MutableConfiguration<Any, Any>()
                //此行设置过期策略。在这种情况下，我们设置为10秒。因此，如果在过去10秒内没有触及（创建，更新或访问）条目，它将被逐出。
                .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(Duration(TimeUnit.SECONDS, 10)))
                .setStoreByValue(false)
                .setStatisticsEnabled(true))

        //创建一个别名为“mycache”的缓存。
        cacheManager!!.createCache("mycache", MutableConfiguration<Any, Any>()
                //此行设置过期策略。在这种情况下，我们设置为10秒。因此，如果在过去10秒内没有触及（创建，更新或访问）条目，它将被逐出。
                .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(Duration(TimeUnit.SECONDS, 10)))
                .setStoreByValue(false)
                .setStatisticsEnabled(true)
        )
    }


}