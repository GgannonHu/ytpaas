package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_CtczDao extends BaseJdbcTemplate {

    public Map<String, Object> getById(String id) {
        Map<String, Object> tmpRet = null;
        String sql = "select t.ID,t.NAME,t.TYPE,t.NR,t.FBR,t.FBDW,t.FBDWMC,t.FBSJ,t.QS,t.PID,t.XFDW,t.XFDWMC,t.FK, ";
        sql+=" wm_concat(distinct f.FKDWMC) FKDWMC,wm_concat(distinct f.FKDW) FKDW,wm_concat(distinct q.qsdwmc) QSDWMC,wm_concat(distinct q.qsdw) QSDW ";
        sql+=" from DTGJ_QT_CTCZ t left join DTGJ_QT_CTCZ_QS q on t.id=q.pid left join DTGJ_QT_CTCZ_FK f on t.id=f.pid where t.ID=? ";
        sql+=" group by t.ID,t.NAME,t.TYPE,t.NR,t.FBR,t.FBDW,t.FBSJ,t.QS,t.PID,t.XFDW,t.FBDWMC,t.XFDWMC,t.FK ";
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

    public List<Map<String, Object>> getQsById(String id) {
        List<Map<String, Object>> tmpRet = null;
        String sql = " select ID,QSR,QSSJ,PID,QSDW,QSDWMC ";
        sql+=" from DTGJ_QT_CTCZ_QS t where t.PID=? order by QSSJ ";
        try {
            tmpRet = jdbcTemplate.queryForList(
                sql, 
                new Object[] { id }
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public List<Map<String, Object>>  getFkById(String id, String dwdm) {
        List<Map<String, Object>> tmpRet = null;
        String sql = " select ID,FKR,FKSJ,PID,FKDW,FKNR,FKDWMC ";
        sql += " from DTGJ_QT_CTCZ_FK t where t.PID=? ";
        if (dwdm != null && !dwdm.equals("")) {
            sql += " and FKDW='" + dwdm + "' ";
        }
        sql += " order by FKSJ ";
        try {
            tmpRet = jdbcTemplate.queryForList(
                sql, 
                new Object[] { id }
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public List<Map<String, Object>> getList(String vardwdm,String varuser, String varmc, String varlx, String varqs, String varfk,
            String varfbsjS, String varfbsjE, String varBegCon, String varEndCon) {
        List<Map<String, Object>> tmpRet = null;
        String tmpWhere = " where 1=1 and (t.FBDW='" + vardwdm + "' or t.XFDW like '%" + vardwdm + "%') ";
        if (varmc != null && varmc.length() > 0) {
            tmpWhere += " and t.NAME ='" + varmc + "' ";
        }
        if (varlx != null && varlx.length() > 0) {
            tmpWhere += " AND t.TYPE=" + varlx + " ";
        }
        if (varqs != null && varqs.length() > 0) {
            if (varqs.equals("0"))
                tmpWhere += " AND (q.QSDW like '%" + vardwdm + "%' or (t.QS=0 and t.FBDW = '" + vardwdm + "')) ";
            else
                tmpWhere += " AND ((q.QSDW not like '%" + vardwdm + "%' and t.XFDW like '%" + vardwdm + "%') or t.QS=1) ";
        }
        if (varfk != null && varfk.length() > 0) {
            if (varfk.equals("1")) {
                tmpWhere += " AND (t.FK IS NULL or f.FKDW not like '%" + vardwdm + "%') ";
            } else {
                tmpWhere += " AND (f.FKDW like '%" + vardwdm + "%' or (t.FK IS NOT NULL and t.FBDW = '" + vardwdm + "')) ";
            }
        }
        if (varfbsjS != null && varfbsjS.length() > 0) {
            tmpWhere += " AND t.FBSJ>=to_date('" + varfbsjS + "','yyyy-MM-dd') ";
        }
        if (varfbsjE != null && varfbsjE.length() > 0) {
            tmpWhere += " AND t.FBSJ<=to_date('" + varfbsjE + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }
        String tmpColumn = " t.ID,t.NAME,t.TYPE,t.NR,t.FBR,t.FBDW,t.FBDWMC,t.FBSJ,t.QS,t.PID,t.XFDW,t.XFDWMC,t.FK,"
            + "wm_concat(distinct q.QSDWMC) QSDWMC,wm_concat(distinct q.QSDW) QSDW,"
            + "wm_concat(distinct f.FKDWMC) FKDWMC,wm_concat(distinct f.FKDW) FKDW,'" + vardwdm + "' DQDW,'" + varuser + "' DQYH ";
        String tmpSql = " select " + tmpColumn + " from DTGJ_QT_CTCZ t left join DTGJ_QT_CTCZ_QS q on t.id=q.pid left join DTGJ_QT_CTCZ_FK f on t.id=f.pid " 
            + tmpWhere + " group by t.ID,t.NAME,t.TYPE,t.NR,t.FBR,t.FBDW,t.FBSJ,t.QS,t.PID,t.XFDW,t.FBDWMC,t.XFDWMC,t.FK order by FBSJ desc ";
        String tmpSqlFy = " select * from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getCon(String vardwdm,String varuser, String varmc, String varlx, String varqs, String varfk, String varfbsjS,
            String varfbsjE) {
        int tmpRet = 0;
        String tmpWhere = " where 1=1 and (t.FBDW='" + vardwdm + "' or t.XFDW like '%" + vardwdm + "%') ";
        if (varmc != null && varmc.length() > 0) {
            tmpWhere += " and t.NAME ='" + varmc + "' ";
        }
        if (varlx != null && varlx.length() > 0) {
            tmpWhere += " AND t.TYPE=" + varlx + " ";
        }
        if (varqs != null && varqs.length() > 0) {
            if (varqs.equals("0"))
                tmpWhere += " AND (q.QSDW like '%" + vardwdm + "%' or (t.QS=0 and t.FBDW = '" + vardwdm + "')) ";
            else
                tmpWhere += " AND ((q.QSDW not like '%" + vardwdm + "%' and t.XFDW like '%" + vardwdm + "%') or t.QS=1) ";
        }
        if (varfk != null && varfk.length() > 0) {
            if (varfk.equals("1")) {
                tmpWhere += " AND (t.FK IS NULL or f.FKDW not like '%" + vardwdm + "%') ";
            } else {
                tmpWhere += " AND (f.FKDW like '%" + vardwdm + "%' or (t.FK IS NOT NULL and t.FBDW = '" + vardwdm + "')) ";
            }
        }
        if (varfbsjS != null && varfbsjS.length() > 0) {
            tmpWhere += " AND t.FBSJ>=to_date('" + varfbsjS + "','yyyy-MM-dd') ";
        }
        if (varfbsjE != null && varfbsjE.length() > 0) {
            tmpWhere += " AND t.FBSJ<=to_date('" + varfbsjE + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }
        String tmpSql = " select COUNT(distinct t.ID) CON from DTGJ_QT_CTCZ t left join DTGJ_QT_CTCZ_QS q on t.id=q.pid left join DTGJ_QT_CTCZ_FK f on t.id=f.pid " + tmpWhere;
        try {
            tmpRet =jdbcTemplate.queryForObject(tmpSql, Integer.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public boolean add(Map<String, Object> item) {
        String sql = "insert into DTGJ_QT_CTCZ(ID,NAME,NR,FBR,FBDW,FBDWMC,XFDW,XFDWMC,TYPE) values (?,?,?,?,?,?,?,?,?)";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.get("id"),
                    item.get("name"),
                    item.get("nr"),
                    item.get("fbr"),
                    item.get("fbdw"),
                    item.get("fbdwmc"),
                    item.get("xfdw"),
                    item.get("xfdwmc"),
                    item.get("type")
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Map<String, Object> item) {
        String sql = "update DTGJ_QT_CTCZ set NAME=?,NR=?,XFDW=?,XFDWMC=? where ID=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.get("name"),
                    item.get("nr"),
                    item.get("xfdw"),
                    item.get("xfdwmc"),
                    item.get("id")
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean qs(Map<String, Object> item) {
        try {
            String sql = "";
            int count = 0;
            sql = "update DTGJ_QT_CTCZ set QS=0,QSR=?,QSSJ=sysdate where ID=?";
            count = jdbcTemplate.update(sql, new Object[] { item.get("qsr"), item.get("id") });
            sql = "insert into DTGJ_QT_CTCZ_QS(QSR,QSSJ,QSDW,QSDWMC,PID) values (?,sysdate,?,?,?)";
            count = jdbcTemplate.update(sql,
                    new Object[] { item.get("qsr"), item.get("qsdw"), item.get("qsdwmc"), item.get("id") });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String id) {
        id = id.contains(",") ? id.substring(0, id.length() - 1) : id;
        String sql = "delete from DTGJ_QT_CTCZ  where ID in ('" + id.replace(",", "','") + "')";
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

    public boolean xf(Map<String, Object> item) {
        String sql = "insert into DTGJ_QT_CTCZ(ID,NAME,NR,XFDW,FBR,FBDW,FBDWMC,XFDWMC,TYPE,PID,FID) values (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.get("id"), 
                    item.get("name"),
                    item.get("nr"),
                    item.get("xfdw"),
                    item.get("fbr"),
                    item.get("fbdw"),
                    item.get("fbdwmc"),
                    item.get("xfdwmc"),
                    item.get("type"),
                    item.get("pid"),
                    item.get("fid")
                }
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean fk(Map<String, Object> item) {
        try {
            String sql = "";
            int count = 0;
            sql = "update DTGJ_QT_CTCZ set FK=?,FKR=?,FKSJ=sysdate where ID=?";
            count = jdbcTemplate.update(sql, new Object[] { item.get("fknr"), item.get("fkr"), item.get("id") });
            sql = "insert into DTGJ_QT_CTCZ_FK(FKR,FKSJ,FKDW,FKDWMC,FKNR,PID) values (?,sysdate,?,?,?,?)";
            count = jdbcTemplate.update(sql, new Object[] { item.get("fkr"), item.get("fkdw"), item.get("fkdwmc"),
                    item.get("fknr"), item.get("id") });
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}