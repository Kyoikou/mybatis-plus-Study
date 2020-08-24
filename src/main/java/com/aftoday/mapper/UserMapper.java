package com.aftoday.mapper;

import com.aftoday.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

// 在对应的Mapper上面继承基本的类BaseMapper 想查哪个表 泛型里面就放什么
@Repository
public interface UserMapper extends BaseMapper<User> {
    // 所有的CRUD已经编写完成了 与JPA相同
    // 你不需要像以前一样配置一大堆文件了
}
