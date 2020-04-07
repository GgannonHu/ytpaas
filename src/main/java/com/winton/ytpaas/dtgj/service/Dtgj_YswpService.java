package com.winton.ytpaas.dtgj.service;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.dtgj.dao.Dtgj_YswpDao;
import com.winton.ytpaas.dtgj.entity.Dtgj_Yswp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dtgjYswpService")
public class Dtgj_YswpService {
    @Autowired
    TokenService tokenService;
    @Autowired
    Dtgj_YswpDao dao;

    public Result getById(String id) {
        Result res = new Result();
        res.setCode("1");
        res.setData(dao.getById(id));
        return res;
    }

    public Result getAll() {
        Result res = new Result();
        res.setCode("1");
        res.setData(dao.getAll());
        return res;
    }
    
    public JSONObject getList(String varuser,String varZt,String varMc,String varMs,String varSqdd,String varSqsjS,String varSqsjE, int varPage, int varLimit, int varIsCon) {
        JSONObject tmpRet = new JSONObject();
        String tmpBegCon = String.valueOf((varPage - 1) * varLimit + 1);
        String tmpEndCon = String.valueOf(varPage * varLimit);
        tmpRet.put("data", dao.getList(varuser,varZt,varMc,varMs,varSqdd,varSqsjS,varSqsjE, tmpBegCon, tmpEndCon));
        if (varIsCon == 1) {
            tmpRet.put("count", dao.getCon(varZt,varMc,varMs,varSqdd,varSqsjS,varSqsjE));
        }
        tmpRet.put("code", "1");
        return tmpRet;
    }
    public JSONObject getListcon(String varZt,String varMc,String varMs,String varSqdd,String varSqsjS,String varSqsjE) {
        JSONObject tmpRet = new JSONObject();
        tmpRet.put("count", dao.getCon(varZt,varMc,varMs,varSqdd,varSqsjS,varSqsjE));
        tmpRet.put("code", "1");
        return tmpRet;
    }
    public Result add(Dtgj_Yswp item) {
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

    public Result update(Dtgj_Yswp item) {
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
    
    public Result rl(Dtgj_Yswp item) {
        Result res = new Result();
        boolean b = dao.rl(item);
        if(b) {
            res.setCode("1");
            res.setMsg("认领成功");
        } else {
            res.setCode("-1");
            res.setMsg("认领失败");
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