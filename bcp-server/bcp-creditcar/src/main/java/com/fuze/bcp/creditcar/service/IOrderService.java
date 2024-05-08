package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/7/19.
 */

public interface IOrderService extends IBaseBillService<PurchaseCarOrder> {


    PurchaseCarOrder findByVehicleEvaluateInfoId(String id);

    PurchaseCarOrder findByVehicleEvaluateInfoIdAndDataStatus(String id,Integer ds);

    List<PurchaseCarOrder> findAllByVehicleEvaluateInfoId(String id);

    List<PurchaseCarOrder> findAllByVehicleEvaluateInfoIdAndDataStatus(String id,Integer ds);

    List<PurchaseCarOrder> findAll(Sort sort);

    List<PurchaseCarOrder> findAllByDataStatusAndApproveStatus(Integer ds, Integer as,Sort sort);

    List<String> getUsedValuationIds();

    Map<Object, Object> getDailyReport(String orgId,String date, PurchaseCarOrder purchaseCarOrder,String groupField);


}
