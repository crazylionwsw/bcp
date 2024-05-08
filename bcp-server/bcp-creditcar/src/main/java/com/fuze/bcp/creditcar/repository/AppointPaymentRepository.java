package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.AppointPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by zqw on 2017/8/16.
 */
public interface AppointPaymentRepository extends BaseBillRepository<AppointPayment,String> {
    Page<AppointPayment> findByDataStatusAndApproveStatusAndCustomerIdIn(Integer save, int approveStatus, List<String> customerIds, Pageable pageable);

    Page<AppointPayment> findByDataStatusAndApproveStatus(Integer save, int approveStatus, Pageable pageable);

    Page<AppointPayment> findAllByCustomerIdIn(List<String> customerIds, Pageable page);

    Page<AppointPayment> findByDataStatusAndStatusOrderByTsDesc(Pageable page, Integer save, Integer status);

    Page<AppointPayment> findByCustomerIdInAndDataStatusAndStatusOrderByTsDesc(Pageable page, List<String> customerIds, Integer save, int status);

    List<AppointPayment> findByCarDealerId(String carDealerId, Sort tsSort);
}
