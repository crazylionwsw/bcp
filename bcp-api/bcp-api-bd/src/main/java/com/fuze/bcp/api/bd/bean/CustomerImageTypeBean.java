package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户资料类型
 */
@Data
@MongoEntity(entityName = "bd_customerimagetype")
public class CustomerImageTypeBean extends APIBaseDataBean {

    /**
     * 分组标签
     */
    private String groupLabel = null;

    /**
     * 是否进行图片解析
     */
    private Boolean resolved = false;

    /**
     * 是否需要合成
     */
    private Boolean merged = false;

    /**
     * 合成模板
     */
    private String mergeTemplateId = null;

    /**
     * 是否需要生成
     */
    private Boolean generated = false;

    /**
     * 生成模板
     */
    private String generateTemplateId = null;

    /**
     * 是否可以追加档案文件
     */
    private Boolean canAppend = false;

    /**
     * 档案支持的文件后缀名
     */
    private List<String> suffixes = new ArrayList<String>();

    /**
     * 样本的文件ID
     */
    private List<String> exampleFileIds = new ArrayList<String>();

    /**
     * 角色权限
     */
    private List<RoleControl> roleControls = new ArrayList<RoleControl>();

}
