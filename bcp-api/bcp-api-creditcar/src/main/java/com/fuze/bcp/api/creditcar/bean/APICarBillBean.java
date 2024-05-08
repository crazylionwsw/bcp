package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.bean.APIBaseBillBean;
import lombok.Data;

/**
 * Created by Lily on 2017/7/28.
 */
@Data
public abstract class APICarBillBean extends APIBaseBillBean {

    //单据类型
    private BillTypeBean billType;

}
