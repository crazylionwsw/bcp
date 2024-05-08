package com.fuze.bcp.blockchain.service;

import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.blockchain.domain.LoanContract;

import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2018/4/20.
 */
public interface ILoanContractService {

    /**
     * 客户签约通过后，通过监控将签约的信息上链
     * @param map
     * @return
     */
    ResultBean<LoanContract> actMonitoringOrder(Map map);

    /**
     * 客户签约抵押后，通过监控将抵押的信息上链
     * @param map
     * @return
     */
    ResultBean<LoanContract> actMonitoringDMVPledge(Map map);


    /**
     * 借贷交易信息上链
     * @param loanContract
     * @return
     */
    ResultBean<LoanContract> actAddLoanTransaction(LoanContract loanContract);

    /**
     * 更新借贷交易信息
     * @param loanContract
     * @return
     */
    ResultBean<LoanContract> actUpdateLoanTransaction(LoanContract loanContract);

    /**
     * 根据hash值和刷卡日期从链上获取交易信息
     * @param hash
     * @param date
     * @return
     */
    ResultBean<List<LoanContract>> actGetLoanTransaction(String hash,String date);

}
