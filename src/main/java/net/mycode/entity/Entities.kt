package net.mycode.entity

/**
 * Created by MAC on 2017/5/7.
 *
 *   data  的 calss 名字 不要跟文件名一样，不然不好导包！！！
 *
 */
import java.io.Serializable
import java.util.Date
import javax.persistence.*


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
        @Column(nullable = false) var accountType: AccountType?,
        @Temporal(TemporalType.TIMESTAMP) var birthday: Date?,
        @Temporal(TemporalType.TIMESTAMP) var addTime: Date?
        //一对多和多对一双向关联，取数据导致死循环，如果使用jackjson可使用@JsonIgnore，
        // @Transient @OneToMany(mappedBy = "account", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY) @OrderBy(value = "id ASC") var books: List<Book>?
        //。mappBy表示关系被维护端也就是外键指向的是那个表，在这里，book的外键指向这里，只有关系端有权去更新外键。这里还有注意OneToMany默认的加载方式是赖加载。当看到设置关系中最后一个单词是Many，那么该加载默认为懒加载
        //  @OneToMany(mappedBy = "account", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY) @OrderBy(value = "id DESC") var books: Set<Book>?
) : Serializable {
    constructor() : this(id = null, name = null, accountType = null, password = null, birthday = null, addTime = null)
}


//如果在类构造方法中添加了默认属性值，就相当于有了空的构造方法，不用在外面额外写constructor()
@Entity
data class Book(
        @Id @GeneratedValue var id: Int? = 0,
        @Column(nullable = true, length = 30) var name: String? = null,
        var description: String? = null,
        @Column(nullable = false, length = 10) var author: String? = null,
        @Temporal(TemporalType.TIMESTAMP) var addTime: Date? = null,
        @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "account_id") var account: Account? = null
) : Serializable


enum class AccountType {
    admin("管理员"),
    agent("代理商"),
    vip("vip用户"),
    user("p普通用户");

    private var roleName: String

    constructor(roleName: String) {
        this.roleName = roleName
    }


}
