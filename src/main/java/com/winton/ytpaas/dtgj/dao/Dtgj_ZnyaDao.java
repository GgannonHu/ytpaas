package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;
import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.dtgj.entity.Dtgj_Znya;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_ZnyaDao extends BaseJdbcTemplate {

    public Dtgj_Znya getById(String id) {
        String sql = "select * from DTGJ_QT_ZNYA where id=?";
        try {
            Dtgj_Znya model = jdbcTemplate.queryForObject(sql, new Object[] { id },
                    new BeanPropertyRowMapper<>(Dtgj_Znya.class));
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> getList(String varName, String dj, String fbsjBegin, String fbsjEnd,
            String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 ";
        if (varName.length() > 0) {
            tmpWhere += " and name like '%" + varName + "%' ";
        }

        if (dj.length() > 0) {
            tmpWhere += " and dj='" + dj + "' ";
        }

        if (fbsjBegin.length() > 0) {
            tmpWhere += " and TJSJ>=to_date('" + fbsjBegin + "','yyyy-MM-dd') ";
        }

        if (fbsjEnd.length() > 0) {
            tmpWhere += " and TJSJ<=to_date('" + fbsjEnd + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }

        String tmpColumn = " * ";
        String tmpSql = " select " + tmpColumn + " from DTGJ_QT_ZNYA " + tmpWhere + " order by TJSJ desc";

        String tmpSqlFy = " select " + tmpColumn + " from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getListCount(String varName, String dj, String fbsjBegin, String fbsjEnd) {
        int tmpRet = 0;

        String tmpWhere = " where 1=1 ";
        if (varName.length() > 0) {
            tmpWhere += " and name like '%" + varName + "%' ";
        }

        if (dj.length() > 0) {
            tmpWhere += " and dj='" + dj + "' ";
        }

        if (fbsjBegin.length() > 0) {
            tmpWhere += " and TJSJ>=to_date('" + fbsjBegin + "','yyyy-MM-dd') ";
        }

        if (fbsjEnd.length() > 0) {
            tmpWhere += " and TJSJ<=to_date('" + fbsjEnd + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }

        String tmpSql = " select COUNT(1) con from DTGJ_QT_ZNYA " + tmpWhere;

        try {
            List<Map<String, Object>> temp = jdbcTemplate.queryForList(tmpSql);
            Map<String, Object> countlyb = temp.get(0);
            tmpRet = Integer.valueOf(countlyb.get("con").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public boolean add(Dtgj_Znya item) {
        String sql = "insert into DTGJ_QT_ZNYA(id,name,dj,nr,tjr,tjdw,tjdwmc) values (?,?,?,?,?,?,?)";
        try {
            int count = jdbcTemplate.update(sql, new Object[] { item.getID(), item.getNAME(), item.getDJ(),
                    item.getNR(), item.getTJR(), item.getTJDW(), item.getTJDWMC() });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Dtgj_Znya item) {
        String sql = "update DTGJ_QT_ZNYA set name=?,dj=?,nr=? where id=?";
        try {
            int count = jdbcTemplate.update(sql,
                    new Object[] { item.getNAME(), item.getDJ(), item.getNR(), item.getID() });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String id) {
        id = id.contains(",") ? id.substring(0, id.length() - 1) : id;
        String sql = "delete from DTGJ_QT_ZNYA where id in ('" + id.replace(",", "','") + "')";
        try {
            int count = jdbcTemplate.update(sql, new Object[] {});
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}