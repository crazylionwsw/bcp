package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by ${Liu} on 2018/3/10.
 * 逾期记录
 */
@Document(collection = "so_overdue_record")
@Data
public class OverdueRecord extends BaseBillEntity{

    /**
     *逾期时间
     */
    private String overdueTime;

    /**
     *逾期金额
     */
    private String overdueAmount;

    @Override
    public String getBillTypeCode() {
        return "A032";
    }
}
