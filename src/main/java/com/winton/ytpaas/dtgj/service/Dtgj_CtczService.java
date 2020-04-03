package com.winton.ytpaas.dtgj.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.dtgj.dao.Dtgj_CtczDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dtgjCtczService")
public class Dtgj_CtczService {
    @Autowired
    TokenService tokenService;
    @Autowired
    Dtgj_CtczDao dao;

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

    public JSONObject getQsById(String id) {
        JSONObject tmpRet = new JSONObject();
        tmpRet.put("data", dao.getQsById(id));
        tmpRet.put("code", "1");
        return tmpRet;
    }
    public JSONObject getFkById(String id,String dwdm) {
        JSONObject tmpRet = new JSONObject();
        tmpRet.put("data", dao.getFkById(id,dwdm));
        tmpRet.put("code", "1");
        return tmpRet;
    }


    public JSONObject getList(String vardwdm,String varuser, String varmc, String varnr, String varlx, String varqs, String varfk, String varfbsjS, String varfbsjE, int varPage, int varLimit, int varIsCon) {
        JSONObject tmpRet = new JSONObject();
        String tmpBegCon = String.valueOf((varPage - 1) * varLimit + 1);
        String tmpEndCon = String.valueOf(varPage * varLimit);
        tmpRet.put("data", dao.getList(vardwdm,varuser,varmc, varnr, varlx, varqs, varfk, varfbsjS, varfbsjE, tmpBegCon, tmpEndCon));
        if (varIsCon == 1) {
            tmpRet.put("count", dao.getCon(vardwdm,varuser,varmc, varnr, varlx, varqs, varfk, varfbsjS, varfbsjE));
        }
        tmpRet.put("code", "1");
        return tmpRet;
    }
    
    public JSONObject getListcon(String vardwdm,String varuser, String varmc, String varnr, String varlx, String varqs, String varfk, String varfbsjS, String varfbsjE) {
        JSONObject tmpRet = new JSONObject();
        tmpRet.put("count", dao.getCon(vardwdm,varuser,varmc, varnr, varlx, varqs, varfk, varfbsjS, varfbsjE));
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

    public Result xf(Map<String, Object> item) {
        Result res = new Result();
        boolean b = dao.xf(item);
        if(b) {
            res.setCode("1");
            res.setMsg("下发成功");
        } else {
            res.setCode("-1");
            res.setMsg("下发失败");
        }
        return res;
    }

    public Result fk(Map<String, Object> item) {
        Result res = new Result();
        boolean b = dao.fk(item);
        if(b) {
            res.setCode("1");
            res.setMsg("反馈成功");
        } else {
            res.setCode("-1");
            res.setMsg("反馈失败");
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
    
    public Result qs(Map<String, Object> item) {
        Result res = new Result();
        boolean b = dao.qs(item);
        if(b) {
            res.setCode("1");
            res.setMsg("签收成功");
        } else {
            res.setCode("-1");
            res.setMsg("签收失败");
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