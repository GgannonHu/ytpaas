package com.winton.ytpaas.dtgj.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.dtgj.service.Dtgj_ZdryyjService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/yjgz/zdryyj")
@Api(value = "地铁公交_重点人员预警", description = "地铁公交_重点人员预警")
public class ApiZdryyjController {

    @Autowired
    Dtgj_ZdryyjService service;
    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "获取列表", notes = "获取列表", httpMethod = "GET")
    @ApiImplicitParams({
            // @ApiImplicitParam(name = "", value = "", dataType = "String", paramType =
            // "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/rlyjlist", produces = "application/json")
    public String rlyjList(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));

        // String token = request.getHeader("token");
        // Sys_User user = tokenService.verifyToken(token);

        Map<String, String> tmpSelTj = new HashMap<String, String>();

        JSONObject res = service.getRlyjList(tmpSelTj, page, limit, iscon);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "根据Id获取信息", notes = "根据Id获取信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sfzh", value = "人员身份证号", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/getrlyjbyid", produces = "application/json")
    public String getRlyjById(HttpServletRequest request, HttpServletResponse response) {
        String tmpId = request.getParameter("id");
        Result res = service.getRlyjById(tmpId);
        return res.toString();
    }

    @ApiOperation(value = "根据身份证获取信息", notes = "根据身份证获取信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sfzh", value = "人员身份证号", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/getzdrybysfzh", produces = "application/json")
    public String getZdryBySfzh(HttpServletRequest request, HttpServletResponse response) {
        String tmpSfzh = request.getParameter("sfzh");
        Result res = service.getZdryBySfzh(tmpSfzh);
        return res.toString();
    }

}