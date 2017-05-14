package net.mycode.config

import com.google.gson.GsonBuilder
import net.mycode.component.JsonMethodArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.nio.charset.Charset
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.GsonHttpMessageConverter


/**
 * Created by MAC on 2017/5/8.
 * SpringMVC之前xml配置文件  可以实在在这里全部配置！
 */

@EnableWebMvc
@Configuration
open class MyWebMvcConfig : WebMvcConfigurerAdapter() {

    //添加一个controller的方法自定义参数解析器 这里设置了 用 gson 解析前端请求的json数据并绑定到方法参数
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>?) {
        super.addArgumentResolvers(argumentResolvers)
        argumentResolvers?.add(JsonMethodArgumentResolver())
    }

    // 添加自定义 消息转换器，返回给浏览器的数据
    // 设置字符串默认 以UTF-8编码返回数据， 设置gson序列化数据到前端
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>?) {
        super.configureMessageConverters(converters)
        val stringConverter = StringHttpMessageConverter(Charset.forName("UTF-8"))
        converters!!.add(stringConverter)
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create()
        val gsonHttpMessageConverter = GsonHttpMessageConverter()
        gsonHttpMessageConverter.gson = gson
        converters!!.add(gsonHttpMessageConverter)
    }
}