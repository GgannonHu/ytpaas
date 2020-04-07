package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;

import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_ZdryyjDao extends BaseJdbcTemplate {

    public List<Map<String, Object>> getRlyjList(Map<String, String> varSelTj, String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 ";

        String tmpXm = varSelTj.get("xm");
        String tmpSfzh = varSelTj.get("sfzh");
        if (tmpXm.length() > 0) {
            tmpWhere += " and t.ZDRY_XM like '%" + tmpXm + "%' ";
        }

        if (tmpSfzh.length() > 0) {
            tmpWhere += " and t.ZDRY_GMSFHM like '%" + tmpSfzh + "%' ";
        }

        String tmpColums = " t.* ";
        String tmpTable = " DTGJ_TYSJ_RLYJ t ";
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

    public int getRlyjCon(Map<String, String> varSelTj) {
        int tmpRet = 0;

        String tmpWhere = " where 1=1 ";
        String tmpTable = " DTGJ_TYSJ_RLYJ ";
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

    public Map<String, Object> getRlyjById(String varId) {
        Map<String, Object> tmpRet = null;

        String tmpWhere = " where ID = '" + varId + "' ";
        String tmpColums = " t.* ";
        String tmpTable = " DTGJ_TYSJ_RLYJ t ";
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

    public Map<String, Object> getZdryBySfzh(String varSfzh) {
        Map<String, Object> tmpRet = null;

        String tmpWhere = " where ZDRY_GMSFHM = '" + varSfzh + "' ";
        String tmpColums = " t.* ";
        String tmpTable = " DTGJ_TYSJ_ZDRY t ";
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
}
