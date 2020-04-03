package com.winton.ytpaas.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.system.dao.Sys_JGDao;
import com.winton.ytpaas.system.dao.Sys_RoleDao;
import com.winton.ytpaas.system.dao.Sys_UserDao;
import com.winton.ytpaas.system.entity.Sys_JG;
import com.winton.ytpaas.system.entity.Sys_Role;
import com.winton.ytpaas.system.entity.Sys_Role_Data;
import com.winton.ytpaas.system.entity.Sys_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysRoleService")
public class Sys_RoleService {

    @Autowired
    Sys_RoleDao dao;
    @Autowired
    Sys_UserDao userDao;
    @Autowired
    Sys_JGDao jgDao;

    public Sys_Role getById(String jsbm) {
        return dao.getById(jsbm);
    }

    public List<Sys_Role> getAll() {
        return dao.getAll();
    }

    public Result add(Sys_Role item) {
        Result res = new Result();
        Sys_Role temp = dao.getByName(item.getJSMC());

        if(temp != null) {
            res.setCode("0");
            res.setMsg(item.getJSMC() + "已存在，不能重复添加");
        } else {
            boolean b = dao.add(item);
            if(b) {
                res.setCode("1");
                res.setMsg(item.getJSMC() + "添加成功");
            } else {
                res.setCode("-1");
                res.setMsg(item.getJSMC() + "添加失败");
            }
        }
       
        return res;
    }

    public Result update(Sys_Role item) {
        Result res = new Result();
        boolean b = dao.update(item);
        if(b) {
            res.setCode("1");
            res.setMsg(item.getJSMC() + "修改成功");
        } else {
            res.setCode("-1");
            res.setMsg(item.getJSMC() + "修改失败");
        }
       
        return res;
    }
    
    public Result delete(String jsbm) {
        Result res = new Result();
        boolean b = dao.delete(jsbm);
        if(b) {
            res.setCode("1");
            res.setMsg("删除成功");
        } else {
            res.setCode("-1");
            res.setMsg("删除失败");
        }
       
        return res;
    }
    
    public Result saveGNQX(String jsbm, String menus) {
        Result res = new Result();
        boolean b = dao.saveGNQX(jsbm, menus.split(","));
        if(b) {
            res.setCode("1");
            res.setMsg("设置成功");
        } else {
            res.setCode("-1");
            res.setMsg("设置失败");
        }
       
        return res;
    }
    public Result saveSJQX(String jsbm, String cdurl, String type, String roles) {
    	// type 1 设置全部，2 设置具体
        Result res = new Result();
        boolean b = false;
        
        if(type.equals("2")) {
        	List<Sys_Role_Data> list = JSON.parseArray(roles, Sys_Role_Data.class);
            b = dao.saveSJQX(jsbm, cdurl, list);
        } else {
        	b = dao.saveDefaultSJQX(jsbm);
        }
        
        if(b) {
            res.setCode("1");
            res.setMsg("设置成功");
        } else {
            res.setCode("-1");
            res.setMsg("设置失败");
        }
       
        return res;
    }
    
    public Result getSJQX(Sys_User user, String url) {
        Result res = new Result();
        
        JSONObject json = new JSONObject();

        // 数据权限
        List<Sys_Role_Data> list = dao.getSJQX(user.getJSBM(), url);
        json.put("sjqx", list);

        // 获取用户的数据权限单位，如果没有，则默认本单位
        List<Map<String, Object>> listDwbm = userDao.getUserDataDwbm(user.getYHBM());
        List<String> dwbms = new ArrayList<String>();
        for(Map<String, Object> item : listDwbm) {
            dwbms.add(item.get("DWBM").toString());
        }
        if(dwbms.size() > 0) {
            String dwbm = String.join(",", dwbms);
            json.put("dwbm", dwbm);
        } else {
            json.put("dwbm", user.getDWDM());
        }

        // 获取用户的用户编码
        json.put("yhbm", user.getYHBM());

    	res.setCode("1");
        res.setData(json);
        return res;
    }
    /**
     * 根据用户编码获取权限单位
     */
    public Result getQXDW(String yhbm) {
        Result res = new Result();
        
        Sys_User user = userDao.getById(yhbm);
        JSONObject json = new JSONObject();

        // 获取用户的数据权限单位，如果没有，则默认本单位
        List<Map<String, Object>> listDwbm = userDao.getUserDataDwbm(user.getYHBM());
        List<String> dwbms = new ArrayList<String>();
        for(Map<String, Object> item : listDwbm) {
            dwbms.add(item.get("DWBM").toString());
        }
        if(dwbms.size() > 0) {
            String dwbm = String.join(",", dwbms);
            json.put("dwbm", dwbm);
        } else {
            json.put("dwbm", user.getDWDM());
        }
        // 根据机构编码获取机构名称
        List<Sys_JG> listDwmc = jgDao.getAll03(json.get("dwbm").toString());
        List<String> dwmcs = new ArrayList<String>();
        for(Sys_JG item : listDwmc) {
            dwmcs.add(item.getJGMC());
        }
        String dwmc = String.join(",", dwmcs);
        json.put("dwmc", dwmc);

    	res.setCode("1");
        res.setData(json);
        return res;
    }
    /**
     * 获取下级所有角色
     * @param jsbm
     * @return
     */
    public Result getXJJS(String jsbm) {
    	Result res = new Result();
    	List<Sys_Role> list = dao.getXJJS(jsbm);
    	res.setCode("1");
        res.setData(list);
        return res;
    }
}