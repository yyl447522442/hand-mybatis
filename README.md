自定义持久化框架总结
1、加载配置文件：描述配置文件的路径，加载配置文件为字节流，存储到内存中
    创建resourceUtil读取配置文件
   
2、创建容器对象：存放解析出来的配置
    Configuration: 核心配置类，存放数据源和sql解析结果
    MapperSatatement: sql关系映射配置类

3、解析配置文件：使用dom4j
创建类sqlSessionFactoryBuilder，解析xml文件
1、解析xml初始化配置到conguration对象中
2、创建sqlSessionFactory对象：负责生产会话 实际上就是去数据源中开启会话

4、创建sqlSessionFactory接口及实现类DefaultSqlSessionFactory
    核心方法:openSession  开启sqlsession会话
 
5、创建sqlSession接口及实现类DefaultSqlSession
    封装数据库查询操作:selectList、selectOne
    
6、创建Executor接口及实现类SimpleExecutor
    封装与JDBC实际的操作，即query、insert、update、delete
