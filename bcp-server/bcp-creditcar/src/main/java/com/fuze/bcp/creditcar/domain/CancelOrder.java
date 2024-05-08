package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by zxp on 2017/3/13.
 * 取消订单
 */
@Document(collection = "so_cancel_order")
@Data
public class CancelOrder  extends BaseBillEntity {

    //取消原因
    private String reason;

    public String getBillTypeCode() {
        return "A012";
    }
}
