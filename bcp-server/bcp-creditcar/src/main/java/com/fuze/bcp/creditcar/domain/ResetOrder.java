package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 重新签约申请单
 * Created by sean on 2016/10/20.
 */
@Document(collection = "so_changeordercar")
@Data
public class ResetOrder extends BaseBillEntity {

    /**
     * 原始订单的ID
     */
    private String purchaseOrderId;

    /**
     * 子类需要定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A014";
    }
}
