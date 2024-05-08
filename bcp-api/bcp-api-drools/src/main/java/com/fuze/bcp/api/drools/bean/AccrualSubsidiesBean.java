package com.fuze.bcp.api.drools.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * Created by CJ on 2017/8/30.
 * 条件 12期	首付超过50% 贴息手续费的50% 首付比例20%-50% 贴息手续费的30%
 */
@Data
public class AccrualSubsidiesBean extends APIBaseBean{

    /**
     * 贷款额度
     */
    private Double creditAmount;

    /**
     * 费率
     */
    private Double ratio;

    /**
     * 期数（月份）
     */
    private Integer  months;

    /**
     * 首付比例
     */
    private Double downPaymentRatio;

    /**
     * 车型
     */
    private String carBrandId;

    /**
     * 车型
     */
    private String carModelId;

    /**
     * 车型
     */
    private String carTypeId;


    /**
     * 贴息额
     */
    private Double compensatoryAmount;

    /**
     * 贴息费率
     */
    private Double compensatoryRatio;

}
