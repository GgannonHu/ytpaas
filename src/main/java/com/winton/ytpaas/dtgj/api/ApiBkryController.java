package com.winton.ytpaas.dtgj.api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Base64;
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
        tmpSelTj.put("xm", request.getParameter("xm"));
        tmpSelTj.put("sfzh", request.getParameter("sfzh"));

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

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
        // HH:mm:ss");sdf.parse("");

        String tmpId = UUID.randomUUID().toString().replaceAll("-", "");
        String tmpPath = "";
        if (request.getParameter("isimg").equals("1")) {
            tmpPath = uploadImg(tmpId, request.getParameter("imgurl"), request.getParameter("imgname"));
        }
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        tmpItem.put("tjr", user.getLOGINNAME());
        tmpItem.put("tjdw", user.getDWDM());
        tmpItem.put("tjdwmc", user.getDWMC());

        tmpItem.put("id", request.getParameter(tmpId));
        tmpItem.put("name", request.getParameter("name"));
        tmpItem.put("idcard", request.getParameter("idcard"));
        tmpItem.put("bknr", request.getParameter("bknr"));
        tmpItem.put("picture", tmpPath);

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

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
        // HH:mm:ss");sdf.parse("");

        String picture = request.getParameter("picture");
        if (request.getParameter("isimg").equals("1")) {
            picture = uploadImg(request.getParameter("id"), request.getParameter("imgurl"),
                    request.getParameter("imgname"));
        }

        Map<String, Object> tmpItem = new HashMap<String, Object>();
        tmpItem.put("id", request.getParameter("id"));
        tmpItem.put("name", request.getParameter("name"));
        tmpItem.put("idcard", request.getParameter("idcard"));
        tmpItem.put("bknr", request.getParameter("bknr"));
        tmpItem.put("picture", picture);

        Result res = service.update(tmpItem);
        String retStr = Tools.toJSONString(res);

        return retStr;
    }

    private String uploadImg(String varId, String varBase64, String varFileName) {
        String tmpRet = "";
        if (varBase64.length() > 0) {
            String id = varId;
            String base64 = varBase64.substring(varBase64.indexOf(","));
            String fileName = varFileName;
            File file = null;
            // 创建文件目录
            String filePath = "/YTPAASUPLOADS/DTGJ/BKRY/" + id;
            File dir = new File(filePath);
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
            }
            BufferedOutputStream bos = null;
            java.io.FileOutputStream fos = null;
            try {
                byte[] bytes = Base64.getMimeDecoder().decode(base64);
                file = new File(dir.getAbsolutePath(), fileName);
                fos = new java.io.FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(bytes);
                tmpRet = file.getPath().replaceAll("\\\\", "/");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return tmpRet;
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

    @ApiOperation(value = "根据id获取信息", notes = "根据id获取信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sfzh", value = "主键ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/getitembysfzh", produces = "application/json")
    public String getItemBySfzh(HttpServletRequest request, HttpServletResponse response) {
        String tmpSfzh = request.getParameter("sfzh");
        Result res = service.getItemBySfzh(tmpSfzh);
        return res.toString();
    }
}