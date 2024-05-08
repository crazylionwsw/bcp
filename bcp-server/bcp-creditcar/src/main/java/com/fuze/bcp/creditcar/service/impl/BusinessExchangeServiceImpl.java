package com.fuze.bcp.creditcar.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.BusinessExchange;
import com.fuze.bcp.creditcar.repository.BusinessExchangeRepository;
import com.fuze.bcp.creditcar.service.IBusinessExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Liu} on 2018/3/1.
 */
@Service
public class BusinessExchangeServiceImpl extends BaseBillServiceImpl<BusinessExchange,BusinessExchangeRepository> implements IBusinessExchangeService{

    @Autowired
    BusinessExchangeRepository businessExchangeRepository;


    @Override
    public List<BusinessExchange> getByCustomerTransactionIdAndByApproveStatus(String transactionId,Integer approveStatus) {
        return businessExchangeRepository.findByCustomerTransactionIdAndApproveStatus(transactionId,approveStatus);
    }
}
