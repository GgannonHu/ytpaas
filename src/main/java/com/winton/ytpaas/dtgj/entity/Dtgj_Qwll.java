package com.winton.ytpaas.dtgj.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Dtgj_Qwll {

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
    @JSONField(name = "GJGSMC")
    private String GJGSMC;
    @JSONField(name = "QWLL_XM")
    private String QWLL_XM;
    @JSONField(name = "QWLL_LXDH")
    private String QWLL_LXDH;
    @JSONField(name = "QWLL_JYBH")
    private String QWLL_JYBH;
    @JSONField(name = "QWLB_QWLBDM")
    private String QWLB_QWLBDM;
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