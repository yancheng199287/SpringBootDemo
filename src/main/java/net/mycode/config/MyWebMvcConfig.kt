package net.mycode.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * Created by MAC on 2017/5/8.
 * SpringMVC之前xml配置文件  可以实在在这里全部配置！
 */

@EnableWebMvc
@Configuration
open class MyWebMvcConfig : WebMvcConfigurerAdapter() {

    //添加一个controller的方法自定义参数解析器
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>?) {
        super.addArgumentResolvers(argumentResolvers)
        argumentResolvers?.add(JsonMethodArgumentResolver())
    }



}