package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sean on 2016/11/29.
 */
public interface BaseBillRepository<T extends BaseBillEntity,ID extends Serializable> extends BaseRepository<T,ID> {

    /**
     * 通过审核状态获取，带分页，按时间排序
     * @param approveStatus
     * @param pageable
     * @return
     */
    Page<T> findAllByApproveStatusOrderByTsDesc(Integer approveStatus, Pageable pageable);

    /**
     * 通过审核状态获取，带分页，按时间排序,只显示可用数据
     * @param approveStatus
     * @param dataStatus
     * @param pageable
     * @return
     */
    Page<T> findAllByApproveStatusAndDataStatusOrderByTsDesc(Integer approveStatus, Integer dataStatus, Pageable pageable);

    /**
     * 获取所有，按时间排序,只显示可用数据
     * @return
     */
    Page<T> findAllByDataStatusOrderByTsDesc(Integer dataStatus,Pageable pageable);

    /**
     * 获取所有，按时间排序
     * @return
     */
    Page<T> findAllByOrderByTsDesc(Pageable pageable);

    /**
     * 根据登录用户获取信息
     * @param userId
     * @return
     */
    List<T> findByLoginUserId(String userId);

    /**
     * 根据登录用户获取单据（带分而）
     * @param userId
     * @return
     */
    Page<T> findByLoginUserId(String userId, Pageable pageable);


    /**
     * 根据部门ID获取单据
     * @param orginfoId
     * @return
     */
    List<T> findByOrginfoId(String orginfoId);

    /**
     * 获取当前用户的待办业务数
     * @param ds
     * @param loginUserId
     * @return
     */
    Long countByDataStatusAndLoginUserId(Integer ds, String loginUserId);

    Long countByDataStatus(Integer ds);


    /**
     * 查询指定客户的所有业务
     * @param customerId
     * @return
     */
    List<T> findAllByCustomerId(String customerId);

    T findOneByCustomerId(String customerId);

    T findOneByCustomerIdOrderByTsDesc(String customerId);

    /**
     * 通过客户交易获取指定单据
     * @param customerTransactionId
     * @return
     */
    T findOneByCustomerTransactionId(String customerTransactionId);

    /**
     * 通过客户交易获取指定的可用单据
     * @param customerTransactionId
     * @param status        数据状态
     * @return
     */
    T findAvailableOneByCustomerTransactionIdAndDataStatus(String customerTransactionId,Integer status);

    /**
     * 获取某几个客户的单据，带分页
     * @param ds
     * @param ids
     * @param pageable
     * @return
     */
    Page<T> findByDataStatusAndCustomerIdInOrderByTsDesc(Integer ds, List<String> ids, Pageable pageable);

    Page<T> findByDataStatusAndApproveStatusAndCustomerIdInOrderByTsDesc(Integer ds, Integer as, List<String> ids, Pageable pageable);

    /**
     * 获取某几个客户的单据，不带分页
     * @param ds
     * @param ids
     * @return
     */
    List<T> findByDataStatusAndCustomerIdIn(Integer ds, List<String> ids);


    /**
     * 获取某
     * @param ds
     * @param customerId
     * @return
     */
    T findOneByDataStatusAndCustomerId(Integer ds, String customerId);

    Page<T> findByLoginUserIdOrderByTsDesc(String userid, Pageable pageable);

    Page<T> findByLoginUserIdAndApproveStatusOrderByTsDesc(String userid, Integer approvestatus, Pageable pageable);

    Page<T> findByLoginUserIdAndDataStatusAndApproveStatusInOrderByTsDesc(String userid, Integer ds, List<Integer> approvestatusList, Pageable pageable);
    Page<T> findByLoginUserIdAndApproveStatusIn(String userid, List<Integer> approvestatusList, Pageable pageable);

    Page<T> findByLoginUserIdAndApproveStatusInAndDataStatusIsNot(String userid, List<Integer> approvestatusList, Integer ds, Pageable pageable);

    Page<T> findByLoginUserIdAndApproveStatusInAndDataStatusIsNotOrCustomerTransactionIdIn(String userid, List<Integer> approvestatusList, Integer ds, List<String> tids, Pageable pageable);

    Page<T> findByLoginUserIdAndApproveStatusInAndDataStatusIsNotAndCustomerTransactionIdIn(String userid, List<Integer> approvestatusList, Integer ds, List<String> tids, Pageable pageable);

    /**
     * 根据用户ID获取数据，带分页
     * @param dataStatus
     * @param userid
     * @param pageable
     * @return
     */
    Page<T> findByDataStatusAndLoginUserId(Integer dataStatus, String userid, Pageable pageable);
    /**
     * 根据用户ID获取数据，带分页
     * @param dataStatus
     * @param userid
     * @param pageable
     * @return
     */
    Page<T> findByLoginUserIdAndDataStatusIn( String userid, List<Integer> dataStatus,Pageable pageable);

    /**
     * 根据用户ID获取数据，不带分页
     * @param dataStatus
     * @param userid
     * @return
     */
    List<T> findByDataStatusAndLoginUserId(Integer dataStatus, String userid);


    List<T> findAllByDataStatusAndApproveStatus(Integer ds, Integer as, Sort s);

    List<T>  findByDataStatusAndApproveStatusAndIdNotInAndTsLessThan(Integer ds, Integer approveStatus, List<String> ids,String date);


    void deleteOneByCustomerTransactionId(String customerTransactionId);
}
