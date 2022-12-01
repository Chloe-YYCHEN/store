package com.cy.store.service;

import com.cy.store.entity.User;

//用户模块业务接口
public interface IUserService {
    /**
     * 用户注册
     * @param user 用户数据
     */
    void reg(User user);

    User login(String username, String password);
}
