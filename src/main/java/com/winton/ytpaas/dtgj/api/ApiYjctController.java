package com.winton.ytpaas.dtgj.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.winton.ytpaas.dtgj.service.Dtgj_CtczService;
import com.winton.ytpaas.dtgj.service.Dtgj_FjxxService;
import com.winton.ytpaas.dtgj.service.Dtgj_YjctService;
import com.winton.ytpaas.system.entity.Sys_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/zhld/yjct")
@Api(value = "地铁公交_应急处突接口", description = "地铁公交_应急处突接口")
public class ApiYjctController {

    @Autowired
    Dtgj_YjctService dtgjYjctService;
    @Autowired
    TokenService tokenService;
    @Autowired
    Dtgj_CtczService dtgjCtczService;
    @Autowired
    Dtgj_FjxxService dtgjFjxxService;

    @ApiOperation(value = "获取列表", notes = "获取列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mc", value = "处突名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/list", produces = "application/json")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));
        String name = request.getParameter("name");

        JSONObject res = dtgjYjctService.getList(name, page, limit, iscon);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "获取列表数量", notes = "获取列表数量", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mc", value = "处突名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/listcon", produces = "application/json")
    @PassToken
    public String listcon(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        JSONObject res = dtgjYjctService.getListCon(name);
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
        Result res = dtgjYjctService.getItemById(tmpId);
        return res.toString();
    }

    @ApiOperation(value = "添加应急处突", notes = "添加应急处突", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "nr", value = "内容", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "地铁公交_添加应急处突", type = LogType.API)
    public String add(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);

        Map<String, Object> tmpItem = new HashMap<String, Object>();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        tmpItem.put("id", id);
        tmpItem.put("name", request.getParameter("name"));
        tmpItem.put("nr", request.getParameter("nr"));

        tmpItem.put("tjr", user.getLOGINNAME());
        tmpItem.put("tjdw", user.getDWDM());
        tmpItem.put("tjdwmc", user.getDWMC());
        
        Date date = new Date();
        tmpItem.put("tjsj", date);

        Result res = dtgjYjctService.add(tmpItem);
        res.setData(id);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "修改应急处突", notes = "修改应急处突", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "nr", value = "内容", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/update", produces = "application/json")
    @SystemLog(description = "地铁公交_修改应急处突", type = LogType.API)
    public String update(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> tmpItem = new HashMap<String, Object>();
        String id = request.getParameter("id");
        tmpItem.put("id", id);
        tmpItem.put("name", request.getParameter("name"));
        tmpItem.put("nr", request.getParameter("nr"));
        tmpItem.put("filedelid", request.getParameter("filedelid"));

        Result res = dtgjYjctService.update(tmpItem);

        res.setData(id);
        String retStr = Tools.toJSONString(res);

        return retStr;
    }

    @ApiOperation(value = "删除应急处突", notes = "删除应急处突", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要删除的主键ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/delete", produces = "application/json")
    @SystemLog(description = "地铁公交_删除应急处突", type = LogType.API)
    public String delete(HttpServletRequest request, HttpServletResponse response) {

        String tmpIds = request.getParameter("ids");

        Result res = dtgjYjctService.delete(tmpIds);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "下发", notes = "下发", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/xf", produces = "application/json")
    @SystemLog(description = "下发应急处突", type = LogType.SYSTEM_OPERATION)
    public String yjctxf(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        Result res = new Result();

        try {
            String urltop = request.getParameter("urltop");
            String tmpIds = request.getParameter("ids");

            List<Map<String, Object>> tmpData = new ArrayList<Map<String, Object>>();
            if (!tmpIds.equals("")) {
                JSONObject tmpDataJson = dtgjFjxxService.getListByIds(tmpIds);
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> tmpDataRet = (List<Map<String, Object>>) tmpDataJson.get("data");
                tmpData = tmpDataRet;
                for (Map<String, Object> item : tmpData) {
                    String tmpFileUrl = item.get("FJDZ").toString();
                    File tmpFile = new File(tmpFileUrl);

                    File f = new File("/YTPAASUPLOADS/" + urltop + id);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    String path = f.getAbsolutePath();
                    String fileName = tmpFile.getName(); // 将文件名重新命名（因为可能会存在相同名字的文件）
                    File tmpFileNew = new File(path, fileName); // 将二进制文件写到服务器磁盘上
                    Files.copy(tmpFile.toPath(), tmpFileNew.toPath());

                    Map<String, Object> tmpItem = new HashMap<String, Object>();
                    String tmpFileName = tmpFileNew.getName();
                    tmpItem.put("pid", id);
                    tmpItem.put("name", tmpFileName);
                    tmpItem.put("type", tmpFileName.substring(tmpFileName.lastIndexOf(".") + 1));
                    tmpItem.put("fjdz", tmpFileNew.getPath().replaceAll("\\\\", "/"));
                    tmpItem.put("fjdx", tmpFileNew.length());
                    res = dtgjFjxxService.add(tmpItem);
                    if (!res.getCode().equals("1")) {
                        break;
                    }
                }
            }
 //测试
            if (res.getCode().equals("1") || tmpData.size() == 0) {
                Map<String, Object> tmpItem = new HashMap<String, Object>();
                tmpItem.put("id", id);
                tmpItem.put("name", request.getParameter("ctczName"));
                tmpItem.put("nr", request.getParameter("ctczNr"));
                tmpItem.put("xfdw", request.getParameter("hdfJsdw"));
                tmpItem.put("xfdwmc", request.getParameter("hdfJsdwMc"));
                tmpItem.put("type", request.getParameter("hdfType"));
                tmpItem.put("fbr", request.getParameter("hdfUser"));
                tmpItem.put("fbdw", request.getParameter("hdfDwbm"));
                tmpItem.put("fbdwmc", request.getParameter("hdfDwbmMc"));

                res = dtgjCtczService.add(tmpItem);
                res.setData(id);
            }
        } catch (IllegalStateException e) {
            res.setCode("-1");
            res.setMsg("上传失败");
            e.printStackTrace();
        } catch (IOException e) {
            res.setCode("-1");
            res.setMsg("上传失败");
            e.printStackTrace();
        }

        String retStr = Tools.toJSONString(res);
        return retStr;
    }

}