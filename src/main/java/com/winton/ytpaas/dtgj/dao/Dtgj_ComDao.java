package com.winton.ytpaas.dtgj.dao;

import java.util.List;
import java.util.Map;
import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_ComDao extends BaseJdbcTemplate {

    public List<Map<String, Object>> getJg(String did) {
        List<Map<String, Object>> tmpRet = null;
        String tmpWhere = " where 1=1 ";
        if (did != null && did.length() > 0) {
            tmpWhere += " and (t.JGDM='" + did + "' or t.SJDM='" + did + "') ";
        }
        String tmpColumn = " distinct t.XZQH,t.JGDM,t.JGMC,t.SJDM,t.JGJB,t.JGLBDM,t.JGLBMC  ";
        String tmpSql = " select " + tmpColumn + " from SYS_JG t " + tmpWhere + " order by JGDM ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public List<Map<String, Object>> getJg() {
        List<Map<String, Object>> tmpRet = null;
        String tmpWhere = " where 1=1 and t.SJDM='230000000000'";
        String tmpColumn = " distinct t.XZQH,t.JGDM,t.JGMC,t.SJDM,t.JGJB,t.JGLBDM,t.JGLBMC  ";
        String tmpSql = " select " + tmpColumn + " from SYS_JG t " + tmpWhere + " order by JGDM ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public List<Map<String, Object>> getJgByPid(String pid) {
        List<Map<String, Object>> tmpRet = null;
        String tmpWhere = " where 1=1 ";
        if (pid != null && pid.length() > 0) {
            tmpWhere += " and t.SJDM='" + pid + "' ";
        }
        String tmpColumn = " distinct t.XZQH,t.JGDM,t.JGMC,t.SJDM,t.JGJB,t.JGLBDM,t.JGLBMC  ";
        String tmpSql = " select " + tmpColumn + " from SYS_JG t " + tmpWhere + " order by JGDM ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public List<Map<String, Object>> bindCon(String tid) {
        List<Map<String, Object>> tmpRet = null;
        String tmpWhere = " where 1=1 ";
        if (tid != null && tid.length() > 0) {
            tmpWhere += " and t.CSLX='" + tid + "' ";
        }
        String tmpColumn = " distinct t.BM,t.MC  ";
        String tmpSql = " select " + tmpColumn + " from TEMP_SYS_XTCS t " + tmpWhere + " order by BM ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public String getCsmcByBm(String tid, String bm) {
        String tmpRet = "";
        String tmpWhere = " where 1=1 ";
        tmpWhere += " and t.CSLX='" + tid + "' and t.BM='" + bm + "' ";

        String tmpColumn = " distinct t.MC  ";
        String tmpSql = " select " + tmpColumn + " from SYS_XTCS t " + tmpWhere + " order by BM ";
        try {
            List<Map<String, Object>> tmpDatas = jdbcTemplate.queryForList(tmpSql);
            if (tmpDatas.size() > 0) {
                tmpRet = tmpDatas.get(0).get("MC").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public String getJgMcByJgdm(String jgdm) {
        String tmpRet = "";
        String tmpWhere = " where 1=1 ";
        tmpWhere += " and t.JGDM='" + jgdm + "' ";

        String tmpColumn = " distinct t.JGMC ";
        String tmpSql = " select " + tmpColumn + " from SYS_JG t " + tmpWhere + " order by JGDM ";
        try {
            List<Map<String, Object>> tmpDatas = jdbcTemplate.queryForList(tmpSql);
            if (tmpDatas.size() > 0) {
                tmpRet = tmpDatas.get(0).get("JGMC").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

}