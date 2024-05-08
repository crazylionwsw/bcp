package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.creditcar.repository.PurchaseCarOrderRepository;
import com.fuze.bcp.creditcar.service.*;
import com.fuze.bcp.service.MappingService;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
 * Created by Lily on 2017/7/19.
 */
@Service
public class OrderServiceImpl extends BaseBillServiceImpl<PurchaseCarOrder, PurchaseCarOrderRepository> implements IOrderService {

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    PurchaseCarOrderRepository purchaseCarOrderRepository;


    @Override
    public PurchaseCarOrder findByVehicleEvaluateInfoId(String id) {
        return baseRepository.findByVehicleEvaluateInfoId(id);
    }

    @Override
    public PurchaseCarOrder findByVehicleEvaluateInfoIdAndDataStatus(String id,Integer ds) {
        return baseRepository.findByVehicleEvaluateInfoIdAndDataStatus(id,ds);
    }

    @Override
    public List<PurchaseCarOrder> findAllByVehicleEvaluateInfoId(String id) {
        return baseRepository.findAllByVehicleEvaluateInfoId(id);
    }

    @Override
    public List<PurchaseCarOrder> findAllByVehicleEvaluateInfoIdAndDataStatus(String id, Integer ds) {
        return baseRepository.findAllByVehicleEvaluateInfoIdAndDataStatus(id,ds);
    }

    @Override
    public List<PurchaseCarOrder> findAllByDataStatusAndApproveStatus(Integer ds, Integer as, Sort sort) {
        return baseRepository.findAllByDataStatusAndApproveStatus(DataStatus.SAVE, as, PurchaseCarOrder.getTsSort());
    }

    @Autowired
    MappingService mappingService;

    @Autowired
    ICustomerDemandService iCustomerDemandService;

    @Autowired
    IDmvpledgeService iDmvpledgeService;

    @Autowired
    IBankCardApplyService iBankCardApplyService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    IPickupCarService iPickupCarService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    IAppointPaymentService iAppointPaymentService;

    @Autowired
    ICarRegistryService iCarRegistryService;

    @Autowired
    ICarTransferService iCarTransferService;

    /**
     * 日报
     *
     * @param date
     * @param
     * @return
     */
    public Map<Object, Object> getDailyReport(String orgId,String date, PurchaseCarOrder purchaseCarOrder) {
        Map<Object, Object> dataMap = super.getDailyReport(orgId ,date, purchaseCarOrder);
        return getDailyTotalByGroupField(orgId, null, null, date, dataMap,"chargePaymentWay");
    }

    public Map<Object, Object> getDailyReport(String orgId,String date, PurchaseCarOrder purchaseCarOrder,String groupField) {
        Map<Object, Object> dataMap = super.getDailyReport(orgId ,date, purchaseCarOrder);
        return getDailyTotalByGroupField(orgId, null, null, date, dataMap,groupField);
    }

    /**
     * 按照渠道进行业务统计
     *
     * @param carDealerId     渠道ID
     * @param date              日期
     * @param t                 实体
     * @return                  渠道的查询统计
     */
    public Map<Object,Object>  getCarDealerReport(String carDealerId, String date, PurchaseCarOrder t) {
        Map<Object, Object> resultMap = super.getCarDealerReport(carDealerId, date, t);
        return getDailyTotalByGroupField(null, null, carDealerId, date, resultMap,null);
    }

    /**
     * 按照员工进行当月业务统计
     *
     * @param date
     * @param purchaseCarOrder
     * @param employeeId
     * @return
     */
    public Map<Object, Object> getEmployeeReport(String employeeId,String date, PurchaseCarOrder purchaseCarOrder) {
        Map<Object, Object> resultMap = super.getEmployeeReport(employeeId, date,purchaseCarOrder);
        return getDailyTotalByGroupField(null, employeeId, null, date, resultMap,null);
    }

    /**
     * 查询符合条件的CustomerLoadIds
     * @return
     */
    private List<ObjectId>    queryCustomerLoanIdsFromBill(List<String> transactionIds,String date,Integer approveStatus,String dateFieldName){
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if(!StringUtils.isEmpty(date)){
            String regex = String.format("^%s",date);
            criteria =criteria.and(dateFieldName).regex(regex, "m");
        }
        if(!StringUtils.isEmpty(approveStatus)){
            criteria =criteria.and("approveStatus").is(approveStatus);
        }
        if(transactionIds!= null){
            criteria.and("customerTransactionId").in(transactionIds);
        }
        List<ObjectId> objectIds = getIdsList(Query.query(criteria),"so_purchasecar","customerLoanId");
        return objectIds;
    }

    /**
     * 获取指定日期刷卡完成的transactionIds
     * @param orginfoID
     * @param employeeID
     * @param cardealerId
     * @param cashSourceId
     * @param businessTypeCode
     * @param date
     * @return
     */
    private List<String>    querySwingTransactionIds(String orginfoID,String employeeID,String cardealerId,String cashSourceId,String businessTypeCode,String date){
        List<ObjectId> customerTransactionIds = iBankCardApplyService.getDailySwipingMoneyTransactionObjectIds(date);
        return queryTransactionIds(orginfoID,employeeID,cardealerId,null,null,customerTransactionIds,getNoCanceledStatus());
    }
    /**
     * 获取指定日期抵押完成的transactionIds
     * @param orginfoID
     * @param employeeID
     * @param cardealerId
     * @param cashSourceId
     * @param businessTypeCode
     * @param date
     * @return
     */
    private List<String>    queryDmvPledgeTransactionIds(String orginfoID,String employeeID,String cardealerId,String cashSourceId,String businessTypeCode,String date){
        List<ObjectId> customerTransactionIds = iDmvpledgeService.getDvmpFinishTransactionObjectIds(date);
        return queryTransactionIds(orginfoID,employeeID,cardealerId,null,null,customerTransactionIds,getNoCanceledStatus());
    }

    /**
     * 获取指定日期登记证已收取的transactionIds
     * @param orginfoID
     * @param employeeID
     * @param cardealerId
     * @param cashSourceId
     * @param businessTypeCode
     * @param date
     * @return
     */
    private List<String>    queryReceiveTransactionIds(String orginfoID,String employeeID,String cardealerId,String cashSourceId,String businessTypeCode,String date){
        List<ObjectId> customerTransactionIds = iDmvpledgeService.getReceiveFinishTransactionObjectIds(date);
        return queryTransactionIds(orginfoID,employeeID,cardealerId,null,null,customerTransactionIds,getNoCanceledStatus());
    }

    /**
     * 获取指定日期上牌完成的transactionIds
     * @param orginfoID
     * @param employeeID
     * @param cardealerId
     * @param cashSourceId
     * @param businessTypeCode
     * @param date
     * @return
     */
    private List<String>    queryCarRegistryTransactionIds(String orginfoID,String employeeID,String cardealerId,String cashSourceId,String businessTypeCode,String date){
        List<ObjectId> customerTransactionIds = iCarRegistryService.getRegistryFinishTransactionObjectIds(date);
        return queryTransactionIds(orginfoID,employeeID,cardealerId,null,null,customerTransactionIds,getNoCanceledStatus());
    }

    /**
     * 获取指定日期过户完成的transactionIds
     * @param orginfoID
     * @param employeeID
     * @param cardealerId
     * @param cashSourceId
     * @param businessTypeCode
     * @param date
     * @return
     */
    private List<String>    queryCarTransferTransactionIds(String orginfoID,String employeeID,String cardealerId,String cashSourceId,String businessTypeCode,String date){
        List<ObjectId> customerTransactionIds = iCarTransferService.getCarTransFinishTransactionObjectIds(date);
        return queryTransactionIds(orginfoID,employeeID,cardealerId,null,null,customerTransactionIds,getNoCanceledStatus());
    }

      /*
         // pc主页面  获取当天的申请额，手续费额，服务费
      */
    private Map<Object, Object> getDailyTotalByGroupField(String orginfoID, String employeeID, String cardealerId, String date, Map<Object, Object> dataMap, String groupField) {
        if (StringUtils.isEmpty(groupField)){
            groupField = "_id";
        }
        //获取符合条件的transactionId
        List<String>    transactionIds = queryTransactionIds(orginfoID,employeeID,cardealerId,null,null,null, getNoCanceledStatus());

        //获取符合条件的CustomerLoadId
        List<ObjectId>  loanIds = queryCustomerLoanIdsFromBill(transactionIds,date,null,"ts");

        List<AggregationOperation> aos = new ArrayList<AggregationOperation>();
        aos.add(Aggregation.match( Criteria.where("dataStatus").is(DataStatus.SAVE).and("_id").in(loanIds)));
        aos.add(Aggregation.group(groupField).count().as("count")
                    .sum("creditAmount").as("totalAmount")
                    .sum("bankFeeAmount").as("totalCharge")
                    .sum("loanServiceFee").as("totalServiceFee"));

        AggregationResults<BasicDBObject> wholeResults = mongoTemplate.aggregate(Aggregation.newAggregation(aos), "cus_loan", BasicDBObject.class);
        List<BasicDBObject> result = wholeResults.getMappedResults();
        fillMapResult("All",groupField, result, dataMap);

        //签约通过
        loanIds = queryCustomerLoanIdsFromBill(transactionIds,date,ApproveStatus.APPROVE_PASSED,"approveDate");
        Criteria cri= Criteria.where("_id").in(loanIds);
        Aggregation  aggregation = Aggregation.
                newAggregation(Aggregation.match(cri)
                        , Aggregation.group(groupField)
                                .sum("creditAmount").as("totalAmount")
                                .sum("bankFeeAmount").as("totalCharge")
                                .sum("loanServiceFee").as("totalServiceFee"));
        wholeResults = mongoTemplate.aggregate(aggregation, "cus_loan", BasicDBObject.class);
        List<BasicDBObject> result2 = wholeResults.getMappedResults();
        fillMapResult("Pass",groupField, result2, dataMap);

        //获取刷卡日期内的TransactionIDs
        transactionIds = querySwingTransactionIds(orginfoID,employeeID,cardealerId,null,null,date);
        loanIds = queryCustomerLoanIdsFromBill(transactionIds,null,null,null);
        aggregation = Aggregation.
                newAggregation( Aggregation.match(Criteria.where("_id").in(loanIds))
                        , Aggregation.group(groupField)
                                .sum("creditAmount").as("totalAmount")
                                .sum("bankFeeAmount").as("totalCharge")
                                .sum("loanServiceFee").as("totalServiceFee").count().as("tt1"));
        wholeResults = mongoTemplate.aggregate(aggregation, "cus_loan", BasicDBObject.class);
        List<BasicDBObject> result4 = wholeResults.getMappedResults();
        fillMapResult("Swing",groupField, result4, dataMap);

        //抵押完成的数据
        transactionIds = queryDmvPledgeTransactionIds(orginfoID,employeeID,cardealerId,null,null,date);
        loanIds = queryCustomerLoanIdsFromBill(transactionIds,null,null,null);
        aggregation = Aggregation.
                newAggregation( Aggregation.match(Criteria.where("_id").in(loanIds))
                        , Aggregation.group(groupField)
                                .sum("creditAmount").as("totalAmount")
                                .sum("bankFeeAmount").as("totalCharge")
                                .sum("loanServiceFee").as("totalServiceFee"));
        wholeResults = mongoTemplate.aggregate(aggregation, "cus_loan", BasicDBObject.class);
        List<BasicDBObject> result3 = wholeResults.getMappedResults();
        fillMapResult("Dmvp",groupField, result3, dataMap);

        //上牌完成的数据
        transactionIds = queryCarRegistryTransactionIds(orginfoID,employeeID,cardealerId,null,null,date);
        loanIds = queryCustomerLoanIdsFromBill(transactionIds,null,null,null);
        aggregation = Aggregation.
                newAggregation( Aggregation.match(Criteria.where("_id").in(loanIds))
                        , Aggregation.group(groupField)
                                .sum("creditAmount").as("totalAmount")
                                .sum("bankFeeAmount").as("totalCharge")
                                .sum("loanServiceFee").as("totalServiceFee"));
        wholeResults = mongoTemplate.aggregate(aggregation, "cus_loan", BasicDBObject.class);
        List<BasicDBObject> result5 = wholeResults.getMappedResults();
        fillMapResult("Registry",groupField, result5, dataMap);

        //过户完成的数据
        transactionIds = queryCarTransferTransactionIds(orginfoID,employeeID,cardealerId,null,null,date);
        loanIds = queryCustomerLoanIdsFromBill(transactionIds,null,null,null);
        aggregation = Aggregation.
                newAggregation( Aggregation.match(Criteria.where("_id").in(loanIds))
                        , Aggregation.group(groupField)
                                .sum("creditAmount").as("totalAmount")
                                .sum("bankFeeAmount").as("totalCharge")
                                .sum("loanServiceFee").as("totalServiceFee"));
        wholeResults = mongoTemplate.aggregate(aggregation, "cus_loan", BasicDBObject.class);
        List<BasicDBObject> result6 = wholeResults.getMappedResults();
        fillMapResult("Trans",groupField, result6, dataMap);

        //登记证已收取【客户抵押资料签收完成】的数据
        transactionIds = queryReceiveTransactionIds(orginfoID,employeeID,cardealerId,null,null,date);
        loanIds = queryCustomerLoanIdsFromBill(transactionIds,null,null,null);
        aggregation = Aggregation.
                newAggregation( Aggregation.match(Criteria.where("_id").in(loanIds))
                        , Aggregation.group(groupField)
                                .sum("creditAmount").as("totalAmount")
                                .sum("bankFeeAmount").as("totalCharge")
                                .sum("loanServiceFee").as("totalServiceFee"));
        wholeResults = mongoTemplate.aggregate(aggregation, "cus_loan", BasicDBObject.class);
        List<BasicDBObject> result7 = wholeResults.getMappedResults();
        fillMapResult("Receive",groupField, result7, dataMap);

        return dataMap;
    }

    private void fillMapResult(String type, String groupField, List<BasicDBObject> result, Map<Object, Object> dataMap) {
        List<Map> chargeWays = (List<Map>) iParamBizService.actGetList("BANK_CHARGE_PAYMENTWAY").getD();
        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                BasicDBObject bbo = result.get(i);
                dataMap.put("total" + type + "Amount" + bbo.getString("_id"), bbo.get("totalAmount") == null ? 0.0 : bbo.get("totalAmount"));
                dataMap.put("total" + type + "Charge" + bbo.getString("_id"), bbo.get("totalCharge") == null ? 0.0 : bbo.get("totalCharge"));
                dataMap.put("total" + type + "ServiceFee" + bbo.getString("_id"), bbo.get("totalServiceFee") == null ? 0.0 : bbo.get("totalServiceFee"));
                dataMap.put("total" + type + "Count" + bbo.getString("_id"), bbo.get("count") == null ? 0 : bbo.get("count"));
            }
        } else {
            if (groupField.equals("chargePaymentWay")) {
                for (int i = 0; i < chargeWays.size(); i++) {
                    dataMap.put("totalAmount" + (String) chargeWays.get(i).get("code"), 0.0);
                    dataMap.put("totalCharge" + (String) chargeWays.get(i).get("code"), 0.0);
                    dataMap.put("totalServiceFee" + (String) chargeWays.get(i).get("code"), 0.0);
                    dataMap.put("totalServiceFee" + (String) chargeWays.get(i).get("code"), 0.0);
                }
            }
        }
    }

    @Override
    public List<PurchaseCarOrder> findAll(Sort sort) {
        return baseRepository.findAll(PurchaseCarOrder.getTsSort());
    }


    /**
     * 获取正在交易中的评估单ID列表
     */
    public List<String> getUsedValuationIds(){
        List<String> tranIDs = iCustomerTransactionBizService.actGetAllUnFinishTransactionIds().getD();

        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE).and("customerTransactionId").in(tranIDs).and("approveStatus").is(ApproveStatus.APPROVE_PASSED);
        return (List<String>) getAllFieldList(Query.query(criteria),"so_purchasecar","vehicleEvaluateInfoId");
    }


}
