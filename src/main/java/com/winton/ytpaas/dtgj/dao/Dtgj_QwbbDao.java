package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.dtgj.entity.Dtgj_Qwbb;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_QwbbDao extends BaseJdbcTemplate {

    public Dtgj_Qwbb getById(String id) {
        String sql = "select * from DTGJ_QT_QWBB where id=?";
        try {
            Dtgj_Qwbb model = jdbcTemplate.queryForObject(sql, new Object[] { id },
                    new BeanPropertyRowMapper<>(Dtgj_Qwbb.class));
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> getList(String varName, String fbsjBegin, String fbsjEnd, String wczt,
            String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 ";
        if (varName.length() > 0) {
            tmpWhere += " and xm like '%" + varName + "%' ";
        }

        if (fbsjBegin.length() > 0) {
            tmpWhere += " and FBSJ>=to_date('" + fbsjBegin + "','yyyy-MM-dd') ";
        }

        if (fbsjEnd.length() > 0) {
            tmpWhere += " and FBSJ<=to_date('" + fbsjEnd + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }

        if (wczt.length() > 0) {
            tmpWhere += " and wczt='" + wczt + "' ";
        }

        String tmpColumn = " * ";
        String tmpSql = " select " + tmpColumn + " from DTGJ_QT_QWBB " + tmpWhere + " order by tjsj desc";

        String tmpSqlFy = " select " + tmpColumn + " from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getListCount(String varName, String fbsjBegin, String fbsjEnd, String wczt) {
        int tmpRet = 0;

        String tmpWhere = " where 1=1 ";
        if (varName.length() > 0) {
            tmpWhere += " and xm like '%" + varName + "%' ";
        }

        if (fbsjBegin.length() > 0) {
            tmpWhere += " and FBSJ>=to_date('" + fbsjBegin + "','yyyy-MM-dd') ";
        }

        if (fbsjEnd.length() > 0) {
            tmpWhere += " and FBSJ<=to_date('" + fbsjEnd + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }

        if (wczt.length() > 0) {
            tmpWhere += " and wczt='" + wczt + "' ";
        }

        String tmpSql = " select COUNT(1) con from DTGJ_QT_QWBB " + tmpWhere;

        try {
            List<Map<String, Object>> temp = jdbcTemplate.queryForList(tmpSql);
            Map<String, Object> countlyb = temp.get(0);
            tmpRet = Integer.valueOf(countlyb.get("con").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public boolean add(Dtgj_Qwbb item) {
        String sql = "insert into DTGJ_QT_QWBB(xm,sfzh,qwnr,fbsj,wcsj,wczt,tjr,tjdw) values (?,?,?,?,?,?,?,?)";
        try {
            int count = jdbcTemplate.update(sql, new Object[] { item.getXM(), item.getSFZH(), item.getQWNR(),
                    item.getFBSJ(), item.getWCSJ(), item.getWCZT(), item.getTJR(), item.getTJDW() });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Dtgj_Qwbb item) {
        String sql = "update DTGJ_QT_QWBB set xm=?,sfzh=?,qwnr=?,fbsj=?,wcsj=?,wczt=? where id=?";
        try {
            int count = jdbcTemplate.update(sql, new Object[] { item.getXM(), item.getSFZH(), item.getQWNR(),
                    item.getFBSJ(), item.getWCSJ(), item.getWCZT(), item.getID() });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String id) {
        id = id.contains(",") ? id.substring(0, id.length() - 1) : id;
        String sql = "delete from DTGJ_QT_QWBB  where id in ('" + id.replace(",", "','") + "')";
        try {
            int count = jdbcTemplate.update(sql, new Object[] {});
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}