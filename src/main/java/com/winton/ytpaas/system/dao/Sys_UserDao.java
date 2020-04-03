package com.winton.ytpaas.system.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.system.entity.Sys_User;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Sys_UserDao extends BaseJdbcTemplate {

    public Sys_User getById(String yhbm) {
        String sql = "select * from Sys_User where YHBM=? and ZT=0";
        try {
            Sys_User model = jdbcTemplate.queryForObject(
                sql, 
                new Object[] { yhbm }, 
                new BeanPropertyRowMapper<>(Sys_User.class)
            );
            return model;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Sys_User getByLoginName(String loginName) {
        String sql = "select * from Sys_User where LOGINNAME=? and ZT=0";
        try {
            Sys_User model = jdbcTemplate.queryForObject(
                sql, 
                new Object[] { loginName }, 
                new BeanPropertyRowMapper<>(Sys_User.class)
            );
            return model;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Sys_User> getAll() {
        List<Sys_User> list = new ArrayList<Sys_User>();
        String sql = "select * from Sys_User where ZT=0";
        try {
            list = jdbcTemplate.query(
                sql, 
                new BeanPropertyRowMapper<>(Sys_User.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Sys_User> getAll(JSONObject where, String begin, String end) {
    	String jgdm = where.getString("jgdm");
    	jgdm = "'" + jgdm.replaceAll(",", "','") + "'";
    	
        List<Sys_User> list = new ArrayList<Sys_User>();
        
        String tmpWhere = " where ZT=0 and DWDM in (" + jgdm + ")";
        String tmpColumn = "*";
        String tmpSql = " select " + tmpColumn + " from Sys_User " + tmpWhere + " order by CJSJ desc ";
        String tmpSqlFy = " select " + tmpColumn + " from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + end + " ) Tab where RN>= " + begin + " ";

        try {
            list = jdbcTemplate.query(
            	tmpSqlFy, 
                new BeanPropertyRowMapper<>(Sys_User.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public int getAllCount(JSONObject where) {
    	String jgdm = where.getString("jgdm");
    	jgdm = "'" + jgdm.replaceAll(",", "','") + "'";
    	
    	int tmpRet = 0;

        String tmpWhere = " where ZT=0 and DWDM in (" + jgdm + ")";
        String tmpSql = " select COUNT(1) con from Sys_User " + tmpWhere;
        try {
        	tmpRet = jdbcTemplate.queryForObject(tmpSql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmpRet;
    }


    public boolean add(Sys_User item) {
        String sql = "insert into Sys_User(YHBM,LOGINNAME,PASSWORD,YHXM,YHSFZH,YHLXDH,YHJH,DWDM,DWMC,JSBM,ZT,CJYH,CJSJ) values (SEQ_SYS_USER.NEXTVAL,?,?,?,?,?,?,?,?,?,0,?,?)";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getLOGINNAME(), 
                    item.getPASSWORD(),
                    item.getYHXM(),
                    item.getYHSFZH(),
                    item.getYHLXDH(),
                    item.getYHJH(),
                    item.getDWDM(),
                    item.getDWMC(),
                    item.getJSBM(),
                    item.getCJYH(),
                    item.getCJSJ()
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Sys_User item) {
        String sql = "update Sys_User set LOGINNAME=?,YHXM=?,YHSFZH=?,YHLXDH=?,YHJH=?,DWDM=?,DWMC=?,JSBM=?,XGYH=?,XGSJ=? where YHBM=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getLOGINNAME(),
                    item.getYHXM(),
                    item.getYHSFZH(),
                    item.getYHLXDH(),
                    item.getYHJH(),
                    item.getDWDM(),
                    item.getDWMC(),
                    item.getJSBM(),
                    item.getXGYH(),
                    item.getXGSJ(),
                    item.getYHBM()
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updatePassword(String yhbm, String password) {
        String sql = "update Sys_User set password=? where YHBM=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    password, yhbm
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String yhbm) {
    	yhbm = yhbm.contains(",") ? yhbm.substring(0, yhbm.length() - 1) : yhbm;
        String sql = "update Sys_User set ZT=1 where YHBM in (" + yhbm + ")";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] { }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    public boolean updateQXDW(String yhbm, String qxdw) {
    	String [] qxdws = qxdw.split(",");
        String sql = "begin delete from SYS_USER_DATA_DWBM where YHBM in (" + yhbm + ");";
        sql += " insert into SYS_USER_DATA_DWBM(YWLSH,YHBM,DWBM,CJSJ)";
        for(String item : qxdws) {
            sql += " select '" + Tools.get32GUID() + "','" + yhbm + "','" + item + "',SYSDATE from dual union all";
        }
        sql = sql.substring(0, sql.length() - 10);
        sql+= "; end;";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] { }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Map<String, Object>> getUserDataDwbm(String yhbm) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql = "select * from SYS_USER_DATA_DWBM where YHBM=?";
        try {
            list = jdbcTemplate.queryForList(
                sql, new Object[]{ yhbm }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


}