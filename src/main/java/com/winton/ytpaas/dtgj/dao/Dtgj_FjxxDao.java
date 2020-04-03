package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;

import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_FjxxDao extends BaseJdbcTemplate {

    public List<Map<String, Object>> getListByPid(String varPid) {
        List<Map<String, Object>> tmpRet = null;

        String tmpSql = " select ID,PID,NAME,TYPE,FJDZ,FJDX,TJSJ from DTGJ_QT_FJXX where PID='" + varPid
                + "' order by TJSJ desc ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public boolean add(Map<String, Object> varItem) {
        String tmpSql = " insert into DTGJ_QT_FJXX(PID,NAME,TYPE,FJDZ,FJDX) values(?,?,?,?,?) ";
        try {
            int count = jdbcTemplate.update(tmpSql, new Object[] { varItem.get("pid"), varItem.get("name"),
                    varItem.get("type"), varItem.get("fjdz"), varItem.get("fjdx") });
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

        String tmpSql = " delete from DTGJ_QT_FJXX where id in ( " + tmpWhere + " ) ";
        try {
            int count = jdbcTemplate.update(tmpSql);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteByPid(String varIds) {

        String[] tmpIds = varIds.split(",");
        String tmpWhere = "";
        for (String id : tmpIds) {
            if (id.length() > 0) {
                tmpWhere += ",'" + id + "'";
            }
        }
        tmpWhere = tmpWhere.substring(1);

        String tmpSql = " delete from DTGJ_QT_FJXX where pid in ( " + tmpWhere + " ) ";
        try {
            int count = jdbcTemplate.update(tmpSql);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Map<String, Object>> getListByIds(String varIds) {
        List<Map<String, Object>> tmpRet = null;

        String[] tmpIds = varIds.split(",");
        String tmpWhere = "";
        for (String id : tmpIds) {
            if (id.length() > 0) {
                tmpWhere += ",'" + id + "'";
            }
        }
        tmpWhere = tmpWhere.substring(1);

        String tmpSql = " select ID,PID,NAME,TYPE,FJDZ,FJDX,TJSJ from DTGJ_QT_FJXX where ID in(" + tmpWhere
                + ") order by TJSJ desc ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public List<Map<String, Object>> getListByPids(String varPIds) {
        List<Map<String, Object>> tmpRet = null;

        String[] tmpIds = varPIds.split(",");
        String tmpWhere = "";
        for (String pid : tmpIds) {
            if (pid.length() > 0) {
                tmpWhere += ",'" + pid + "'";
            }
        }
        tmpWhere = tmpWhere.substring(1);

        String tmpSql = " select ID,PID,NAME,TYPE,FJDZ,FJDX,TJSJ from DTGJ_QT_FJXX where PID in(" + tmpWhere
                + ") order by TJSJ desc ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }
}
