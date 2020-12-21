package com.cex.config;

import com.cex.common.security.dataCryptor.DataCryptor;
import com.cex.common.security.dataCryptor.MysqlDataCryptorImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(basePackages = {"com.cex.**.mapper"})
@EnableTransactionManagement
public class DbConfig {
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${mysql.url}")
    private String url;
    @Value("${mysql.user}")
    private String user;
    @Value("${mysql.password}")
    private String password;
    @Bean
    public DataCryptor mysqlDataCryptor() {
        return new MysqlDataCryptorImpl();
    }

    @Bean
    public DataSource dataSource() {
        DataCryptor dataCryptor = mysqlDataCryptor();
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");


        dataSource.setJdbcUrl(dataCryptor.decryptData(url));
        dataSource.setUsername(dataCryptor.decryptData(user));
        dataSource.setPassword(dataCryptor.decryptData(password));
        dataSource.setMinimumIdle(5);
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setMaximumPoolSize(20);

        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());

    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/com/cex/**/mapper/*Mapper.xml"));
        factoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis/mybatis-config.xml"));
//        factoryBean.setTypeAliasesPackage("");
//        factoryBean.setTypeHandlersPackage("");

        return factoryBean;

    }

    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
