package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.dtgj.entity.Dtgj_Abry;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_AbryDao extends BaseJdbcTemplate {

    public Dtgj_Abry getById(String id) {
        String sql = "select * from DTGJ_TYSJ_ABRY where id=?";
        try {
            Dtgj_Abry model = jdbcTemplate.queryForObject(sql, new Object[] { id },
                    new BeanPropertyRowMapper<>(Dtgj_Abry.class));
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> getList(String dtzdmc, String gjxlbm, String gmsfhm, String xm, String dw,
            String varBegCon, String varEndCon, String gajgjgdm) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 and gajgjgdm like '" + gajgjgdm + "%'";
        if (dtzdmc.length() > 0) {
            tmpWhere += " and DTZDBM = '" + dtzdmc + "' ";
        }

        if (gjxlbm.length() > 0) {
            tmpWhere += " and GJXLBM = '" + gjxlbm + "' ";
        }

        if (gmsfhm.length() > 0) {
            tmpWhere += " and ABRY_GMSFHM like '%" + gmsfhm + "%' ";
        }

        if (xm.length() > 0) {
            tmpWhere += " and ABRY_XM like '%" + xm + "%' ";
        }

        if (dw.length() > 0) {
            tmpWhere += " and ABRY_DWMC like '%" + dw + "%' ";
        }

        String tmpColumn = " * ";
        String tmpSql = " select " + tmpColumn + " from DTGJ_TYSJ_ABRY " + tmpWhere + " order by CREATE_TIME desc";

        String tmpSqlFy = " select " + tmpColumn + " from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getListCount(String dtzdmc, String gjxlbm, String gmsfhm, String xm, String dw, String gajgjgdm) {
        int tmpRet = 0;

        String tmpWhere = " where 1=1 and gajgjgdm like '" + gajgjgdm + "%'";
        if (dtzdmc.length() > 0) {
            tmpWhere += " and DTZDBM = '" + dtzdmc + "' ";
        }

        if (gjxlbm.length() > 0) {
            tmpWhere += " and GJXLBM = '" + gjxlbm + "' ";
        }

        if (gmsfhm.length() > 0) {
            tmpWhere += " and ABRY_GMSFHM like '%" + gmsfhm + "%' ";
        }

        if (xm.length() > 0) {
            tmpWhere += " and ABRY_XM like '%" + xm + "%' ";
        }

        if (dw.length() > 0) {
            tmpWhere += " and ABRY_DWMC like '%" + dw + "%' ";
        }

        String tmpSql = " select COUNT(1) con from DTGJ_TYSJ_ABRY " + tmpWhere;

        try {
            List<Map<String, Object>> temp = jdbcTemplate.queryForList(tmpSql);
            Map<String, Object> countlyb = temp.get(0);
            tmpRet = Integer.valueOf(countlyb.get("con").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public List<Map<String, Object>> getXlxx(String xzqh) {
        List<Map<String, Object>> tmpRet = null;
        String tmpSql = " select DTXLBH as BM,DTXLMC as MC from DTGJ_DTZS_XLXX where XZQHDM like '" + xzqh
                + "%' union all select GJXLBM as BM,GJXLMC as MC from DTGJ_GJZS_XLXX where XZQHDM like '" + xzqh + "%'";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public List<Map<String, Object>> getZdxx(String bm) {
        List<Map<String, Object>> tmpRet = null;
        String tmpSql = "";
        tmpSql = "select DTZDBM BM,DTZDMC as MC from DTGJ_DTZS_DTZDXX where DTXLBM = '" + bm
                + "' union all select GJZDBM BM,GJZDMC as MC from DTGJ_GJZS_ZDXX where GJXLBM = '" + bm + "'";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public boolean add(Dtgj_Abry item) {
        String sql = "insert into DTGJ_TYSJ_ABRY(GAJGJGDM,DTZDBM,DTZDMC,GJXLBM,ABRY_GMSFHM,ABRY_XM,ABRY_YDDH,ABRY_ZZMMDM,ABRY_MZDM,ABRY_DZMC,ABRY_DWMC,TJR,TJDW,TJDWMC)values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            int count = jdbcTemplate.update(sql,
                    new Object[] { item.getGAJGJGDM(), item.getDTZDBM(), item.getDTZDMC(), item.getGJXLBM(),
                            item.getABRY_GMSFHM(), item.getABRY_XM(), item.getABRY_YDDH(), item.getABRY_ZZMMDM(),
                            item.getABRY_MZDM(), item.getABRY_DZMC(), item.getABRY_DWMC(), item.getTJR(),
                            item.getTJDW(), item.getTJDWMC() });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Dtgj_Abry item) {
        String sql = "update DTGJ_TYSJ_ABRY set dtzdbm=?,dtzdmc=?,gjxlbm=?,abry_gmsfhm=?,abry_xm=?,abry_yddh=?,abry_zzmmdm=?,abry_mzdm=?,abry_dzmc=?,abry_dwmc=? where id=?";
        try {
            int count = jdbcTemplate.update(sql,
                    new Object[] { item.getDTZDBM(), item.getDTZDMC(), item.getGJXLBM(), item.getABRY_GMSFHM(),
                            item.getABRY_XM(), item.getABRY_YDDH(), item.getABRY_ZZMMDM(), item.getABRY_MZDM(),
                            item.getABRY_DZMC(), item.getABRY_DWMC(), item.getID() });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String id) {
        id = id.contains(",") ? id.substring(0, id.length() - 1) : id;
        String sql = "delete from DTGJ_TYSJ_ABRY where id in ('" + id.replace(",", "','") + "')";
        try {
            int count = jdbcTemplate.update(sql, new Object[] {});
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}