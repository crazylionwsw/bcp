package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.DealerEmployee;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by guotaiping on 2016/10/20.
 */
public interface DealerEmployeeRepository extends BaseDataRepository<DealerEmployee, String> {

    /**
     * @param carDealerId
     * @param pageable
     * @return
     */
    Page<DealerEmployee> findAllByCarDealerId(String carDealerId, Pageable pageable);

    /**
     * 获取员工数
     *
     * @param save
     * @param carDealerId
     * @return
     */
    Integer countByDataStatusAndCarDealerId(Integer save,String carDealerId);

    /**
     * 根据所选择的经销商查询4S店的销售人员
     *
     * @param carDealerId
     * @return
     */
    List<DealerEmployee> findByDataStatusAndCarDealerId(Integer save, String carDealerId);


}
