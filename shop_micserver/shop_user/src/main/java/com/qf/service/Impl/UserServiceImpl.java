package com.qf.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.UserMapper;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public int register(User user) {
        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.eq("username",user.getUsername());
        User user1 = userMapper.selectOne(wrapper1);
        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper2.eq("email",user.getEmail());
        User user2 = userMapper.selectOne(wrapper2);
        if(user1 != null){
            return -1;
        }else if (user2 != null){
            return -2;
        }
        userMapper.insert(user);
        return 0;
    }

    @Override
    public User queryUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.eq("username",username);
        return  userMapper.selectOne(wrapper);
    }

    @Override
    public void setPass(String username, String password) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);
        user.setPassword(password);
        userMapper.updateById(user);
    }
}
