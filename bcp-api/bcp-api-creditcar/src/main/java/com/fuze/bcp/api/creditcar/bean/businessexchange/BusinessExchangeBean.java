package com.fuze.bcp.api.creditcar.bean.businessexchange;

import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Liu} on 2018/3/1.
 */
@Data
public class BusinessExchangeBean extends APICarBillBean{

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



    @Override
    public String getBillTypeCode() {
        return "A030";
    }
}
