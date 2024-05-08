package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.APIBaseBillBean;
import lombok.Data;

/**
 * 征信查询
 * Created by Lily on 2017/07/17.
 */
@Data
public class CreditReportQueryBean extends APICarBillBean {


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
