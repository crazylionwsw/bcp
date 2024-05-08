package com.fuze.bcp.api.file.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.List;

/**
 * 文档类型（包括合同、收入证明等）
 */
@Data
@MongoEntity(entityName = "bd_documenttype")
public class DocumentTypeBean extends APIBaseDataBean {

    /**
     *  资金提供访
     */
    public static final Integer CONTRACTING_PARTY_CASHSOURCE  = 0;

    /**
     * 客户
     */
    public static final Integer CONTRACTING_PARTY_CUSTOMER = 1;

    /**
     * 渠道
     */
    public static final Integer CONTRACTING_PARTY_CARDEALER = 2;

    /**
     * 富择
     */
    public static final Integer CONTRACTING_PARTY_FUZE = 3;

    /**
     * 合同签订方
     */
    private List<Integer> contractingParties;

    /**
     * 合同编号规则
     */
    private String codeRule;

    /**
     *      模板对象（TemplateObject）的ID
     */
    private String templateObjectId;
    private String fileType;

    /**
     * 关联的档案资料
     */
    private List<String> imageTypeCodes;

}
