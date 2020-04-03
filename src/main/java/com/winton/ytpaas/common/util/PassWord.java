package com.winton.ytpaas.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


public class PassWord {

    private static String mstr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * 对称加密
     * @param value
     * @return
     */
    public static String EnString(String value) {
        if (value == null || value.equals("")) {
            return "";
        }

        byte[] buff;
        StringBuilder sb = new StringBuilder();
        try {
            buff = value.getBytes("gbk");
            int[] n = new int[buff.length];

            int j, k, m;
            int len = mstr.length();
            Random r = new Random();

            for (int i = 0; i < buff.length; i++) {
                n[i] = (int)byteToInteger(buff[i]);
                j = r.nextInt(6);
                n[i] = ((int) n[i] ^ j);
                k = (int) n[i] % len;
                m = (int) n[i] / len;
                m = m * 8 + j;

                sb.append(mstr.substring(k, k + 1) + mstr.substring(m, m + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 对称解密
     * @param value
     * @return
     */
    public static String DeString(String value) {
        if (value == null || value.equals("")) {
            return "";
        }

        try {
            int j, k, m, n = 0;
            int len = mstr.length();
            byte[] buff = new byte[value.length() / 2];

            for (int i = 0; i < value.length(); i += 2) {
                k = mstr.indexOf(value.substring(i, i + 1));
                m = mstr.indexOf(value.substring(i + 1, i + 2));
                j = m / 8;
                m = m - j * 8;
                buff[n] = (byte) (j * len + k);
                buff[n] = (byte) ((int) buff[n] ^ m);
                n++;
            }

            return new String(buff, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * md5加密
     * @param value
     * @return
     */
    public static String md5(String value) {
        return instance(value, "md5");
    }
    /**
     * sha1加密
     * @param value
     * @return
     */
    public static String sha1(String value) {
        return instance(value, "sha-1");
    }

    private static Integer byteToInteger(Byte b) {
        return 0xff & b;
    }
    private static String instance(String str, String ins) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(ins);
			md.update(str.getBytes());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return new BigInteger(1, md.digest()).toString(16);
    }
    
}