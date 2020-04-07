package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.dtgj.entity.Dtgj_Qwll;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_QwllDao extends BaseJdbcTemplate {

    public Dtgj_Qwll getById(String id) {
        String sql = "select * from DTGJ_TYSJ_QWLL where id=?";
        try {
            Dtgj_Qwll model = jdbcTemplate.queryForObject(sql, new Object[] { id },
                    new BeanPropertyRowMapper<>(Dtgj_Qwll.class));
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> getList(String gjgsmc, String xm, String lxdh, String varBegCon, String varEndCon,
            String gajgjgdm) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 and gajgjgdm like '" + gajgjgdm + "%'";
        if (gjgsmc.length() > 0) {
            tmpWhere += " and GJGSMC like '%" + gjgsmc + "%' ";
        }

        if (xm.length() > 0) {
            tmpWhere += " and QWLL_XM like '%" + xm + "%' ";
        }

        if (lxdh.length() > 0) {
            tmpWhere += " and QWLL_LXDH like '%" + lxdh + "%' ";
        }

        String tmpColumn = " * ";
        String tmpSql = " select " + tmpColumn + " from DTGJ_TYSJ_QWLL " + tmpWhere + " order by CREATE_TIME desc";

        String tmpSqlFy = " select " + tmpColumn + " from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getListCount(String gjgsmc, String xm, String lxdh, String gajgjgdm) {
        int tmpRet = 0;

        String tmpWhere = " where 1=1 and gajgjgdm like '" + gajgjgdm + "%'";
        if (gjgsmc.length() > 0) {
            tmpWhere += " and GJGSMC like '%" + gjgsmc + "%' ";
        }

        if (xm.length() > 0) {
            tmpWhere += " and QWLL_XM like '%" + xm + "%' ";
        }

        if (lxdh.length() > 0) {
            tmpWhere += " and QWLL_LXDH like '%" + lxdh + "%' ";
        }

        String tmpSql = " select COUNT(1) con from DTGJ_TYSJ_QWLL " + tmpWhere;

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

    public boolean add(Dtgj_Qwll item) {
        String sql = "insert into DTGJ_TYSJ_QWLL(GAJGJGDM,DTZDBM,DTZDMC,GJXLBM,GJGSMC,QWLL_XM,QWLL_LXDH,QWLL_JYBH,QWLB_QWLBDM,TJR,TJDW,TJDWMC)values (?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            int count = jdbcTemplate.update(sql,
                    new Object[] { item.getGAJGJGDM(), item.getDTZDBM(), item.getDTZDMC(), item.getGJXLBM(),
                            item.getGJGSMC(), item.getQWLL_XM(), item.getQWLL_LXDH(), item.getQWLL_JYBH(),
                            item.getQWLB_QWLBDM(), item.getTJR(), item.getTJDW(), item.getTJDWMC() });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Dtgj_Qwll item) {
        String sql = "update DTGJ_TYSJ_QWLL set dtzdbm=?,dtzdmc=?,gjxlbm=?,gjgsmc=?,qwll_xm=?,qwll_lxdh=?,qwll_jybh=?,qwlb_qwlbdm=? where id=?";
        try {
            int count = jdbcTemplate.update(sql,
                    new Object[] { item.getDTZDBM(), item.getDTZDMC(), item.getGJXLBM(), item.getGJGSMC(),
                            item.getQWLL_XM(), item.getQWLL_LXDH(), item.getQWLL_JYBH(), item.getQWLB_QWLBDM(),
                            item.getID() });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String id) {
        id = id.contains(",") ? id.substring(0, id.length() - 1) : id;
        String sql = "delete from DTGJ_TYSJ_QWLL where id in ('" + id.replace(",", "','") + "')";
        try {
            int count = jdbcTemplate.update(sql, new Object[] {});
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}