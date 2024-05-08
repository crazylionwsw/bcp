package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.CreditReportQuery;
import com.fuze.bcp.creditcar.repository.CreditReportQueryRepository;
import com.fuze.bcp.creditcar.service.ICreditReportQueryService;
import org.springframework.stereotype.Service;

/**
 * Created by Lily on 2017/7/19.
 */
@Service
public class CreditReportQueryServiceImpl extends BaseBillServiceImpl<CreditReportQuery, CreditReportQueryRepository> implements ICreditReportQueryService{

}
