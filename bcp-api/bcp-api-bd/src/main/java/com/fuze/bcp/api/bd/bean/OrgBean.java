package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APITreeDataBean;
import lombok.Data;

/**
 * 组织结构定义
 */
@Data
@MongoEntity(entityName = "bd_orginfo")
public class OrgBean extends APITreeDataBean {

    /**
     * 是否虚拟组织
     */
    private Boolean virtual = false;

    /**
     * 部门领导
     */
    private String leaderId = null;

    /**
     * 部门所属的地区
     */
    private String provinceId = null;

    /**
     * 企业微信ID
     */
    private String wcqyId;


}
