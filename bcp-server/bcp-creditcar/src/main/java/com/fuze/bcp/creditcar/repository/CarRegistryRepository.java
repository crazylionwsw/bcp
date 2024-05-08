package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.CarRegistry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 上牌申请信息
 * Created by sean on 2016/11/29.
 */
public interface CarRegistryRepository extends BaseBillRepository<CarRegistry,String> {

    Page<CarRegistry> findAllByDataStatusAndApproveStatusAndCustomerIdInOrderByTsDesc(Integer save, int approveStatus, List<String> customerIds, Pageable pageable);
}
