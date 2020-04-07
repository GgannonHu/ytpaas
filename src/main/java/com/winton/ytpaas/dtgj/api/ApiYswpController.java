package com.winton.ytpaas.dtgj.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.configuration.log.LogType;
import com.winton.ytpaas.common.configuration.log.SystemLog;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.dtgj.entity.Dtgj_Yswp;
import com.winton.ytpaas.dtgj.service.Dtgj_YswpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/yswp")
@Api(value = "遗失物品接口", description = "遗失物品接口")
public class ApiYswpController {
    @Autowired
    Dtgj_YswpService dtgjYswpService;
    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "获取遗失物品列表", notes = "获取遗失物品列表", httpMethod = "GET")
    @RequestMapping(value = "/list", produces = "application/json")
    public String yswpList(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));

        String user = request.getParameter("user");
        String zt = request.getParameter("zt");
        String mc = request.getParameter("mc");
        String ms = request.getParameter("ms");
        String sqdd = request.getParameter("sqdd");
        String sqsjS = request.getParameter("sqsjS");
        String sqsjE = request.getParameter("sqsjE");
        JSONObject res = dtgjYswpService.getList(user,zt,mc,ms,sqdd,sqsjS,sqsjE, page, limit, iscon);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "获取遗失物品列表总数", notes = "获取遗失物品列表总数", httpMethod = "GET")
    @RequestMapping(value = "/listcon", produces = "application/json")
    public String yswpcon(HttpServletRequest request, HttpServletResponse response) {
        String zt = request.getParameter("zt");
        String mc = request.getParameter("mc");
        String ms = request.getParameter("ms");
        String sqdd = request.getParameter("sqdd");
        String sqsjS = request.getParameter("sqsjS");
        String sqsjE = request.getParameter("sqsjE");
        JSONObject res = dtgjYswpService.getListcon(zt,mc,ms,sqdd,sqsjS,sqsjE);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }
    
    @ApiOperation(value = "根据id获取遗失物品列表", notes = "根据id获取遗失物品列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "遗失物品id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getById", produces = "application/json")
    public String getYswpById(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Result res = dtgjYswpService.getById(id);
        return res.toString();
    }

    @ApiOperation(value = "添加遗失物品", notes = "添加遗失物品", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", value = "遗失物品名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "yswpName", value = "昵称", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加遗失物品", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_YswpAdd(HttpServletRequest request, HttpServletResponse response) throws ParseException
    {
        Dtgj_Yswp yswp = new Dtgj_Yswp();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        yswp.setID(id);
        Date date=null;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sj=request.getParameter("yswpSqsj");
        if (sj != null) {
            date = formatter.parse(sj);
        }
        yswp.setTJDW(request.getParameter("hdfDwdm"));
        yswp.setTJDWMC(request.getParameter("hdfDwmc"));
        yswp.setTJR(request.getParameter("hdfUser"));
        yswp.setMC(request.getParameter("yswpName"));
        yswp.setMS(request.getParameter("yswpWpms"));
        yswp.setSQDD(request.getParameter("yswpSqdd"));
        yswp.setZT("招领中");
        yswp.setSQSJ(date);
        
        Result res = dtgjYswpService.add(yswp);
        res.setData(id);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "修改遗失物品", notes = "修改遗失物品", httpMethod = "POST")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "yswpName", value = "昵称", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "id", value = "遗失物品id", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value="/update",produces = "application/json")
    @SystemLog(description = "修改遗失物品", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_YswpUpdate(HttpServletRequest request,HttpServletResponse response) throws ParseException
    {
        Dtgj_Yswp yswp = new Dtgj_Yswp();
        String id = request.getParameter("id");
        Date date=null;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date=formatter.parse(request.getParameter("yswpSqsj"));
        yswp.setMC(request.getParameter("yswpName"));
        yswp.setMS(request.getParameter("yswpWpms"));
        yswp.setSQDD(request.getParameter("yswpSqdd"));
        yswp.setSQSJ(date);
        yswp.setID(id);
        
        Result res = dtgjYswpService.update(yswp);
        res.setData(id);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "删除遗失物品", notes = "删除遗失物品", httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "遗失物品id", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value="/delete",produces = "application/json")
    @SystemLog(description = "删除遗失物品", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_YswpDelete(HttpServletRequest request,HttpServletResponse response)
    {
        String id = request.getParameter("ids");
        Result res = dtgjYswpService.delete(id);
        return res.toString();
    }
    
    @ApiOperation(value = "认领遗失物品", notes = "认领遗失物品", httpMethod = "POST")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "yswpRlr", value = "认领人", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "yswpRlrSfzh", value = "认领人身份证号", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value="/rl",produces = "application/json")
    @SystemLog(description = "认领遗失物品", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_YswpRl(HttpServletRequest request,HttpServletResponse response) throws ParseException
    {
        Dtgj_Yswp yswp = new Dtgj_Yswp();
        yswp.setRLR(request.getParameter("yswpRlr"));
        yswp.setRLRSFZH(request.getParameter("yswpRlrSfzh"));
        yswp.setZT("已认领");
        yswp.setID(request.getParameter("id"));
        Result res = dtgjYswpService.rl(yswp);
        return res.toString();
    }

}