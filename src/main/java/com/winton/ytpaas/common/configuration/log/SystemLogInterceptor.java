package com.winton.ytpaas.common.configuration.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.system.entity.Sys_User;
import com.winton.ytpaas.system.service.Sys_UserService;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SystemLogInterceptor implements HandlerInterceptor {
    @Autowired
    Sys_UserService sysUserService;
    @Autowired
    TokenService tokenService;

    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");
    
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        
        //线程绑定变量（该数据只有当前请求的线程可见）
        Date beginTime = new Date();
        beginTimeThreadLocal.set(beginTime);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, ModelAndView modelAndView) throws Exception {
        try {
            HandlerMethod handlerMethod = (HandlerMethod)object;
            Method method = handlerMethod.getMethod();

            Log log = new Log();

            Map<String, String[]> requestParams = new HashMap<String, String[]>(httpServletRequest.getParameterMap());
            String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
            Sys_User user = tokenService.verifyToken(token);
            
            String loginid = "";
            String username = "";
            String depno = "";
            String depname = "";
            if (user != null) {
                loginid = user.getLOGINNAME();
                username = user.getYHXM();
                depno = user.getDWDM();
                depname = user.getDWMC();
            }
            log.setLoginName(loginid);
            log.setYhxm(username);
            log.setDwbm(depno);
            log.setDwmc(depname);


            UserAgent userAgent = UserAgent.parseUserAgentString(httpServletRequest.getHeader("User-Agent"));
            String browser = userAgent.getBrowser().getName() + userAgent.getBrowser().getVersion(httpServletRequest.getHeader("User-Agent")).getVersion();
            String operatSystem = userAgent.getOperatingSystem().getName();
            log.setBrowser(browser);
            log.setOperatSystem(operatSystem);

            if (method.isAnnotationPresent(SystemLog.class)) {
                SystemLog systemLog = method.getAnnotation(SystemLog.class);
                String description = systemLog.description();
                String type = systemLog.type().toString();
                if(type.equals("LOGIN")) {
                    requestParams.put("password", new String[] {"******"});
                }

                //日志标题
                log.setDescription(description);
                //日志类型
                log.setLogType(type);
                //日志请求url
                log.setRequestUrl(httpServletRequest.getRequestURI());
                //请求方式
                log.setRequestType(httpServletRequest.getMethod());
                //请求参数
                log.setRequestParam(Tools.toJSONString(requestParams));

                //其他属性
                log.setIp(Tools.getRequestIP(httpServletRequest));
                log.setCreateTime(new Date());

                //.......
                //请求开始时间
                long beginTime = beginTimeThreadLocal.get().getTime();
                long endTime = System.currentTimeMillis();
                //请求耗时
                Long logElapsedTime = endTime - beginTime;
                log.setCostTime(logElapsedTime.intValue());

                SystemLogService logger = new SystemLogService(systemLog.type());
                logger.log(Tools.toJSONString(log));

            }
        } catch(ClassCastException ex) {
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        
    }
}