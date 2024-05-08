package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.CarValuation;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Lily on 2017/8/14.
 */
public interface ICarValuationService extends IBaseBillService<CarValuation> {

    /**
     * 通过渠道行查询未签约的评估记录
     * @return
     */
    List<CarValuation> findAllByCarDealerId(String carDealerId);

    /**
     * 通过VIN码获取评估信息
     * @param vin
     * @return
     */
    CarValuation findOneByVin(String vin);

    /**
     * 通过车辆Id和Vin码获取评估信息
     * @param carTypeId
     * @param vin
     * @return
     */
    CarValuation findOneByCarTypeIdAndVin(String carTypeId, String vin);

    /**
     * 通过VIN获取一条有效的评估记录
     * @param vin
     * @return
     */
    CarValuation findAvailableOneByVin(String vin);

    /**
     * 获取所有可用的二手车评估报告（1、未签约，2、未废弃）
     * @return
     */
    Page<CarValuation> getAvailableCarValuations(Integer page);

    /**
     * 获取所有可用的二手车评估报告（1、未签约，2、未废弃）
     * @param pageindex
     * @param pagesize
     * @return
     */
    Page<CarValuation> getAvailableCarValuations(Integer pageindex, Integer pagesize);

    /**
     * 获取所有可用的二手车评估报告（1、未签约，2、未废弃）
     * @param userId
     * @param pageindex
     * @param pagesize
     * @return
     */
    Page<CarValuation> getAvailableCarValuations(String userId, Integer pageindex, Integer pagesize);

    Page<CarValuation> getAllCarValuatiOrderByTs(Integer page);

    Page<CarValuation> getAllByApproveStatusOrderByTsDesc(int approveStatus, int currentPage);
}
