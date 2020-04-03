package com.winton.ytpaas.system.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Sys_JG {

	@JSONField(name = "JGDM")
    private String JGDM;
	@JSONField(name = "JGMC")
    private String JGMC;
	@JSONField(name = "JGMCPY")
    private String JGMCPY;
	@JSONField(name = "JGLBDM")
    private String JGLBDM;
	@JSONField(name = "JGLBMC")
    private String JGLBMC;
	@JSONField(name = "ZXBS")
    private String ZXBS;
	@JSONField(name = "WZPX")
    private Integer WZPX;
	@JSONField(name = "CJSJ")
    private Date CJSJ;
	@JSONField(name = "YSJCJSJ")
    private Date YSJCJSJ;
	@JSONField(name = "CJYH")
    private String CJYH;
	@JSONField(name = "XGSJ")
    private Date XGSJ;
	@JSONField(name = "ZHDCSJ")
    private Date ZHDCSJ;
	@JSONField(name = "SJDM")
    private String SJDM;
	@JSONField(name = "JGJB")
    private String JGJB;
	@JSONField(name = "XZQH")
    private String XZQH;
	@JSONField(name = "X")
    private String X;
	@JSONField(name = "Y")
    private String Y;
	@JSONField(name = "YLZD1")
    private String YLZD1;
	@JSONField(name = "YLZD2")
    private String YLZD2;
	@JSONField(name = "YLZD3")
    private String YLZD3;
	@JSONField(name = "YLZD4")
    private String YLZD4;
	@JSONField(name = "YLZD5")
    private String YLZD5;
	@JSONField(name = "QXFL")
    private Integer QXFL;

}