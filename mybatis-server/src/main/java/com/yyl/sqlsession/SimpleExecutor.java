package com.yyl.sqlsession;

import com.yyl.handler.GenericTokenParser;
import com.yyl.handler.ParameterMapping;
import com.yyl.handler.ParameterMappingTokenHandler;
import com.yyl.pojo.BoundSql;
import com.yyl.pojo.Configuration;
import com.yyl.pojo.MapperStatement;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {
    @Override
    public <E> List<E> query(Configuration configuration, MapperStatement statement, Object... params) throws Exception {
        //1、获取连接
        Connection connection = configuration.getDataSource().getConnection();

        //2、获取sql语句:未解析前的 解析SQL
        String sql = statement.getSql();
        BoundSql boundSql = parseSql(sql);
        //3、获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4、设置参数
        String paramType = statement.getParamType();
        Class<?> classType = getClassType(paramType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();
            //反射获取实体对象中的属性值
            Field declaredField = classType.getDeclaredField(content);
            declaredField.setAccessible(true);
            Object val = declaredField.get(params[0]);
            preparedStatement.setObject(i + 1, val);
        }
        //5、执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        //6、结果参数赋值
        String resultType = statement.getResultType();
        Class<?> resultClassType = getClassType(resultType);
        List list = new ArrayList();
        while (resultSet.next()) {
            Object o = resultClassType.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnLabel = metaData.getColumnName(i);
                Object val = resultSet.getObject(columnLabel);
                //使用反射，根据实体和数据库对应关系设置值
                //内省库中类，对类中的字段生成读写方法
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnLabel, resultClassType);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, val);
            }
            list.add(o);
        }
        return list;
    }

    private Class<?> getClassType(String paramType) throws ClassNotFoundException {
        if (paramType != null) {
            Class<?> aClass = Class.forName(paramType);
            return aClass;
        }
        return null;
    }

    /**
     * 解析sql
     * 1、将#｛｝用？代替
     * 2、将#{}里面值进行存储
     *
     * @param sql
     * @return
     */
    private BoundSql parseSql(String sql) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler();
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String psql = parser.parse(sql);
        List<ParameterMapping> parameterMappings = handler.getParameterMappings();
        BoundSql build = BoundSql.builder().sqlText(psql).parameterMappingList(parameterMappings).build();
        return build;
    }
}
