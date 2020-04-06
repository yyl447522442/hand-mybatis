package com.yyl.sqlsession;

import com.yyl.pojo.Configuration;
import com.yyl.pojo.MapperStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    public <E> List<E> query(Configuration configuration, MapperStatement statement, Object... params) throws SQLException, Exception;
}
