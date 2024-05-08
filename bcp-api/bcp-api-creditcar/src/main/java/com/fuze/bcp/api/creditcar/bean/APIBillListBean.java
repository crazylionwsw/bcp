package com.fuze.bcp.api.creditcar.bean;


import com.fuze.bcp.api.transaction.bean.TransactionSummaryBean;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 单据对象基类
 * Created by JZ on 2017/02/17.
 */
@Data
public abstract class APIBillListBean extends APIBaseBean {

    /**
     * 交易概览
     */
    TransactionSummaryBean transactionSummary = null;

}
