package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;

import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_YjctDao extends BaseJdbcTemplate {

    public List<Map<String, Object>> getList(String varName, String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 ";
        if (varName.length() > 0) {
            tmpWhere += " and NAME like '%" + varName + "%' ";
        }

        String tmpColumn = " ID,NAME,NR,TJR,TJDW,TJSJ ";
        String tmpSql = " select " + tmpColumn + " from DTGJ_QT_YJCT " + tmpWhere + " order by tjsj desc ";

        String tmpSqlFy = " select " + tmpColumn + " from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getCon(String varName) {
        int tmpRet = 0;

        String tmpWhere = " where 1=1 ";
        if (varName.length() > 0) {
            tmpWhere += " and NAME like '%" + varName + "%' ";
        }
        String tmpSql = " select COUNT(1) con from DTGJ_QT_YJCT " + tmpWhere;

        try {
            List<Map<String, Object>> temp = jdbcTemplate.queryForList(tmpSql);
            Map<String, Object> countlyb = temp.get(0);
            tmpRet = Integer.valueOf(countlyb.get("con").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public Map<String, Object> getItemById(String varId) {
        Map<String, Object> tmpRet = null;

        String tmpSql = " select ID,NAME,NR,TJR,TJDW,TJSJ from DTGJ_QT_YJCT where ID = '" + varId + "' ";

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
        String tmpSql = "insert into DTGJ_QT_YJCT(id,name,nr,tjr,tjdw,tjsj) values (?,?,?,?,?,?) ";
        try {
            int count = jdbcTemplate.update(tmpSql, new Object[] { varItem.get("id"), varItem.get("name"),
                    varItem.get("nr"), varItem.get("tjr"), varItem.get("tjdw"), varItem.get("tjsj") });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Map<String, Object> varItem) {

        String tmpSql = "update DTGJ_QT_YJCT set name=?,nr=? where id=? ";
        try {
            int count = jdbcTemplate.update(tmpSql,
                    new Object[] { varItem.get("name"), varItem.get("nr"), varItem.get("id") });
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

        String tmpSql = " delete from DTGJ_QT_YJCT where id in ( " + tmpWhere + " ) ";
        try {
            int count = jdbcTemplate.update(tmpSql);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
