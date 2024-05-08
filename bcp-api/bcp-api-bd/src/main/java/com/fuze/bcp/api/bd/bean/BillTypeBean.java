package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 单据类型定义
 * Created by sean on 2016/10/20.
 */
@Data
@MongoEntity(entityName = "bd_billtype")
public class BillTypeBean extends APIBaseDataBean {

    /**
     * 必须要有的资料类型列表
     */
    @NotNull
    private List<String> requiredImageTypeCodes;


    /**
     * 建议需要提交的资料列表
     */
    private List<String> suggestedImageTypeCodes;

    /**
     * 必须快递的资料列表 （编码)
     */
    private List<String> requiredExpImageTypeCodes;

    /**
     * 建议快递的资料列表（编码)
     */
    private List<String> suggestedExpImageTypeCodes;

    /**
     * 需要打印的文档类型
     */
    private List<String> documentIds;

    /**
     * 审批流程ID
     */
    private String activitiFlowID = null;



}
