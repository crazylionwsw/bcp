package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.DMVPledge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 车管所抵押
 * Created by sean on 2016/11/29.
 */
public interface DMVPledgeRepository extends BaseBillRepository<DMVPledge,String> {

    Page<DMVPledge> findAllByStatusOrderByTsDesc(Integer status, Pageable pageable);

    List<DMVPledge> findByDataStatusAndCarDealerIdAndPledgeDateReceiveTimeBetween(Integer status, String carDealerId, String startTime, String endTime);

    List<DMVPledge> findByDataStatusAndPledgeDateReceiveTimeLike(Integer save, String saleMonth);

    List<DMVPledge> findByDataStatusAndCustomerTransactionIdInAndPledgeDateReceiveTimeLike(Integer save, List<String> transactionIds, String saleMonth);
}
