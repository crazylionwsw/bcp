package com.fuze.bcp.statistics.service.impl;

import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.statistics.domain.ChargeFeePlan;
import com.fuze.bcp.statistics.domain.ChargeFeePlanDetail;
import com.fuze.bcp.statistics.repository.ChargeFeePlanDetailRepository;
import com.fuze.bcp.statistics.repository.ChargeFeePlanRepository;
import com.fuze.bcp.statistics.service.IChareFeePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by GQR on 2017/10/23.
 */
@Service
public class ChargeFeePlanServiceImpl extends BaseServiceImpl<ChargeFeePlan, ChargeFeePlanRepository> implements IChareFeePlanService {
    @Autowired
    ChargeFeePlanRepository chargeFeePlanRepository;

    @Autowired
    ChargeFeePlanDetailRepository chargeFeePlanDetailRepository;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;


    @Override
    public ChargeFeePlan findOneById(String id) {
        return chargeFeePlanRepository.findOne(id);
    }

    @Override
    public ChargeFeePlan findOneByIdAndStatus(String id, Integer status) {
        return chargeFeePlanRepository.findOneByIdAndStatus(id,status);
    }

    @Override
    public List<ChargeFeePlanDetail> findAllDetail() {
        return chargeFeePlanDetailRepository.findAll();
    }

    @Override
    public List<ChargeFeePlan> findByStatus(Integer status) {
        List<ChargeFeePlan> byStatus = chargeFeePlanRepository.findByStatus(status);
        return byStatus;
    }

    @Override
    public ChargeFeePlan findOneByTransactionId(String transactionId) {
        ChargeFeePlan chargeFeePlan = chargeFeePlanRepository.findOneByCustomerTransactionId(transactionId);
        return chargeFeePlan;
    }

    @Override
    public List<ChargeFeePlan> findAllByStatusOrderByCardealerId(Integer status) {
        return chargeFeePlanRepository.findAllByStatusOrderByCarDealerId(status);
    }

    @Override
    public List<ChargeFeePlan> findAllByStatusAndOrderTimeBeforeOrderByCardealerId(Integer status, String data) {
        return chargeFeePlanRepository.findAllByStatusAndSwingCardDateBeforeOrderByCarDealerId(status,data);
    }

    @Override
    public void deleteOneChargeFeePlan(ChargeFeePlan chargeFeePlan) {
        chargeFeePlanRepository.delete(chargeFeePlan);
    }

    @Override
    public ChargeFeePlan saveOneChargeFeePlan(ChargeFeePlan chargeFeePlan) {
        return chargeFeePlanRepository.save(chargeFeePlan);
    }

    @Override
    public Page<ChargeFeePlan> findChargeFeePlans(Integer currentPage) {

        PageRequest pageRequest=new PageRequest(currentPage,20);

        return chargeFeePlanRepository.findAllByOrderByTsDesc(pageRequest);
    }

    @Override
    public List<ChargeFeePlanDetail> saveDetail(List<ChargeFeePlanDetail> rp) {
        return  chargeFeePlanDetailRepository.save(rp);
    }

    @Override
    public void deleteDetailByChargeFeePlanId(String chargeFeePlanId) {
        chargeFeePlanDetailRepository.deleteByChargeFeePlanId(chargeFeePlanId);
    }

    @Override
    public List<ChargeFeePlanDetail> findOneDetail(String chargeFeePlanId) {
        return chargeFeePlanDetailRepository.findOneByChargeFeePlanId(chargeFeePlanId);
    }

    @Override
    public List<ChargeFeePlanDetail> queryAllDetaillByYearAndMonthAnd(String year, String month) {
        List<ChargeFeePlanDetail>   chargeFeePlanDetails = chargeFeePlanDetailRepository.findAllByYearAndMonthAndDataStatus(year,month, DataStatus.SAVE);

        return chargeFeePlanDetails;
    }

    @Override
    public ChargeFeePlan findByCustomerTransactionId(String customerTransactionId) {
        return chargeFeePlanDetailRepository.findByCustomerTransactionId(customerTransactionId);
    }

    @Override
    public Page<ChargeFeePlan> findAllBySearchBean(Class<ChargeFeePlan> chargeFeePlanClass, SearchBean searchBean, int stageNum) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));

        if (searchBean.getStatusName() != null && searchBean.getStatusValue() != null && searchBean.getStatusValue() != -1 ){
            query.addCriteria(Criteria.where(searchBean.getStatusName()).is(searchBean.getStatusValue()));
        }

        if(searchBean.getTimeName() != null && !StringUtils.isEmpty(searchBean.getDayTime())){
            query.addCriteria(Criteria.where(searchBean.getTimeName()).regex(searchBean.getDayTime(),"m"));
        }

        if (!this.checkSearchBeanNull(searchBean)){
            query.addCriteria(Criteria.where("customerTransactionId").in(this.getSearchTransactionIds(searchBean,stageNum)));
        }

        Pageable pageable = new PageRequest(searchBean.getCurrentPage(),searchBean.getPageSize());
        if (searchBean.getSortWay().equals("ASC")){
            query.with(ChargeFeePlan.getSortASC(searchBean.getSortName())).with(pageable);
        } else {
            query.with(ChargeFeePlan.getSortDESC(searchBean.getSortName())).with(pageable);
        }

        List list = mongoTemplate.find(query,ChargeFeePlan.class);
        Page page  = new PageImpl(list,pageable, mongoTemplate.count(query,ChargeFeePlan.class));
        return page;
    }

    public boolean checkSearchBeanNull(SearchBean searchBean){
        if (!StringUtils.isEmpty(searchBean.getName())){
            return  false;
        }
        if (!StringUtils.isEmpty(searchBean.getIdentifyNo())){
            return  false;
        }
        return true;
    }
}
