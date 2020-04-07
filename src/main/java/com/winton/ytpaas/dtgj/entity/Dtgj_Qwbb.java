package com.winton.ytpaas.dtgj.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Dtgj_Qwbb {

    @JSONField(name = "ID")
    private String ID;
    @JSONField(name = "XM")
    private String XM;
    @JSONField(name = "SFZH")
    private String SFZH;
    @JSONField(name = "QWNR")
    private String QWNR;
    @JSONField(name = "FBSJ")
    private Date FBSJ;
    @JSONField(name = "WCSJ")
    private Date WCSJ;
    @JSONField(name = "WCZT")
    private String WCZT;
    @JSONField(name = "TJR")
    private String TJR;
    @JSONField(name = "TJDW")
    private String TJDW;
    @JSONField(name = "TJSJ")
    private Date TJSJ;
    @JSONField(name = "TJDWMC")
    private String TJDWMC;
}