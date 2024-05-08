package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.DealerRepayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by zxp on 2017/3/13.
 */
public interface DealerRepaymentRepository extends BaseBillRepository<DealerRepayment,String> {

    Page<DealerRepayment> findAllByStatusAndDataStatusOrderByTsDesc(Integer status, Integer dataStatus, Pageable pageable);

    Page<DealerRepayment> findAllByDataStatusAndStatusAndCustomerIdInOrderByTsDesc(Integer ds, Integer as, List<String> ids, Pageable pageable);

    Page<DealerRepayment> findByLoginUserIdAndApproveStatusIn(String loginUserId, List<Integer> as, Pageable page);
}
