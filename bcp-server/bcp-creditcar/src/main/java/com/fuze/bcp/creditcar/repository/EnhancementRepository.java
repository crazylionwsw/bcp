package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.Enhancement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by gqr on 2017/4/9.
 */
public interface EnhancementRepository extends BaseBillRepository<Enhancement,String> {


    Page<Enhancement> findAllByApproveStatusOrderByTsDesc(int approveStatus, Pageable pageable);

    Page<Enhancement> findAllByDataStatusAndApproveStatusAndCustomerIdIn(Integer save, int approveStatus, List<String> customerIds, Pageable pageable);

    Page<Enhancement> findAllByLoginUserIdAndApproveStatusInAndCustomerTransactionId(String loginUserId, List<Integer> as,String customerTransactionId, Pageable pageable);
}
