package com.fuze.bcp.transaction.repository;

import com.fuze.bcp.repository.BaseRepository;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 客户的业务
 */
public interface CustomerTransactionRepository extends BaseRepository<CustomerTransaction,String> {

    /**
     * 根据状态查询某客户的业务
     * @param status
     * @param customerId
     * @return
     */
    List<CustomerTransaction> findByStatusAndCustomerId(Integer status, String customerId);


    /**
     * 根据业务状态查询
     * @param status
     * @return
     */
    List<CustomerTransaction> findByStatus(Integer status);


    /**
     * 根据客户ID查询
     * @param customerId
     * @return
     */
    List<CustomerTransaction> findByCustomerId(String customerId);


    /**
     * 根据客户ID查询
     * @param employeeId
     * @return
     */
    Page<CustomerTransaction> findByEmployeeIdAndStatusIn(String employeeId, List<Integer> list, Pageable pageable);


    /**
     * 获取客户的在办业务数
     * @param customerId
     * @param status
     * @return
     */
    Integer countByCustomerIdAndStatusIn(String customerId, List<Integer> status);


    /**
     * 根据客户ID查询
     * @param employeeId
     * @return
     */
    Page<CustomerTransaction> findByEmployeeId(String employeeId, Pageable pageable);


    /**
     * 根據loginUserId查询
     * @param loginUserId
     * @param pageable
     * @return
     */
    Page<CustomerTransaction> findByDataStatusAndLoginUserId(Integer save, String loginUserId, Pageable pageable);

    List<CustomerTransaction> findByDataStatusAndStatusInAndLoginUserId(Integer save, List<Integer> ss, String loginUserId);


    /**
     * 根据LoginuUserId  任务状态查询
     * @param loginUserId
     * @return
     */
    Page<CustomerTransaction> findByDataStatusAndLoginUserIdAndStatusIn(Integer save,String loginUserId, List<Integer> list, Pageable pageable);

    Page<CustomerTransaction> findByDataStatusAndCustomerIdIn(Integer save, List<String> customerIds, Pageable pageable);

    Page<CustomerTransaction> findByDataStatusAndCustomerIdInAndLoginUserId(Integer save, List<String> customerIds, String loginUserId, Pageable pageable);

    Page<CustomerTransaction> findByDataStatusAndStatusAndCustomerIdIn(Integer save,int status, List<String> customerIds, Pageable page);

    Page<CustomerTransaction> findAllByDataStatusAndStatus(Integer save, Integer status, Pageable pageable);

    List<CustomerTransaction> findByCarDealerId(String carDealerId);

    Page<CustomerTransaction> findAllByStatusOrderByTsDesc(int status,Pageable pageable);

    Integer countByTsStartingWithAndFileNumberIsNotNull(String today);

    List<CustomerTransaction> findByDataStatusAndCustomerIdInAndLoginUserId(Integer save, List<String> customerIds, String loginUserId);

    CustomerTransaction findOneByCustomerId(String customerId);

    List<CustomerTransaction> findAllByCarDealerIdIn(List<String> cardealerIds);

}
