package com.fuze.bcp.blockchain.service.impl;

import com.fuze.bcp.blockchain.service.ILoanContractService;
import com.fuze.bcp.blockchain.service.IWeb3jReceiverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by CJ on 2017/7/3.
 */

@Service
public class Web3jReceiverImpl implements IWeb3jReceiverService {

    private Logger log = LoggerFactory.getLogger(Web3jReceiverImpl.class);

    @Autowired
    private ILoanContractService iLoanContractService;

    @Override
    public void process(Map<String,Object> map) {
        try {
            String eventType = (String) map.get("eventType");
            if(eventType.equals("BankCard_Swiping")){
                //签约审核通过后将交易数据上链
                iLoanContractService.actMonitoringOrder(map);
            }else if(eventType.equals("DMVP_PledgeEnd")){
                iLoanContractService.actMonitoringDMVPledge(map);
            }
            log.info("1th Log执行action完毕！");
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
