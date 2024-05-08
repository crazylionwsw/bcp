package com.fuze.bcp.api.credithome.bean;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.bean.APIBaseBillBean;
import lombok.Data;

/**
 * 家装分期单据
 */
@Data
public abstract class APIHomeBillBean extends APIBaseBillBean {

    //单据类型
    private BillTypeBean billType;

}
