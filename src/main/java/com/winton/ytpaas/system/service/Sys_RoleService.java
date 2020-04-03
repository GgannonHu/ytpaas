package com.winton.ytpaas.system.service;

import java.util.List;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.system.dao.Sys_RoleDao;
import com.winton.ytpaas.system.entity.Sys_Role;
import com.winton.ytpaas.system.entity.Sys_Role_Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysRoleService")
public class Sys_RoleService {

    @Autowired
    Sys_RoleDao dao;

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
    
    public Result getSJQX(String jsbm, String url) {
    	Result res = new Result();
    	List<Sys_Role_Data> list = dao.getSJQX(jsbm, url);
    	res.setCode("1");
        res.setData(list);
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