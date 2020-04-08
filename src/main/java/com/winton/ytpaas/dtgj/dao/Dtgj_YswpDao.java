package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.dtgj.entity.Dtgj_Yswp;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_YswpDao extends BaseJdbcTemplate {

    public Dtgj_Yswp getById(String id) {
        String sql = "select t.ID,t.MC,t.MS,t.SQDD,t.ZT,t.TJR,t.TJDW,t.TJSJ,t.RLR,t.RLRSFZH,t.RLSJ,t.SQSJ from DTGJ_QT_YSWP t where t.ID=?";
        try {
            Dtgj_Yswp model = jdbcTemplate.queryForObject(
                sql, 
                new Object[] { id }, 
                new BeanPropertyRowMapper<>(Dtgj_Yswp.class)
            );
            return model;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> getList(String varuser,String varZt,String varMc,String varMs,String varSqdd,String varSqsjS,String varSqsjE, String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 ";
        if (varZt != null && varZt.length() > 0) {
            tmpWhere += " and ZT ='" + varZt + "' ";
        }
        if (varMc != null && varMc.length() > 0) {
            tmpWhere += " and MC like '%" + varMc + "%' ";
        }
        if (varMs != null && varMs.length() > 0) {
            tmpWhere += " and MS like '%" + varMs + "%' ";
        }
        if (varSqdd != null && varSqdd.length() > 0) {
            tmpWhere += " and SQDD like '%" + varSqdd + "%' ";
        }
        if (varSqsjS != null && varSqsjS.length() > 0) {
            tmpWhere += " and SQSJ>=to_date('" + varSqsjS + "','yyyy-MM-dd') ";
        }
        if (varSqsjE != null && varSqsjE.length() > 0) {
            tmpWhere += " and SQSJ<=to_date('" + varSqsjE + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }
        String tmpColumn = " '"+varuser+"' DQYH,ID,MC,MS,SQDD,ZT,TJR,TJDW,TJSJ,RLR,RLRSFZH,RLSJ,SQSJ ";
        String tmpSql = " select " + tmpColumn + " from DTGJ_QT_YSWP " + tmpWhere + " order by ZT desc,TJSJ desc ";
        String tmpSqlFy = " select " + tmpColumn + " from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getCon(String varZt,String varMc,String varMs,String varSqdd,String varSqsjS,String varSqsjE) {
        int tmpRet = 0;
        String tmpWhere = " where 1=1 ";
        if (varZt != null && varZt.length() > 0) {
            tmpWhere += " and ZT ='" + varZt + "' ";
        }
        if (varMc != null && varMc.length() > 0) {
            tmpWhere += " and MC like '%" + varMc + "%' ";
        }
        if (varMs != null && varMs.length() > 0) {
            tmpWhere += " and MS like '%" + varMs + "%' ";
        }
        if (varSqdd != null && varSqdd.length() > 0) {
            tmpWhere += " and SQDD like '%" + varSqdd + "%' ";
        }
        if (varSqsjS != null && varSqsjS.length() > 0) {
            tmpWhere += " and SQSJ>=to_date('" + varSqsjS + "','yyyy-MM-dd') ";
        }
        if (varSqsjE != null && varSqsjE.length() > 0) {
            tmpWhere += " and SQSJ<=to_date('" + varSqsjE + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }
        String tmpSql = " select COUNT(1) con from DTGJ_QT_YSWP " + tmpWhere;
        try {
            tmpRet = jdbcTemplate.queryForObject(tmpSql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public boolean add(Dtgj_Yswp item) {
        String sql = "insert into DTGJ_QT_YSWP(ID,MC,MS,SQDD,SQSJ,ZT,TJR,TJDW,TJDWMC) values (?,?,?,?,?,?,?,?,?)";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getID(),
                    item.getMC(),
                    item.getMS(),
                    item.getSQDD(),
                    item.getSQSJ(),
                    item.getZT(),
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

    public boolean update(Dtgj_Yswp item) {
        String sql = "update dtgj_qt_yswp set MC=?,MS=?,SQDD=?,SQSJ=? where ID=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getMC(),
                    item.getMS(),
                    item.getSQDD(),
                    item.getSQSJ(),
                    item.getID()
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean rl(Dtgj_Yswp item) {
        String sql = "update DTGJ_QT_YSWP set RLR=?,RLRSFZH=?,ZT=?,RLSJ=sysdate where ID=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getRLR(),
                    item.getRLRSFZH(),
                    item.getZT(),
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
        String sql = "delete from DTGJ_QT_YSWP  where ZT not in ('已认领') and ID in ('" + id.replace(",", "','") + "')";
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