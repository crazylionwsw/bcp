package com.fuze.bcp.credithome.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by ZQW on 2018/3/19.
 */
@Document(collection = "so_domestic_outfit")
@Data
public class DomesticOutfit extends HomeBillEntity {

    /**
     *  申请额度
     */
    private Double applyAmount = 0.0;

    /**
     *  申请日期
     */
    private String applyTime;

    /**
     *  批贷金额
     */
    private Double approvedAmount = 0.0;

    /**
     *  批贷日期
     */
    private String approvedTime;

    /**
     *  放款金额
     */
    private Double loanAmount = 0.0;

    /**
     *  放款日期
     */
    private String loanTime;


    public String getBillTypeCode() {
        return "H002";
    }
}
