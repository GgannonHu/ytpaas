package com.winton.ytpaas.dtgj.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.dtgj.dao.Dtgj_ZdryyjDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dtgjZdryyjService")
public class Dtgj_ZdryyjService {
    @Autowired
    Dtgj_ZdryyjDao dao;

    public JSONObject getRlyjList(Map<String, String> varSelTj, int varPage, int varLimit, int varIsCon) {
        JSONObject tmpRet = new JSONObject();

        String tmpBegCon = String.valueOf((varPage - 1) * varLimit + 1);
        String tmpEndCon = String.valueOf(varPage * varLimit);
        tmpRet.put("data", dao.getRlyjList(varSelTj, tmpBegCon, tmpEndCon));
        if (varIsCon == 1) {
            tmpRet.put("count", dao.getRlyjCon(varSelTj));
        }
        tmpRet.put("code", "1");

        return tmpRet;
    }

    public Result getRlyjById(String varId) {
        Result res = new Result();
        Map<String, Object> tmpItem = dao.getRlyjById(varId);
        if (tmpItem != null) {
            res.setCode("1");
            res.setData(tmpItem);
        } else {
            res.setCode("-1");
        }
        return res;
    }

    public Result getZdryBySfzh(String varSfzh) {
        Result res = new Result();
        Map<String, Object> tmpItem = dao.getZdryBySfzh(varSfzh);
        if (tmpItem != null) {
            res.setCode("1");
            res.setData(tmpItem);
        } else {
            res.setCode("-1");
        }
        return res;
    }
}