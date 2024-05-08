package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.AppointPayment;
import com.fuze.bcp.creditcar.repository.AppointPaymentRepository;
import com.fuze.bcp.creditcar.service.IAppointPaymentService;
import com.fuze.bcp.creditcar.service.IBankCardApplyService;
import com.fuze.bcp.creditcar.service.IPickupCarService;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zqw on 2017/8/16.
 */
@Service
public class AppointPaymentServiceImpl extends BaseBillServiceImpl<AppointPayment,AppointPaymentRepository> implements IAppointPaymentService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    IPickupCarService iPickupCarService;

    @Autowired
    IBankCardApplyService iBankCardApplyService;

    @Autowired
    AppointPaymentRepository appointPaymentRepository;
    @Override
    public Page<AppointPayment> findByDataStatusAndStatusOrderByTsDesc(Integer currentPage, Integer save, Integer status) {
        PageRequest page = new PageRequest(currentPage, 20);
        return appointPaymentRepository.findByDataStatusAndStatusOrderByTsDesc(page,save,status);
    }

    @Override
    public Page<AppointPayment> findByCustomerIdInAndDataStatusAndStatusOrderByTsDesc(List<String> customerIds, Integer save, int status, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return appointPaymentRepository.findByCustomerIdInAndDataStatusAndStatusOrderByTsDesc(page,customerIds,save,status);
    }

    @Override
    public Map<Object, Object> getDailyReport(String orgId,String date, AppointPayment appointPayment) {
        Map dataMap =  super.getDailyReport(orgId,date, appointPayment);

        return getDailyPayAmountTotal(orgId,null,null,date,dataMap);
    }

    public  Map<Object,Object>  getEmployeeReport(String employeeId, String date, AppointPayment t) {
        Map<Object,Object>  resultMap = super.getBillReportData(null,employeeId,null,date,t);
        return getDailyPayAmountTotal(null,employeeId,null,date,resultMap);
    }

    private Map<Object,Object> getDailyPayAmountTotal(String orginfoId,String employeeId,String cardealerId,String date,Map<Object,Object> map) {
        //获取符合条件的transactionId
        List<String>    transactionIds = queryTransactionIds(orginfoId,employeeId,cardealerId,null,null,null, null);

        List<AggregationOperation> aos = new ArrayList<AggregationOperation>();
        Criteria cri = Criteria.where("approveStatus").is(ApproveStatus.APPROVE_PASSED);
        if(!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s",date);
            cri =  cri.and("approveDate").regex(regex, "m");
        }
        if(transactionIds != null) {
            cri =  cri.and("customerTransactionId").in(transactionIds);
        }
        aos.add(Aggregation.match(cri));
        aos.add( Aggregation.group("dataStatus").sum("appointPayAmount").as("totalPaidAmount"));
        AggregationResults<BasicDBObject> wholeResults = mongoTemplate.aggregate(Aggregation.newAggregation(aos), AppointPayment.class, BasicDBObject.class);
        List<BasicDBObject> result = wholeResults.getMappedResults();
        if(result.size()>0){
            map.put("totalPaidAmount",result.get(0).get("totalPaidAmount"));
        }else{
            map.put("totalPaidAmount",0.0);
        }
        return map;

    }


    @Override
    public List<String> getDailyPaidMoneyOrderIds(String date) {


        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if(!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s",date);
            criteria.and("approveDate").regex(regex, "m");
        }else{
            criteria.and("approveDate").ne(null);
        }
       // criteria.and("status").nin(getCanceledStatus());
        criteria.and("approveStatus").is(ApproveStatus.APPROVE_PASSED);

        List<String>    customerTransactionIds = (List<String>)getAllFieldList(Query.query(criteria),"so_appoint_payment","customerTransactionId");
        criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
       // criteria.and("status").nin(getCanceledStatus());
        criteria = criteria.and("customerTransactionId").in(customerTransactionIds);

        List<String>    customertransactionIds = (List<String>)getAllFieldList(Query.query(criteria),"so_car_registry","customerTransactionId");

        return customertransactionIds;
    }

    @Override
    public List<AppointPayment> findAll(Sort sort) {
        return appointPaymentRepository.findAll(AppointPayment.getTsSort());
    }

    @Override
    public List<AppointPayment> findAllByCardearlerId(String carDealerId, Sort sort) {
        return appointPaymentRepository.findByCarDealerId(carDealerId,AppointPayment.getSortASC("ts"));
    }

    @Override
    public List<AppointPayment> findAllByDataStatusAndApproveStatus(Integer ds, Integer as, Sort sort) {
        return appointPaymentRepository.findAllByDataStatusAndApproveStatus(DataStatus.SAVE,as,AppointPayment.getTsSort());
    }

    @Override
    public List<AppointPayment> getAppointPaymentBySelectTime(String selectTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if(!StringUtils.isEmpty(selectTime)){
            query.addCriteria(Criteria.where("ts").regex(String.format(selectTime,"m")));
        }
        query.with(AppointPayment.getSortASC("payTime"));
        List list = mongoTemplate.find(query,AppointPayment.class);
        return list;
    }
}
