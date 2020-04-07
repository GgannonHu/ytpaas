package com.winton.ytpaas.dtgj.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.PassToken;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.configuration.log.LogType;
import com.winton.ytpaas.common.configuration.log.SystemLog;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.dtgj.entity.Dtgj_Qwbb;
import com.winton.ytpaas.dtgj.service.Dtgj_QwbbService;
import com.winton.ytpaas.system.entity.Sys_User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/zhld/qwgl/qwbb")
@Api(value = "勤务报备管理接口", description = "勤务报备管理接口")
public class ApiQwbbController {
    @Autowired
    Dtgj_QwbbService dtgjQwbbService;
    @Autowired
    TokenService tokenService;

    @PassToken
    @ApiOperation(value = "获取勤务报备列表", notes = "获取勤务报备列表", httpMethod = "GET")
    @RequestMapping(value = "/list", produces = "application/json")
    public String qwbbList(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));

        String xm = request.getParameter("xm");
        String fbsjBegin = request.getParameter("fbsjBegin");
        String fbsjEnd = request.getParameter("fbsjEnd");
        String wczt = request.getParameter("wczt");
        JSONObject res = dtgjQwbbService.getList(xm, fbsjBegin, fbsjEnd, wczt, page, limit, iscon);
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
        String xm = request.getParameter("xm");
        String fbsjBegin = request.getParameter("fbsjBegin");
        String fbsjEnd = request.getParameter("fbsjEnd");
        String wczt = request.getParameter("wczt");
        JSONObject res = dtgjQwbbService.getListCount(xm, fbsjBegin, fbsjEnd, wczt);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "根据id获取勤务报备信息", notes = "根据id获取勤务报备信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getById", produces = "application/json")
    public String getQwbbById(HttpServletRequest request, HttpServletResponse response) {
        Result res = dtgjQwbbService.getById(request.getParameter("id"));
        System.out.print(res.toString());
        return res.toString();
    }

    @ApiOperation(value = "添加勤务报备", notes = "添加勤务报备", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xm", value = "姓名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "sfzh", value = "身份证号", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "qwnr", value = "勤务内容", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "fbsj", value = "发布时间", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "wcsj", value = "完成时间", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "wczt", value = "完成状态", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加勤务报备", type = LogType.SYSTEM_OPERATION)
    public String dtgj_QwbbAdd(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);

        Dtgj_Qwbb qwbb = new Dtgj_Qwbb();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fbsj = new Date();
        Date wcsj = new Date();

        try {
            fbsj = df.parse((String) request.getParameter("fbsj"));
            wcsj = df.parse((String) request.getParameter("wcsj"));
        } catch (Exception e) {
        }

        qwbb.setXM(request.getParameter("xm"));
        qwbb.setSFZH(request.getParameter("sfzh"));
        qwbb.setQWNR(request.getParameter("qwnr"));
        qwbb.setFBSJ(fbsj);
        qwbb.setWCSJ(wcsj);
        qwbb.setWCZT(request.getParameter("wczt"));
        qwbb.setTJR(user.getLOGINNAME());
        qwbb.setTJDW(user.getDWDM());
        qwbb.setTJDW(user.getDWDM());
        qwbb.setTJDWMC(user.getDWMC());

        Result res = dtgjQwbbService.add(qwbb);
        return res.toString();
    }

    @ApiOperation(value = "修改勤务报备", notes = "修改勤务报备", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xm", value = "姓名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "sfzh", value = "身份证号", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "qwnr", value = "勤务内容", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "fbsj", value = "发布时间", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "wcsj", value = "完成时间", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "wczt", value = "完成状态", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/update", produces = "application/json")
    @SystemLog(description = "修改勤务报备", type = LogType.SYSTEM_OPERATION)
    public String dtgj_QwbbUpdate(HttpServletRequest request, HttpServletResponse response) {
        Dtgj_Qwbb qwbb = new Dtgj_Qwbb();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fbsj = new Date();
        Date wcsj = new Date();

        try {
            fbsj = df.parse((String) request.getParameter("fbsj"));
            wcsj = df.parse((String) request.getParameter("wcsj"));
        } catch (Exception e) {
        }

        qwbb.setID(request.getParameter("id"));
        qwbb.setXM(request.getParameter("xm"));
        qwbb.setSFZH(request.getParameter("sfzh"));
        qwbb.setQWNR(request.getParameter("qwnr"));
        qwbb.setFBSJ(fbsj);
        qwbb.setWCSJ(wcsj);
        qwbb.setWCZT(request.getParameter("wczt"));

        Result res = dtgjQwbbService.update(qwbb);
        return res.toString();
    }

    @ApiOperation(value = "删除勤务报备信息", notes = "删除勤务报备信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", paramType = "query", required = true) })
    @RequestMapping(value = "/delete", produces = "application/json")
    @SystemLog(description = "删除勤务报备信息", type = LogType.SYSTEM_OPERATION)
    public String dtgj_QwbbDelete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("ids");

        Result res = dtgjQwbbService.delete(id);
        return res.toString();
    }
}