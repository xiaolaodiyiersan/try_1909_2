package com.qf.service;

import com.qf.entity.User;

public interface IUserService {
    int register(User user);

    User queryUserByUsername(String username);

    void setPass(String username, String password);
}
