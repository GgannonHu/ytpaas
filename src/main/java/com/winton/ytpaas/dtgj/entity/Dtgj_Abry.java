package com.winton.ytpaas.dtgj.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Dtgj_Abry {

    @JSONField(name = "ID")
    private String ID;
    @JSONField(name = "GAJGJGDM")
    private String GAJGJGDM;
    @JSONField(name = "DTZDBM")
    private String DTZDBM;
    @JSONField(name = "DTZDMC")
    private String DTZDMC;
    @JSONField(name = "GJXLBM")
    private String GJXLBM;
    @JSONField(name = "ABRY_GMSFHM")
    private String ABRY_GMSFHM;
    @JSONField(name = "ABRY_XM")
    private String ABRY_XM;
    @JSONField(name = "ABRY_YDDH")
    private String ABRY_YDDH;
    @JSONField(name = "ABRY_ZZMMDM")
    private String ABRY_ZZMMDM;
    @JSONField(name = "ABRY_MZDM")
    private String ABRY_MZDM;
    @JSONField(name = "ABRY_DZMC")
    private String ABRY_DZMC;
    @JSONField(name = "ABRY_DWMC")
    private String ABRY_DWMC;
    @JSONField(name = "CREATE_TIME")
    private Date CREATE_TIME;
    @JSONField(name = "XGSJ")
    private Date XGSJ;
    @JSONField(name = "ZXSJ")
    private Date ZXSJ;
    @JSONField(name = "OPER_REASON")
    private String OPER_REASON;
    @JSONField(name = "TJR")
    private String TJR;
    @JSONField(name = "TJDW")
    private String TJDW;
    @JSONField(name = "TJDWMC")
    private String TJDWMC;
}