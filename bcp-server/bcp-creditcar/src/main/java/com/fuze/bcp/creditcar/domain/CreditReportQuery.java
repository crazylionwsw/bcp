package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 征信查询请求
 *
 */
@Document(collection = "so_credit_report_query")
@Data
public class CreditReportQuery extends BaseBillEntity {

    /**
     * 征信报告提交的时间
     */
    private String submitTime;

    /**
     * 子类需要定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A000";
    }
}
