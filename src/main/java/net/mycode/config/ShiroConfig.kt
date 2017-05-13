package net.mycode.config

import net.mycode.component.shiro.MyAuthorizationFilter
import net.mycode.component.shiro.MyRealm
import net.mycode.component.shiro.RedisSessionDao
import org.apache.shiro.codec.Base64
import org.apache.shiro.spring.LifecycleBeanPostProcessor
import org.apache.shiro.web.mgt.CookieRememberMeManager
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.web.servlet.SimpleCookie
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
import org.springframework.context.annotation.DependsOn
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Import
import org.springframework.core.Ordered
import java.util.LinkedHashMap
import javax.servlet.Filter


/**
 * Created by YanCheng on 2017/5/12.
 */
@Configuration
//@AutoConfigureAfter(value = *arrayOf(MyAuthorizationFilter::class,MyRealm::class,RedisSessionDao::class))
//@DependsOn(*arrayOf("myRealm", "myAuthorizationFilter", "redisSessionDao"))
//@ConditionalOnBean(value = *arrayOf(MyAuthorizationFilter::class,MyRealm::class,RedisSessionDao::class))
//@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
//@Import(value = *arrayOf(MyAuthorizationFilter::class, MyRealm::class, RedisSessionDao::class))
open class ShiroConfig {


    //单位天，设置cookies时间
    private val day = 5
    private var filterChainDefinitionMap = LinkedHashMap<String, String>()
    private var filtersMap = LinkedHashMap<String, Filter>()


    //@Configuration类中无法直接 @Autowaried 实例化bean 一种办法设置static ，在这里我们去声明bean，取消类中的@Component
    @Bean
    open fun getMyAuthyRealmBean(): MyRealm {
        return MyRealm()
    }

    @Bean
    open fun getRedisSessionDaoBean(): RedisSessionDao {
        return RedisSessionDao()
    }

    @Bean
    open fun getMyAuthorizationFilterBean(): MyAuthorizationFilter {
        return MyAuthorizationFilter()
    }


    @Bean
    open fun getSimpleCookie(): SimpleCookie {
        var simpleCookie = SimpleCookie()
        //不允许js操作cookies，请求时候浏览器自己携带
        simpleCookie.isHttpOnly = true
        //（单位：秒）
        simpleCookie.maxAge = 86400 * day
        //cookies名称
        simpleCookie.name = "simpleCookie"
        return simpleCookie
    }

    @Bean
    open fun getCookieRememberMeManager(): CookieRememberMeManager {
        var cookieRememberMeManager = CookieRememberMeManager()
        cookieRememberMeManager.cookie = getSimpleCookie()
        cookieRememberMeManager.cipherKey = Base64.decode("eHlxMTUzNTEwQUJDRDUyNg==")
        return cookieRememberMeManager
    }

    @Bean
    open fun getDefaultWebSessionManager(): DefaultWebSessionManager {
        var sessionManager = DefaultWebSessionManager()
        sessionManager.sessionIdCookie = getSimpleCookie()
        sessionManager.isSessionIdCookieEnabled = true
        //  sessionManager.sessionDAO = getRedisSessionDaoBean()
        //session验证调度关闭，已启用redis过期检查
        sessionManager.isSessionValidationSchedulerEnabled = false
        return sessionManager
    }

    @Bean
    open fun getDefaultWebSecurityManager(): DefaultWebSecurityManager {
        var securityManager = DefaultWebSecurityManager()
        securityManager.rememberMeManager = getCookieRememberMeManager()
        //  securityManager.setRealm(getMyAuthyRealmBean())
        securityManager.sessionManager = getDefaultWebSessionManager()
        return securityManager
    }


    @Bean
    open fun getMethodInvokingFactoryBean(): MethodInvokingFactoryBean {
        var factoryBean = MethodInvokingFactoryBean()
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager")
        factoryBean.arguments = arrayOf(getDefaultWebSecurityManager())
        return factoryBean
    }

    @Bean
    open fun getLifecycleBeanPostProcessor(): LifecycleBeanPostProcessor {
        var processor = LifecycleBeanPostProcessor()
        return processor
    }

    @Bean
    open fun getAuthorizationAttributeSourceAdvisor(): AuthorizationAttributeSourceAdvisor {
        val advisor = AuthorizationAttributeSourceAdvisor()
        advisor.securityManager = getDefaultWebSecurityManager()
        return advisor
    }

    @Bean
    open fun getAutoProxyCreator(): DefaultAdvisorAutoProxyCreator {
        val creator = DefaultAdvisorAutoProxyCreator()
        creator.isProxyTargetClass = true
        return creator
    }


    @Bean
    open fun getShiroFilterFactoryBean(): ShiroFilterFactoryBean {
        val factoryBean = ShiroFilterFactoryBean()
        factoryBean.securityManager = getDefaultWebSecurityManager()
        // 这里的key authRole  可以在filterChainDefinitionMap中使用此过滤器，如：   filterChainDefinitionMap.put("/login**", "authRole")
        // 将对 login开头的页面用此过滤器进行拦截认证
        //  filtersMap.put("authRole", getMyAuthorizationFilterBean())
        factoryBean.filters = filtersMap

        //静态配置自定义页面拦截
        //  factoryBean.loginUrl = "/toLogin"
        //  filterChainDefinitionMap.put("/resources/**", "anon")
        // filterChainDefinitionMap.put("/login**", "anon")
        // filterChainDefinitionMap.put("/**", "user")
        factoryBean.filterChainDefinitionMap = filterChainDefinitionMap
        return factoryBean
    }


}