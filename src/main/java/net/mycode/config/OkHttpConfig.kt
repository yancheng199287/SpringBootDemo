package net.mycode.config

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by YanCheng on 2017/5/15.
 */

@Configuration
open class OkHttpConfig {

    var baseUrl = "http://www.520code.net/"

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    open fun retrofit(): Retrofit {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        var retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient())
                .build()
        return retrofit
    }


    @Bean
    // 默认bean都是单例  如果需要多例 请指定 @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    open fun okHttpClient(): OkHttpClient {
        var okHttpClient = OkHttpClient.Builder().connectTimeout(1500, TimeUnit.SECONDS).build()
        return okHttpClient
    }


}
