package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by wsw on 2017/4/13.
 * 业务转移单
 * 发生业务转移时需要填写单据才可以生效，不可以改变原有的业务单据数据。
 */
@Document(collection = "so_transferbill")
@Data
public class TransferBill extends BaseBillEntity {

    /**
     * 在生效
     */
    private Integer STATUS_ONGOING = 1;

    /**
     * 已失效
     */
    private Integer STATUS_INVALID = 0;



    /**
     * 原始单据ID
     */
    private String sourceBillId = null;

    /**
     * 单据类型ID
     */
    private String billTypeId = null;

    /**
     * 原业务人员
     */
    private String fromEmployeeId = null;

    /**
     * 接手业务人员
     */
    private String toEmployeeId = null;

    /**
     * 有效期
     */
    private String expiredDate = null;

    /**
     * 转移单生效状态
     */
    private Integer status = STATUS_ONGOING;
    /**
     * 转移原因
     */
    private String reason;


    public String getBillTypeCode() {
        return "A012";
    }
}
