package com.fuze.bcp.cardealer.service;

import com.fuze.bcp.cardealer.domain.CarDealer;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by admin on 2017/6/2.
 */
public interface ICarDealerService extends IBaseDataService<CarDealer> {

    List<CarDealer> getCarDealersByLoginUserId(String loginUserId);

    List<CarDealer> getMyCarDealers(List<String> employeeIds);

    Page<CarDealer> getMyCarDealers(List<String> employeeIds, Integer pageIndex, Integer pageSize);

    Page<CarDealer> getChannelCarDealers(String employeeId, Integer pageIndex, Integer pageSize);

    Page<CarDealer> getSalesCarDealers(String employeeId, Integer pageIndex, Integer pageSize);

    Page<CarDealer> getCarDealersByLoginUserId(Integer pageIndex, Integer pageSize, String loginUserId);

    Page<CarDealer> getAllOrderByTs(Integer currentPage);

    Page<CarDealer> searchCarDealers(String name, String loginUserId, Integer pageIndex, Integer pageSize);

    List<CarDealer> getCarDealersByEmployeeIds(List<String> employeeIds);

    List<CarDealer> getCarDealersByGroupId(String groupId);

    List<CarDealer> getCardealerMore(List<String> ids);

    Page<CarDealer> searchCarDealers(Integer currentPage, CarDealer carDealerBean);

    List<CarDealer> getCarDealerByOrgIds(List<String> orgIds);

}
