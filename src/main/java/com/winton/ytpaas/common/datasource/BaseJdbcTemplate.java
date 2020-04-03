package com.winton.ytpaas.common.datasource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BaseJdbcTemplate {

    @Autowired
    @Qualifier("oracleJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;
    
}