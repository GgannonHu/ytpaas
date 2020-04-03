package com.winton.ytpaas.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 通过WebClient GET/POST发送请求
 */
public class WebClientUtil {
    /**
     * GET/POST发送请求
     * @param urlInfo 请求url
     * @param charset 字符集：utf-8、gbk、gb2312等
     * @param postData 发送的数据
     * @return 发送请求后url的返回值
     */
    public static String sendMessage(String urlInfo, String charset, Map<String, String> postData) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlInfo);
            HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
            httpUrl.setRequestProperty("Content-Type", "text/plain; charset=" + charset);
            httpUrl.setInstanceFollowRedirects(false);

            if(null != postData) {
                OutputStream outputStream = httpUrl.getOutputStream();
                String outputStr = "";
                for(String key : postData.keySet()) {
                    outputStr += key + "=" + postData.get(key) + "&";
                }
                outputStr = outputStr.contains("&") ? outputStr.substring(0, outputStr.length() - 1) : outputStr;
                outputStream.write(outputStr.getBytes(charset));
                outputStream.close();
            }

            InputStream is = httpUrl.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

}