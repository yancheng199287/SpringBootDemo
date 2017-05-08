package net.mycode.entity

/**
 * Created by MAC on 2017/5/7.
 *
 *   data  的 calss 名字 不要跟文件名一样，不然不好导包！！！
 *
 */
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "520_user")
data class User(
        @Id @GeneratedValue var id: Int? = 0,
        @Column(name = "name", nullable = false, length = 30) var name: String?,
        var age: Int,
        @Temporal(TemporalType.TIMESTAMP) var addTime: Date?) : Serializable {
    public constructor() : this(id = null, name = null, age = 0, addTime = null)
}


@Entity
@Table(name = "520_student")
data class Student(
        @Id @GeneratedValue var id: Int? = 0,
        @Column(name = "name", nullable = false, length = 30) var name: String?,
        var age: Int,
        @Temporal(TemporalType.TIMESTAMP) var addTime: Date?) : Serializable {
    protected constructor() : this(id = null, name = null, age = 0, addTime = null)
}
