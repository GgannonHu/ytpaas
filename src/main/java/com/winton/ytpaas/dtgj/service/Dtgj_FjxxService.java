package com.winton.ytpaas.dtgj.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.dtgj.dao.Dtgj_FjxxDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dtgjFjxxService")
public class Dtgj_FjxxService {
    @Autowired
    Dtgj_FjxxDao dao;

    public JSONObject getListByPid(String varPid) {
        JSONObject tmpRet = new JSONObject();

        tmpRet.put("data", dao.getListByPid(varPid));
        tmpRet.put("code", "1");

        return tmpRet;
    }

    public JSONObject getListByIds(String varIds) {
        JSONObject tmpRet = new JSONObject();

        tmpRet.put("data", dao.getListByIds(varIds));
        tmpRet.put("code", "1");

        return tmpRet;
    }

    public JSONObject getListByPids(String varPids) {
        JSONObject tmpRet = new JSONObject();

        tmpRet.put("data", dao.getListByPids(varPids));
        tmpRet.put("code", "1");

        return tmpRet;
    }

    

    public Result add(Map<String, Object> varItem) {
        Result res = new Result();
        boolean b = dao.add(varItem);
        if (b) {
            res.setCode("1");
            res.setMsg("文件上传成功");
        } else {
            res.setCode("-1");
            res.setMsg("文件上传失败");
        }
        return res;
    }

    public Result delete(String varIds) {
        Result res = new Result();
        boolean b = dao.delete(varIds);
        if (b) {
            res.setCode("1");
            res.setMsg("文件删除成功");
        } else {
            res.setCode("-1");
            res.setMsg("文件删除失败");
        }
        return res;
    }

    public Result deleteByPid(String varIds) {
        Result res = new Result();
        boolean b = dao.deleteByPid(varIds);
        if (b) {
            res.setCode("1");
            res.setMsg("文件删除成功");
        } else {
            res.setCode("-1");
            res.setMsg("文件删除失败");
        }
        return res;
    }
}