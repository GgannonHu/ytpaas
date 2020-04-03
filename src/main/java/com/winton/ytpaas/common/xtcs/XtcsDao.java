package com.winton.ytpaas.common.xtcs;

import java.util.List;
import java.util.Map;
import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class XtcsDao extends BaseJdbcTemplate {

    public List<Map<String, Object>> bindCon(String tid) {
        List<Map<String, Object>> tmpRet = null;
        String tmpWhere = " where 1=1 ";
        if (tid != null && tid.length() > 0) {
            tmpWhere += " and t.CSLX='" + tid + "' ";
        }
        String tmpColumn = " distinct t.BM,t.MC  ";
        String tmpSql = " select " + tmpColumn + " from SYS_XTCS t " + tmpWhere + " order by BM ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

}