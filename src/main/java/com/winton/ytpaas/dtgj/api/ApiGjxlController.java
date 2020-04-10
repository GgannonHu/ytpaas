package com.winton.ytpaas.dtgj.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.winton.ytpaas.dtgj.service.Dtgj_GjxlService;
import com.winton.ytpaas.system.entity.Sys_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/xxgl/gjxl")
@Api(value = "地铁公交_公交线路", description = "地铁公交_公交线路")
public class ApiGjxlController {

    @Autowired
    Dtgj_GjxlService service;
    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "获取列表", notes = "获取列表", httpMethod = "GET")
    @ApiImplicitParams({
            // @ApiImplicitParam(name = "", value = "", dataType = "String", paramType =
            // "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/list", produces = "application/json")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));

        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        String tmpDwdm = user.getDWDM();
        if (tmpDwdm.indexOf("0000000000") > 0) {
            tmpDwdm = tmpDwdm.substring(0, 2);
        } else if (tmpDwdm.indexOf("00000000") > 0) {
            tmpDwdm = tmpDwdm.substring(0, 4);
        } else if (tmpDwdm.indexOf("000000") > 0) {
            tmpDwdm = tmpDwdm.substring(0, 6);
        }

        Map<String, String> tmpSelTj = new HashMap<String, String>();
        tmpSelTj.put("xlmc", request.getParameter("xlmc"));
        tmpSelTj.put("zdmc", request.getParameter("zdmc"));
        tmpSelTj.put("dwdm", tmpDwdm);

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

    @ApiOperation(value = "添加公交线路", notes = "添加公交线路", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "地铁公交_添加公交线路", type = LogType.API)
    public String add(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        Map<String, Object> tmpItem = new HashMap<String, Object>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmpTopDate = "2020-01-01 ";

        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        tmpItem.put("xzqhdm", user.getDWDM().substring(0, 6));// request.getParameter("xzqhdm")
        tmpItem.put("tjr", user.getLOGINNAME());
        tmpItem.put("tjdw", user.getDWDM());
        tmpItem.put("tjdwmc", user.getDWMC());

        tmpItem.put("gjxlbm", request.getParameter("gjxlbm"));
        tmpItem.put("gjxlmc", request.getParameter("gjxlmc"));
        tmpItem.put("gjxlqdz", request.getParameter("gjxlqdz"));
        tmpItem.put("gjxlzdz", request.getParameter("gjxlzdz"));

        String qdzsmc_kssj = request.getParameter("qdzsmc_kssj");
        tmpItem.put("qdzsmc_kssj", qdzsmc_kssj != "" ? sdf.parse(tmpTopDate + qdzsmc_kssj) : null);
        String qdzsmc_jssj = request.getParameter("qdzsmc_jssj");
        tmpItem.put("qdzsmc_jssj", qdzsmc_jssj != "" ? sdf.parse(tmpTopDate + qdzsmc_jssj) : null);
        String zdzsmc_kssj = request.getParameter("zdzsmc_kssj");
        tmpItem.put("zdzsmc_kssj", zdzsmc_kssj != "" ? sdf.parse(tmpTopDate + zdzsmc_kssj) : null);
        String zdzsmc_jssj = request.getParameter("zdzsmc_jssj");
        tmpItem.put("zdzsmc_jssj", zdzsmc_jssj != "" ? sdf.parse(tmpTopDate + zdzsmc_jssj) : null);

        Result res = service.add(tmpItem);

        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "修改公交线路", notes = "修改公交线路", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/update", produces = "application/json")
    @SystemLog(description = "地铁公交_修改公交线路", type = LogType.API)
    public String update(HttpServletRequest request, HttpServletResponse response) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmpTopDate = "2020-01-01 ";
        Map<String, Object> tmpItem = new HashMap<String, Object>();
        tmpItem.put("id", request.getParameter("id"));
        // tmpItem.put("xzqhdm", request.getParameter("xzqhdm"));
        // tmpItem.put("gjxlbm", request.getParameter("gjxlbm"));
        // tmpItem.put("gjxlmc", request.getParameter("gjxlmc"));
        tmpItem.put("gjxlqdz", request.getParameter("gjxlqdz"));
        tmpItem.put("gjxlzdz", request.getParameter("gjxlzdz"));

        String qdzsmc_kssj = request.getParameter("qdzsmc_kssj");
        tmpItem.put("qdzsmc_kssj", qdzsmc_kssj != "" ? sdf.parse(tmpTopDate + qdzsmc_kssj) : null);
        String qdzsmc_jssj = request.getParameter("qdzsmc_jssj");
        tmpItem.put("qdzsmc_jssj", qdzsmc_jssj != "" ? sdf.parse(tmpTopDate + qdzsmc_jssj) : null);
        String zdzsmc_kssj = request.getParameter("zdzsmc_kssj");
        tmpItem.put("zdzsmc_kssj", zdzsmc_kssj != "" ? sdf.parse(tmpTopDate + zdzsmc_kssj) : null);
        String zdzsmc_jssj = request.getParameter("zdzsmc_jssj");
        tmpItem.put("zdzsmc_jssj", zdzsmc_jssj != "" ? sdf.parse(tmpTopDate + zdzsmc_jssj) : null);

        Result res = service.update(tmpItem);
        String retStr = Tools.toJSONString(res);

        return retStr;
    }

    @ApiOperation(value = "删除公交线路", notes = "删除公交线路", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要删除的主键ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/delete", produces = "application/json")
    @SystemLog(description = "地铁公交_删除公交线路", type = LogType.API)
    public String delete(HttpServletRequest request, HttpServletResponse response) {

        String tmpIds = request.getParameter("ids");

        Result res = service.delete(tmpIds);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "根据编码获取名称", notes = "根据编码获取名称", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bm", value = "线路编码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/getmcbybm", produces = "application/json")
    public String GetMcByBm(HttpServletRequest request, HttpServletResponse response) {
        String tmpBm = request.getParameter("bm");
        Result res = service.GetMcByBm(tmpBm);
        return res.toString();
    }
}