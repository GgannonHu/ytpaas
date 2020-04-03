package com.winton.ytpaas.dtgj.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Dtgj_Jqxx {
    @JSONField(name="ID")
    private String ID;  
    @JSONField(name="GAJGJGDM")
    private String GAJGJGDM;//  公安机关机构代码 
    @JSONField(name="DTZDDM")
    private String DTZDDM;//  地铁站点代码 
    @JSONField(name="DTZDMC")
    private String DTZDMC;// 地铁站点名称 
    @JSONField(name="GJXLDM")
    private String GJXLDM;// 公交线路代码 
    @JSONField(name="JJBH")
    private String JJBH;// 接警编号 
    @JSONField(name="BJFSDM")
    private String BJFSDM;// 报警方式代码 
    @JSONField(name="JQLBDM")
    private String JQLBDM;// 警情类别代码 
    @JSONField(name="BJR_XM")
    private String BJR_XM;// 姓名 
    @JSONField(name="BJR_XBDM")
    private String BJR_XBDM;// 性别代码 
    @JSONField(name="BJR_LXDH")
    private String BJR_LXDH;// 联系电话 
    @JSONField(name="BJDH")
    private String BJDH;// 报警电话 
    @JSONField(name="BJSJ")
    private Date BJSJ;// 报警时间 
    @JSONField(name="JYJQ")
    private String JYJQ;// 简要警情 
    @JSONField(name="TJR")
    private String TJR;//  ssxq        添加人 
    @JSONField(name="TJDW")
    private String TJDW;//  ssxq        添加人单位 
    @JSONField(name="TJDWMC")
    private String TJDWMC;//  ssxq        添加人单位名称  
    @JSONField(name="DQUSER")
    private String DQUSER;//  ssxq        添加人单位名称 
}