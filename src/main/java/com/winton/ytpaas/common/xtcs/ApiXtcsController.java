package com.winton.ytpaas.common.xtcs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winton.ytpaas.common.configuration.jwt.PassToken;
import com.winton.ytpaas.common.util.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/common")
public class ApiXtcsController {

    @Autowired
    XtcsService stcsService;
    
    @ApiOperation(value = "获取绑定常数", notes = "获取绑定常数", httpMethod = "GET")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "tid", value = "参数类别", dataType = "String", paramType = "query", required = true)
    })
    @PassToken
    @RequestMapping(value = "/xtcs", produces = "application/json")
    public String xtcs(HttpServletRequest request, HttpServletResponse response) {
        String tid = request.getParameter("tid");
        Result res = stcsService.bindCon(tid);
        return res.toString();
    }
    
}