package com.yyl.test;

import com.yyl.io.ResourceUtil;
import com.yyl.mybatis.domain.User;
import com.yyl.sqlsession.SqlSession;
import com.yyl.sqlsession.SqlSessionFactory;
import com.yyl.sqlsession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;

public class ResourceTest {
    @Test
    public void test() throws Exception {
        InputStream resourceAsStream = ResourceUtil.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        User user = new User();
        user.setId(1);
        User o = sqlSession.selectOne("com.yyl.mybatis.mapper.UserMapper.queryUser", user);
        System.out.println(o.toString());
    }


}
