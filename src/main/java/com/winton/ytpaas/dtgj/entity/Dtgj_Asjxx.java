package com.winton.ytpaas.dtgj.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Dtgj_Asjxx {
    @JSONField(name="ID")
    private String ID; 
    @JSONField(name="GAJGJGDM")
    private String GAJGJGDM;  //gajgjgdm    公安机关机构代码
    @JSONField(name="DTZDBM")
    private String DTZDBM;//  dtzdbm      地铁站点编码
    @JSONField(name="DTZDMC")
    private String DTZDMC;//  dtzdmc      地铁站点名称
    @JSONField(name="GJXLBM")
    private String GJXLBM;//  gjxlbm      公交线路编码
    @JSONField(name="GJGSMC")
    private String GJGSMC;//  gjgsmc      公交公司名称
    @JSONField(name="AJBH")
    private String AJBH;//  ajbh        案件编号
    @JSONField(name="AJLB")
    private String AJLB;//  ajlb        案件类别
    @JSONField(name="AJMC")
    private String AJMC;//  ajmc        案件名称
    @JSONField(name="JYAQ")
    private String JYAQ;//  jyaq        简要案情
    @JSONField(name="ASJFSKSSJ")
    private Date ASJFSKSSJ;//  asjfskssj   案事件发生开始时间
    @JSONField(name="AFDD")
    private String AFDD;//  afdd        案发地点
    @JSONField(name="SFCS")
    private String SFCS;//  sfcs        事发场所
    @JSONField(name="SSXQ")
    private String SSXQ;//  ssxq        添加人 
    @JSONField(name="TJR")
    private String TJR;//  ssxq        添加人 
    @JSONField(name="TJDW")
    private String TJDW;//  ssxq        添加人单位 
    @JSONField(name="TJDWMC")
    private String TJDWMC;//  ssxq        添加人单位名称  
    @JSONField(name="DQUSER")
    private String DQUSER;//  ssxq        添加人单位名称 
}