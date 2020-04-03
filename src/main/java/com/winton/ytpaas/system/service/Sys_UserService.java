package com.winton.ytpaas.system.service;

import java.util.Date;
import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.configuration.log.LogType;
import com.winton.ytpaas.common.configuration.log.SystemLogService;
import com.winton.ytpaas.common.util.PassWord;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.system.dao.Sys_UserDao;
import com.winton.ytpaas.system.entity.Sys_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysUserService")
public class Sys_UserService {
    @Autowired
    TokenService tokenService;
    @Autowired
    Sys_UserDao dao;
     

    public Result getById(String yhbm) {
        Result res = new Result();
        res.setCode("1");
        res.setData(dao.getById(yhbm));
        return res;
    }

    public Result getAll() {
        Result res = new Result();
        res.setCode("1");
        res.setData(dao.getAll());
        return res;
    }
    public Result getAll(JSONObject where, int page, int limit) {
    	String begin = String.valueOf((page - 1) * limit + 1);
        String end = String.valueOf(page * limit);
        
        Result res = new Result();
        res.setCode("1");
        res.setData(dao.getAll(where, begin, end));
        return res;
    }
    public Result getAllCount(JSONObject where) {
    	Result res = new Result();
        res.setCode("1");
        res.setData(dao.getAllCount(where));
        return res;
    }


    
    public Result login(String loginName, String password) {
        Result res = new Result();
        Sys_User temp = dao.getByLoginName(loginName);
        
        JSONObject logJson = new JSONObject();
        logJson.put("loginName", loginName);
        logJson.put("createTime", Tools.getDateTimeString(new Date()));

        if(temp == null) {
            res.setCode("0");
            res.setMsg("登录失败，用户不存在！");
            logJson.put("res", "登录失败，用户不存在！");
        } else {
            password = PassWord.md5(password);
            if(temp.getPASSWORD().equals(password)) {
                String token = tokenService.getToken(temp);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", token);

                res.setCode("1");
                res.setMsg("登录成功！");
                res.setData(jsonObject);
                logJson.put("res", "登录成功！");
            } else {
                res.setCode("0");
                res.setMsg("登录失败，密码错误！");
                logJson.put("res", "登录失败，密码错误！");
            }
        }
        
        new SystemLogService(LogType.LOGIN).log(Tools.toJSONString(logJson));
        return res;
    }

    public Result add(Sys_User item) {
        Result res = new Result();
        Sys_User temp = dao.getByLoginName(item.getLOGINNAME());

        if(temp != null) {
            res.setCode("0");
            res.setMsg(item.getLOGINNAME() + "已存在，不能重复添加");
        } else {
            boolean b = dao.add(item);
            if(b) {
                res.setCode("1");
                res.setMsg(item.getLOGINNAME() + "添加成功");
            } else {
                res.setCode("-1");
                res.setMsg(item.getLOGINNAME() + "添加失败");
            }
        }
       
        return res;
    }

    public Result updatePassword(String yhbm, String password) {
        
        Result res = new Result();
        boolean b = dao.updatePassword(yhbm, PassWord.md5(password));
        if(b) {
            res.setCode("1");
            res.setMsg("密码修改成功");
        } else {
            res.setCode("-1");
            res.setMsg("密码修改失败");
        }
       
        return res;
    }
    public Result updatePassword(String yhbm, String password, String newPassword) {
        
        Result res = new Result();
        Sys_User user = dao.getById(yhbm);
        if(!PassWord.md5(password).equals(user.getPASSWORD())) {
            res.setCode("-1");
            res.setMsg("原密码输入错误");
            return res;
        }
        boolean b = dao.updatePassword(yhbm, PassWord.md5(newPassword));
        if(b) {
            res.setCode("1");
            res.setMsg("密码修改成功");
        } else {
            res.setCode("-1");
            res.setMsg("密码修改失败");
        }
       
        return res;
    }

    public Result update(Sys_User item) {
        Result res = new Result();
        boolean b = dao.update(item);
        if(b) {
            res.setCode("1");
            res.setMsg(item.getYHXM() + "修改成功");
        } else {
            res.setCode("-1");
            res.setMsg(item.getYHXM() + "修改失败");
        }
       
        return res;
    }
    
    public Result delete(String id) {
        Result res = new Result();
        boolean b = dao.delete(id);
        if(b) {
            res.setCode("1");
            res.setMsg("删除成功");
        } else {
            res.setCode("-1");
            res.setMsg("删除失败");
        }
       
        return res;
    }


    


}