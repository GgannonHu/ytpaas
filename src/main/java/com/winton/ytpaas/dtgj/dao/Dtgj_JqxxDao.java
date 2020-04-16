package com.winton.ytpaas.dtgj.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.winton.ytpaas.common.datasource.BaseJdbcTemplate;
import com.winton.ytpaas.dtgj.entity.Dtgj_Jqxx;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class Dtgj_JqxxDao extends BaseJdbcTemplate {

    public Dtgj_Jqxx getById(String id) {
        
        String sql = "select t.GAJGJGDM,t.DTZDDM,t.DTZDMC,t.GJXLDM,t.JJBH,t.BJFSDM,"+
        "t.JQLBDM,t.BJR_XM,t.BJR_XBDM,t.BJR_LXDH,t.BJDH,t.BJSJ,t.JYJQ,t.TJR,t.TJDW,t.TJDWMC from "+
        "Dtgj_Tysj_Jqxx t where t.ID=?";
        try {
            Dtgj_Jqxx model = jdbcTemplate.queryForObject(
                sql, 
                new Object[] { id }, 
                new BeanPropertyRowMapper<>(Dtgj_Jqxx.class)
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
    public List<Dtgj_Jqxx> getAll() {
        List<Dtgj_Jqxx> list = new ArrayList<Dtgj_Jqxx>();
        String sql = "select  t.GAJGJGDM,t.DTZDDM,t.DTZDMC,t.GJXLDM,t.JJBH,t.BJFSDM,"+
        "t.JQLBDM,t.BJR_XM,t.BJR_XBDM,t.BJR_LXDH,t.BJDH,t.BJSJ,t.JYJQ  from Dtgj_Tysj_Jqxx t";
        try {
            list = jdbcTemplate.query(
                sql, 
                new BeanPropertyRowMapper<>(Dtgj_Jqxx.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Map<String, Object>> getList(String VarGAJGJGDM,String VarDTZDMC,String VarBJR_XM,String VarBJSJS,String VarBJSJE,String VarBJDH,String varEndCon,String varBegCon) {
        List<Map<String, Object>> tmpRet = null;

        String tmpWhere = " where 1=1 "; 
        if (VarDTZDMC != null && VarDTZDMC.length() > 0) {
            tmpWhere += " and DTZDMC like '%" + VarDTZDMC + "%' ";
        }
        if (VarBJR_XM != null && VarBJR_XM.length() > 0) {
            tmpWhere += " and BJR_XM like '%" + VarBJR_XM + "%' ";
        } 
        if (VarBJSJS != null && VarBJSJS.length() > 0) {
            tmpWhere += " and BJSJ>=to_date('" + VarBJSJS + "','yyyy-MM-dd') ";
        }
        if (VarBJSJE != null && VarBJSJE.length() > 0) {
            tmpWhere += " and BJSJ<=to_date('" + VarBJSJE + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }
        if (VarBJDH != null && VarBJDH.length() > 0) {
            tmpWhere += " and BJDH like '%" + VarBJDH + "%' ";
        }  
         
        String tmpColumn = "ID,GAJGJGDM,DTZDDM,DTZDMC,GJXLDM,JJBH,BJFSDM,JQLBDM,BJR_XM,BJR_XBDM,BJR_LXDH,BJDH,BJSJ,JYJQ,TJR,TJDW,TJDWMC";
        String tmpSql = " select " + tmpColumn + " from DTGJ_TYSJ_JQXX " + tmpWhere + " order by BJSJ desc ";
        String tmpSqlFy = " select " + tmpColumn + " from( select ROWNUM RN , inTab.* from ( " + tmpSql
                + " ) inTab where ROWNUM<= " + varEndCon + " ) Tab where RN>= " + varBegCon + " ";
        try {
            tmpRet = jdbcTemplate.queryForList(tmpSqlFy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public int getCon(String VarGAJGJGDM,String VarDTZDMC,String VarBJR_XM,String VarBJSJS,String VarBJSJE,String VarBJDH) {
        int tmpRet = 0;
        String tmpWhere = " where 1=1 "; 
        if (VarDTZDMC != null && VarDTZDMC.length() > 0) {
            tmpWhere += " and DTZDMC like '%" + VarDTZDMC + "%' ";
        }
        if (VarBJR_XM != null && VarBJR_XM.length() > 0) {
            tmpWhere += " and BJR_XM like '%" + VarBJR_XM + "%' ";
        } 
        if (VarBJSJS != null && VarBJSJS.length() > 0) {
            tmpWhere += " and BJSJS>=to_date('" + VarBJSJS + "','yyyy-MM-dd') ";
        }
        if (VarBJSJE != null && VarBJSJE.length() > 0) {
            tmpWhere += " and BJSJS<=to_date('" + VarBJSJE + " 23:59:59','yyyy-MM-dd hh24:mi:ss') ";
        }
        if (VarBJDH != null && VarBJDH.length() > 0) {
            tmpWhere += " and BJDH like '%" + VarBJDH + "%' ";
        }  
        String tmpSql = " select COUNT(1) con from DTGJ_TYSJ_JQXX " + tmpWhere;
        try {
            tmpRet = jdbcTemplate.queryForObject(tmpSql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpRet;
    }

    public boolean add(Dtgj_Jqxx item) {
        String sql = "insert into DTGJ_TYSJ_JQXX(GAJGJGDM,DTZDDM,DTZDMC,GJXLDM,JJBH,BJFSDM,JQLBDM,BJR_XM,BJR_XBDM,BJR_LXDH,BJDH,BJSJ,JYJQ,TJR,TJDW,TJDWMC) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] { 
                    item.getGAJGJGDM(), // 公安机关机构代码 
                    item.getDTZDDM(), // 地铁站点代码 
                    item.getDTZDMC(), // 地铁站点名称 
                    item.getGJXLDM(), // 公交线路代码 
                    item.getJJBH(), // 接警编号 
                    item.getBJFSDM(), // 报警方式代码 
                    item.getJQLBDM(), // 警情类别代码 
                    item.getBJR_XM(), // 姓名 
                    item.getBJR_XBDM(), // 性别代码 
                    item.getBJR_LXDH(), // 联系电话 
                    item.getBJDH(), // 报警电话 
                    item.getBJSJ(), // 报警时间 
                    item.getJYJQ(), // 简要警情  
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

    public boolean update(Dtgj_Jqxx item) {
        String sql = "update Dtgj_Tysj_Jqxx set GAJGJGDM=?,DTZDDM=?,DTZDMC=?,GJXLDM=?,JJBH=?,BJFSDM=?"+
        ",JQLBDM=?,BJR_XM=?,BJR_XBDM=?,BJR_LXDH=?,BJDH=?,BJSJ=?,JYJQ=?,TJR=?,TJDW=?,TJDWMC=?  where id=?";
        try {
            int count = jdbcTemplate.update(
                sql, 
                new Object[] {
                    item.getGAJGJGDM(), // 公安机关机构代码 
                    item.getDTZDDM(), // 地铁站点代码 
                    item.getDTZDMC(), // 地铁站点名称 
                    item.getGJXLDM(), // 公交线路代码 
                    item.getJJBH(), // 接警编号 
                    item.getBJFSDM(), // 报警方式代码 
                    item.getJQLBDM(), // 警情类别代码 
                    item.getBJR_XM(), // 姓名 
                    item.getBJR_XBDM(), // 性别代码 
                    item.getBJR_LXDH(), // 联系电话 
                    item.getBJDH(), // 报警电话 
                    item.getBJSJ(), // 报警时间 
                    item.getJYJQ(), // 简要警情 
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

    
    public boolean delete(String id) {
        id = id.contains(",") ? id.substring(0, id.length() - 1) : id;
        String sql = "delete from Dtgj_Tysj_Jqxx  where  id in ('" + id.replace(",", "','") + "')";
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