package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;

import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_GjzdDao extends BaseJdbcTemplate {

    public List<Map<String, Object>> getList(Map<String, String> varSelTj, String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 ";

        tmpWhere += " and GJXLBM ='" + varSelTj.get("xlbm") + "' ";

        String tmpZdmc = varSelTj.get("zdmc");
        if (tmpZdmc.length() > 0) {
            tmpWhere += " GJZDMC like '%" + tmpZdmc + "%' ";
        }

        String tmpColums = " t.* ";
        tmpColums+=" ,( select JGMC from (select JGDM,JGMC from sys_jg order by JGDM ) where  rownum=1 and substr(JGDM,1,6)=t.XZQHDM ) XZQHMC ";
        String tmpTable = " DTGJ_GJZS_ZDXX t ";
        String tmpOrder = " order by CREATE_TIME desc ";

        String tmpSqlFy = " select * from( select ROWNUM RN , inTab.* from ( select " + tmpColums
                + " from " + tmpTable + tmpWhere + tmpOrder + " ) inTab where ROWNUM<= " + varEndCon
                + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getCon(Map<String, String> varSelTj) {
        int tmpRet = 0;

        String tmpWhere = " where 1=1 ";

        tmpWhere += " and GJXLBM ='" + varSelTj.get("xlbm") + "' ";

        String tmpZdmc = varSelTj.get("zdmc");
        if (tmpZdmc.length() > 0) {
            tmpWhere += " GJZDMC like '%" + tmpZdmc + "%' ";
        }

        String tmpTable = " DTGJ_GJZS_ZDXX ";
        String tmpSql = " select COUNT(1) con from " + tmpTable + tmpWhere;

        try {
            List<Map<String, Object>> temp = jdbcTemplate.queryForList(tmpSql);
            Map<String, Object> countlyb = temp.get(0);
            tmpRet = Integer.valueOf(countlyb.get("con").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getConByZdbm(Object varZdbm) {
        int tmpRet = 1;
        String tmpSql = " select count(1) from DTGJ_GJZS_ZDXX where GJZDBM=? ";
        try {
            tmpRet = jdbcTemplate.queryForObject(tmpSql, new Object[] { varZdbm }, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public Map<String, Object> getItemById(String varId) {
        Map<String, Object> tmpRet = null;

        String tmpWhere = " where ID = '" + varId + "' ";
        String tmpColums = "  t.* ";
        tmpColums+=" ,( select JGMC from (select JGDM,JGMC from sys_jg order by JGDM ) where  rownum=1 and substr(JGDM,1,6)=t.XZQHDM ) XZQHMC ";
        String tmpTable = " DTGJ_GJZS_ZDXX t ";
        String tmpSql = " select  " + tmpColums + " from " + tmpTable + tmpWhere;

        try {
            List<Map<String, Object>> tmpItems = jdbcTemplate.queryForList(tmpSql);
            if (tmpItems.size() > 0) {
                tmpRet = tmpItems.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public boolean add(Map<String, Object> varItem) {
        String tmpSql = " insert into DTGJ_GJZS_ZDXX (xzqhdm, gjxlbm, gjzdbm, gjzdmc, kssj, jssj, tjr, tjdw, tjdwmc) values(?,?,?,?,?,?,?,?,?) ";
        try {
            int count = jdbcTemplate.update(tmpSql,
                    new Object[] { varItem.get("xzqhdm"), varItem.get("gjxlbm"), varItem.get("gjzdbm"),
                            varItem.get("gjzdmc"), varItem.get("kssj"), varItem.get("jssj"), varItem.get("tjr"),
                            varItem.get("tjdw"), varItem.get("tjdwmc") });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Map<String, Object> varItem) {
        // xzqhdm=?, gjxlbm=?, gjzdbm=?, gjzdmc=?,
        // varItem.get("xzqhdm"), varItem.get("gjxlbm"), varItem.get("gjzdbm"),
        // varItem.get("gjzdmc"),
        String tmpSql = " update DTGJ_GJZS_ZDXX set kssj=?, jssj=? where id=? ";
        try {
            int count = jdbcTemplate.update(tmpSql,
                    new Object[] { varItem.get("kssj"), varItem.get("jssj"), varItem.get("id") });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String varIds) {

        String[] tmpIds = varIds.split(",");
        String tmpWhere = "";
        for (String id : tmpIds) {
            if (id.length() > 0) {
                tmpWhere += ",'" + id + "'";
            }
        }
        tmpWhere = tmpWhere.substring(1);

        String tmpSql = " delete from DTGJ_GJZS_ZDXX where id in ( " + tmpWhere + " ) ";
        try {
            int count = jdbcTemplate.update(tmpSql);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
