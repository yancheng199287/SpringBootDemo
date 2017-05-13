package net.mycode.component.shiro

import net.mycode.service.RedisService
import org.apache.log4j.Logger
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.SerializationUtils
import java.io.Serializable

/**
 * Created by YanCheng on 2017/5/12.
 */
class RedisSessionDao : AbstractSessionDAO() {

    @Autowired
    lateinit var redisService :RedisService

    var logger = Logger.getLogger(RedisSessionDao::class.java)

    override fun doCreate(session: Session?): Serializable {
        logger.info("开始创建session会话中.....")
        var s=this.generateSessionId(session)
        this.assignSessionId(session,s )

        return s
       // redisService.setKeyValue(,SerializationUtils.serialize(session))
    }

    override fun update(session: Session?) {
    }

    override fun getActiveSessions(): MutableCollection<Session> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun doReadSession(session: Serializable?): Session {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun delete(session: Session?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
