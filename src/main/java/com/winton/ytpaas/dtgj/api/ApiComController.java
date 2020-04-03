package com.winton.ytpaas.dtgj.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.dtgj.service.Dtgj_ComService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/com")
@Api(value = "通用接口", description = "通用接口")
public class ApiComController {

    @Autowired
    Dtgj_ComService dtgjComService;
    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "获取单位列表", notes = "获取单位列表", httpMethod = "GET")
    @RequestMapping(value = "/jg", produces = "application/json")
    public String getJg(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        Result res = dtgjComService.getJg(pid);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "获取单位列表(不包含省厅)", notes = "获取单位列表(不包含省厅)", httpMethod = "GET")
    @RequestMapping(value = "/jg1", produces = "application/json")
    public String getJg1(HttpServletRequest request, HttpServletResponse response) {
        JSONObject res = dtgjComService.getJg();
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "获取单位列表", notes = "获取单位列表", httpMethod = "GET")
    @RequestMapping(value = "/jglist", produces = "application/json")
    public String getJgByPid(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        JSONObject res = dtgjComService.getJgByPid(pid);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "获取绑定常数", notes = "获取绑定常数", httpMethod = "GET")
    @RequestMapping(value = "/bindcon", produces = "application/json")
    public String bindcon(HttpServletRequest request, HttpServletResponse response) {
        String tid = request.getParameter("tid");
        JSONObject res = dtgjComService.bindCon(tid);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }
}