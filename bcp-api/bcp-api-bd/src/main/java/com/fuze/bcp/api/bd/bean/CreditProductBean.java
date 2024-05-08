package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品销售策略
 */
@Data
@MongoEntity(entityName = "bd_creditproduct")
public class CreditProductBean extends APIBaseDataBean {

    /**
     * 资金来源，方便其它业务模块使用
     */
    private String cashSourceId;

    /**
     * 对应的资金利率
     */
    private String sourceRateId = null;

    /**
     * 地区列表 (限制的省列表 - 不能销售)
     */
    private List<String> provinceIds = new ArrayList<String>();

    /**
     * 允许的还款方式列表
     */
    private List<String> repaymentWayIds = new ArrayList<String>();

    /**
     * 允许的担保方式列表
     */
    private List<String> guaranteeWayIds = new ArrayList<String>();

    /**
     * 借款金额（下限）
     */
    private Double minCreditAmount;

    /**
     * 借款金额（上限）
     */
    private Double maxCreditAmount;


    /**
     * 开始日期
     */
    private String startDate = null;

    /**
     * 截止日期
     */
    private String endDate = null;


}
