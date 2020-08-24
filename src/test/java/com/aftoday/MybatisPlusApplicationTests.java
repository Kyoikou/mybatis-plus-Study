package com.aftoday;

import com.aftoday.mapper.UserMapper;
import com.aftoday.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        users.forEach(System.out::println);
    }

    /**
     * 测试插入
     */
    @Test
    public void testInsert(){
        User user = new User();     // 帮我们自动生成ID
        user.setName("asdasdas");    // 受影响的行数
        user.setAge(18);
        user.setEmail("21313@qq.com");
        user.setId(3333L);
        int result = userMapper.insert(user);
        System.out.println(result);
        System.out.println(user);   // 发现 id 自动回调
    }

    /**
     * 更新操作
     */
    @Test
    public void testUpdate(){
        User user = new User();     // 帮我们自动生成ID
        // 通过条件动态配置
        user.setId(3333L);
        user.setName("关注狂神说");
        user.setAge(20);
        // 注意 : updateById 但是参数是一个 对象！
        int i = userMapper.updateById(user);
        System.out.println(i);
        System.out.println(user);

    }
}
