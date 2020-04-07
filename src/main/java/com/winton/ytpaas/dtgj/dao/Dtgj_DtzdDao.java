package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;

import org.springframework.stereotype.Repository;

@Repository 
public class Dtgj_DtzdDao extends BaseJdbcTemplate {

    public List<Map<String, Object>> getList(Map<String, String> varSelTj, String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 ";

        tmpWhere += " and DTXLBM = '" + varSelTj.get("xlbm") + "' ";

        String tmpZdmc = varSelTj.get("zdmc");
        if (tmpZdmc.length() > 0) {
            tmpWhere += " and DTZDMC like '%" + tmpZdmc + "%' ";
        }

        String tmpColums = " t.* ";
        //tmpColums+=" ,( select JGMC from (select JGDM,JGMC from SYS_JG order by JGDM ) where rownum=1 and substr(JGDM,1,6)=t.XZQHDM ) XZQHMC ";
        String tmpTable = " DTGJ_DTZS_DTZDXX t ";
        String tmpOrder = " order by CREATE_TIME desc ";

        String tmpSqlFy = " select * from( select ROWNUM RN , inTab.* from ( select " + tmpColums + " from " + tmpTable
                + tmpWhere + tmpOrder + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
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

        tmpWhere += " and DTXLBM = '" + varSelTj.get("xlbm") + "' ";

        String tmpZdmc = varSelTj.get("zdmc");
        if (tmpZdmc.length() > 0) {
            tmpWhere += " and DTZDMC like '%" + tmpZdmc + "%' ";
        }

        String tmpTable = " DTGJ_DTZS_DTZDXX ";
        String tmpSql = " select count(1) CON from " + tmpTable + tmpWhere;

        try {
            List<Map<String, Object>> temp = jdbcTemplate.queryForList(tmpSql);
            Map<String, Object> countlyb = temp.get(0);
            tmpRet = Integer.valueOf(countlyb.get("CON").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getConByZdbm(Object varZdbm) {
        int tmpRet = 1;
        String tmpSql = " select count(1) from DTGJ_DTZS_DTZDXX where DTZDBM=? ";
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
        String tmpColums = " t.* ";
        //tmpColums+=" ,( select JGMC from (select JGDM,JGMC from SYS_JG order by JGDM ) where  rownum=1 and substr(JGDM,1,6)=t.XZQHDM ) XZQHMC ";
        String tmpTable = " DTGJ_DTZS_DTZDXX t ";
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

        String tmpSql = " insert into DTGJ_DTZS_DTZDXX (XZQHDM, DTXLBM, DTZDBM, DTZDMC, SDPCSDM, SDPCSMC, KSSJ, JSSJ, TJR, TJDW, TJDWMC) values(?,?,?,?,?,?,?,?,?,?,?) ";
        try {
            int count = jdbcTemplate.update(tmpSql,
                    new Object[] { varItem.get("xzqhdm"), varItem.get("dtxlbm"), varItem.get("dtzdbm"),
                            varItem.get("dtzdmc"), varItem.get("sdpcsdm"), varItem.get("sdpcsmc"), varItem.get("kssj"),
                            varItem.get("jssj"), varItem.get("tjr"), varItem.get("tjdw"), varItem.get("tjdwmc") });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Map<String, Object> varItem) {
        // xzqhdm=?, sdpcsdm=?, sdpcsmc=?, dtxlbm=?, dtzdbm=?, dtzdmc=?,
        // varItem.get("xzqhdm"), varItem.get("sdpcsdm"), varItem.get("sdpcsmc"),
        // varItem.get("dtxlbm"), varItem.get("dtzdbm"), varItem.get("dtzdmc"),
        String tmpSql = " update DTGJ_DTZS_DTZDXX set KSSJ=?, JSSJ=? where ID=? ";
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

        String tmpSql = " delete from DTGJ_DTZS_DTZDXX where ID in ( " + tmpWhere + " ) ";
        try {
            int count = jdbcTemplate.update(tmpSql);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
