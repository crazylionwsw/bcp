package com.fuze.bcp.cardealer.repository;

import com.fuze.bcp.cardealer.domain.CarDealer;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CarDealerRepository extends BaseDataRepository<CarDealer, String> {

    /**
     * 按时间降序
     *
     * @param pageable
     * @return
     */
    Page<CarDealer> findAllByOrderByTsDesc(Pageable pageable);

    /**
     * 查询经销商是否存在
     *
     * @param name
     * @param address
     * @param manager
     */
    CarDealer findByNameAndAddressAndManager(String name, String address, String manager);

    /**
     * 查询渠道经理的经销商
     *
     * @param id
     * @return
     */
    List<CarDealer> findByDataStatusAndEmployeeIdAndStatus(Integer save, String id, Integer status);

    List<CarDealer> findByDataStatusAndEmployeeId(Integer save, String id);

    /**
     * 查询登陸用戶的经销商
     *
     * @param id
     * @return
     */
    List<CarDealer> findByDataStatusAndLoginUserIdAndStatus(Integer save, String id, Integer status);

    List<CarDealer> findByDataStatusAndLoginUserIdAndStatusIn(Integer save, String id, List<Integer> status);

    Page<CarDealer> findByDataStatusAndLoginUserIdAndNameContaining(Integer save, String id, String name, Pageable pageable);

    List<CarDealer> findByDataStatusAndLoginUserId(Integer save, String id);

    /**
     * 分页查询登陸用戶的经销商
     *
     * @param save        保准状态
     * @param loginUserId
     * @param pageable
     * @return
     */
    Page<CarDealer> findByDataStatusAndLoginUserId(Integer save, String loginUserId, Pageable pageable);

    /**
     * 查询渠道经理的经销商
     *
     * @return
     */
//    List<CarDealer> findByDataStatusAndEmployeeIdOrBusinessManId(Integer save, String employeeid, String businessManid);
//
//    List<CarDealer> findByDataStatusAndEmployeeIdInOrBusinessManIdIn(Integer save, List<String> employeeids, List<String> businessManids);
    List<CarDealer> findByDataStatusAndEmployeeIdInOrBusinessManIdsInOrderByTsDesc(Integer save, List<String> employeeids, List<String> businessManids);

    Page<CarDealer> findByDataStatusAndEmployeeIdInOrBusinessManIdsIn(Integer save, List<String> employeeids, List<String> businessManids, Pageable pageable);

    Page<CarDealer> findByDataStatusAndBusinessManIdsContaining(Integer save, String businessManid, Pageable pageable);

    Page<CarDealer> findByDataStatusAndEmployeeIdOrBusinessManIdsContaining(Integer save, String channelId, String businessManId, Pageable pageable);

    /**
     * 查询分期经理所属的经销商
     */
    //  List<CarDealer> findByDataStatusAndBusinessManIdIn(Integer save, String employeeId);
//    /**
//     * 查询分期经理所属的经销商
//     * @param save
//     * @param id
//     * @return
//     */
//    List<CarDealer> findByDataStatusAndBusinessManIds(Integer save, String id);
//
//    /**
//     * 查询分期经理的经销商（正常合作中）
//     * @param save
//     * @param id
//     * @param status
//     * @return
//     */
//    List<CarDealer> findByDataStatusAndBusinessManIdsAndStatus(Integer save, String id, Integer status);


    List<CarDealer> findAllByDataStatusAndEmployeeIdInOrderByEmployeeId(Integer ds, List<String> employeeIds);

    List<CarDealer> findAllByDataStatusAndDealerGroupId(Integer ds, String groupId);

    List<CarDealer> findAllByIdIn(List<String> ids);

    List<CarDealer> findByDataStatusAndOrginfoIdIn(Integer save, List<String> orgIds);
}
