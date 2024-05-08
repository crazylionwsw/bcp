package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.CarValuation;
import com.fuze.bcp.creditcar.repository.CarValuationRepository;
import com.fuze.bcp.creditcar.service.ICarValuationService;
import com.fuze.bcp.creditcar.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 2017/8/14.
 */
@Service
public class CarValuationServiceImpl extends BaseBillServiceImpl<CarValuation, CarValuationRepository> implements ICarValuationService {

    @Autowired
    CarValuationRepository carValuationRepository;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<CarValuation> findAllByCarDealerId(String carDealerId) {
        List<Integer> as = new ArrayList<>();
        as.add(ApproveStatus.APPROVE_INIT);
        as.add(ApproveStatus.APPROVE_ONGOING);
        as.add(ApproveStatus.APPROVE_PASSED);

        return carValuationRepository.findAllByCarDealerIdAndApproveStatusIn(carDealerId,as);
    }

    @Override
    public CarValuation findOneByVin(String vin) {
        return carValuationRepository.findOneByVinOrderByTsDesc(vin);
    }

    @Override
    public CarValuation findOneByCarTypeIdAndVin(String carTypeId, String vin) {
        return carValuationRepository.findOneByCarTypeIdAndVin(carTypeId,vin);
    }

    public CarValuation findAvailableOneByVin(String vin) {
        return carValuationRepository.findOneByVinAndDataStatus(vin, DataStatus.SAVE);
    }

    @Override
    public Page<CarValuation> getAvailableCarValuations(Integer page) {
        PageRequest pr = new PageRequest(page, 20);
        return carValuationRepository.findAllByDataStatus(DataStatus.SAVE, pr);
    }


    @Override
    public Page<CarValuation> getAvailableCarValuations(Integer pageindex, Integer pagesize) {
        PageRequest pr = new PageRequest(pageindex, pagesize, CarValuation.getTsSort());
        return carValuationRepository.findAllByFinishOrderAndDataStatus(false, DataStatus.SAVE, pr);
    }

    @Override
    public Page<CarValuation> getAvailableCarValuations(String userId, Integer pageindex, Integer pagesize) {
        PageRequest pr = new PageRequest(pageindex, pagesize, CarValuation.getTsSort());

        List<Integer> as = new ArrayList<>();
        as.add(ApproveStatus.APPROVE_INIT);
        as.add(ApproveStatus.APPROVE_ONGOING);
        as.add(ApproveStatus.APPROVE_PASSED);
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        criteria.and("loginUserId").is(userId).and("approveStatus").in(as);
        criteria.and("_id").nin(iOrderService.getUsedValuationIds());
        List<CarValuation> carValuations =  mongoTemplate.find( Query.query(criteria).with(pr),CarValuation.class);
        return  new PageImpl<CarValuation>(carValuations,pr,mongoTemplate.count(Query.query(criteria), CarValuation.class));
    }

    @Override
    public Page<CarValuation> getAllCarValuatiOrderByTs(Integer page) {
        PageRequest pr = new PageRequest(page, 20);
        return carValuationRepository.findAllByOrderByTsDesc(pr);
    }

    @Override
    public Page<CarValuation> getAllByApproveStatusOrderByTsDesc(int approveStatus, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return carValuationRepository.findAllByApproveStatusOrderByTsDesc(approveStatus,page);
    }
}
