package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.Enhancement;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by gqr on 2017/8/18.
 */
public interface IEnhancementService extends IBaseBillService<Enhancement> {

    Page<Enhancement> findByLoginUserIdAndApproveStatusIn(String loginUserId, List<Integer> as, Integer currentPage, Integer currentSize,String customerTransactionId);
}
