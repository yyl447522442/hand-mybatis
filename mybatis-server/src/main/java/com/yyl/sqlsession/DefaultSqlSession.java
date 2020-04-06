package com.yyl.sqlsession;

import com.yyl.pojo.Configuration;
import com.yyl.pojo.MapperStatement;

import java.lang.reflect.*;
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
        List<E> query = new SimpleExecutor().query(configuration, mapperStatement, params);
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

    public <T> T getMapper(final Class<?> mapper) {
        //使用动态代理生成jdk动态代理
        Object o = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapper}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //根据不同情况调用select one和selctlist
                //1、statementId 2、namespace对应类名、id对应方法名
                String statementId = mapper.getName() + "." + method.getName();
                //2、传递参数 params = args
                //获取被调用方法的返回值类型
                Type genericReturnType = method.getGenericReturnType();
                //判断类型是否为泛型集合
                if (genericReturnType instanceof ParameterizedType) {
                    return selectList(statementId, args);
                }
                return selectOne(statementId, args);
            }
        });
        return (T) o;
    }
}
