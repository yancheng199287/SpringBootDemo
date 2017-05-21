package net.mycode.entity

/**
 * Created by MAC on 2017/5/7.
 *
 *   data  的 calss 名字 不要跟文件名一样，不然不好导包！！！
 *
 */
import sun.security.x509.AccessDescription
import java.io.Serializable
import java.util.Date
import javax.persistence.*
import java.util.HashSet


@Entity
@Table(name = "520_user")
data class User(
        @Id @GeneratedValue var id: Int? = 0,
        @Column(name = "name", nullable = false, length = 30) var name: String?,
        var age: Int,
        // r如果你需要指定数据库字段类似是timestamp 长度3代表保留毫秒位数@Column(columnDefinition = "timestamp", length = 3)
        @Column(length = 3) var addTime: Date?) : Serializable {
    constructor() : this(id = null, name = null, age = 0, addTime = null)
}


@Entity
@Table(name = "520_student")
data class Student(
        @Id @GeneratedValue var id: Int? = 0,
        @Column(name = "name", nullable = false, length = 30) var name: String?,
        var age: Int,
        @Temporal(TemporalType.TIMESTAMP) var addTime: Date?) : Serializable {
    constructor() : this(id = null, name = null, age = 0, addTime = null)
}


//@OneToMany的单向和双向 有待进一步解决！！！
@Entity
data class Account(
        @Id @GeneratedValue var id: Int? = 0,
        @Column(nullable = true, length = 30) var name: String?,
        @Column(nullable = true, length = 18) var password: String?,
        @Temporal(TemporalType.TIMESTAMP) var birthday: Date?,
        @Temporal(TemporalType.TIMESTAMP) var addTime: Date?
        // @OneToMany(mappedBy="order",cascade = CascadeType.ALL, fetch = FetchType.LAZY) @OrderBy(value = "id ASC") books: Set<Book>
        //。mappBy表示关系被维护端，只有关系端有权去更新外键。这里还有注意OneToMany默认的加载方式是赖加载。当看到设置关系中最后一个单词是Many，那么该加载默认为懒加载
        //  @OneToMany(mappedBy = "account", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY) @OrderBy(value = "id DESC") var books: Set<Book>?
) : Serializable {
    constructor() : this(id = null, name = null, password = null, birthday = null, addTime = null)
}


//如果在类构造方法中添加了默认属性值，就相当于有了空的构造方法，不用在外面额外写constructor()
@Entity
data class Book(
        @Id @GeneratedValue var id: Int? = 0,
        @Column(nullable = true, length = 30) var name: String? = null,
        var description: String? = null,
        @Column(nullable = false, length = 10) var author: String? = null,
        @Temporal(TemporalType.TIMESTAMP) var addTime: Date? = null,
        @ManyToOne @JoinColumn(name = "account_id") var account: Account? = null
) : Serializable
