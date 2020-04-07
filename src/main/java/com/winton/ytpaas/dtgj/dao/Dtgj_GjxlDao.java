package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;

import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_GjxlDao extends BaseJdbcTemplate {

    public List<Map<String, Object>> getList(Map<String, String> varSelTj, String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 ";

        String tmpXlmc = varSelTj.get("xlmc");
        if (tmpXlmc.length() > 0) {
            tmpWhere += " and GJXLMC like '%" + tmpXlmc + "%' ";
        }

        String tmpZdmc = varSelTj.get("zdmc");
        if (tmpZdmc.length() > 0) {
            tmpWhere += " and GJXLBM in (select GJXLBM from DTGJ_GJZS_ZDXX where GJZDMC like '%" + tmpZdmc + "%') ";
        } 

        tmpWhere += " and XZQHDM like '" + varSelTj.get("dwdm") + "%' ";

        String tmpColums = " t.* ";
        //tmpColums+=" ,( select JGMC from (select JGDM,JGMC from SYS_JG order by JGDM ) where ROWNUM=1 and substr(JGDM,1,6)=t.XZQHDM ) XZQHMC ";
        String tmpTable = " DTGJ_GJZS_XLXX t ";
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

        String tmpXlmc = varSelTj.get("xlmc");
        if (tmpXlmc.length() > 0) {
            tmpWhere += " and GJXLMC like '%" + tmpXlmc + "%' ";
        }

        String tmpZdmc = varSelTj.get("zdmc");
        if (tmpZdmc.length() > 0) {
            tmpWhere += " and GJXLBM in (select GJXLBM from DTGJ_GJZS_ZDXX where GJZDMC like '%" + tmpZdmc + "%') ";
        }

        tmpWhere += " and XZQHDM like '" + varSelTj.get("dwdm") + "%' ";

        String tmpTable = " DTGJ_GJZS_XLXX ";
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

    public int getConByXlbm(Object varXlbm) {
        int tmpRet = 1;

        String tmpSql = " select count(1) from DTGJ_GJZS_XLXX where GJXLBM=? ";

        try {
            tmpRet = jdbcTemplate.queryForObject(tmpSql, new Object[] { varXlbm }, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public Map<String, Object> getItemById(String varId) {
        Map<String, Object> tmpRet = null;

        String tmpWhere = " where ID = '" + varId + "' ";
        String tmpColums = " t.* ";
        //tmpColums+=" ,( select JGMC from (select JGDM,JGMC from SYS_JG order by JGDM ) where ROWNUM=1 and substr(JGDM,1,6)=t.XZQHDM ) XZQHMC ";
        String tmpTable = " DTGJ_GJZS_XLXX t ";
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

        String tmpSql = " insert into DTGJ_GJZS_XLXX (XZQHDM, GJXLBM, GJXLMC, QDZSMC_KSSJ, QDZSMC_JSSJ, ZDZSMC_KSSJ, ZDZSMC_JSSJ, GJXLQDZ, GJXLZDZ, TJR, TJDW, TJDWMC) values(?,?,?,?,?,?,?,?,?,?,?,?) ";
        try {
            int count = jdbcTemplate.update(tmpSql,
                    new Object[] { varItem.get("xzqhdm"), varItem.get("gjxlbm"), varItem.get("gjxlmc"),
                            varItem.get("qdzsmc_kssj"), varItem.get("qdzsmc_jssj"), varItem.get("zdzsmc_kssj"),
                            varItem.get("zdzsmc_jssj"), varItem.get("gjxlqdz"), varItem.get("gjxlzdz"),
                            varItem.get("tjr"), varItem.get("tjdw"), varItem.get("tjdwmc") });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Map<String, Object> varItem) {
        // XZQHDM=?, GJXLBM=?, GJXLMC=?,
        // varItem.get("xzqhdm"), varItem.get("gjxlbm"), varItem.get("gjxlmc"),
        String tmpSql = " update DTGJ_GJZS_XLXX set QDZSMC_KSSJ=?, QDZSMC_JSSJ=?, ZDZSMC_KSSJ=?, ZDZSMC_JSSJ=?, GJXLQDZ=?, GJXLZDZ=? where ID=? ";
        try {
            int count = jdbcTemplate.update(tmpSql,
                    new Object[] { varItem.get("qdzsmc_kssj"), varItem.get("qdzsmc_jssj"), varItem.get("zdzsmc_kssj"),
                            varItem.get("zdzsmc_jssj"), varItem.get("gjxlqdz"), varItem.get("gjxlzdz"),
                            varItem.get("id") });
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

        String tmpSql = " delete from DTGJ_GJZS_XLXX where ID in ( " + tmpWhere + " ) ";
        try {
            int count = jdbcTemplate.update(tmpSql);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
