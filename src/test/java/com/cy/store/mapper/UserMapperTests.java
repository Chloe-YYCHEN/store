package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

// @RunWith(SpringRunner.class)注解是一个测试启动器，可以加载Springboot测试注解
//启动这个单元测试类
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTests {
    //idea有检测功能，接口是不能直接创建Bean的（动态代理技术解决）
    @Resource
    private UserMapper userMapper;
    /**
     * 1.必须被@Test注解修饰
     * 2.返回值必须是void
     * 3.方法的参数列表不指定任何类型
     * 4.方法的访问修饰符必须是public
     */
    @Test
    public void insert(){
        User user = new User();
        user.setUsername("tim");
        user.setPassword("122");
        Integer rows = userMapper.insert(user);
        System.out.println(rows);
    }
    @Test
    public void findByUsername() {
        String username = "user01";
        User result = userMapper.findByUsername(username);
        System.out.println(result);
    }
}