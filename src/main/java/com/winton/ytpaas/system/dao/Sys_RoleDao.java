package com.winton.ytpaas.system.dao;

import java.util.ArrayList;
import java.util.List;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.system.entity.Sys_Role;
import com.winton.ytpaas.system.entity.Sys_Role_Data;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Sys_RoleDao extends BaseJdbcTemplate {

    public Sys_Role getById(String jsbm) {
        String sql = "select * from Sys_Role where JSBM=? and ZT=0";
        try {
            Sys_Role model = jdbcTemplate.queryForObject(
                sql, 
                new Object[] { jsbm }, 
                new BeanPropertyRowMapper<>(Sys_Role.class)
            );
            return model;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Sys_Role getByName(String jsmc) {
        String sql = "select * from Sys_Role where JSMC=? and ZT=0";
        try {
            Sys_Role model = jdbcTemplate.queryForObject(
                sql, 
                new Object[] { jsmc }, 
                new BeanPropertyRowMapper<>(Sys_Role.class)
            );
            return model;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Sys_Role> getAll() {
        List<Sys_Role> list = new ArrayList<Sys_Role>();
        String sql = "select * from Sys_Role where ZT=0";
        try {
            list = jdbcTemplate.query(
                sql, 
                new BeanPropertyRowMapper<>(Sys_Role.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean add(Sys_Role item) {
        String sql = "insert into Sys_Role(JSBM,JSMC,JSMS,JSLX,ZT,CJYH,CJSJ,BZ) values (SEQ_SYS_ROLE.NEXTVAL,?,?,?,0,?,?,?)";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getJSMC(), 
                    item.getJSMS(),
                    item.getJSLX(),
                    item.getCJYH(),
                    item.getCJSJ(),
                    item.getBZ()
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Sys_Role item) {
        String sql = "update Sys_Role set JSMC=?,JSMS=?,XGYH=?,XGSJ=? where JSBM=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getJSMC(), 
                    item.getJSMS(),
                    item.getXGYH(),
                    item.getXGSJ(),
                    item.getJSBM()
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String jsbm) {
    	jsbm = jsbm.contains(",") ? jsbm.substring(0, jsbm.length() - 1) : jsbm;
        String sql = "update Sys_Role set ZT=1 where JSBM in (" + jsbm + ")";
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
    
    public boolean saveGNQX(String jsbm, String[] menus) {
        String sql = "begin delete from SYS_ROLE_MENU  where JSBM='" + jsbm + "';";
        sql += " insert into SYS_ROLE_MENU(QXBM, JSBM, CDBM)";
        sql+= " select  SEQ_SYS_ROLE_MENU.NEXTVAL,t.* from (";

        for(String item : menus) {
            sql += " select'" + jsbm + "' a, '" + item + "' b from dual union all ";
        }
        sql = sql.substring(0, sql.length() - 10) + ")t; end;";
        try {
            int count = jdbcTemplate.update(
                sql
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveDefaultSJQX(String jsbm) {
        String sql = "begin delete from SYS_ROLE_DATA  where JSBM='" + jsbm + "';";
        sql += " insert into SYS_ROLE_DATA(QXBM,JSBM, SJCDBM, QXMC,QXURL,QXLB,QXNR)";
        sql+= " select SEQ_SYS_ROLE_DATA.NEXTVAL,t.* from (" + 
        		" select '"+jsbm+"' cc2,t1.URL cc1,t2.a||t1.CDMC cc3,t1.URL cc4,t2.b cc5,'' c6 from (" + 
        		" select b.CDMC,b.URL from SYS_ROLE_MENU a inner join SYS_MENU b on a.CDBM=b.CDBM where a.JSBM='"+jsbm+"'" + 
        		" ) t1," + 
        		"(" + 
        		"	select '查询权限-' a,'CXQX02' b from dual union all" + 
        		"	select '添加权限-' a,'TJQX' b from dual union all" + 
        		"	select '修改权限-' a,'XGQX' b from dual union all" + 
        		"	select '删除权限-' a,'SCQX' b from dual" + 
        		") t2" + 
        		")t;";

        sql += " end;";
        try {
            int count = jdbcTemplate.update(
                sql
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveSJQX(String jsbm, String cdurl, List<Sys_Role_Data> roles) {
        String sql = "begin delete from SYS_ROLE_DATA  where JSBM='" + jsbm + "' and SJCDBM='"+ cdurl +"';";
        if(roles.size() > 0 ) {
        	sql += " insert into SYS_ROLE_DATA(QXBM,JSBM, SJCDBM, QXMC,QXURL,QXLB,QXNR)";
            sql+= " select  SEQ_SYS_ROLE_DATA.NEXTVAL,t.* from (";

            for(Sys_Role_Data item : roles) {
                sql += " select " + jsbm + " a, '" + item.getSJCDBM() + "' b, '" + item.getQXMC() + "' c, '" + item.getQXURL() + "' d, '" + item.getQXLB() + "' e, '" + item.getQXNR() + "' f from dual union all ";
            }
            sql = sql.substring(0, sql.length() - 10) + ")t; end;";
        } else {
        	sql += " end;";
        }
        
        try {
            int count = jdbcTemplate.update(
                sql
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Sys_Role_Data> getSJQX(String jsbm, String url) {
    	List<Sys_Role_Data> list = new ArrayList<Sys_Role_Data>();
        String sql = "select distinct SJCDBM,QXLB,QXURL,QXNR from Sys_Role_Data where JSBM in (" + jsbm + ") and SJCDBM=?";
        try {
            list = jdbcTemplate.query(
                sql, 
                new Object[] {url},
                new BeanPropertyRowMapper<>(Sys_Role_Data.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Sys_Role> getXJJS(String jsbm) {
    	List<Sys_Role> list = new ArrayList<Sys_Role>();
        String sql = "select * from SYS_ROLE where jslx>=(select min(JSLX) from SYS_ROLE where JSBM in (" + jsbm + ")) and ZT=0";
        try {
            list = jdbcTemplate.query(
                sql, 
                new Object[] { },
                new BeanPropertyRowMapper<>(Sys_Role.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
}