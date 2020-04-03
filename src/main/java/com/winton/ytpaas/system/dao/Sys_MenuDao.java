package com.winton.ytpaas.system.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.system.entity.Sys_Menu;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Sys_MenuDao extends BaseJdbcTemplate {

    public Sys_Menu getById(String cdbm) {
        String sql = "select * from Sys_Menu where CDBM=? and ZT=0";
        try {
            Sys_Menu model = jdbcTemplate.queryForObject(
                sql, 
                new Object[] { cdbm }, 
                new BeanPropertyRowMapper<>(Sys_Menu.class)
            );
            return model;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Sys_Menu> getAll() {
        List<Sys_Menu> list = new ArrayList<Sys_Menu>();
        String sql = "select * from Sys_Menu where ZT=0";
        try {
            list = jdbcTemplate.query(
                sql, 
                new BeanPropertyRowMapper<>(Sys_Menu.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Map<String, Object>> getGNQX(String jsbm) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql = "select a.*,(case when exists(select 1 from SYS_ROLE_MENU b where b.JSBM='" + jsbm + "' and b.CDBM=a.CDBM) then 'true' else '' end) LAY_CHECKED from Sys_Menu a where ZT=0";
        try {
            list = jdbcTemplate.queryForList(
                sql
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Map<String, Object>> getAllByJSBM(String jsbms) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql = "select distinct b.*,(case when (select count(1) from SYS_MENU d where d.SJBM=b.CDBM) > 0 then 1 else 0 end) SFZCD from SYS_ROLE_MENU a inner join SYS_MENU b on a.CDBM=b.CDBM" + 
        		" where a.JSBM in ("+ jsbms +") and b.ZT=0 order by to_number(PX)";
        
        try {
            list = jdbcTemplate.queryForList(
                sql, 
                new Object[] { }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Sys_Menu> getByJSBM(String jsbm) {
        List<Sys_Menu> list = new ArrayList<Sys_Menu>();
        String sql = "select b.* from SYS_ROLE_MENU a inner join SYS_MENU b on a.CDBM=b.CDBM" + 
        		" where a.JSBM in ("+ jsbm +") and b.ZT=0 order by to_number(PX)";
        
        try {
            list = jdbcTemplate.query(
                sql, 
                new Object[] { },
                new BeanPropertyRowMapper<>(Sys_Menu.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<Sys_Menu> getAll(String sjbm) {
        List<Sys_Menu> list = new ArrayList<Sys_Menu>();
        String sql = "select * from Sys_Menu where SJBM =? and ZT=0";
        try {
            list = jdbcTemplate.query(
                sql, 
                new Object[] {sjbm},
                new BeanPropertyRowMapper<>(Sys_Menu.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }



    public boolean add(Sys_Menu item) {
        String sql = "insert into Sys_Menu(CDBM,CDMC,SJBM,URL,PX,ZT,CJYH,CJSJ,BZ ) values (SEQ_SYS_MENU.NEXTVAL,?,?,?,?,0,?,?,?)";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getCDMC(),
                    item.getSJBM(),
                    item.getURL(),
                    item.getPX(),
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

    public boolean update(Sys_Menu item) {
        String sql = "update Sys_Menu set CDMC=?,URL=?,PX=?,BZ=?,XGYH=?,XGSJ=? where CDBM=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getCDMC(),
                    item.getURL(),
                    item.getPX(),
                    item.getBZ(),
                    item.getXGYH(),
                    item.getXGSJ(),
                    item.getCDBM()
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String cdbm) {
    	cdbm = cdbm.contains(",") ? cdbm.substring(0, cdbm.length() - 1) : cdbm;
        String sql = "update Sys_Menu set ZT=1 where CDBM in (" + cdbm + ")";
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

    
    public boolean updatesort(JSONArray arr) {
        try {
            String sql = "";
            for (Object object : arr) {
                JSONObject obj = JSONObject.parseObject(object.toString());
                sql += "update Sys_Menu set PX="+obj.getString("px")+" where CDBM="+obj.getString("cdbm")+";";
            }
            int count = jdbcTemplate.update(
                sql
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}