package net.mycode.component.shiro

import org.apache.shiro.web.filter.authz.AuthorizationFilter
import org.springframework.stereotype.Component
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

/**
 * Created by YanCheng on 2017/5/12.
 */

//自定义权限过滤器
 class MyAuthorizationFilter : AuthorizationFilter() {


    override fun isAccessAllowed(servletRequest : ServletRequest , servletResponse : ServletResponse , mappedValue :Any? ): Boolean {

        return true
    }

}