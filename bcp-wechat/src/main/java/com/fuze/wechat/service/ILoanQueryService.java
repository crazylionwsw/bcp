package com.fuze.wechat.service;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.LoanQuery;

public interface ILoanQueryService {
    /**
     * 公众号录入客户需求信息
     */
    ResultBean<LoanQuery> actSaveLoanQuery(LoanQuery loanQueryBean);

    /**
     * 获取一条数据
     */
    ResultBean<LoanQuery> actGetLoanQuery(String loanQueryId);
}
