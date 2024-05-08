package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.bd.bean.FeeBillBean;
import com.fuze.bcp.api.bd.bean.FeeValueBean;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Liu} on 2018/3/1.
 * 业务调整单
 */
@Document(collection = "so_business_exchange")
@Data
public class BusinessExchange extends BaseBillEntity {

    /**
     * 更换的购车信息
     */
    private String customerExchangeCarId = null;

    /**
     * 更换的评估单Id(二手车)
     */
    private String exVehicleEvaluateInfoId = null;


    /**
     * 更换的经销商销售人员Id
     */
    private String exDealerEmployeeId = null;

    /**
     * * 更换的借款信息
     */
    private String customerExchangeLoanId = null;

    /**
     * 收费明细列表，用编码标识不同的费用项目
     * 贷款服务费，担保服务费，风险押金 杂费
     */
    List<FeeValueBean> feeItemList = new ArrayList<FeeValueBean>();


    public String getBillTypeCode() {
        return "A030";
    }
}
