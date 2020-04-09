package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;

import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_BkryDao extends BaseJdbcTemplate {

    public List<Map<String, Object>> getList(Map<String, String> varSelTj, String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 ";
        String tmpColums = " t.* ";
        String tmpTable = " DTGJ_QT_BKRY t ";
        String tmpOrder = " order by TJSJ desc ";

        String tmpSqlFy = " select * from( select ROWNUM RN , inTab.* from ( select " + tmpColums + " from " + tmpTable
                + tmpWhere + tmpOrder + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getListCon(Map<String, String> varSelTj) {
        int tmpRet = 0;

        String tmpWhere = " where 1=1 ";
        String tmpTable = " DTGJ_QT_BKRY t ";
        String tmpSql = " select count(1) CON from " + tmpTable + tmpWhere;

        try {
            tmpRet = jdbcTemplate.queryForObject(tmpSql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getConByTj(Map<String, Object> varSelTj) {
        int tmpRet = 1;

        String tmpWhere = " where 1=1 ";
        String tmpTable = " DTGJ_QT_BKRY t ";
        String tmpSql = " select count(1) CON from " + tmpTable + tmpWhere;

        try {
            tmpRet = jdbcTemplate.queryForObject(tmpSql, new Object[] {}, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public Map<String, Object> getItemById(String varId) {
        Map<String, Object> tmpRet = null;

        String tmpWhere = " where ID = '" + varId + "' ";
        String tmpColums = " t.* ";
        String tmpTable = " DTGJ_QT_BKRY t ";
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

        String tmpTable = " DTGJ_QT_BKRY ";
        String tmpCols = " NAME , IDCARD , BKNR , PICTURE ,TJR ,TJDW ,TJDWMC ";
        String tmpVals = " ?, ?, ?, ? ,? ,?, ? ";
        String tmpSql = " insert into " + tmpTable + " (" + tmpCols + ") values(" + tmpVals + ") ";
        try {
            int count = jdbcTemplate.update(tmpSql,
                    new Object[] { varItem.get("name"), varItem.get("idcard"), varItem.get("bknr"),
                            varItem.get("picture"), varItem.get("tjr"), varItem.get("tjdw"), varItem.get("tjdwmc") });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Map<String, Object> varItem) {
        String tmpTable = " DTGJ_QT_BKRY ";
        String tmpSets = " NAME=?, IDCARD=?, BKNR=?, PICTURE=? ";
        String tmpWhere = " where ID=? ";
        String tmpSql = " update " + tmpTable + " set " + tmpSets + tmpWhere;
        try {
            int count = jdbcTemplate.update(tmpSql, new Object[] { varItem.get("name"), varItem.get("idcard"),
                    varItem.get("bknr"), varItem.get("picture"), varItem.get("id") });
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

        String tmpTable = " DTGJ_QT_BKRY ";
        String tmpSql = " delete from " + tmpTable + " where ID in ( " + tmpWhere + " ) ";
        try {
            int count = jdbcTemplate.update(tmpSql);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
