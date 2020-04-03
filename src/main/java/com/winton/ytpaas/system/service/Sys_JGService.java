package com.winton.ytpaas.system.service;

import java.util.ArrayList;
import java.util.List;

import com.winton.ytpaas.system.dao.Sys_JGDao;
import com.winton.ytpaas.system.entity.Sys_JG;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysJGService")
public class Sys_JGService {

    @Autowired
    Sys_JGDao dao;



    /**
     * 根据上级代码查询下级机构信息
     * @param sjdm
     * @return
     */
    public JSONObject getBySjdm(String sjdm) {
    	List<Sys_JG> list = dao.getBySjdm(sjdm);
    	JSONObject json = new JSONObject();
		json.put("code", 1);
		json.put("count", list.size());
		json.put("data", list);
        return json;
    }

    /**
     * 根据机构代码获取机构信息
     * @param jgdm
     * @return
     */
    public JSONObject getByJgdm(String jgdm) {
    	Sys_JG list = dao.getByJgdm(jgdm);
    	JSONObject json = new JSONObject();
		json.put("code", 1);
		json.put("count", 1);
		json.put("data", list);
        return json;
    }

    public String getByJgmc(String jgdms) {
        List<Sys_JG> listDwmc = dao.getAll03(jgdms);
        List<String> dwmcs = new ArrayList<String>();
        for(Sys_JG item : listDwmc) {
            dwmcs.add(item.getJGMC());
        }
        String dwmc = String.join(",", dwmcs);
        return dwmc;
    }


    /**
     * 查询本单位及下属所有单位
     * @param sjqx 数据权限：
     * CXQX01 查询全部
     * CXQX02 查询本单位及下属所有单位
     * CXQX03 查询本单位
     * @param jgdm 机构代码
     * @return
     */
    public JSONObject getAll(String sjqx, String jgdm) {
    	List<Sys_JG> list = new ArrayList<Sys_JG>();
    	if(sjqx.equals("CXQX01")) {
    		list = dao.getAll01();
    	} else if(sjqx.equals("CXQX02")) {
    		list = dao.getAll02(jgdm);
    	} else if(sjqx.equals("CXQX03")) {
    		list = dao.getAll03(jgdm);
    	}
    	JSONObject json = new JSONObject();
		json.put("code", 1);
		json.put("count", list.size());
		json.put("data", list);
        return json;
    }
    /**
     * 查询本单位及下属所有单位
     * @param sjqx 数据权限：
     * CXQX01 查询全部
     * CXQX02 查询本单位及下属所有单位
     * CXQX03 查询本单位
     * @param jgdm 机构代码
     * @return
     */
    public JSONObject getYJQX(String sjqx, String jgdm) {
    	List<Sys_JG> list = new ArrayList<Sys_JG>();
    	if(sjqx.equals("CXQX01")) {
    		jgdm = "230000000000"; // 查询全部，绑定黑龙江省公安厅
    	}
    	list = dao.getAll03(jgdm);
    	
    	JSONObject json = new JSONObject();
		json.put("code", 1);
		json.put("count", list.size());
		json.put("data", list);
        return json;
    }
}