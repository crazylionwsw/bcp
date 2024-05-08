package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.Customer;
import com.fuze.bcp.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Hao on 2016/10/20.
 */
public interface CustomerRepository extends BaseRepository<Customer,String> {

    /**
     * 通过姓名查找
     * @param name
     * @param pageable
     * @return
     */
    Page<Customer> findByNameLike(String name, Pageable pageable);

    /**
     * 根据客户身份证号查询客户信息
     * @param identifyNo
     * @return
     */
    Customer findOneByDataStatusAndIdentifyNo(Integer save, String identifyNo);

    /**
     *
     * @param tempsave
     * @param pageable
     * @return
     */
    Page<Customer> findByDataStatusGreaterThan(Integer tempsave, Pageable pageable);



}
