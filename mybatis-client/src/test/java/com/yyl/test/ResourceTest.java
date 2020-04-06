package com.yyl.test;

import com.yyl.io.ResourceUtil;
import com.yyl.mybatis.dao.UserMapper;
import com.yyl.mybatis.domain.User;
import com.yyl.sqlsession.SqlSession;
import com.yyl.sqlsession.SqlSessionFactory;
import com.yyl.sqlsession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class ResourceTest {
    @Test
    public void test() throws Exception {
        InputStream resourceAsStream = ResourceUtil.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        User user = new User();
        user.setId(1);
        User o = sqlSession.selectOne("com.yyl.mybatis.dao.UserMapper.queryUser", user);
        System.out.println(o.toString());
        List<User> objects = sqlSession.selectList("com.yyl.mybatis.dao.UserMapper.queryUserList", user);
        System.out.println(objects.toString());
        //优化后测试 使用动态代理来访问
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = mapper.queryUserList();
        System.out.println(objects.toString());
    }


}
