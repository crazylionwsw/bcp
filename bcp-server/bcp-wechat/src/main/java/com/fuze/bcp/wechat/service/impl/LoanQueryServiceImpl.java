package com.fuze.bcp.wechat.service.impl;

import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import com.fuze.bcp.wechat.domain.LoanQuery;
import com.fuze.bcp.wechat.repository.LoanQueryRepository;
import com.fuze.bcp.wechat.service.ILoanQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ${Liu} on 2018/4/18.
 */
@Service
public class LoanQueryServiceImpl extends BaseDataServiceImpl<LoanQuery,LoanQueryRepository> implements ILoanQueryService{

    @Autowired
    LoanQueryRepository loanQueryRepository;
}
