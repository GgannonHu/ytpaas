package com.winton.ytpaas.common.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据源配置类
 */
@Configuration
public class DataSourceConfig {
    
    
    @Primary
    @Bean(name = "oracleDataSourceProperties")
    @Qualifier("oracleDataSourceProperties")
    @ConfigurationProperties(prefix="spring.datasource.oracle")
    public DataSourceProperties oracleDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "oracleDataSource")
    @Qualifier("oracleDataSource")
    @ConfigurationProperties(prefix="spring.datasource.oracle")
    public DataSource oracleDataSource(@Qualifier("oracleDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }


    @Bean(name = "oracleJdbcTemplate")
    public JdbcTemplate oracleJdbcTemplate(
            @Qualifier("oracleDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}