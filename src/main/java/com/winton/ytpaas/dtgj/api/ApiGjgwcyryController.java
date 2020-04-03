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
import com.winton.ytpaas.dtgj.service.Dtgj_GjgwcyryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/gjgwcyry")
@Api(value = "关键岗位从业人员接口", description = "关键岗位从业人员接口")
public class ApiGjgwcyryController {
    @Autowired
    Dtgj_GjgwcyryService dtgjService;
    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "获取关键岗位从业人员列表", notes = "获取关键岗位从业人员列表", httpMethod = "GET")
    @RequestMapping(value = "/list", produces = "application/json")
    public String List(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));

        String user = request.getParameter("user");
        String name = request.getParameter("name");
        String sfzh = request.getParameter("sfzh");
        String dwmc = request.getParameter("dwmc");
        JSONObject res = dtgjService.getList(user,name,sfzh,dwmc, page, limit, iscon);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "获取关键岗位从业人员列表总数", notes = "获取关键岗位从业人员列表总数", httpMethod = "GET")
    @RequestMapping(value = "/listcon", produces = "application/json")
    public String con(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String sfzh = request.getParameter("sfzh");
        String dwmc = request.getParameter("dwmc");
        JSONObject res = dtgjService.getListcon(name,sfzh,dwmc);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }
    
    @ApiOperation(value = "根据id获取关键岗位从业人员列表", notes = "根据id获取关键岗位从业人员列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "关键岗位从业人员id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getById", produces = "application/json")
    public String getById(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Result res = dtgjService.getById(id);
        return res.toString();
    }

    @ApiOperation(value = "添加关键岗位从业人员", notes = "添加关键岗位从业人员", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", value = "关键岗位从业人员名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "Name", value = "昵称", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加关键岗位从业人员", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_Add(HttpServletRequest request, HttpServletResponse response) throws ParseException
    {
        Map<String, Object> tmpItem = new HashMap<String, Object>();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        tmpItem.put("ID", id);
        tmpItem.put("TJR", request.getParameter("hdfUser"));
        tmpItem.put("XZQYDM", request.getParameter("hdfDwdm").substring(0, 6));
        tmpItem.put("TJDW", request.getParameter("hdfDwdm"));
        tmpItem.put("TJDWMC", request.getParameter("hdfDwmc"));
        tmpItem.put("GJGWCYRY_XM", request.getParameter("gjgwcyryName"));
        tmpItem.put("GJGWCYRY_GMSFZH", request.getParameter("gjgwcyryIdcard"));
        tmpItem.put("GJGWCYRY_MZDM", request.getParameter("gjgwcyryMz"));
        tmpItem.put("GJGWCYRY_DWMC", request.getParameter("gjgwcyryDwmc"));
        tmpItem.put("GJGWCYRY_QYBM", request.getParameter("gjgwcyryQybm"));
        tmpItem.put("GJGWCYRY_GWMC", request.getParameter("gjgwcyryGwmc"));
        tmpItem.put("GJGWCYRY_LXDH", request.getParameter("gjgwcyryLxdh"));
        tmpItem.put("GJGWCYRY_DZMC", request.getParameter("gjgwcyryDzmc"));
        tmpItem.put("GJGWCYRY_WFFZJLMS", request.getParameter("gjgwcyryWffz"));
        Result res = dtgjService.add(tmpItem);
        res.setData(id);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "修改关键岗位从业人员", notes = "修改关键岗位从业人员", httpMethod = "POST")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "Name", value = "昵称", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "id", value = "关键岗位从业人员id", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value="/update",produces = "application/json")
    @SystemLog(description = "修改关键岗位从业人员", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_Update(HttpServletRequest request,HttpServletResponse response) throws ParseException
    {
        Map<String, Object> tmpItem = new HashMap<String, Object>();
        String id = request.getParameter("id");
        tmpItem.put("ID", id);
        tmpItem.put("GJGWCYRY_XM", request.getParameter("gjgwcyryName"));
        tmpItem.put("GJGWCYRY_GMSFZH", request.getParameter("gjgwcyryIdcard"));
        tmpItem.put("GJGWCYRY_MZDM", request.getParameter("gjgwcyryMz"));
        tmpItem.put("GJGWCYRY_DWMC", request.getParameter("gjgwcyryDwmc"));
        tmpItem.put("GJGWCYRY_QYBM", request.getParameter("gjgwcyryQybm"));
        tmpItem.put("GJGWCYRY_GWMC", request.getParameter("gjgwcyryGwmc"));
        tmpItem.put("GJGWCYRY_LXDH", request.getParameter("gjgwcyryLxdh"));
        tmpItem.put("GJGWCYRY_DZMC", request.getParameter("gjgwcyryDzmc"));
        tmpItem.put("GJGWCYRY_WFFZJLMS", request.getParameter("gjgwcyryWffz"));
        
        Result res = dtgjService.update(tmpItem);
        res.setData(id);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "删除关键岗位从业人员", notes = "删除关键岗位从业人员", httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "关键岗位从业人员id", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value="/delete",produces = "application/json")
    @SystemLog(description = "删除关键岗位从业人员", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_Delete(HttpServletRequest request,HttpServletResponse response)
    {
        String id = request.getParameter("ids");
        Result res = dtgjService.delete(id);
        return res.toString();
    }
    
}