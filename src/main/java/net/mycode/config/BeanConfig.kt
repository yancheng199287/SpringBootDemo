package net.mycode.config

import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.boot.autoconfigure.web.HttpMessageConverters
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.GsonHttpMessageConverter
import java.util.ArrayList
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter


/**
 * Created by MAC on 2017/5/8.
 */

@Configuration
open class BeanConfig {

    @Bean
    open fun gsonConverterBeanConfig(): GsonHttpMessageConverter {

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create()
        val messageConverters = ArrayList<HttpMessageConverter<*>>()
        val gsonHttpMessageConverter = GsonHttpMessageConverter()
        gsonHttpMessageConverter.gson = gson
        messageConverters.add(gsonHttpMessageConverter)
        // return HttpMessageConverters(true, messageConverters)
        return gsonHttpMessageConverter
    }


 /*   @Bean
    open fun gsonMappingBeanConfig(): RequestMappingHandlerAdapter {
        var list = listOf<HandlerMethodArgumentResolver>()
        list.plus(JsonMethodArgumentResolver())
        var r = RequestMappingHandlerAdapter()
        r.setSynchronizeOnSession(true)
        r.customArgumentResolvers = list
        return r
    }*/

}