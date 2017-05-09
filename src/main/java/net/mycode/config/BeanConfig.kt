package net.mycode.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.GsonHttpMessageConverter
import java.util.ArrayList
import com.google.gson.GsonBuilder
import org.springframework.boot.autoconfigure.web.HttpMessageConverters
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.nio.charset.Charset
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor


/**
 * Created by MAC on 2017/5/8.
 */

@Configuration  //这个注解类似于xml中的配置<beans> </beans> 表示这个类开始设置依赖bean
open class BeanConfig {

   /* @Bean  //设置一个依赖bean  可以在后面指定名称@Bean(name="") ,默认名称是该返回类的驼峰命名，如 gsonHttpMessageConverter   类似于<bean id="名字">
    // 这个转换器 也可以单独只指定 GsonHttpMessageConverter
    open fun gsonConverterBean(): GsonHttpMessageConverter {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create()
        val gsonHttpMessageConverter = GsonHttpMessageConverter()
        gsonHttpMessageConverter.gson = gson
        //val messageConverters = ArrayList<HttpMessageConverter<*>>()
        //  messageConverters.add(gsonHttpMessageConverter)
        // return HttpMessageConverters(true, messageConverters)
        //  return gsonHttpMessageConverter
        // messageConverters.add(s)
        //  return HttpMessageConverters(messageConverters)
        return gsonHttpMessageConverter
    }*/

   /* @Bean
    fun stringConverter(): HttpMessageConverter<String> {
        val converter = StringHttpMessageConverter(Charset.forName("UTF-8"))
        return converter
    }*/


}

