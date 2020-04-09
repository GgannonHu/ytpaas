package com.winton.ytpaas.dtgj.api;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.configuration.log.LogType;
import com.winton.ytpaas.common.configuration.log.SystemLog;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.dtgj.service.Dtgj_CtczService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/ctcz")
@Api(value = "常态处置接口", description = "常态处置接口")
public class ApiCtczController {

    @Autowired
    Dtgj_CtczService dtgjCtczService;
    @Autowired
    TokenService tokenService;
    //测试Git1
    @ApiOperation(value = "获取列表", notes = "获取列表", httpMethod = "GET")
    @RequestMapping(value = "/list", produces = "application/json")
    public String getList(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));
        
        String dwdm = request.getParameter("dwdm");
        String user = request.getParameter("user");
        String mc = request.getParameter("mc");
        // String nr = request.getParameter("nr");
        String lx = request.getParameter("lx");
        String qs = request.getParameter("qs");
        String fk = request.getParameter("fk");
        String fbsjS = request.getParameter("fbsjS");
        String fbsjE = request.getParameter("fbsjE");
        JSONObject res = dtgjCtczService.getList(dwdm,user,mc, lx, qs, fk, fbsjS, fbsjE, page, limit, iscon);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }
    
    @ApiOperation(value = "获取列表总数", notes = "获取列表总数", httpMethod = "GET")
    @RequestMapping(value = "/listcon", produces = "application/json")
    public String getCon(HttpServletRequest request, HttpServletResponse response) {
        String dwdm = request.getParameter("dwdm");
        String user = request.getParameter("user");
        String mc = request.getParameter("mc");
        // String nr = request.getParameter("nr");
        String lx = request.getParameter("lx");
        String qs = request.getParameter("qs");
        String fk = request.getParameter("fk");
        String fbsjS = request.getParameter("fbsjS");
        String fbsjE = request.getParameter("fbsjE");
        JSONObject res = dtgjCtczService.getListcon(dwdm,user,mc, lx, qs, fk, fbsjS, fbsjE);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "根据id获取列表", notes = "根据id获取列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getById", produces = "application/json")
    public String getCtczById(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Result res = dtgjCtczService.getById(id);
        return res.toString();
    }

    @ApiOperation(value = "根据id获取签收列表", notes = "根据id获取签收列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getQsById", produces = "application/json")
    public String getQsList(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        JSONObject res = dtgjCtczService.getQsById(id);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "根据id获取反馈列表", notes = "根据id获取反馈列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getFkById", produces = "application/json")
    public String getFkList(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String dwdm = request.getParameter("dwdm");
        JSONObject res = dtgjCtczService.getFkById(id,dwdm);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "添加", notes = "添加", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", value = "名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "ctczName", value = "昵称", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_CtczAdd(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        Map<String, Object> tmpItem = new HashMap<String, Object>();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        tmpItem.put("id", id);
        tmpItem.put("name", request.getParameter("ctczName"));
        tmpItem.put("nr", request.getParameter("ctczNr"));
        tmpItem.put("xfdw", request.getParameter("hdfJsdw"));
        tmpItem.put("xfdwmc", request.getParameter("hdfJsdwMc"));
        tmpItem.put("type", request.getParameter("hdfType"));
        tmpItem.put("fbr", request.getParameter("hdfUser"));
        tmpItem.put("fbdw", request.getParameter("hdfDwbm"));
        tmpItem.put("fbdwmc", request.getParameter("hdfDwbmMc"));

        Result res = dtgjCtczService.add(tmpItem);
        res.setData(id);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "下发", notes = "下发", httpMethod = "POST")
    @RequestMapping(value = "/xf", produces = "application/json")
    @SystemLog(description = "下发", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_CtczXf(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        Map<String, Object> tmpItem = new HashMap<String, Object>();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        tmpItem.put("id", id);
        tmpItem.put("name", request.getParameter("ctczName"));
        tmpItem.put("nr", request.getParameter("ctczNr"));
        tmpItem.put("xfdw", request.getParameter("hdfJsdw"));
        tmpItem.put("xfdwmc", request.getParameter("hdfJsdwMc"));
        tmpItem.put("type", request.getParameter("hdfType"));
        tmpItem.put("fbr", request.getParameter("hdfUser"));
        tmpItem.put("fbdw", request.getParameter("hdfDwbm"));
        tmpItem.put("fbdwmc", request.getParameter("hdfDwbmMc"));
        tmpItem.put("pid", request.getParameter("id"));
        tmpItem.put("fid", request.getParameter("id"));

        Result res = dtgjCtczService.add(tmpItem);
        res.setData(id);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "修改", notes = "修改", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ctczName", value = "名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", paramType = "query", required = true) })
    @RequestMapping(value = "/update", produces = "application/json")
    @SystemLog(description = "修改", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_CtczUpdate(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        Map<String, Object> tmpItem = new HashMap<String, Object>();
        String id = request.getParameter("id");
        tmpItem.put("name", request.getParameter("ctczName"));
        tmpItem.put("nr", request.getParameter("ctczNr"));
        tmpItem.put("xfdw", request.getParameter("hdfJsdw"));
        tmpItem.put("xfdwmc", request.getParameter("hdfJsdwMc"));
        tmpItem.put("fbr", request.getParameter("hdfUser"));
        tmpItem.put("id", id);

        Result res = dtgjCtczService.update(tmpItem);
        res.setData(id);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "删除", notes = "删除", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", paramType = "query", required = true) })
    @RequestMapping(value = "/delete", produces = "application/json")
    @SystemLog(description = "删除", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_CtczDelete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("ids");
        Result res = dtgjCtczService.delete(id);
        return res.toString();
    }

    @ApiOperation(value = "签收", notes = "签收", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ctczqsr", value = "签收人", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/qs", produces = "application/json")
    @SystemLog(description = "签收", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_Ctczqs(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        Map<String, Object> tmpItem = new HashMap<String, Object>();
        tmpItem.put("qsdw", request.getParameter("dwdm"));
        tmpItem.put("qsdwmc", request.getParameter("dwmc"));
        tmpItem.put("qsr", request.getParameter("user"));
        tmpItem.put("id", request.getParameter("id"));
        Result res = dtgjCtczService.qs(tmpItem);
        return res.toString();
    }

    @ApiOperation(value = "反馈", notes = "反馈", httpMethod = "POST")
    @RequestMapping(value = "/fk", produces = "application/json")
    @SystemLog(description = "反馈", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_Ctczfk(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        Map<String, Object> tmpItem = new HashMap<String, Object>();
        tmpItem.put("fknr", request.getParameter("ctczFk"));
        tmpItem.put("fkr", request.getParameter("hdfUser"));
        tmpItem.put("fkdw", request.getParameter("hdfDwbm"));
        tmpItem.put("fkdwmc", request.getParameter("hdfDwbmMc"));
        tmpItem.put("id", request.getParameter("id"));

        Result res = dtgjCtczService.fk(tmpItem);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

}