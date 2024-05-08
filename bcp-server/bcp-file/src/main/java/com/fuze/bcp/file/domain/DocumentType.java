package com.fuze.bcp.file.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 文档（包括合同、收入证明等）
 * Created by JZ on 2017/02/17
 */
@Data
@Document(collection="bd_documenttype")
public class DocumentType extends BaseDataEntity {

    /**
     * 关联的业务类型
     */
    //private BusinessType businessType;

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
     * 模板对象（TemplateObject）的ID
     */
    private String templateObjectId;

    private String fileType;


    /**
     * 关联的档案资料
     */
    private List<String> imageTypeCodes;



}
