package com.fuze.bcp.api.creditcar.bean.businessexchange;

import com.fuze.bcp.api.bd.bean.FeeBillBean;
import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.creditcar.bean.BillSubmissionBean;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.bean.CustomerCarExchangeBean;
import com.fuze.bcp.api.customer.bean.LoanSubmissionBean;
import com.fuze.bcp.api.customer.bean.PadCustomerCarBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Liu} on 2018/3/1.
 */
@Data
public class BusinessExchangeSubmitBean extends BillSubmissionBean {

    /**
     * 评估单Id
     */
    private String vehicleEvaluateInfoId;


    /**
     * 客户选中销售人员id
     */
    private String dealerEmployeeId;

    /**
     * 车辆信息
     */
//    private CustomerCarExchangeBean customerCar;
    private PadCustomerCarBean customerCar;

    /**
     * 借款信息
     */
    private LoanSubmissionBean customerLoan;

    /**
     * 收费明细列表，用编码标识不同的费用项目
     * 贷款服务费，担保服务费，风险押金 杂费
     */
    List<FeeValueBean> feeItemList = new ArrayList<FeeValueBean>();

}
