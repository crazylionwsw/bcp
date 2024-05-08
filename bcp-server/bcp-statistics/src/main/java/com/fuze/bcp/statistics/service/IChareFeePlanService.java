package com.fuze.bcp.statistics.service;

import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.service.IBaseService;
import com.fuze.bcp.statistics.domain.ChargeFeePlan;
import com.fuze.bcp.statistics.domain.ChargeFeePlanDetail;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by GQR on 2017/10/23.
 */
public interface IChareFeePlanService extends IBaseService<ChargeFeePlan> {

    ChargeFeePlan findOneById(String Id);

    ChargeFeePlan findOneByIdAndStatus(String Id,Integer status);

    List<ChargeFeePlanDetail> findAllDetail();

    List<ChargeFeePlan> findByStatus(Integer status);

    ChargeFeePlan findOneByTransactionId(String transactionId);

    List<ChargeFeePlan> findAllByStatusOrderByCardealerId(Integer status);

    List<ChargeFeePlan> findAllByStatusAndOrderTimeBeforeOrderByCardealerId(Integer status,String data);

    void deleteOneChargeFeePlan(ChargeFeePlan chargeFeePlan);

    ChargeFeePlan saveOneChargeFeePlan(ChargeFeePlan chargeFeePlan);

    Page<ChargeFeePlan> findChargeFeePlans(Integer currentPage);

    List<ChargeFeePlanDetail> saveDetail(List<ChargeFeePlanDetail> rp);

    void deleteDetailByChargeFeePlanId(String chargeFeePlanId);

    List<ChargeFeePlanDetail> findOneDetail(String chargeFeePlanId);

    List<ChargeFeePlanDetail> queryAllDetaillByYearAndMonthAnd(String year, String month);


    ChargeFeePlan findByCustomerTransactionId(String customerTransactionId);

    Page<ChargeFeePlan> findAllBySearchBean(Class<ChargeFeePlan> chargeFeePlanClass, SearchBean searchBean, int stageOrder);
}
