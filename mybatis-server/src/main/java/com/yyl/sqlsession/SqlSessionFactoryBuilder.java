package com.yyl.sqlsession;

import com.yyl.config.XMLConfigBuilder;
import com.yyl.pojo.Configuration;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream in) throws Exception {
        //使用dom4j解析配置文件，将解析出来的内容封装到configuration
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(in);

        //创建sqlserssiongfactory 生产sqlsession
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }
}
