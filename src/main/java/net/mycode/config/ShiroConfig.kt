

package net.mycode.config

import net.mycode.component.shiro.MyAuthorizationFilter
import net.mycode.component.shiro.MyRealm
import net.mycode.component.shiro.RedisSessionDao
import org.apache.shiro.authc.credential.HashedCredentialsMatcher
import org.apache.shiro.codec.Base64
import org.apache.shiro.crypto.AesCipherService
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


/** * Shiro Realm 继承自AuthorizingRealm的自定义Realm,
     * 即指定Shiro验证用户登录的类为自定义的 * * @param cacheManager * @return */


    @Bean //注意这些 bean 实例化这个对象的时候 用的 id 是 myAuthyRealmBean 即方法名
    open fun myAuthyRealmBean(): MyRealm {
        return MyRealm()
    }

    @Bean
    open fun redisSessionDaoBean(): RedisSessionDao {
        return RedisSessionDao()
    }

    @Bean
    open fun myAuthorizationFilterBean(): MyAuthorizationFilter {
        return MyAuthorizationFilter()
    }


    @Bean
    open fun lifecycleBeanPostProcessor(): LifecycleBeanPostProcessor {
        return LifecycleBeanPostProcessor()
    }

    @Bean
    open fun hashedCredentialsMatcher(): HashedCredentialsMatcher {
        val credentialsMatcher = HashedCredentialsMatcher()
        credentialsMatcher.hashAlgorithmName = "MD5"
        credentialsMatcher.hashIterations = 1
        credentialsMatcher.isStoredCredentialsHexEncoded = true
        return credentialsMatcher
    }

    //配置cookies模版， 注意这个sessionIdCookie指的的打开项目的那一瞬间，创建的 会话 分配给客户端的cookies，与remberCookies不同的是
    //remberCookies 需要授权登录才会分配的，所以sessionIdCookies需要设置过期时间为-1 即关闭浏览器失效
    //而remberCookies 需要设置过期时间，下次可以直接登录，他是用户登录成功分配的标识cookies
   @Bean
    open fun sessionIdCookie(): SimpleCookie {
        val cookie = SimpleCookie("sid")
        cookie.isHttpOnly = true
        cookie.maxAge = -1
        return cookie
    }

    @Bean
    open fun rememberMeCookie(): SimpleCookie {
        val simpleCookie = SimpleCookie("rememberMe")
        simpleCookie.isHttpOnly = true
        simpleCookie.maxAge =  86400 * day
        return simpleCookie
    }

    @Bean
    open fun rememberManager(): CookieRememberMeManager {
        val meManager = CookieRememberMeManager()
        meManager.cipherKey = Base64.decode("aHR0dHA6Ly93d3cuNTIwY29kZS5uZXQgUVE6NjQ4ODMwNjA1")
        meManager.cookie = rememberMeCookie()
        return meManager
    }

    @Bean
    open fun sessionManage(): DefaultWebSessionManager {
        val sessionManager = DefaultWebSessionManager()
        //开启session调度，每隔多久执行检查
        sessionManager.isSessionValidationSchedulerEnabled = false
        //无效session是否删除
        sessionManager.isDeleteInvalidSessions = true
        //开启sessionid cookies
        sessionManager.isSessionIdCookieEnabled = true
        sessionManager.sessionIdCookie = sessionIdCookie()
        return sessionManager
    }

    @Bean
    open fun defaultWebSecurityManager(): DefaultWebSecurityManager {
        val securityManager = DefaultWebSecurityManager()
       // securityManager.cacheManager = getCacheManage()
        securityManager.sessionManager = sessionManage()
        securityManager.rememberMeManager = rememberManager()
       // securityManager.setRealm(getShiroRealm())
        return securityManager
    }

    @Bean
    open fun methodInvokingFactoryBean(): MethodInvokingFactoryBean {
        val factoryBean = MethodInvokingFactoryBean()
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager")
        factoryBean.arguments = arrayOf<Any>(defaultWebSecurityManager())
        return factoryBean
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    open fun getAutoProxyCreator(): DefaultAdvisorAutoProxyCreator {
        val creator = DefaultAdvisorAutoProxyCreator()
        creator.isProxyTargetClass = true
        return creator
    }


    @Bean
    open fun authorizationAttributeSourceAdvisor(): AuthorizationAttributeSourceAdvisor {
        val advisor = AuthorizationAttributeSourceAdvisor()
        advisor.securityManager = defaultWebSecurityManager()
        return advisor
    }


    @Bean
    open fun shiroFilterFactoryBean(): ShiroFilterFactoryBean {
        val factoryBean = ShiroFilterFactoryBean()
        factoryBean.securityManager = defaultWebSecurityManager()
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

