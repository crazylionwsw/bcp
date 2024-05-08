package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.DMVPledge;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by GQR on 2017/8/24.
 */
public interface IDmvpledgeService extends IBaseBillService<DMVPledge> {

    Page<DMVPledge> findAllByStatusOrderByTsDesc(int currentPage, Integer status);

    List<DMVPledge> findByCarDealerIdAndPledgeDateReceiveTimeBetween(String carDealerId, String startTime, String endTime);

    List<DMVPledge> findByPledgeDateReceiveTimeLike(String saleMonth);


    Map<Object, Object> getBillDailyReport(String orginfoId, String employeeId, String cardealerId, String date, DMVPledge dmvPledge);


    /**
     * 获取抵押完成的transactionId
     * @param date
     * @return
     */
    List<ObjectId> getDvmpFinishTransactionObjectIds(String date);

    List<ObjectId> getReceiveFinishTransactionObjectIds(String date);

    List<DMVPledge> findByCustomerTransactionIdInAndPledgeDateReceiveTimeLike(List<String> transactionIds, String saleMonth);
}
