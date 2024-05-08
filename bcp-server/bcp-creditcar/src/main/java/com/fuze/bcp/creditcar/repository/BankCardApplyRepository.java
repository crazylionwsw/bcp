package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.BankCardApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 制卡申请单存储接口
 * Created by sean on 2016/11/29.
 */
public interface BankCardApplyRepository extends BaseBillRepository<BankCardApply,String> {

    Page<BankCardApply> findAllByCustomerIdIn(List<String> customerIds, Pageable page);

    Page<BankCardApply> findByDataStatusAndStatusOrderByTsDesc(Pageable page, Integer save, Integer status);

    Page<BankCardApply> findByCustomerIdInAndDataStatusAndStatusOrderByTsDesc(Pageable page, List<String> customerIds, Integer save, int status);

    void deleteById(String id);

    Page<BankCardApply> findByCashSourceIdOrderByTsDesc(Pageable page, String cashSourceId);

    Page<BankCardApply> findByCashSourceIdAndStatusOrderByTsAsc(Pageable page, String cashSourceId, Integer status);

    BankCardApply findByIdAndCashSourceIdOrderByTsDesc(String key, String cashSourceId);

    List<BankCardApply> findByStatusInOrderByTsAsc(List<Integer> ds);
}
