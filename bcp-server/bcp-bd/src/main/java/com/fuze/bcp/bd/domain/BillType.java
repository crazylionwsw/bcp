package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 单据类型定义
 * Created by sean on 2016/10/20.
 */
@Document(collection="bd_billtype")
@Data
public class BillType extends BaseDataEntity {

    /**
     * 必须要有的资料类型列表 （编码)
     */
    private List<String> requiredImageTypeCodes = new ArrayList<String>();


    /**
     * 建议需要提交的资料列表（编码)
     */
    private List<String> suggestedImageTypeCodes = new ArrayList<String>();


    /**
     * 必须快递的资料列表 （编码)
     */
    private List<String> requiredExpImageTypeCodes = new ArrayList<String>();


    /**
     * 建议快递的资料列表（编码)
     */
    private List<String> suggestedExpImageTypeCodes = new ArrayList<String>();


    /**
     * 需要打印的文档类型
     */
    private List<String> documentIds;

    /**
     * 审批流程ID
     */
    private String activitiFlowID = null;

}
