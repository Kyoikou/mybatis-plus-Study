# Mybatis-plus

## 快速入门

使用第三方组件：

- 导入对应依赖
- 研究依赖如何配置
- 代码如何编写
- 提升拓展技术能力！



1、新建数据库

2、创建user插入数据

3、新建项目，使用SpringBoot初始化

4、依赖

```xml
 <dependency>
     <groupId>mysql</groupId>
     <artifactId>mysql-connector-java</artifactId>
     <version>8.0.13</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.10</version>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus</artifactId>
    <version>3.0.5</version>
</dependency>
```

说明：不要同时导入mybatis与mybatis-plus

5、连接数据库mysql 8 需要加时区

```properties
spring.datasource.username=root
spring.datasource.password=yuzaixia95..
spring.datasource.url=jdbc:mysql://localhost:3306/mybatis_plus?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

```

<font color="red">6、传统方式pojo-dao(连接mybatis，配置mapper.xml文件)-service-controller</font>

6、使用mybatis-plus之后

- pojo

  ```java
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class User {
  
      private Long id;
      private String name;
      private Integer age;
      private String email;
  }
  ```

  

- mapper接口

  ```
  // 在对应的Mapper上面继承基本的类BaseMapper 想查哪个表 泛型里面就放什么
  @Repository
  public interface UserMapper extends BaseMapper<User> {
      // 所有的CRUD已经编写完成了 与JPA相同
      // 你不需要像以前一样配置一大堆文件了
  }
  ```

- 使用

  ```java
  @MapperScan("com.aftoday.mapper")
  @SpringBootApplication
  public class MybatisPlusApplication {
  
      public static void main(String[] args) {
          SpringApplication.run(MybatisPlusApplication.class, args);
      }
  
  }
  ```

  ```java
  @SpringBootTest
  class MybatisPlusApplicationTests {
  
      // 继承了父类BaseMapper  有许多方法可以调用
      @Autowired
      private UserMapper userMapper;
  
      @Test
      void contextLoads() {
          // 参数是一个 wrapper  条件构造器，这里我们先不要  null
          // selectList 查询全部用户
          List<User> users = userMapper.selectList(null);
          users.forEach(System.out::println);	// 语法糖
      }
  
  }
  ```

  > 1、SQL谁帮我们写的？  MyBatis-Plus
  >
  > 2、方法哪里来的？MyBatis-Plus-BaseMapper

---

## 配置日志

我们所有的sql现在是不可见的，我们希望知道塔是怎么执行的，所以我们必须要看日志！

生产环境应该去掉，浪费时间

```properties
# 配置日志
# 就使用原生的控制台输出
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

配置日志完成，查看自动生成的SQL语句，就会喜欢上Mybatis-plus!

---

## CRUD拓展

### 插入

```java
 /**
 * 测试插入
 */
@Test
public void testInsert(){
    User user = new User();     // 帮我们自动生成ID
    user.setName("狂神说Java");    // 受影响的行数
    user.setAge(3);
    user.setEmail("21313@qq.com");

    int result = userMapper.insert(user);
    System.out.println(result);
    System.out.println(user);   // 发现 id 自动回调
}
```

### 主键生成策略

**雪花算法** *(SnowFlake)*

snowflake是Twitter开源的分布式ID生成算法，结果是一个long型的ID。Mybatis-plus默认主键生成策略

其核心是：

>使用41bit作为毫秒数，10bit作为机器的ID，（5个bit是数据中心：北京、上海、西雅图，5个bit的机器ID），12bit作为毫秒内的流水号(意味着每个节点在每毫秒可以尝试4096个ID)，最后还有一个符号位，永远是0。可以保证几乎全球唯一！

```java
 @TableId(type = IdType.ID_WORKER)
    private Long id;
```

> ID_WORKER 全局唯一ID

**自增**

```java
@TableId(type = IdType.AUTO)
```

> AUTO 主键自增  一定要在数据表设置自增！



> 源码解释

```java
public enum IdType {
    AUTO(0),	// 数据库id自增
    NONE(1),	// 未设置主键
    INPUT(2),	// 手动输入
    ID_WORKER(3),	// 默认的全局唯一ID
    UUID(4),		// 全局唯一id uuid
    ID_WORKER_STR(5);	// ID_WORKER 字符串表示法
    ...
```

---

### 更新操作

```java
/**
* 更新操作
*/
@Test
public void testUpdate(){
    User user = new User();     // 帮我们自动生成ID
    // 通过条件动态配置
    user.setId(3333L);
    user.setName("关注狂神说");
    user.setAge(11);
    // 注意 : updateById 但是参数是一个 对象！
    int i = userMapper.updateById(user);
    System.out.println(i);
    System.out.println(user);

}
```

所以SQL都是自动帮你动态配置的！

### 自动填充

创建时间、修改时间！这些个操作一遍都是自动完成的，我们不希望手动更新！

<font color="red">阿里巴巴开发手册：所有的数据库表：gmt_create、gmt_modified几乎所有的表都要配置上！而且需要自动化！</font>

> 方式一：数据库级别（工作中不允许）

1、在表中新增字段 create_time、update_time

2、再次测试插入方法，我们需要先把实体类同步！

> 方式二：代码级别

1、删除数据库级别的默认值

2、<font color="red">在实体类字段属性上需要增加注解</font>

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    // 对应数据库中的逐渐(uuid、自增id、雪花算法、redis、zookeeper!)
    @TableId(type = IdType.INPUT)
    private Long id;
    private String name;
    private Integer age;
    private String email;
	// 
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
```

3、<font color="red">编写处理器来处理这个注解即可！</font>

```java
@Slf4j
@Component  // 一定不要忘记把处理器加到IOC容器中！我们需要IOC的反射
public class MyMetaObjectHandler implements MetaObjectHandler {
    // 插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ...");
        // setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject)
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    // 更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ...");
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
```

4、测试插入，测试更新。观测结果即可！然而我们并没有注入任何东西！



### 乐观锁

在面试过程中，我们经常会被问到乐观锁，悲观锁！这个其实非常简单！

> 乐观锁：顾名思义十分乐观，他总是任务不会出现问题，无论该干什么不去上锁！如果出现了问题，再次更新值测试。
>
> 悲观锁：顾名思义十分悲观，它总是任务总是出现问题，无论干什么都好上锁！再去操作！

