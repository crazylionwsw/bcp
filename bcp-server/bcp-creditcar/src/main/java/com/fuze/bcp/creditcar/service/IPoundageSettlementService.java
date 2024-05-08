package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.PoundageSettlement;
import com.fuze.bcp.service.IBaseService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by zqw on 2017/8/29.
 */
public interface IPoundageSettlementService extends IBaseService<PoundageSettlement>{

    Page<PoundageSettlement> findAllByCustomerIdsAndStartTimeAndEndTimeAndPoundageSettlement(List<String> customerIds, String startTime, String endTime, PoundageSettlement poundageSettlement,int currentPage);

    PoundageSettlement getOneByCustomerTransactionId(String customerTransactionId);

    PoundageSettlement getOneByCustomerTransactionId(String customerTransactionId,int save);

    Page<PoundageSettlement> findAllByStartTimeAndEndTimeAndPoundageSettlementOrderByOrderTimeDesc(String startTime, String endTime, PoundageSettlement poundageSettlement,int currentPage);

    Page<PoundageSettlement> getAllByOrderByOrderTimeDesc(Integer currentPage);

}
