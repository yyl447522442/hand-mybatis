package com.yyl.config;

import com.yyl.pojo.Configuration;
import com.yyl.pojo.MapperStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {
    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream in) throws Exception {
        Document read = new SAXReader().read(in);
        Element rootElement = read.getRootElement();
        List<Element> list = rootElement.selectNodes("//select");
        String namespace = rootElement.attributeValue("namespace");
        for (Element e : list) {
            MapperStatement mapperStatement = new MapperStatement();
            String id = e.attributeValue("id");
            mapperStatement.setId(id);
            mapperStatement.setParamType(e.attributeValue("paramType"));
            mapperStatement.setResultType(e.attributeValue("resultType"));
            mapperStatement.setSql(e.getTextTrim());
            String statementId = String.format("%s.%s", namespace, id);
            configuration.getMapperStatementMap().put(statementId, mapperStatement);
        }
    }
}
