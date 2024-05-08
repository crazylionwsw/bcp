package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.DealerRepayment;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Lily on 2017/9/23.
 */
public interface IDealerRepaymentService extends IBaseBillService<DealerRepayment> {

    Page<DealerRepayment> findAllByStatusOrderByTsDesc(int status, int currentPage);

    Page<DealerRepayment> findAllByStatusAndCustomerIds(List<String> customerIds, int status, int currentPage);

    Page<DealerRepayment> findByLoginUserIdAndApproveStatusIn(String loginUserId, List<Integer> as, Integer currentPage, Integer currentSize);
}
