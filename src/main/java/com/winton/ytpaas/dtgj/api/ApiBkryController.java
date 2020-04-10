package com.winton.ytpaas.dtgj.api;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.configuration.log.LogType;
import com.winton.ytpaas.common.configuration.log.SystemLog;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.dtgj.service.Dtgj_BkryService;
import com.winton.ytpaas.system.entity.Sys_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/xxgl/bkry")
@Api(value = "地铁公交_布控人员", description = "地铁公交_布控人员")
public class ApiBkryController {

    @Autowired
    Dtgj_BkryService service;
    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "获取列表", notes = "获取列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/list", produces = "application/json")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));

        // String token = request.getHeader("token");
        // Sys_User user = tokenService.verifyToken(token);
        Map<String, String> tmpSelTj = new HashMap<String, String>();

        JSONObject res = service.getList(tmpSelTj, page, limit, iscon);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "根据id获取信息", notes = "根据id获取信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/getitembyid", produces = "application/json")
    public String getItemById(HttpServletRequest request, HttpServletResponse response) {
        String tmpId = request.getParameter("id");
        Result res = service.getItemById(tmpId);
        return res.toString();
    }

    @ApiOperation(value = "添加", notes = "添加", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加", type = LogType.API)
    public String add(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        Map<String, Object> tmpItem = new HashMap<String, Object>();

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");sdf.parse("");
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        tmpItem.put("tjr", user.getLOGINNAME());
        tmpItem.put("tjdw", user.getDWDM());
        tmpItem.put("tjdwmc", user.getDWMC());
        
        tmpItem.put("name", request.getParameter("name"));
        tmpItem.put("idcard", request.getParameter("idcard"));
        tmpItem.put("bknr", request.getParameter("bknr"));
        tmpItem.put("picture", request.getParameter("picture"));

        Result res = service.add(tmpItem);

        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "修改", notes = "修改", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/update", produces = "application/json")
    @SystemLog(description = "修改", type = LogType.API)
    public String update(HttpServletRequest request, HttpServletResponse response) throws ParseException {

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");sdf.parse("");
        Map<String, Object> tmpItem = new HashMap<String, Object>();
        tmpItem.put("id", request.getParameter("id"));
        tmpItem.put("name", request.getParameter("name"));
        tmpItem.put("idcard", request.getParameter("idcard"));
        tmpItem.put("bknr", request.getParameter("bknr"));
        tmpItem.put("picture", request.getParameter("picture"));

        Result res = service.update(tmpItem);
        String retStr = Tools.toJSONString(res);

        return retStr;
    }

    @ApiOperation(value = "删除", notes = "删除", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要删除的主键ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/delete", produces = "application/json")
    @SystemLog(description = "删除", type = LogType.API)
    public String delete(HttpServletRequest request, HttpServletResponse response) {

        String tmpIds = request.getParameter("ids");

        Result res = service.delete(tmpIds);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }
}