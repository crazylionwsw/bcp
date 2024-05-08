package com.fuze.bcp.transaction.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.fuze.bcp.api.transaction.bean.CustomerTransactionBean.TRANSACTION_INIT;


/**
 * 客户交易
  */
@Document(collection = "cus_transaction")
@Data
public class CustomerTransaction extends MongoBaseEntity {

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 业务员
     */
    private String employeeId;

    /**
     * 用户ID
     */
    private String loginUserId;

    /**
     * 业务部门
     */
    private String orginfoId;

    /**
     * 来源经销商
     */
    private String carDealerId;

    /**
     *  渠道ID
     */
    private String channelId = null;

    /**
     * 报单行
     */
    private String cashSourceId;

    /**
     * 业务类型 (新车/二手车）
     */
    private String businessTypeCode;

    /**
     * 交易状态
     */
    private Integer status = TRANSACTION_INIT;

    /**
     * 所在阶段（每个阶段都有对应的单据，此处使用单据类型编码）
     */
    private String billTypeCode;

    /**
    * 客户的档案编号
    */
    private String fileNumber = null;

    /**
     * 是否为直客模式
     * 1：是  0：否
     */
    private int isStraight = 0;

}
