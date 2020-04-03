package com.winton.ytpaas.dtgj.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.dtgj.dao.Dtgj_YjctDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dtgjYjctService")
public class Dtgj_YjctService {
    @Autowired
    Dtgj_YjctDao dao;

    public JSONObject getList(String varName, int varPage, int varLimit, int varIsCon) {
        JSONObject tmpRet = new JSONObject();

        String tmpBegCon = String.valueOf((varPage - 1) * varLimit + 1);
        String tmpEndCon = String.valueOf(varPage * varLimit);
        tmpRet.put("data", dao.getList(varName, tmpBegCon, tmpEndCon));
        if (varIsCon == 1) {
            tmpRet.put("count", dao.getCon(varName));
        }
        tmpRet.put("code", "1");

        return tmpRet;
    }

    public JSONObject getListCon(String varName) {
        JSONObject tmpRet = new JSONObject();

        tmpRet.put("count", dao.getCon(varName));
        tmpRet.put("code", "1");

        return tmpRet;
    }

    public Result getItemById(String varId) {
        Result res = new Result();
        Map<String, Object> tmpItem = dao.getItemById(varId);
        if (tmpItem != null) {
            res.setCode("1");
            res.setData(tmpItem);
        } else {
            res.setCode("-1");
        }
        return res;
    }

    public Result add(Map<String, Object> varItem) {
        Result res = new Result();
        boolean b = dao.add(varItem);
        if (b) {
            res.setCode("1");
            res.setMsg("添加成功");
        } else {
            res.setCode("-1");
            res.setMsg("添加失败");
        }
        return res;
    }

    public Result update(Map<String, Object> varItem) {
        Result res = new Result();
        boolean b = dao.update(varItem);
        if (b) {
            res.setCode("1");
            res.setMsg("修改成功");
        } else {
            res.setCode("-1");
            res.setMsg("修改失败");
        }
        return res;
    }

    public Result delete(String varIds) {
        Result res = new Result();
        boolean b = dao.delete(varIds);
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