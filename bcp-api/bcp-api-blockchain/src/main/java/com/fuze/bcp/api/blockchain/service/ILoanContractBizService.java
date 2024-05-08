package com.fuze.bcp.api.blockchain.service;

import com.fuze.bcp.api.blockchain.bean.LoanContractBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * Created by Lily on 2018/4/20.
 */
public interface ILoanContractBizService {

    /**
     * 客户签约通过后，通过监控将签约的信息上链
     * @param orderId
     * @return
     */
    ResultBean<LoanContractBean> actMonitoringOrder(String orderId);

    /**
     * 客户签约抵押后，通过监控将抵押的信息上链
     * @param pledgeId
     * @return
     */
    ResultBean<LoanContractBean> actMonitoringDMVPledge(String pledgeId);


    /**
     * 借贷交易信息上链
     * @param loanContractBean
     * @return
     */
    ResultBean<LoanContractBean> actAddLoanTransaction(LoanContractBean loanContractBean);

    /**
     * 从链上获取交易信息
     * @param hash
     * @return
     */
    ResultBean<List<LoanContractBean>> actGetLoanTransaction(String hash);

}
