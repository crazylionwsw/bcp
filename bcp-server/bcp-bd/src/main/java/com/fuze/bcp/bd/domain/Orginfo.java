package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.TreeDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
/**
 * 组织结构定义
 * Created by sean on 16/10/10.
 */
//@Document(collection = "bd_orginfo_wx")
@Document(collection = "bd_orginfo")
@Data
public class Orginfo extends TreeDataEntity {

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
