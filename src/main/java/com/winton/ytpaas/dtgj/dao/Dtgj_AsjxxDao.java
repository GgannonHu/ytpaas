package com.winton.ytpaas.dtgj.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.dtgj.entity.Dtgj_Asjxx;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_AsjxxDao extends BaseJdbcTemplate {

    public Dtgj_Asjxx getById(String id) {
        String sql = "select t.ID, "+
        "t.GAJGJGDM, "+
        "t.DTZDBM,   "+
        "t.DTZDMC,   "+
        "t.GJXLBM,   "+
        "t.GJGSMC,   "+
        "t.AJBH,     "+
        "t.AJLB,     "+
        "t.AJMC,     "+
        "t.JYAQ,     "+
        "t.ASJFSKSSJ,"+
        "t.AFDD,     "+
        "t.SFCS,     "+ 
        "t.SSXQ  from Dtgj_Tysj_Asjxx t where t.ID=?";
        try {
            Dtgj_Asjxx model = jdbcTemplate.queryForObject(
                sql, 
                new Object[] { id }, 
                new BeanPropertyRowMapper<>(Dtgj_Asjxx.class)
            );
            return model;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public List<Dtgj_Asjxx> getAll() {
        List<Dtgj_Asjxx> list = new ArrayList<Dtgj_Asjxx>();
        String sql = "select  t.ID, "+
        "t.GAJGJGDM, "+
        "t.DTZDBM,   "+
        "t.DTZDMC,   "+
        "t.GJXLBM,   "+
        "t.GJGSMC,   "+
        "t.AJBH,     "+
        "t.AJLB,     "+
        "t.AJMC,     "+
        "t.JYAQ,     "+
        "t.ASJFSKSSJ,"+
        "t.AFDD,     "+
        "t.SFCS,     "+ 
        "t.SSXQ  from dtgj_qt_yswp t";
        try {
            list = jdbcTemplate.query(
                sql, 
                new BeanPropertyRowMapper<>(Dtgj_Asjxx.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Map<String, Object>> getList(String VarGAJGJGDM,String VarDTZDMC,String VarAJMC,String VarASJFSKSSJS,String VarASJFSKSSJE,String VarAFDD,String VarSSXQ,String varEndCon,String varBegCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 "; 
        if (VarGAJGJGDM != null && VarGAJGJGDM.length() > 0) {
            tmpWhere += " and GAJGJGDM like '" + VarGAJGJGDM + "%' ";
        }
        if (VarDTZDMC != null && VarDTZDMC.length() > 0) {
            tmpWhere += " and DTZDMC like '%" + VarDTZDMC + "%' ";
        }
        if (VarAJMC != null && VarAJMC.length() > 0) {
            tmpWhere += " and AJMC like '%" + VarAJMC + "%' ";
        } 
        if (VarASJFSKSSJS != null && VarASJFSKSSJS.length() > 0) {
            tmpWhere += " and ASJFSKSSJ>=to_date('" + VarASJFSKSSJS + "','yyyy-MM-dd') ";
        }
        if (VarASJFSKSSJE != null && VarASJFSKSSJE.length() > 0) {
            tmpWhere += " and ASJFSKSSJ<=to_date('" + VarASJFSKSSJE + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }
        if (VarAFDD != null && VarAFDD.length() > 0) {
            tmpWhere += " and AFDD like '%" + VarAFDD + "%' ";
        } 
        if (VarSSXQ != null && VarSSXQ.length() > 0) {
            tmpWhere += " and SSXQ like '%" + VarSSXQ + "%' ";
        } 
         
        String tmpColumn = "ID,GAJGJGDM,DTZDBM,DTZDMC,GJXLBM,GJGSMC,AJBH,AJLB,AJMC,JYAQ,ASJFSKSSJ,AFDD,SFCS,SSXQ,TJR,TJDW,TJDWMC";
        String tmpSql = " select " + tmpColumn + " from Dtgj_Tysj_Asjxx " + tmpWhere + " order by ASJFSKSSJ desc ";
        String tmpSqlFy = " select " + tmpColumn + " from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getCon(String VarGAJGJGDM,String VarDTZDMC,String VarAJMC,String VarASJFSKSSJS,String VarASJFSKSSJE,String VarAFDD,String VarSSXQ) {
        int tmpRet = 0;
        String tmpWhere = " where 1=1 "; 
        if (VarGAJGJGDM != null && VarGAJGJGDM.length() > 0) {
            tmpWhere += " and GAJGJGDM like '" + VarGAJGJGDM + "%' ";
        }
        if (VarDTZDMC != null && VarDTZDMC.length() > 0) {
            tmpWhere += " and DTZDMC like '%" + VarDTZDMC + "%' ";
        }
        if (VarAJMC != null && VarAJMC.length() > 0) {
            tmpWhere += " and AJMC like '%" + VarAJMC + "%' ";
        } 
        if (VarASJFSKSSJS != null && VarASJFSKSSJS.length() > 0) {
            tmpWhere += " and ASJFSKSSJ>=to_date('" + VarASJFSKSSJS + "','yyyy-MM-dd') ";
        }
        if (VarASJFSKSSJE != null && VarASJFSKSSJE.length() > 0) {
            tmpWhere += " and ASJFSKSSJ<=to_date('" + VarASJFSKSSJE + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }
        if (VarAFDD != null && VarAFDD.length() > 0) {
            tmpWhere += " and AFDD like '%" + VarAFDD + "%' ";
        } 
        if (VarSSXQ != null && VarSSXQ.length() > 0) {
            tmpWhere += " and SSXQ like '%" + VarSSXQ + "%' ";
        } 
        String tmpSql = " select COUNT(1) con from Dtgj_Tysj_Asjxx " + tmpWhere;
        try {
            tmpRet = jdbcTemplate.queryForObject(tmpSql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public boolean add(Dtgj_Asjxx item) {
        String sql = "insert into Dtgj_Tysj_Asjxx(GAJGJGDM,DTZDBM,DTZDMC,GJXLBM,GJGSMC,AJBH,AJLB,AJMC,JYAQ,ASJFSKSSJ,AFDD,SFCS,SSXQ,TJR,TJDW,TJDWMC) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getGAJGJGDM(),
                    item.getDTZDBM(),
                    item.getDTZDMC(),
                    item.getGJXLBM(),
                    item.getGJGSMC(),
                    item.getAJBH(),
                    item.getAJLB(),
                    item.getAJMC(),
                    item.getJYAQ(),
                    item.getASJFSKSSJ(),
                    item.getAFDD(),
                    item.getSFCS(),
                    item.getSSXQ(),
                    item.getTJR(),
                    item.getTJDW(),
                    item.getTJDWMC()
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Dtgj_Asjxx item) {
        String sql = "update Dtgj_Tysj_Asjxx set  DTZDBM=?,DTZDMC=?,GJXLBM=?,GJGSMC=?,AJBH=?,AJLB=?,AJMC=?,JYAQ=?,ASJFSKSSJ=?,AFDD=?,SFCS=?,SSXQ=?,TJR=?,TJDW=?,TJDWMC=? where id=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] { 
                    item.getDTZDBM(),
                    item.getDTZDMC(),
                    item.getGJXLBM(),
                    item.getGJGSMC(),
                    item.getAJBH(),
                    item.getAJLB(),
                    item.getAJMC(),
                    item.getJYAQ(),
                    item.getASJFSKSSJ(),
                    item.getAFDD(),
                    item.getSFCS(),
                    item.getSSXQ(),
                    item.getTJR(),
                    item.getTJDW(),
                    item.getTJDWMC(),
                    item.getID()
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean rl(Dtgj_Asjxx item) {
        String sql = "update Dtgj_Tysj_Asjxx set GAJGJGDM=?,DTZDBM=?,DTZDMC=?,GJXLBM=?,GJGSMC=?,AJBH=?,AJLB=?,AJMC=?,JYAQ=?,ASJFSKSSJ=?,AFDD=?,SFCS=?,SSXQ=? where id=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getGAJGJGDM(),
                    item.getDTZDBM(),
                    item.getDTZDMC(),
                    item.getGJXLBM(),
                    item.getGJGSMC(),
                    item.getAJBH(),
                    item.getAJLB(),
                    item.getAJMC(),
                    item.getJYAQ(),
                    item.getASJFSKSSJ(),
                    item.getAFDD(),
                    item.getSFCS(),
                    item.getSSXQ(),
                    item.getID()
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
        String sql = "delete from Dtgj_Tysj_Asjxx  where  id in ('" + id.replace(",", "','") + "')";
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