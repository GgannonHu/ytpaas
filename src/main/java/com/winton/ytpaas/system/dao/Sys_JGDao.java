package com.winton.ytpaas.system.dao;

import java.util.ArrayList;
import java.util.List;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.system.entity.Sys_JG;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Sys_JGDao extends BaseJdbcTemplate {

	/**
	 * 查询全部(CXQX01)
	 * @return
	 */
    public List<Sys_JG> getAll01() {
        List<Sys_JG> list = new ArrayList<Sys_JG>();
        String sql = "select * from Sys_JG where ZXBS=0";
        try {
            list = jdbcTemplate.query(
                sql, 
                new BeanPropertyRowMapper<>(Sys_JG.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 查询本单位及下属所有单位(CXQX02)
     * @param jgdm
     * @return
     */
    public List<Sys_JG> getAll02(String jgdm) {
    	jgdm = "'" + jgdm.replaceAll(",", "','") + "'";
        List<Sys_JG> list = new ArrayList<Sys_JG>();
        String sql = "select * from Sys_JG jg where ZXBS=0 start with jg.JGDM in (" + jgdm + ") connect by jg.SJDM = prior jg.JGDM";
        try {
            list = jdbcTemplate.query(
                sql, 
                new Object[] {  }, 
                new BeanPropertyRowMapper<>(Sys_JG.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    /**
     * 查询本单位信息(CXQX03)
     * @param jgdm
     * @return
     */
    public List<Sys_JG> getAll03(String jgdm) {
    	jgdm = "'" + jgdm.replaceAll(",", "','") + "'";
        String sql = "select * from Sys_JG where JGDM in (" + jgdm + ") and ZXBS=0";
        List<Sys_JG> list = new ArrayList<Sys_JG>();
        try {
            list = jdbcTemplate.query(
                sql, 
                new Object[] {  }, 
                new BeanPropertyRowMapper<>(Sys_JG.class)
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    /**
     * 根据机构代码获取机构信息
     * @param jgdm
     * @return
     */
    public Sys_JG getByJgdm(String jgdm) {
        String sql = "select * from Sys_JG where JGDM=? and ZXBS=0";
        Sys_JG item = new Sys_JG();
        try {
        	item = jdbcTemplate.queryForObject(
                sql, 
                new Object[] { jgdm }, 
                new BeanPropertyRowMapper<>(Sys_JG.class)
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
        return item;
    }
    
    /**
     * 根据上级代码查询下级机构信息
     * @param sjdm
     * @return
     */
    public List<Sys_JG> getBySjdm(String sjdm) {
        String sql = "select * from Sys_JG where SJDM=? and ZXBS=0 order by WZPX";
        List<Sys_JG> list = new ArrayList<Sys_JG>();
        try {
            list = jdbcTemplate.query(
                sql, 
                new Object[] { sjdm }, 
                new BeanPropertyRowMapper<>(Sys_JG.class)
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
}