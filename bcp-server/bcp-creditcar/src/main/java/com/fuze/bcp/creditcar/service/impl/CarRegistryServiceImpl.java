package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.api.creditcar.bean.DMVPledgeBean;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistryBean;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.AppointPayment;
import com.fuze.bcp.creditcar.domain.CarRegistry;
import com.fuze.bcp.creditcar.domain.CarTransfer;
import com.fuze.bcp.creditcar.domain.DMVPledge;
import com.fuze.bcp.creditcar.repository.CarRegistryRepository;
import com.fuze.bcp.creditcar.service.ICarRegistryService;
import com.fuze.bcp.utils.SimpleUtils;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GQR on 2017/8/19.
 */
@Service
public class CarRegistryServiceImpl extends BaseBillServiceImpl<CarRegistry, CarRegistryRepository> implements ICarRegistryService {

    @Autowired
    CarRegistryRepository carRegistryRepository;

    @Override
    public CarRegistry findByCustomer(CustomerBean customerBean) {
        return carRegistryRepository.findOneByDataStatusAndCustomerId(DataStatus.SAVE,customerBean.getId());
    }

    @Override
    public List<ObjectId> getRegistryFinishTransactionObjectIds(String date) {
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if (!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s", date);
            criteria.and("registryDate").regex(regex, "m");
        } else {
            criteria.and("registryDate").ne(null);
        }
        List<ObjectId> datas = getIdsList(Query.query(criteria), "so_car_registry", "customerTransactionId");
        return datas;
    }

    /**
     * 获取部门月份日报统计
     * @param date
     * @param t
     * @param loginUserId
     * @return
     */
    public Map<Object,Object> getEmployeeReport(String date, AppointPayment t , String loginUserId) {
        Map<Object,Object> map = new HashMap<Object,Object>();
        String year = null;
        if(date == null){
            String  da= SimpleUtils.getCreateTime();
            year=da.substring(0,7);
            // year="2017-11";
        }
        //获取符合条件的transactionId
        List<String>    transactionIds = queryTransactionIds(null,loginUserId,null,null,null,null, getNoCanceledStatus());

        //上牌完成数据  CarRegistry
        Criteria c = Criteria.where("approveStatus").is(CarRegistryBean.APPROVE_PASSED);
        if(!StringUtils.isEmpty(transactionIds)){
            c =c.and("customerTransactionId").in(transactionIds);
        }
        c = c.and("dataStatus").is(DataStatus.SAVE);
        c = c.and("approveDate").regex(String.format("^%s", year), "m");
        List<CarRegistry> carRegistriesCount = mongoTemplate.find(new Query(c), CarRegistry.class);
        //过户完成
        Criteria criteria1 = Criteria.where("approveStatus").is(CarTransfer.APPROVE_PASSED).and("dataStatus").is(DataStatus.SAVE).and("transferDate").regex(String.format("^%s", year), "m");
        if(!StringUtils.isEmpty(transactionIds)){
            criteria1 =criteria1.and("customerTransactionId").in(transactionIds);
        }
        Query ferquery = new Query(criteria1);
        List<CarTransfer> cartransferCount = mongoTemplate.find(ferquery, CarTransfer.class);
        map.put("carRegistryCount",carRegistriesCount.size()+cartransferCount.size());
        // 抵押 dmvpledge

        Criteria cri = Criteria.where("dataStatus").is(DataStatus.SAVE).and("status").is(DMVPledgeBean.STATUS_END);
        if(transactionIds != null){
            cri = cri.and("customerTransactionId").in(transactionIds);
        }
        if(year == null){
            cri =cri.and("pledgeEndTime").regex(String.format("^%s", year), "m");
        }
        Query dmvpquery = new Query(cri);
        List<DMVPledge> dmvpCount = mongoTemplate.find(dmvpquery, DMVPledge.class);
        map.put("dmvpCount",dmvpCount.size());
        if(map != null){
            return getDailyAmountTotalByGroupField(null,loginUserId ,null,date,map,"chargePaymentWay", transactionIds);
        }
        return null;
    }

    private Map<Object, Object> getDailyAmountTotalByGroupField(String orginfoId, String employeeId, String cardealerId, String date, Map<Object, Object> dataMap,String groupField,List<String> transactionIds) {


        String year = null;
        if(date == null){
            String  da= SimpleUtils.getCreateTime();
            year=da.substring(0,7);
        }
        //上牌完成的费用
        List<AggregationOperation> aos = new ArrayList<AggregationOperation>();
        Criteria acriteria = Criteria.where("approveStatus").is(CarRegistryBean.APPROVE_PASSED);
        if(!StringUtils.isEmpty(year)){
            String regex = String.format("^%s",year);
            acriteria =acriteria.and("registryDate").regex(regex, "m");
        }
        if(!StringUtils.isEmpty(transactionIds)){
            acriteria =acriteria.and("custoomerTransactionId").in(transactionIds);
        }
        List<String> allFieldList1 = (List<String>)getAllFieldList(new Query(acriteria), "so_car_registry", "customerTransactionId");
        Criteria tcr = Criteria.where("customerTransactionId").in(allFieldList1);
        List<ObjectId> loanIds = getIdsList(new Query(tcr), "so_purchasecar", "customerLoanId");
        aos.add(Aggregation.match(Criteria.where("_id").in(loanIds)));
        aos.add(Aggregation.group().count().as("count")
                .sum("creditAmount").as("totalAmount")
                .sum("bankFeeAmount").as("totalCharge")
                .sum("loanServiceFee").as("totalServiceFee"));
        AggregationResults<BasicDBObject> wholeResults = mongoTemplate.aggregate(Aggregation.newAggregation(aos), "cus_loan", BasicDBObject.class);
        List<BasicDBObject> result = wholeResults.getMappedResults();
        fillMapResult("carRegistry", groupField, result, dataMap);

        //渠道上牌完成（登记证已收取）
        List<AggregationOperation> caraos = new ArrayList<AggregationOperation>();
        Criteria carCri = Criteria.where("customerTransactionId").in(transactionIds);
        if(!StringUtils.isEmpty(year)){
            String regex = String.format("^%s",year);
            carCri =carCri.and("pledgeDateReceiveTime").regex(regex, "m");
        }
        List<String> cartransacid = (List<String>)getAllFieldList(new Query(carCri), "so_dmvpledge", "customerTransactionId");
        Criteria tids = Criteria.where("customerTransactionId").in(cartransacid);
        List<ObjectId> carloanIds = getIdsList(new Query(tids), "so_purchasecar", "customerLoanId");
        caraos.add(Aggregation.match(Criteria.where("_id").in(carloanIds)));
        caraos.add(Aggregation.group().count().as("count") .sum("creditAmount").as("totalAmount"));
        AggregationResults<BasicDBObject> carwholeResults = mongoTemplate.aggregate(Aggregation.newAggregation(caraos), "cus_loan", BasicDBObject.class);
        List<BasicDBObject> results = carwholeResults.getMappedResults();
        fillMapResult("cardealerCar", groupField, results, dataMap);


        //过户完成
        List<AggregationOperation> transferAos= new ArrayList<AggregationOperation>();
        Criteria criteria1 = Criteria.where("approveStatus").is(CarTransfer.APPROVE_PASSED).and("customerTransactionId").in(transactionIds).and("transferDate").regex(String.format("^%s", year), "m");
        if(!StringUtils.isEmpty(employeeId)){
            criteria1 =criteria1.and("loginUserId").is(employeeId);
        }
        Query ferquery = new Query(criteria1);
        List<String> tranferIds = (List<String>)getAllFieldList(ferquery, "so_cartransfer", "customerTransactionId");
        Criteria tranferCri = Criteria.where("customerTransactionId").in(tranferIds);
        List<ObjectId> cusLoanIds = getIdsList(new Query(tranferCri), "so_purchasecar", "customerLoanId");
        transferAos.add(Aggregation.match(Criteria.where("_id").in(cusLoanIds)));
        transferAos.add(Aggregation.group().count().as("count")
                .sum("creditAmount").as("totalAmount")
                .sum("bankFeeAmount").as("totalCharge")
                .sum("loanServiceFee").as("totalServiceFee"));
        AggregationResults<BasicDBObject> wholeResultsss = mongoTemplate.aggregate(Aggregation.newAggregation(transferAos), "cus_loan", BasicDBObject.class);
        List<BasicDBObject> resultss = wholeResultsss.getMappedResults();
        fillMapResult("carTranfer", groupField, resultss, dataMap);


        //抵押完成
        List<AggregationOperation> aoss= new ArrayList<AggregationOperation>();
        Criteria criteriasa = Criteria.where("status").nin(getCanceledStatus());

        List<String> allFieldList =(List<String>)getAllFieldList(new Query(criteriasa), "cus_transaction", "_id");
        Criteria dmpCri= Criteria.where("status").is(DMVPledgeBean.STATUS_END).and("customerTransactionId").in(transactionIds).and("pledgeEndTime").regex(String.format("^%s",year), "m");
        List<String> orderTransaction = (List<String>)getAllFieldList(new Query(dmpCri), "so_dmvpledge", "customerTransactionId");
        List<ObjectId> idsList = getIdsList(new Query(Criteria.where("customerTransactionId").in(orderTransaction)), "so_purchasecar", "customerLoanId");
        Criteria loancri = Criteria.where("_id").in(idsList);
        aoss.add(Aggregation.match(loancri));
        aoss.add(Aggregation.group(groupField).count().as("count")
                .sum("creditAmount").as("totalAmount")
                .sum("bankFeeAmount").as("totalCharge")
                .sum("loanServiceFee").as("totalServiceFee"));
        AggregationResults<BasicDBObject> wholeResultss = mongoTemplate.aggregate(Aggregation.newAggregation(aoss), "cus_loan", BasicDBObject.class);
        List<BasicDBObject> diyaresult = wholeResultss.getMappedResults();
        fillMapResult("dmvpledge", groupField, diyaresult, dataMap);
        return dataMap;
    }

    private void fillMapResult(String type, String groupField, List<BasicDBObject> result, Map<Object, Object> dataMap) {
        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                BasicDBObject bbo = result.get(i);
                dataMap.put("total" + type + "Amount" , bbo.get("totalAmount") == null ? 0.0 : bbo.get("totalAmount"));
                dataMap.put("total" + type + "Charge" , bbo.get("totalCharge") == null ? 0.0 : bbo.get("totalCharge"));
                dataMap.put("total" + type + "ServiceFee", bbo.get("totalServiceFee") == null ? 0.0 : bbo.get("totalServiceFee"));
                dataMap.put("total" + type + "Count" , bbo.get("count") == null ? 0.0 : bbo.get("count"));
            }
        }else {
            dataMap.put("total" + type + "Amount" ,  0.0 );
            dataMap.put("total" + type + "Charge" ,  0.0 );
            dataMap.put("total" + type + "ServiceFee", 0.0 );
            dataMap.put("total" + type + "Count" ,0.0 );
        }
    }



}
