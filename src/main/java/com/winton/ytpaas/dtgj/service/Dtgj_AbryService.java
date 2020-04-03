package com.winton.ytpaas.dtgj.service;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.dtgj.dao.Dtgj_AbryDao;
import com.winton.ytpaas.dtgj.entity.Dtgj_Abry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dtgjAbryService")
public class Dtgj_AbryService {
    @Autowired
    TokenService tokenService;
    @Autowired
    Dtgj_AbryDao dao;

    public Result getById(String id) {
        Result res = new Result();
        res.setCode("1");
        res.setData(dao.getById(id));
        return res;
    }

    public JSONObject getList(String dtzdmc, String gjxlbm, String gmsfhm, String xm, String dw, int varPage,
            int varLimit, int varIsCon,String gajgjgdm) {
        JSONObject tmpRet = new JSONObject();

        String tmpBegCon = String.valueOf((varPage - 1) * varLimit + 1);
        String tmpEndCon = String.valueOf(varPage * varLimit);
        tmpRet.put("data", dao.getList(dtzdmc, gjxlbm, gmsfhm, xm, dw, tmpBegCon, tmpEndCon,gajgjgdm));
        if (varIsCon == 1) {
            tmpRet.put("count", dao.getListCount(dtzdmc, gjxlbm, gmsfhm, xm, dw,gajgjgdm));
        }
        tmpRet.put("code", "1");

        return tmpRet;
    }

    public JSONObject getXlxx(String xzqh) {
        JSONObject tmpRet = new JSONObject();
        tmpRet.put("data", dao.getXlxx(xzqh));
        tmpRet.put("code", "1");
        return tmpRet;
    }

    public JSONObject getZdxx(String bm) {
        JSONObject tmpRet = new JSONObject();
        tmpRet.put("data", dao.getZdxx(bm));
        tmpRet.put("code", "1");
        return tmpRet;
    }

    public Result add(Dtgj_Abry item) {
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

    public Result update(Dtgj_Abry item) {
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