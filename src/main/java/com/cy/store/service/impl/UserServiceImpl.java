package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicateException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/** 处理用户数据的业务层实现类 */
@Service//自动创建对象以及对象的维护
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        // 调用findByUsername(username)判断用户是否被注册过
        String username = user.getUsername();
        User result = userMapper.findByUsername(username);
        if(result != null){
            throw new UsernameDuplicateException("用户名被占用");
        }

        //密码加密处理的实现：md5算法的形式
        //（串 + password + 串） ---md5算法进行加密，连续加载三次
        //盐值 + password + 盐值 ---盐值就是一个随机字符串
        String oldPassword = user.getPassword();
        //获取盐值（随机生成一个盐值）
        String salt = UUID.randomUUID().toString().toUpperCase();
        //补全数据：盐值的记录
        user.setSalt(salt);
        //将密码和盐值作为一个整体进行加密处理
        String md5Password = getMd5Password(oldPassword,salt);
        user.setPassword(md5Password);

        //补全数据：is_delete设置成0
        user.setIsDelete(0);
        //补全数据：4个日志字段信息
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);

        //执行注册业务功能的实现（rows == 1）
        Integer rows = userMapper.insert(user);
        if(rows != 1){
            throw new InsertException("在用户注册过程中产生了未知的异常");
        }
    }

    @Override
    public User login(String username, String password) {
        return null;
    }

    /**
     * 执行密码加密
     * @param password 原始密码
     * @param salt 盐值
     * @return 加密后的密文
     */
    private String getMd5Password(String password, String salt) {
        /*
         * 加密规则：
         * 1、无视原始密码的强度
         * 2、使用UUID作为盐值，在原始密码的左右两侧拼接
         * 3、循环加密3次
         */
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }
}