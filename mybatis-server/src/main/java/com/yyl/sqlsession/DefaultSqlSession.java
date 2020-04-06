package com.yyl.sqlsession;

import com.yyl.pojo.Configuration;
import com.yyl.pojo.MapperStatement;

import java.util.List;

public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        //调用excutor query
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        List<E> query = new SimpleExecutor().query(configuration,mapperStatement , params);
        return query;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("查询结果为空或者返回结果过多");
        }
    }
}
