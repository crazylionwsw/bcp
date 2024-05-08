package com.fuze.bcp.cardealer.service.impl;

import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.cardealer.domain.CarDealer;
import com.fuze.bcp.cardealer.repository.CarDealerRepository;
import com.fuze.bcp.cardealer.service.ICarDealerService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by CJ on 2017/6/14.
 */
@Service
public class CarDealerServiceImpl extends BaseDataServiceImpl<CarDealer, CarDealerRepository> implements ICarDealerService {

    @Autowired
    CarDealerRepository carDealerRepository;

    @Override
    public List<CarDealer> getCarDealersByLoginUserId(String loginUserId) {
        List<Integer> list = new ArrayList<>();
        list.add(CarDealerBean.STATUS_CONTRACT);
        list.add(CarDealerBean.STATUS_ONGOING);
        return this.carDealerRepository.findByDataStatusAndLoginUserIdAndStatusIn(DataStatus.SAVE, loginUserId, list);
    }

    @Override
    public List<CarDealer> getMyCarDealers(List<String> employeeIds) {
        return this.carDealerRepository.findByDataStatusAndEmployeeIdInOrBusinessManIdsInOrderByTsDesc(DataStatus.SAVE, employeeIds, employeeIds);
    }

    @Override
    public Page<CarDealer> getMyCarDealers(List<String> employeeIds, Integer pageIndex, Integer pageSize) {
        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Sort sort = CarDealer.getTsSort();
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        return this.carDealerRepository.findByDataStatusAndEmployeeIdInOrBusinessManIdsIn(DataStatus.SAVE, employeeIds, employeeIds, pageable);
    }

    @Override
    public Page<CarDealer> getChannelCarDealers(String employeeId, Integer pageIndex, Integer pageSize) {
        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Sort sort = CarDealer.getTsSort();
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        return this.carDealerRepository.findByDataStatusAndEmployeeIdOrBusinessManIdsContaining(DataStatus.SAVE, employeeId, employeeId, pageable);
    }

    @Override
    public Page<CarDealer> getSalesCarDealers(String employeeId, Integer pageIndex, Integer pageSize) {
        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Sort sort = CarDealer.getTsSort();
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        return this.carDealerRepository.findByDataStatusAndBusinessManIdsContaining(DataStatus.SAVE, employeeId, pageable);
    }


    public Page<CarDealer> getCarDealersByLoginUserId(Integer pageIndex, Integer pageSize, String loginUserId) {
        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        if (StringUtils.isBlank(loginUserId)) {
            return null;
        }
        Sort sort = CarDealer.getTsSort();
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        return carDealerRepository.findByDataStatusAndLoginUserId(DataStatus.SAVE, loginUserId, pageable);
    }


    /**
     * 事件倒序排列
     * @param currentPage
     * @return
     */
    @Override
    public Page<CarDealer> getAllOrderByTs(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage,20);
        return carDealerRepository.findAllByOrderByTsDesc(pr);
    }

    @Override
    public Page<CarDealer> searchCarDealers(String name, String loginUserId, Integer pageIndex, Integer pageSize) {
        Pageable pageable = new PageRequest(pageIndex, pageSize, CarDealer.getSortDESC("name"));
        return this.carDealerRepository.findByDataStatusAndLoginUserIdAndNameContaining(DataStatus.SAVE, loginUserId, name, pageable);
    }

    @Override
    public List<CarDealer> getCarDealersByEmployeeIds(List<String> employeeIds) {
        return baseRepository.findAllByDataStatusAndEmployeeIdInOrderByEmployeeId(DataStatus.SAVE, employeeIds);
    }

    @Override
    public List<CarDealer> getCarDealersByGroupId(String groupId) {
        return baseRepository.findAllByDataStatusAndDealerGroupId(DataStatus.SAVE, groupId);
    }

    @Override
    public List<CarDealer> getCardealerMore(List<String> ids) {
        return carDealerRepository.findAllByIdIn(ids);
    }

    public Page<CarDealer> searchCarDealers(Integer currentPage, CarDealer carDealerBean) {

        Query query = new Query();
        if (!org.springframework.util.StringUtils.isEmpty(carDealerBean.getName()))
            query.addCriteria(Criteria.where("name").regex(Pattern.compile("^.*"+ carDealerBean.getName() +".*$", Pattern.CASE_INSENSITIVE)));

        if (!org.springframework.util.StringUtils.isEmpty(carDealerBean.getOrginfoId()))
            query.addCriteria(Criteria.where("orginfoId").is(carDealerBean.getOrginfoId()));
        if (!org.springframework.util.StringUtils.isEmpty(carDealerBean.getEmployeeId()))
            query.addCriteria(Criteria.where("employeeId").is(carDealerBean.getEmployeeId()));

        if (carDealerBean.getBusinessManIds() != null && carDealerBean.getBusinessManIds().size()>0){
            query.addCriteria(Criteria.where("businessManIds").in(carDealerBean.getBusinessManIds()));
        }
        if (!org.springframework.util.StringUtils.isEmpty(carDealerBean.getCashSourceId()))
            query.addCriteria(Criteria.where("cashSourceId").is(carDealerBean.getCashSourceId()));
        if (!org.springframework.util.StringUtils.isEmpty(carDealerBean.getCooperationCashSourceId()))
            query.addCriteria(Criteria.where("cooperationCashSourceId").is(carDealerBean.getCooperationCashSourceId()));

        Pageable pageable = new PageRequest(currentPage,20,CarDealer.getTsSort());
        List list = mongoTemplate.find(query.with(pageable),CarDealer.class);
        Page page  = new PageImpl(list,pageable, mongoTemplate.count(query,CarDealer.class));
        return page;
    }

    @Override
    public List<CarDealer> getCarDealerByOrgIds(List<String> orgIds) {
        return baseRepository.findByDataStatusAndOrginfoIdIn(DataStatus.SAVE, orgIds);
    }

}
