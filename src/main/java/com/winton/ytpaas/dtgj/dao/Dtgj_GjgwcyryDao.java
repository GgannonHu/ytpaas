package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;

import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_GjgwcyryDao extends BaseJdbcTemplate {

    public Map<String, Object> getById(String id) {
        Map<String, Object> tmpRet = null;
        String sql = "select t.ID,m.MC MZ,t.XZQYDM,t.GJGWCYRY_DWMC,t.GJGWCYRY_QYBM,t.GJGWCYRY_GWMC,t.GJGWCYRY_GMSFZH,t.GJGWCYRY_XM,"
                + "t.GJGWCYRY_MZDM,t.GJGWCYRY_WFFZJLMS,t.GJGWCYRY_LXDH,t.GJGWCYRY_DZMC,t.CREATE_TIME,t.XGSJ,t.ZXSJ,t.OPER_REASON,"
                + "t.TJR,t.TJDW,t.TJDWMC from DTGJ_TYSJ_GJGWCYRY t inner join sys_xtcs m on t.gjgwcyry_mzdm=m.bm and m.cslx='MZ' where t.id=?";
        try {
            List<Map<String, Object>> model = jdbcTemplate.queryForList(
                sql, 
                new Object[] { id }
            );
            if (model.size() > 0) {
                tmpRet = model.get(0);
            }
            return tmpRet;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Map<String, Object>> getList(String varuser,String varname,String varsfzh,String vardwmc, String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;
        String tmpWhere = " where 1=1 ";
        if (varname != null && varname.length() > 0) {
            tmpWhere += " and GJGWCYRY_XM like '%" + varname + "%' ";
        }
        if (varsfzh != null && varsfzh.length() > 0) {
            tmpWhere += " and GJGWCYRY_GMSFZH like '%" + varsfzh + "%' ";
        }
        if (vardwmc != null && vardwmc.length() > 0) {
            tmpWhere += " and GJGWCYRY_DWMC like '%" + vardwmc + "%' ";
        }
        String tmpColumn = " '"+varuser+"' DQYH,t.XZQYDM,t.GJGWCYRY_DWMC,t.GJGWCYRY_QYBM,t.GJGWCYRY_GWMC,t.GJGWCYRY_GMSFZH,t.GJGWCYRY_XM,"
        + "t.GJGWCYRY_MZDM,t.GJGWCYRY_WFFZJLMS,t.GJGWCYRY_LXDH,t.GJGWCYRY_DZMC,t.CREATE_TIME,t.XGSJ,t.ZXSJ,t.OPER_REASON,"
        + "t.ID,t.TJR,t.TJDW,t.TJDWMC ";
        String tmpSql = " select " + tmpColumn + " from DTGJ_TYSJ_GJGWCYRY t " + tmpWhere + " order by CREATE_TIME desc ";
        String tmpSqlFy = " select * from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getCon(String varname,String varsfzh,String vardwmc) {
        int tmpRet = 0;
        String tmpWhere = " where 1=1 ";
        if (varname != null && varname.length() > 0) {
            tmpWhere += " and GJGWCYRY_XM like '%" + varname + "%' ";
        }
        if (varsfzh != null && varsfzh.length() > 0) {
            tmpWhere += " and GJGWCYRY_GMSFZH like '%" + varsfzh + "%' ";
        }
        if (vardwmc != null && vardwmc.length() > 0) {
            tmpWhere += " and GJGWCYRY_DWMC like '%" + vardwmc + "%' ";
        }
        String tmpSql = " select COUNT(1) con from DTGJ_TYSJ_GJGWCYRY t " + tmpWhere;
        try {
            tmpRet = jdbcTemplate.queryForObject(tmpSql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public boolean add(Map<String, Object> item) {
        String sql = "insert into DTGJ_TYSJ_GJGWCYRY t(ID,TJR,TJDW,TJDWMC,XZQYDM,GJGWCYRY_DWMC,"
        +"GJGWCYRY_QYBM,GJGWCYRY_GWMC,GJGWCYRY_GMSFZH,GJGWCYRY_XM,GJGWCYRY_MZDM,GJGWCYRY_WFFZJLMS,"
        +"GJGWCYRY_LXDH,GJGWCYRY_DZMC) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.get("ID"),
                    item.get("TJR"),
                    item.get("TJDW"),
                    item.get("TJDWMC"),
                    item.get("XZQYDM"),
                    item.get("GJGWCYRY_DWMC"),
                    item.get("GJGWCYRY_QYBM"),
                    item.get("GJGWCYRY_GWMC"),
                    item.get("GJGWCYRY_GMSFZH"),
                    item.get("GJGWCYRY_XM"),
                    item.get("GJGWCYRY_MZDM"),
                    item.get("GJGWCYRY_WFFZJLMS"),
                    item.get("GJGWCYRY_LXDH"),
                    item.get("GJGWCYRY_DZMC")
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Map<String, Object> item) {
        String sql = "update DTGJ_TYSJ_GJGWCYRY t set XZQYDM=?,GJGWCYRY_DWMC=?,GJGWCYRY_QYBM=?,GJGWCYRY_GWMC=?,"
        +"GJGWCYRY_GMSFZH=?,GJGWCYRY_XM=?,GJGWCYRY_MZDM=?,GJGWCYRY_WFFZJLMS=?,GJGWCYRY_LXDH=?,GJGWCYRY_DZMC=? where ID=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.get("XZQYDM"),
                    item.get("GJGWCYRY_DWMC"),
                    item.get("GJGWCYRY_QYBM"),
                    item.get("GJGWCYRY_GWMC"),
                    item.get("GJGWCYRY_GMSFZH"),
                    item.get("GJGWCYRY_XM"),
                    item.get("GJGWCYRY_MZDM"),
                    item.get("GJGWCYRY_WFFZJLMS"),
                    item.get("GJGWCYRY_LXDH"),
                    item.get("GJGWCYRY_DZMC"),
                    item.get("ID")
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String id) {
        id = id.contains(",") ? id.substring(0, id.length() - 1) : id;
        String sql = "delete from DTGJ_TYSJ_GJGWCYRY t  where ID in ('" + id.replace(",", "','") + "')";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] { }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}