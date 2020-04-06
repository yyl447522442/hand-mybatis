package com.yyl.mybatis.dao;

import com.yyl.mybatis.annotation.Mapper;
import com.yyl.mybatis.domain.User;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<User> queryUserList();

    public User queryUser();
}
