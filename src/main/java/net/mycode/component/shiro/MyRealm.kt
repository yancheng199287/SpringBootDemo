package net.mycode.component.shiro

import org.apache.log4j.Logger
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.core.Ordered
import org.springframework.stereotype.Component

/**
 * Created by YanCheng on 2017/5/12.
 */

 class MyRealm : AuthorizingRealm() {

    var logger = Logger.getLogger(MyRealm::class.java)

    override fun doGetAuthenticationInfo(authenticationToken: AuthenticationToken?): AuthenticationInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun doGetAuthorizationInfo(principalCollection: PrincipalCollection?): AuthorizationInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }









}