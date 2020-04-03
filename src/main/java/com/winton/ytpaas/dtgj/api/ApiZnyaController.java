package com.winton.ytpaas.dtgj.api;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.PassToken;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.configuration.log.LogType;
import com.winton.ytpaas.common.configuration.log.SystemLog;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.dtgj.entity.Dtgj_Znya;
import com.winton.ytpaas.dtgj.service.Dtgj_ZnyaService;
import com.winton.ytpaas.system.entity.Sys_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/jcfz/znya")
@Api(value = "智能预案管理接口", description = "智能预案管理接口")
public class ApiZnyaController {
    @Autowired
    Dtgj_ZnyaService dtgjZnyaService;
    @Autowired
    TokenService tokenService;

    @PassToken
    @ApiOperation(value = "获取智能预案列表", notes = "获取智能预案列表", httpMethod = "GET")
    @RequestMapping(value = "/list", produces = "application/json")
    public String znyaList(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));

        String name = request.getParameter("name");
        String dj = request.getParameter("dj");
        String fbsjBegin = request.getParameter("fbsjBegin");
        String fbsjEnd = request.getParameter("fbsjEnd");

        JSONObject res = dtgjZnyaService.getList(name, dj, fbsjBegin, fbsjEnd, page, limit, iscon);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @PassToken
    @ApiOperation(value = "获取勤务报备列表总数", notes = "获取勤务报备列表总数", httpMethod = "GET")
    @RequestMapping(value = "/listcount", produces = "application/json")
    public String listcount(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String dj = request.getParameter("dj");
        String fbsjBegin = request.getParameter("fbsjBegin");
        String fbsjEnd = request.getParameter("fbsjEnd");
        JSONObject res = dtgjZnyaService.getListCount(name, dj, fbsjBegin, fbsjEnd);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "根据id获取智能预案信息", notes = "根据id获取智能预案信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getById", produces = "application/json")
    public String getZnyaById(HttpServletRequest request, HttpServletResponse response) {
        Result res = dtgjZnyaService.getById(request.getParameter("id"));
        System.out.print(res.toString());
        return res.toString();
    }

    @ApiOperation(value = "添加智能预案", notes = "添加智能预案", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "dj", value = "等级", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "nr", value = "内容", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加智能预案", type = LogType.SYSTEM_OPERATION)
    public String dtgj_ZnyaAdd(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        Sys_User user = tokenService.verifyToken(token);

        Dtgj_Znya znya = new Dtgj_Znya();

        znya.setID(id);
        znya.setNAME(request.getParameter("name"));
        znya.setDJ(request.getParameter("dj"));
        znya.setNR(request.getParameter("nr"));
        znya.setFBR(user.getLOGINNAME());
        znya.setFBDW(user.getDWDM());

        Result res = dtgjZnyaService.add(znya);
        res.setData(id);
        return res.toString();
    }

    @ApiOperation(value = "修改智能预案", notes = "修改智能预案", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "dj", value = "等级", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "nr", value = "内容", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/update", produces = "application/json")
    @SystemLog(description = "修改智能预案", type = LogType.SYSTEM_OPERATION)
    public String dtgj_ZnyaUpdate(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Dtgj_Znya znya = new Dtgj_Znya();

        znya.setID(id);
        znya.setNAME(request.getParameter("name"));
        znya.setDJ(request.getParameter("dj"));
        znya.setNR(request.getParameter("nr"));
        znya.setFBR(request.getParameter("fbr"));
        znya.setFBDW(request.getParameter("fbdw"));

        Result res = dtgjZnyaService.update(znya);
        res.setData(id);
        return res.toString();
    }

    @ApiOperation(value = "删除智能预案信息", notes = "删除智能预案信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", paramType = "query", required = true) })
    @RequestMapping(value = "/delete", produces = "application/json")
    @SystemLog(description = "删除智能预案信息", type = LogType.SYSTEM_OPERATION)
    public String dtgj_ZnyaDelete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("ids");

        Result res = dtgjZnyaService.delete(id);
        return res.toString();
    }
}