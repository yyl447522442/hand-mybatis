package com.yyl.sqlsession;

import java.util.List;

public interface SqlSession {
    //查询所有
    public <E> List<E> selectList(String statementId, Object... params) throws Exception;

    //返回唯一
    public <T> T selectOne(String statementId, Object... params) throws Exception;

    public <T> T getMapper(Class<?> mapper);
}
