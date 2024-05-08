package com.fuze.bcp.api.wechat.service;

import com.fuze.bcp.api.wechat.bean.LoanQueryBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * Created by ${Liu} on 2018/4/18.
 */
public interface ILoanQueryBizService {

    /**
     * 公众号录入客户需求信息
     */
    ResultBean<LoanQueryBean> actSaveLoanQuery(LoanQueryBean loanQueryBean);

    /**
     * 获取公众号里的数据
     */
    ResultBean<List<LoanQueryBean>> actGetAllLoanQuery();

    /**
     * 获取一条数据
     */
    ResultBean<LoanQueryBean> actGetLoanQuery(String loanQueryId);

}
