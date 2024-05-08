package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.CreditReportQuery;

/**
 * Created by Hao on 2016/10/20.
 */
public interface CreditReportQueryRepository extends BaseBillRepository<CreditReportQuery,String> {

    CreditReportQuery findByCustomerId(String customerId);

    CreditReportQuery findOneByCustomerIdOrderByTsDesc(String customerId);
}
