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
    public String yswpList(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));

        String zt = request.getParameter("zt");
        String mc = request.getParameter("mc");
        String ms = request.getParameter("ms");
        String sqdd = request.getParameter("sqdd");
        String sqsjS = request.getParameter("sqsjS");
        String sqsjE = request.getParameter("sqsjE");
        JSONObject res = dtgjService.getList(zt,mc,ms,sqdd,sqsjS,sqsjE, page, limit, iscon);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "获取关键岗位从业人员列表总数", notes = "获取关键岗位从业人员列表总数", httpMethod = "GET")
    @RequestMapping(value = "/listcon", produces = "application/json")
    public String yswpcon(HttpServletRequest request, HttpServletResponse response) {
        String zt = request.getParameter("zt");
        String mc = request.getParameter("mc");
        String ms = request.getParameter("ms");
        String sqdd = request.getParameter("sqdd");
        String sqsjS = request.getParameter("sqsjS");
        String sqsjE = request.getParameter("sqsjE");
        JSONObject res = dtgjService.getListcon(zt,mc,ms,sqdd,sqsjS,sqsjE);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }
    
    @ApiOperation(value = "根据id获取关键岗位从业人员列表", notes = "根据id获取关键岗位从业人员列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "关键岗位从业人员id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getById", produces = "application/json")
    public String getYswpById(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Result res = dtgjService.getById(id);
        return res.toString();
    }

    @ApiOperation(value = "添加关键岗位从业人员", notes = "添加关键岗位从业人员", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", value = "关键岗位从业人员名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "yswpName", value = "昵称", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加关键岗位从业人员", type = LogType.SYSTEM_OPERATION)
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
        yswp.setTJR(request.getParameter("hdfUser"));
        yswp.setMC(request.getParameter("yswpName"));
        yswp.setMS(request.getParameter("yswpWpms"));
        yswp.setSQDD(request.getParameter("yswpSqdd"));
        yswp.setZT("招领中");
        yswp.setSQSJ(date);
        
        Result res = dtgjService.add(yswp);
        res.setData(id);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "修改关键岗位从业人员", notes = "修改关键岗位从业人员", httpMethod = "POST")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "yswpName", value = "昵称", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "id", value = "关键岗位从业人员id", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value="/update",produces = "application/json")
    @SystemLog(description = "修改关键岗位从业人员", type = LogType.SYSTEM_OPERATION)
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
        
        Result res = dtgjService.update(yswp);
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
    public String Dtgj_YswpDelete(HttpServletRequest request,HttpServletResponse response)
    {
        String id = request.getParameter("ids");
        Result res = dtgjService.delete(id);
        return res.toString();
    }
    
}