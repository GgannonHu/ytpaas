package com.winton.ytpaas.common.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import org.springframework.web.multipart.MultipartFile;

public class Tools {

	/**
	 * 生成32位guid
	 * @return
	 */
	public static String get32GUID() {
		return java.util.UUID.randomUUID().toString().replaceAll("-", "");
	}
	/**
	 * 生成36位guid
	 * @return
	 */
	public static String get36GUID() {
		return java.util.UUID.randomUUID().toString();
	}
	/**
	 * Date转str
	 * 
	 * @param date
	 * @return dateStr
	 */
	public static String getDateString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = null;
		if (date != null)
			dateStr = sdf.format(date);
		return dateStr;
	}

	/**
	 * Datetime转str
	 * 
	 * @param date
	 * @return dateTime
	 */
	public static String getDateTimeString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = sdf.format(date);
		return dateTime;
	}


	/**
	 * str转Date
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date getStringToDate(String strDate, String formate) {
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		Date date = new Date();
		try {
			if (strDate != null && !"".equals(strDate))
				date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取客户端IP
	 * 
	 * @param HttpServletRequest
	 * @return
	 */
	public static String getRequestIP(javax.servlet.http.HttpServletRequest request) {
		try {
			String ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (ipAddress != null && ipAddress.length() > 15) {
				// "***.***.***.***".length() = 15
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
			if("0:0:0:0:0:0:0:1".equals(ipAddress)){
				ipAddress = "127.0.0.1";
			}
			return ipAddress;
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				if (!Character.isDigit(str.charAt(i))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 获取所有的cookie
	 */
	public static  Map<String,Cookie> ReadCookieMap(HttpServletRequest request) {   
       Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();  
       Cookie[] cookies = request.getCookies();  
       if(null!=cookies){  
           for(Cookie cookie : cookies){  
               cookieMap.put(cookie.getName(), cookie);  
           }  
       }  
       return cookieMap;  
	}
	/**
	 * 根据key获取cookie
	 * @param request
	 * @param name
	 * @return
	 */
	public static  String getCookieByName(HttpServletRequest request,String name){  
        Map<String,Cookie> cookieMap = ReadCookieMap(request);  
        if(cookieMap.containsKey(name)){  
			Cookie cookie = (Cookie)cookieMap.get(name);  
			String res = cookie.getValue();
			try {
				res = URLDecoder.decode(res.trim(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				res = "";
			}
            return res;  
        }else{  
            return "";  
        }    
    }
	/**
	 * 删除cookie
	 * @param request
	 * @param response
	 * @param name
	 */
	public static void delCookie(HttpServletRequest request,HttpServletResponse response,String name){  
        Cookie[] cookies = request.getCookies();  
        if (null==cookies) {  
            
        } else {  
            for(Cookie cookie : cookies){  
                if(cookie.getName().equals(name)){  
                    cookie.setValue(null);  
                    cookie.setMaxAge(0);// 立即销毁cookie  
                    cookie.setPath("/");
                    response.addCookie(cookie);  
                    break;  
                }  
            }  
        }  
    }
	/**
	 * 修改cookie
	 * @param request
	 * @param response
	 * @param name cookie名称
	 * @param value cookie值
	 * @param times cookie存活时间（单位：分钟）
	 */
	 public static void editCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int times){  
         Cookie[] cookies = request.getCookies();  
         if (null==cookies) {  
             
         } else {  
             for(Cookie cookie : cookies){  
                 if(cookie.getName().equals(name)){  
                     cookie.setValue(value);  
                     cookie.setPath("/");  
                     cookie.setMaxAge(times * 60);
                     response.addCookie(cookie);  
                     break;  
                 }  
             }  
         }  
            
     }  
	 
	 /**
	  * 添加cookie
	  * @param response
	 * @param name cookie名称
	 * @param value cookie值
	 * @param times cookie存活时间（单位：分钟）
	  */
	 public static void addCookie(HttpServletResponse response, String name, String value, int times){  
		try {
			value = URLEncoder.encode(value.trim(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			
		}
         Cookie cookie = new Cookie(name.trim(), value);  
         cookie.setMaxAge(times * 60);
         cookie.setPath("/");  
         response.addCookie(cookie);  
	}
	
	/**
	 * 将spring的MultipartFile转换为java的File，并保存到项目所在磁盘的跟磁盘S9UPLOADS文件夹下
	 * @param mfile MultipartFile
	 * @return File
	 * @throws IOException
	 */
	public static File multipartFile2File(MultipartFile mfile) throws IOException {
		File f=new File("/YTPAASUPLOADS/" + UUID.randomUUID().toString().replaceAll("-",""));
		if (!f.exists())
			f.mkdirs();
		String path = f.getAbsolutePath();
		// 将文件名重新命名（因为可能会存在相同名字的文件）
		String fileName = mfile.getOriginalFilename();
		// 将二进制文件写到服务器磁盘上
		File file = new File(path, fileName);
		mfile.transferTo(file);

		return file;
	}

	/**
	 * 转换为json字符串（已校验非空以及格式）
	 * @param obj
	 * @return
	 */
	public static String toJSONString(Object obj){
		SerializeConfig config = new SerializeConfig();
		config.put(Long.class, ToStringSerializer.instance);
		
		return JSONObject.toJSONString(obj, config,
				SerializerFeature.DisableCircularReferenceDetect, 
				SerializerFeature.WriteDateUseDateFormat, 
				SerializerFeature.WriteMapNullValue);
	}
}