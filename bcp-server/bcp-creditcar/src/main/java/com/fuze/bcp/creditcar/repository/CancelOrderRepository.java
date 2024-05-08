package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.CancelOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by zxp on 2017/3/13.
 */
public interface CancelOrderRepository extends BaseBillRepository<CancelOrder,String> {

    /**
     * 根据登录账号和审批状态查询取消订单
     * @param userid
     * @param pageable
     * @return
     */
    Page<CancelOrder> findByLoginUserIdAndApproveStatus(String userid, Integer approvestatus, Pageable pageable);

    List<CancelOrder> findAllByDataStatusAndCustomerTransactionId(int save,String customerTransactionId);

}
