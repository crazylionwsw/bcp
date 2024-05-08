package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.AppointPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 预约垫资服务
 * Created by zqw on 2017/8/16.
 */
public interface IAppointPaymentService extends IBaseBillService<AppointPayment> {

    Page<AppointPayment> findByDataStatusAndStatusOrderByTsDesc(Integer currentPage, Integer save, Integer status);

    Page<AppointPayment> findByCustomerIdInAndDataStatusAndStatusOrderByTsDesc(List<String> customerIds, Integer save, int status, int currentPage);

    List<String> getDailyPaidMoneyOrderIds(String date);

    List<AppointPayment> findAll(Sort sort);

    List<AppointPayment> findAllByDataStatusAndApproveStatus(Integer ds, Integer as, Sort sort);

    List<AppointPayment> findAllByCardearlerId(String carDealerId, Sort sort);

    List<AppointPayment> getAppointPaymentBySelectTime(String selectTime);

}
