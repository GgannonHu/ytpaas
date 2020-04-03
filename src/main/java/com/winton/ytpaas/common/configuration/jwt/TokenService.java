package com.winton.ytpaas.common.configuration.jwt;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.winton.ytpaas.common.util.PassWord;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.system.entity.Sys_User;
import com.winton.ytpaas.system.service.Sys_UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tokenService")
public class TokenService {
    @Autowired
    Sys_UserService sysUserService;

    /**
     * 返回一定时间后的日期
     * 
     * @param date   开始计时的时间
     * @param year   增加的年
     * @param month  增加的月
     * @param day    增加的日
     * @param hour   增加的小时
     * @param minute 增加的分钟
     * @param second 增加的秒
     * @return
     */
    public Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = new GregorianCalendar();

        cal.setTime(date);
        if (year != 0) {
            cal.add(Calendar.YEAR, year);
        }
        if (month != 0) {
            cal.add(Calendar.MONTH, month);
        }
        if (day != 0) {
            cal.add(Calendar.DATE, day);
        }
        if (hour != 0) {
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != 0) {
            cal.add(Calendar.MINUTE, minute);
        }
        if (second != 0) {
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }

    /**
     * 根据用户信息生成token
     * 
     * @param user
     * @return
     */
    public String getToken(Sys_User user) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        Date nowDate = new Date();
        Date expireDate = getAfterDate(nowDate, 0, 0, 0, 3, 0, 0); //token有效期3小时

        String userJson = Tools.toJSONString(user);
        // String userJsonBase64 = Base64.getEncoder().encodeToString(userJson.getBytes());
        String userJsonBase64 = PassWord.EnString(userJson);

        Algorithm algorithm = Algorithm.HMAC256("winton@2019+===");

        String token = JWT.create().withHeader(map).withClaim("USER", userJsonBase64).withIssuer("WINTON")
                .withSubject("LOGIN_TOKEN").withAudience("APP", "AJAX").withIssuedAt(nowDate).withExpiresAt(expireDate)
                .sign(algorithm);
        return token;
    }

    /**
     * 验证token
     * 
     * @param token
     * @return
     */
    public Sys_User verifyToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256("winton@2019+===");
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("WINTON").build();
        
        Map<String, Claim> claims = new HashMap<String, Claim>();
        Sys_User user = new Sys_User();
        
        try {
            DecodedJWT jwt = verifier.verify(token);
            claims = jwt.getClaims();
        } catch (Exception e) {
            return null;
        }

        Claim claim = claims.get("USER");
        String userJson = claim.asString();
        try {
            // userJson = new String(Base64.getDecoder().decode(userJson), "UTF-8");
            userJson = PassWord.DeString(userJson);
            JSONObject obj = JSONObject.parseObject(userJson);

            user.setYHBM(obj.getString("YHBM"));
            user.setLOGINNAME(obj.getString("LOGINNAME"));
            user.setPASSWORD(obj.getString("PASSWORD"));
            user.setYHXM(obj.getString("YHXM"));
            user.setYHSFZH(obj.getString("YHSFZH"));
            user.setYHLXDH(obj.getString("YHLXDH"));
            user.setYHJH(obj.getString("YHJH"));
            user.setDWDM(obj.getString("DWDM"));
            user.setDWMC(obj.getString("DWMC"));
            user.setJSBM(obj.getString("JSBM"));
        } catch (Exception e) {
            return null;
        }
        return user;
    }
}