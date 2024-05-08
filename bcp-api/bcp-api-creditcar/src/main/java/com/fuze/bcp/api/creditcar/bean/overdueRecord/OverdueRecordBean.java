package com.fuze.bcp.api.creditcar.bean.overdueRecord;

import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import lombok.Data;

/**
 * Created by ${Liu} on 2018/3/10.
 * 逾期记录
 */
@Data
public class OverdueRecordBean extends APICarBillBean{

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
