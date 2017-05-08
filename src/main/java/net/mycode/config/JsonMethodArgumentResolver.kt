package net.mycode.config

import com.google.gson.*
import com.google.gson.stream.MalformedJsonException
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

import javax.servlet.http.HttpServletRequest
import java.io.BufferedReader
import java.math.BigInteger
import java.util.Arrays

import org.springframework.util.SerializationUtils.serialize

/**
 * Created by YanCheng on 2016/11/3.
 */

open class JsonMethodArgumentResolver : HandlerMethodArgumentResolver {

    private var content = ""

    var logger = LoggerFactory.getLogger(javaClass)

    override fun supportsParameter(methodParameter: MethodParameter): Boolean {
        val names = arrayOfNulls<String>(6)
        //  names[0]= methodParameter.getConstructor().getName();
        names[0] = methodParameter.containingClass.name

        //com.itorrus.controller.admin.AdminController
        names[1] = methodParameter.declaringClass.name

        //creg
        names[2] = methodParameter.method.name

        //admin
        names[3] = methodParameter.parameterName

        //reg
        names[4] = methodParameter.member.name

        names[5] = methodParameter.parameterType.name

        logger.info("\n\nsupportsParameter： gson解决者开始判断支持类型：methodParameter:=======>" + Arrays.toString(names))

        return true
    }

    @Throws(Exception::class)
    override fun resolveArgument(methodParameter: MethodParameter, modelAndViewContainer: ModelAndViewContainer, nativeWebRequest: NativeWebRequest, webDataBinderFactory: WebDataBinderFactory): Any? {
        logger.info("\n\nresolveArgument： gson解决者开始 解析数据绑定到方法参数中......")

        val request = nativeWebRequest.getNativeRequest(HttpServletRequest::class.java)
        if (!request.contentType.contains("application/json")) {
            return null
        }
        content= request.reader.readText()
        logger.info("获取前端的json的数据字符串内容是：" + content)
        try {
            //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            val gson = GsonBuilder().setPrettyPrinting().create()
            //gson.fromJson(reader,methodParameter.getParameterType());
            val jsonObject = JsonParser().parse(content).asJsonObject

            if (jsonObject.get(methodParameter.parameterName) == null) {
                return null
            }

            if (jsonObject.get(methodParameter.parameterName).isJsonObject) {
                val nodeJsonObject = jsonObject.getAsJsonObject(methodParameter.parameterName)
                if (nodeJsonObject != null) {
                    return gson.fromJson(nodeJsonObject.toString(), methodParameter.parameterType)
                }
            } else if (jsonObject.get(methodParameter.parameterName).isJsonPrimitive) {
                // String str=jsonObject.get(methodParameter.getParameterName()).getAsString();
                if (methodParameter.parameterType.isAssignableFrom(Int::class.java)) {
                    return jsonObject.get(methodParameter.parameterName).asInt
                } else if (methodParameter.parameterType.isAssignableFrom(Double::class.java)) {
                    return jsonObject.get(methodParameter.parameterName).asDouble
                } else if (methodParameter.parameterType.isAssignableFrom(String::class.java)) {
                    return jsonObject.get(methodParameter.parameterName).asString
                } else if (methodParameter.parameterType.isAssignableFrom(BigInteger::class.java)) {
                    return jsonObject.get(methodParameter.parameterName).asBigInteger
                }
            } else if (jsonObject.get(methodParameter.parameterName).isJsonArray) {
                val nodeJsonObject = jsonObject.getAsJsonArray(methodParameter.parameterName)
                return gson.fromJson(nodeJsonObject.toString(), methodParameter.parameterType)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            //new JsonParseException()
            throw MalformedJsonException("解析json数据发生错误,可能是json格式错误或者参数无法匹配")
        }

        /*     // if (methodParameter.getParameterType().isAssignableFrom(A))
        AdminTest adminTest=new AdminTest();
        adminTest.setAdminPassword("99985555524");
        adminTest.setAdminUsername("554yanchengasda2");*/
        return null
    }


}
