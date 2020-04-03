package com.winton.ytpaas.dtgj.service;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.dtgj.dao.Dtgj_ZnyaDao;
import com.winton.ytpaas.dtgj.entity.Dtgj_Znya;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("Dtgj_ZnyaService")
public class Dtgj_ZnyaService {
    @Autowired
    TokenService tokenService;
    @Autowired
    Dtgj_ZnyaDao dao;

    public Result getById(String id) {
        Result res = new Result();
        res.setCode("1");
        res.setData(dao.getById(id));
        return res;
    }

    public JSONObject getList(String varName, String dj, String fbsjBegin, String fbsjEnd, int varPage, int varLimit,
            int varIsCon) {
        JSONObject tmpRet = new JSONObject();

        String tmpBegCon = String.valueOf((varPage - 1) * varLimit + 1);
        String tmpEndCon = String.valueOf(varPage * varLimit);
        tmpRet.put("data", dao.getList(varName, dj, fbsjBegin, fbsjEnd, tmpBegCon, tmpEndCon));
        if (varIsCon == 1) {
            tmpRet.put("count", dao.getListCount(varName, dj, fbsjBegin, fbsjEnd));
        }
        tmpRet.put("code", "1");

        return tmpRet;
    }

    public JSONObject getListCount(String varName, String dj, String fbsjBegin, String fbsjEnd) {
        JSONObject tmpRet = new JSONObject();
        tmpRet.put("count", dao.getListCount(varName, dj, fbsjBegin, fbsjEnd));
        tmpRet.put("code", "1");

        return tmpRet;
    }

    public Result add(Dtgj_Znya item) {
        Result res = new Result();
        boolean b = dao.add(item);
        if (b) {
            res.setCode("1");
            res.setMsg("添加成功");
        } else {
            res.setCode("-1");
            res.setMsg("添加失败");
        }
        return res;
    }

    public Result update(Dtgj_Znya item) {
        Result res = new Result();
        boolean b = dao.update(item);
        if (b) {
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
        if (b) {
            res.setCode("1");
            res.setMsg("删除成功");
        } else {
            res.setCode("-1");
            res.setMsg("删除失败");
        }
        return res;
    }
}