package com.winton.ytpaas.dtgj.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.dtgj.dao.Dtgj_GjgwcyryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dtgjGjgwcyryService")
public class Dtgj_GjgwcyryService {
    @Autowired
    TokenService tokenService;
    @Autowired
    Dtgj_GjgwcyryDao dao;

    public Result getById(String id) {
        Result res = new Result();
        Map<String, Object> tmpItem = dao.getById(id);
        if (tmpItem != null) {
            res.setCode("1");
            res.setData(tmpItem);
        } else { 
            res.setCode("-1");
        }
        return res;
    }

    public JSONObject getList(String varuser,String varname,String varsfzh,String vardwmc, int varPage, int varLimit, int varIsCon) {
        JSONObject tmpRet = new JSONObject();
        String tmpBegCon = String.valueOf((varPage - 1) * varLimit + 1);
        String tmpEndCon = String.valueOf(varPage * varLimit);
        tmpRet.put("data", dao.getList(varuser,varname,varsfzh,vardwmc, tmpBegCon, tmpEndCon));
        if (varIsCon == 1) {
            tmpRet.put("count", dao.getCon(varname,varsfzh,vardwmc));
        }
        tmpRet.put("code", "1");
        return tmpRet;
    }
    
    public JSONObject getListcon(String varname,String varsfzh,String vardwmc) {
        JSONObject tmpRet = new JSONObject();
        tmpRet.put("count", dao.getCon(varname,varsfzh,vardwmc));
        tmpRet.put("code", "1");
        return tmpRet;
    }
    
    public Result add(Map<String, Object> item) {
        Result res = new Result();
        boolean b = dao.add(item);
        if(b) {
            res.setCode("1");
            res.setMsg("添加成功");
        } else {
            res.setCode("-1");
            res.setMsg("添加失败");
        }
        return res;
    }

    public Result update(Map<String, Object> item) {
        Result res = new Result();
        boolean b = dao.update(item);
        if(b) {
            res.setCode("1");
            res.setMsg("修改成功");
        } else {
            res.setCode("-1");
            res.setMsg("修改失败");
        }
        return res;
    }
    
    public Result delete(String id) {
        Result res = new Result();
        boolean b = dao.delete(id);
        if(b) {
            res.setCode("1");
            res.setMsg("删除成功");
        } else {
            res.setCode("-1");
            res.setMsg("删除失败");
        }
       
        return res;
    }

}