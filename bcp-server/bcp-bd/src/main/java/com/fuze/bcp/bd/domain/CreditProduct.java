package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品销售策略
 */
@Document(collection = "bd_creditproduct")
@Data
public class CreditProduct extends BaseDataEntity {

    /**
     * 资金来源，方便其它业务模块使用
     */
    private String cashSourceId;

    /**
     * 对应的资金利率
     */
    private String sourcerateId = null;


    /**
     * 地区列表 (限制的省列表 - 不能销售)
     */
    private List<String> provinceIds = new ArrayList<String>();

    /**
     * 允许的还款方式列表
     */
    private List<String> repaymentWayIds = null;

    /**
     * 允许的担保方式列表
     */
    private List<String> guaranteeWayIds = null;

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

    /**
     * 贷款用途
     */
    private List<String> creditUsageIds = null;

}
