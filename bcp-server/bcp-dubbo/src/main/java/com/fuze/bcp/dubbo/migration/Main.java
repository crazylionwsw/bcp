package com.fuze.bcp.dubbo.migration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.workflow.bean.SignCondition;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bd.domain.CarType;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.RateType;
import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.creditcar.domain.CancelOrder;
import com.fuze.bcp.creditcar.domain.CreditPhotograph;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.customer.domain.CustomerCar;
import com.fuze.bcp.customer.domain.CustomerLoan;
import com.fuze.bcp.dubbo.migration.mongo.MongoConnect;
import com.fuze.bcp.dubbo.migration.mongo.ParamObject;
import com.fuze.bcp.sys.domain.SysParam;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.workflow.domain.WorkFlowBill;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/10/16.
 */
public class Main {

    static List<ParamObject> transactionList = new ArrayList<>();

    static {

        transactionList.add(
                new ParamObject("com.fuze.bcp.dubbo.migration.service.impl.OrderImpl", "so_purchasecar", "com.fuze.bcp.creditcar.domain.PurchaseCarOrder")); // 订单
//        transactionList.add(
//                new ParamObject("com.fuze.bcp.dubbo.migration.service.impl.CarValuationImpl", "so_purchasecar", "com.fuze.bcp.creditcar.domain.CarValuation")); // 评估单
        transactionList.add(
                new ParamObject("com.fuze.bcp.dubbo.migration.service.impl.BankCardImpl", "so_bankcardapply", "com.fuze.bcp.creditcar.domain.BankCardApply")); // 银行制卡
        transactionList.add(
                new ParamObject("com.fuze.bcp.dubbo.migration.service.impl.AppointSwipingCardImpl", "so_pickupcar", "com.fuze.bcp.creditcar.domain.AppointSwipingCard")); // 预约刷卡
        transactionList.add(
                new ParamObject("com.fuze.bcp.dubbo.migration.service.impl.AppointPaymentImpl", "so_pickupcar", "com.fuze.bcp.creditcar.domain.AppointPayment")); // 预约垫资
        transactionList.add(
                new ParamObject("com.fuze.bcp.dubbo.migration.service.impl.CarRegistryImpl", "so_pickupcar", "com.fuze.bcp.creditcar.domain.CarRegistry")); // 车辆上牌
        transactionList.add(
                new ParamObject("com.fuze.bcp.dubbo.migration.service.impl.CarTransferImpl", "so_pickupcar", "com.fuze.bcp.creditcar.domain.CarTransfer")); // 转移过户
        transactionList.add(
                new ParamObject("com.fuze.bcp.dubbo.migration.service.impl.DmvpledgeImpl", "so_dmvpledge", "com.fuze.bcp.creditcar.domain.DMVPledge")); // 抵押登记
        transactionList.add(
                new ParamObject("com.fuze.bcp.dubbo.migration.service.impl.DeclarationImpl", "so_declaration", "com.fuze.bcp.creditcar.domain.Declaration")); // 电子报批
//        transactionList.add(
//                new ParamObject("com.fuze.bcp.dubbo.migration.service.impl.CancelOrderImpl", "so_cancelorder", "com.fuze.bcp.creditcar.domain.CancelOrder")); // 取消业务
        // wll

        // zqw

    }

    public static void execute(CustomerDemand customerDemand, String orderId, CustomerTransaction transaction, MongoTemplate source, MongoTemplate target) throws Exception {
        for (ParamObject p : transactionList) {
            try {
                Query query = new Query();
                if (p.getOldCollectionName().equals("so_purchasecar")) {
                    query.addCriteria(Criteria.where("carDemandId").is(customerDemand.getId()));
                } else if (p.getOldCollectionName().equals("so_dmvpledge")) {
                    query.addCriteria(Criteria.where("customerId").is(customerDemand.getCustomerId()));
                }
                else {
                    query.addCriteria(Criteria.where("purchaseCarOrderId").is(orderId));
                }
                // 旧库 需要迁移的对象
                BasicDBObject oldObj = source.findOne(query, BasicDBObject.class, p.getOldCollectionName());
                // 旧库 公共对象
                BasicDBObject baseOrder = source.findOne(new Query(Criteria.where("carDemandId").is(customerDemand.getId())), BasicDBObject.class, "so_purchasecar");

                if (oldObj != null) {
                    BaseBillEntity b = null;
                    DataMigration d = null;
                    try {
                        Class c1 = Class.forName(p.getNewName());
                        Class c2 = Class.forName(p.getServerName());
                        b = (BaseBillEntity) c1.newInstance();
                        d = (DataMigration) c2.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    saveBaseBillEntity(baseOrder, b, transaction.getId(), target);
                    b.setApproveDate(oldObj.getString("approveDate"));
                    b.setApproveUserId(oldObj.getString("approveUserId"));
                    Map<String, String> map = d.billDataMigration(customerDemand, transaction, oldObj, b, source, target);
                    if (map.containsKey(DataMigration.SAVED) && DataMigration.YES.equals(map.get(DataMigration.SAVED))) {
                        target.save(b);
                        WorkFlowBill workFlowBill = new WorkFlowBill();
                        List<SignInfo> signInfos = new ArrayList<>();
                        if (oldObj.containsField("signInfos")) {
                            List<BasicDBObject> list = (List) oldObj.get("signInfos");
                            for (int i = 0; i < list.size(); i++) {
                                SignInfo s = new SignInfo();
                                s.setResult(list.get(i).get("result") != null ? Double.valueOf(list.get(i).get("result").toString()).intValue() : null);
                                s.setTs(list.get(i).get("ts") != null ? list.get(i).get("ts").toString() : null);
                                s.setEmployeeId(list.get(i).get("employeeId") != null ? list.get(i).get("employeeId").toString() : null);
                                s.setUserId(list.get(i).get("userId") != null ? list.get(i).get("userId").toString() : null);
                                s.setComment(list.get(i).get("comment") != null ? list.get(i).get("comment").toString() : null);
                                s.setFromSalesman(list.get(i).get("fromSalesman") != null ? (Boolean) list.get(i).get("fromSalesman") : false);
                                if (s.getFromSalesman()) {
                                    s.setFlag(SignInfo.FLAG_COMMIT);
                                } else {
                                    s.setFlag(SignInfo.FLAG_SIGN);
                                }
                                signInfos.add(s);
                            }
                        }
                        workFlowBill.setSignInfos(signInfos);
                        workFlowBill.setFlowCode(b.getBillTypeCode());
                        workFlowBill.setSourceId(b.getId());
                        workFlowBill.setTransactionId(customerDemand.getCustomerTransactionId());
                        workFlowBill.setActivitiId(workFlowBill.getFlowCode() + "." + workFlowBill.getSourceId());

                        Document document = b.getClass().getAnnotation(Document.class);
                        workFlowBill.setCollectionName(document.collection());
                        workFlowBill.setBusinessTypeCode(b.getBusinessTypeCode());
                        workFlowBill.setApproveStatus(b.getApproveStatus());
                        workFlowBill.setDataStatus(1);
                        workFlowBill.setTs(b.getTs());
                        workFlowBill.setSignInfos(signInfos);
                        if (!"A015".equals(workFlowBill.getFlowCode())) {
                            target.save(workFlowBill);
                        }
                    }
                }
            } catch (Exception e) {
                File file = new File(p.getOldCollectionName());
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream outputStream = new FileOutputStream(file, true);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write("业务:" + p.getOldCollectionName() + "," + "orderId:" + orderId + ",transactionId:" + transaction.getId() + "\\n");
                writer.close();
                throw new Exception("业务:" + p.getOldCollectionName() + "," + "orderId:" + orderId + ",transactionId:" + transaction.getId(), e);
            }
        }
    }

    public static void main(String[] args) { // Criteria.where("ts").gt("2017-10-01 00:00:00")

//        String sb1 = new String("insert  into `ACT_ID_MEMBERSHIP`(`USER_ID_`,`GROUP_ID_`) values ('13020045990','5'),('13051275287','6'),('13051551698','4'),('13051551698','5'),('13051551698','7'),('13121424286','4'),('13121424286','5'),('13121424286','6'),('13311341441','6'),('13381172197','6'),('13426350085','6'),('13426443817','6'),('13466305181','6'),('13488696122','6'),('13501172958','6'),('13521625799','6'),('13522720086','6'),('13552022693','6'),('13552189382','9'),('13581973232','6'),('13601214149','4'),('13611157577','9'),('13641149661','9'),('13681073412','9'),('13683218626','6'),('13693259623','6'),('13693687199','6'),('13701012068','6'),('13716329068','5'),('13810888752','9'),('13811003187','6'),('13901335263','6'),('13910207375','7'),('13910917483','6'),('13910917483','8'),('13911270427','6'),('13911828227','6'),('15010018898','6'),('15011092181','6'),('15036123161','6'),('15138247749','4'),('15138247749','5'),('15138247749','7'),('15173399911','6'),('15201538001','6'),('15210348734','6'),('15210689309','6'),('15236212365','6'),('15321378796','6'),('15601000025','6'),('15652597055','5'),('15810550816','6'),('15910846604','6'),('17310065100','6'),('17338158986','6'),('17600164922','6'),('17600530807','4'),('17600530807','5'),('17600530807','6'),('17600530807','7'),('17600530807','8'),('17600530807','9'),('17604860626','6'),('17604860626','8'),('17647699470','6'),('17735862002','6'),('17744517975','6'),('18211099827','6'),('18255691232','6'),('18311331213','6'),('18401475136','5'),('18500220960','6'),('18511597451','5'),('18511999111','6'),('18513907320','6'),('18514763801','8'),('18536201236','5'),('18600133977','6'),('18600345663','6'),('18600367171','6'),('18600367171','8'),('18600741975','6'),('18601047986','6'),('18601176708','8'),('18610189239','6'),('18610282737','6'),('18611676900','6'),('18611872549','6'),('18612850718','6'),('18618230183','6'),('18618234650','6'),('18618481046','6'),('18630684855','6'),('18801350592','6'),('18810266447','6'),('18810292080','5'),('18810292825','6'),('18810382694','4'),('18811167384','4'),('18910721771','4'),('18910721771','5'),('18911526267','6');");
//        sb1 = sb1.replaceAll("'4'", "'G_APPROVAL'");
//        sb1 = sb1.replaceAll("'5'", "'G_REVIEW'");
//        sb1 = sb1.replaceAll("'6'", "'G_SUBMIT'");
//        sb1 = sb1.replaceAll("'7'", "'G_SIGN'");
//        sb1 = sb1.replaceAll("'8'", "'G_MANAGER'");
//        sb1 = sb1.replaceAll("'9'", "'G_FINANCE'");
//        System.out.println(sb1.toString());
//        if (true) return;
        MongoTemplate source = MongoConnect.getMongoTemplate("139.198.11.30", 38289, "root", "admin", "2017Fuzefenqi998", "fzfq-prod");
        MongoTemplate target = MongoConnect.getMongoTemplate("172.16.2.5", 27017, "mongolive", "admin", "FuzefenqiPa88word", "bcp_v11");
        List<BasicDBObject> cardemands = source.find(new Query(), BasicDBObject.class, "so_cardemand");
        for (int i = 0; i < cardemands.size(); i = i + 300) {
            int index = i;
            Thread thread = new Thread(() -> {
                try {
                    for (int j = 0; j < 300 && index + j < cardemands.size(); j++) {
                        BasicDBObject obj = cardemands.get(index + j);
                        if (obj.getInt("dataStatus") == 9) {
                            continue;
                        }
                        CustomerTransaction customerTransaction = new CustomerTransaction();
                        saveCustomerTransaction(obj, customerTransaction);
                        target.save(customerTransaction);
                    /*
                    doneTime
                    signInfos
                    currentTasks
                    currentTask
                    completedTask
                     */
                        WorkFlowBill w1 = new WorkFlowBill();
                        w1.setFlowCode(customerTransaction.getBusinessTypeCode());
                        w1.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
                        w1.setSourceId(customerTransaction.getId());
                        w1.setTransactionId(customerTransaction.getId());
                        w1.setActivitiId(customerTransaction.getBusinessTypeCode() + "." + customerTransaction.getId());
                        w1.setApproveStatus(ApproveStatus.APPROVE_INIT);
                        w1.setCollectionName("cus_transaction");
                        w1.setParams(getConditions(customerTransaction.getId()));
                        target.save(w1);

                        CustomerDemand customerDemand = new CustomerDemand();
                        try {
                            saveCustomerDemand(customerTransaction.getId(), obj, customerDemand, target);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        target.save(customerDemand);

                        WorkFlowBill w2 = new WorkFlowBill();
                        List<SignInfo> signInfos = new ArrayList<>();
                        if (obj.containsField("signInfos")) {
                            List<BasicDBObject> list = (List) obj.get("signInfos");
                            for (int a = 0; a < list.size(); a++) {
                                SignInfo s = new SignInfo();
                                s.setResult(list.get(a).get("result") != null ? Double.valueOf(list.get(a).get("result").toString()).intValue() : null);
                                s.setTs(list.get(a).get("ts") != null ? list.get(a).get("ts").toString() : null);
                                s.setEmployeeId(list.get(a).get("employeeId") != null ? list.get(a).get("employeeId").toString() : null);
                                s.setUserId(list.get(a).get("userId") != null ? list.get(a).get("userId").toString() : null);
                                s.setComment(list.get(a).get("comment") != null ? list.get(a).get("comment").toString() : null);
                                s.setFromSalesman(list.get(a).get("fromSalesman") != null ? (Boolean) list.get(a).get("fromSalesman") : false);
                                if (s.getFromSalesman()) {
                                    s.setFlag(SignInfo.FLAG_COMMIT);
                                } else {
                                    s.setFlag(SignInfo.FLAG_SIGN);
                                }
                                signInfos.add(s);
                            }
                            // 完成的资质设置signInfo
                        }
//                        w2.setSignInfos(signInfos);
                        w2.setFlowCode("A001");
                        w2.setSourceId(customerDemand.getId());
                        w2.setTransactionId(customerTransaction.getId());
                        w2.setActivitiId(customerDemand.getBillTypeCode() + "." + customerDemand.getId());
                        w2.setCollectionName("so_customerdemand");
                        w2.setBusinessTypeCode(customerDemand.getBusinessTypeCode());
                        w2.setDataStatus(customerDemand.getDataStatus());
                        w2.setApproveStatus(customerDemand.getApproveStatus());
                        w2.setTs(customerDemand.getTs());
                        target.save(w2);

                        //征信拍照迁移
                        saveCreditPhotograph(source, target, customerDemand, customerTransaction.getId());
                        Query query = new Query(Criteria.where("carDemandId").is(customerDemand.getId()));
                        BasicDBObject basicDBObject = source.findOne(query, BasicDBObject.class, "so_purchasecar");
                        if (basicDBObject != null) {
                            ObjectId objectId = (ObjectId) basicDBObject.get("_id");
                            try {
                                Criteria criteria = Criteria.where("dataStatus").is(1);
                                criteria.orOperator(Criteria.where("orderId").is(customerDemand.getId()),Criteria.where("orderId").is(objectId.toString()));
                                BasicDBObject cancelMap = source.findOne(new Query(criteria), BasicDBObject.class, "so_cancelorder");
                                if (cancelMap != null) {
                                    try{
                                        CancelOrder cancelOrder = new CancelOrder();
                                        cancelOrder.setCustomerId(customerDemand.getCustomerId());
                                        String ts = cancelMap.get("ts") != null ? (String) cancelMap.get("ts") : null;
                                        Integer dataStatus = cancelMap.get("dataStatus") != null ? new Double(cancelMap.get("dataStatus").toString()).intValue() : null;
                                        String comment = cancelMap.get("comment") != null ? (String) cancelMap.get("comment") : null;
                                        String customerId = cancelMap.get("customerId") != null ? (String) cancelMap.get("customerId") : null;
                                        String carDealerId = cancelMap.get("carDealerId") != null ? (String) cancelMap.get("carDealerId") : null;
                                        String employeeId = cancelMap.get("employeeId") != null ? (String) cancelMap.get("employeeId") : null;
                                        String orginfoId = cancelMap.get("orginfoId") != null ? (String) cancelMap.get("orginfoId") : null;
                                        String loginUserId = cancelMap.get("loginUserId") != null ? (String) cancelMap.get("loginUserId") : null;
                                        String cashSourceId = cancelMap.get("cashSourceId") != null ? (String) cancelMap.get("cashSourceId") : null;
                                        cancelOrder.setTs(ts);
                                        cancelOrder.setDataStatus(dataStatus);
                                        cancelOrder.setComment(comment);
                                        if (cancelMap.get("businessType") != null) {
                                            com.mongodb.DBRef businessType = (com.mongodb.DBRef) cancelMap.get("businessType");
                                            ObjectId businessTypeId = (ObjectId) businessType.getId();
                                            if (businessTypeId.toString().equals("5897ea19722e05ea2511ffa6")) {
                                                cancelOrder.setBusinessTypeCode("NC");
                                            } else if (businessTypeId.toString().equals("58c75860e4b0d35997f3d42f")) {
                                                cancelOrder.setBusinessTypeCode("OC");
                                            }
                                        }
                                        cancelOrder.setCarDealerId(carDealerId);
                                        cancelOrder.setCashSourceId(cashSourceId);
                                        cancelOrder.setCustomerId(customerId);
                                        cancelOrder.setCustomerTransactionId(customerTransaction.getId());
                                        cancelOrder.setEmployeeId(employeeId);
                                        cancelOrder.setLoginUserId(loginUserId);
                                        cancelOrder.setOrginfoId(orginfoId);
                                        cancelOrder.setApproveStatus(cancelMap.getInt("approveStatus"));
                                        target.save(cancelOrder);
                                        WorkFlowBill w = new WorkFlowBill();
                                        w.setFlowCode("A012");
                                        w.setSourceId(cancelOrder.getId());
                                        w.setTransactionId(customerTransaction.getId());
                                        w.setActivitiId(cancelOrder.getBillTypeCode() + "." + cancelOrder.getId());
                                        w.setCollectionName("so_cancelorder");
                                        w.setBusinessTypeCode(cancelOrder.getBusinessTypeCode());
                                        w.setDataStatus(cancelOrder.getDataStatus());
                                        w.setApproveStatus(cancelOrder.getApproveStatus());
                                        w.setTs(cancelOrder.getTs());
                                        target.save(w);
                                    } catch (Exception e){
                                        System.out.println("error  cancelId:" + cancelMap.getString("_id"));
                                        e.printStackTrace();
                                    }
                                }
                                execute(customerDemand, objectId.toString(), customerTransaction, source, target);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }

    public static void saveCustomerDemand(String transactionId, BasicDBObject obj, CustomerDemand customerDemand, MongoTemplate target) throws IOException {
        customerDemand.setApproveStatus(obj.containsField("approveStatus") ? obj.getInt("approveStatus") : null);
        com.mongodb.DBRef businessType = (com.mongodb.DBRef) obj.get("businessType");
        org.bson.types.ObjectId businessTypeId = (org.bson.types.ObjectId) businessType.getId();
        if ("5897ea19722e05ea2511ffa6".equals(businessTypeId.toString())) {
            customerDemand.setBusinessTypeCode("NC");
        } else if ("58c75860e4b0d35997f3d42f".equals(businessTypeId.toString())) {
            customerDemand.setBusinessTypeCode("OC");
        }
        customerDemand.setCashSourceId((String) obj.get("cashSourceId"));
        customerDemand.setCarDealerId((String) obj.get("carDealerId"));
        customerDemand.setCustomerId((String) obj.get("customerId"));
        customerDemand.setCustomerTransactionId(transactionId);
        //String creditProductId = (String) obj.get("creditProductId");
        //customerDemand.setCreditProductId(creditProductId);
        customerDemand.setCreditMasterId((String) obj.get("creditMasterId"));
        customerDemand.setComment((String) obj.get("comment"));

        String dealerEmployeeId = (String) obj.get("dealerEmployeeId");
        customerDemand.setDealerEmployeeId(dealerEmployeeId);

        customerDemand.setDataStatus(obj.get("dataStatus") != null ? new Double(obj.get("dataStatus").toString()).intValue() : null);
        customerDemand.setEmployeeId((String) obj.get("employeeId"));

        ObjectId id = (ObjectId) obj.get("_id");
        customerDemand.setId(id.toString());

        customerDemand.setLoginUserId((String) obj.get("loginUserId"));

        String mateCustomerId = (String) obj.get("mateCustomerId");
        customerDemand.setMateCustomerId(mateCustomerId);
        customerDemand.setOrginfoId((String) obj.get("orginfoId"));
        String pledgeCustomerId = (String) obj.get("pledgeCustomerId");
        customerDemand.setPledgeCustomerId(pledgeCustomerId);
        String relation = (String) obj.get("relation");
        customerDemand.setRelation(relation);

        customerDemand.setTs((String) obj.get("ts"));
        CustomerCar customerCar = new CustomerCar();
        List<Map<String, String>> selectCar = (List<Map<String, String>>) obj.get("selectCars");
        customerCar.setCustomerId(customerDemand.getCustomerId());
        customerCar.setCustomerTransactionId(transactionId);
        //  从系统参配项中查询车辆颜色配置，然后根据1.0车辆颜色得到相对应的颜色编码，并保存
        SysParam sysParams = target.findOne(new Query(Criteria.where("code").is("CAR_COLORS")), SysParam.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Map<String, String>> list = objectMapper.readValue(sysParams.getParameterValue(), ArrayList.class);
        for (Map<String, String> map : list) {
            if (map.get("name").equals(selectCar.get(0).get("color"))) {
                customerCar.setCarColor((String) map.get("code"));
            }
        }
        customerCar.setCarColorName(selectCar.get(0).get("color"));
        customerCar.setCarTypeId(selectCar.get(0).get("carTypeId"));



        CustomerLoan customerLoan = new CustomerLoan();
        customerLoan.setCustomerId(customerDemand.getCustomerId());
        customerLoan.setCustomerTransactionId(transactionId);

        Double total = 0.0;
        if (obj.containsField("downParmentRatio")) {
            customerLoan.setDownPaymentRatio(obj.getDouble("downParmentRatio"));
        }else {
            customerLoan.setDownPaymentRatio(0.0);
        }
        if (obj.containsField("downPayment")) {
            customerLoan.setDownPayment(obj.getDouble("downPayment"));
        }else{
            customerLoan.setDownPayment(0.0);
        }
        if (obj.containsField("creditRatio")) {
            customerLoan.setCreditRatio(obj.getDouble("creditRatio"));
        }else{
            customerLoan.setCreditRatio(0.0);
        }
        if (obj.containsField("creditAmount")) {
            customerLoan.setCreditAmount(obj.getDouble("creditAmount"));
        }else{
            customerLoan.setCreditAmount(0.0);
        }
        if(obj.containsField("downPayment") && obj.containsField("creditAmount")){
            total = obj.getDouble("downPayment") + obj.getDouble("creditAmount");
        }
        if (total > 0) {
            customerLoan.setReceiptPrice(total);
            customerLoan.setRealPrice(total);
            customerCar.setReceiptPrice(total);
            customerCar.setEstimatedPrice(total);
            customerCar.setEvaluatePrice(total);
        } else {
            customerLoan.setReceiptPrice(0.0);
            customerLoan.setRealPrice(0.0);
            customerCar.setReceiptPrice(0.0);
            customerCar.setEstimatedPrice(0.0);
            customerCar.setEvaluatePrice(0.0);
        }
        CarType carType = target.findOne(new Query(Criteria.where("_id").is(selectCar.get(0).get("carTypeId"))), CarType.class);
        if (carType != null && carType.getPrice() > 0) {
            customerCar.setGuidePrice(carType.getPrice());
        } else {
            customerCar.setGuidePrice(0.0);
        }
        // 分期申请车价（开票价，评估价，成交价三者最低为购车价格）= 贷款额度+首付金额
        customerLoan.setApplyAmount(total);

        Map<Integer, Double> m = new HashMap<>();
        m.put(12, 0.04);
        m.put(18, 0.06);
        m.put(24, 0.08);
        m.put(30, 0.1);
        m.put(36, 0.12);
        m.put(48, 0.16);
        m.put(60, 0.20);
        Map<Integer, Double> m1 = new HashMap<>();
        m1.put(12, 0.05);
        m1.put(18, 0.075);
        m1.put(24, 0.1);
        m1.put(36, 0.15);
        RateType rateType = new RateType();
        rateType.setMonths(obj.getInt("months"));
        //  银行手续费
        if ("NC".equals(customerDemand.getBusinessTypeCode())) {
            rateType.setRatio(m.get(obj.getInt("months")));
            if (customerLoan.getCreditAmount() != null && m.get(obj.getInt("months")) != null) {
                customerLoan.setBankFeeAmount(customerLoan.getCreditAmount() * m.get(obj.getInt("months")));
            } else {
                customerLoan.setBankFeeAmount(0.0);
            }
        } else if ("OC".equals(customerDemand.getBusinessTypeCode())) {
            rateType.setRatio(m1.get(obj.getInt("months")));
            if (customerLoan.getCreditAmount() != null && m1.get(obj.getInt("months")) != null) {
                customerLoan.setBankFeeAmount(customerLoan.getCreditAmount() * m1.get(obj.getInt("months")));
            } else {
                customerLoan.setBankFeeAmount(0.0);
            }
        }
        customerLoan.setRateType(rateType);
        try{
            Map map = actCalculateCompensatoryWay(customerLoan.getBankFeeAmount(), customerLoan.getCompensatoryAmount(), customerLoan.getRateType().getMonths());
            if (map != null && !"".equals((String) map.get("compensatoryWay"))) {
                customerLoan.setCompensatoryWay((String) map.get("compensatoryWay"));
                customerLoan.setCompensatoryMonth((Integer) map.get("compensatoryMonth"));
            }
        }catch (Exception e){
            try {
                throw new Exception("actCalculateCompensatoryWay error", e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        target.save(customerCar);
        target.save(customerLoan);
        customerDemand.setCustomerCarId(customerCar.getId());
        customerDemand.setCustomerLoanId(customerLoan.getId());
        customerDemand.setMateCustomerId((String) obj.get("mateCustomerId"));

    }

    public static void saveCreditPhotograph(MongoTemplate source, MongoTemplate target, CustomerDemand demand, String tid) {
        Query query = new Query();
        Criteria criteria = Criteria.where("customerId").is(demand.getCreditMasterId());
        if (demand.getMateCustomerId() != null) {
            criteria.orOperator(Criteria.where("customerId").is(demand.getMateCustomerId()));
        }
        query.addCriteria(criteria);
        List<BasicDBObject> uploads = source.find(query, BasicDBObject.class, "so_creditreport_upload");
        for (int i = 0; i < uploads.size(); i++) {
            Map obj = uploads.get(i).toMap();

            CreditPhotograph creditPhotograph = new CreditPhotograph();
            creditPhotograph.setId(obj.get("_id").toString());
            creditPhotograph.setCustomerId(demand.getCreditMasterId());
            //creditPhotograph.setCustomerTransactionId(tid);
            creditPhotograph.setDataStatus(DataStatus.SAVE);
            creditPhotograph.setImageFiles((Map<Integer, String>) obj.get("imageFiles"));
            creditPhotograph.setUploadFinish((Boolean) obj.get("uploadFinish"));
            creditPhotograph.setApproveStatus(0);
            creditPhotograph.setCashSourceId((String) obj.get("cashSourceId"));
            creditPhotograph.setStatus(obj.get("status") != null ? new Double(obj.get("status").toString()).intValue() : null);
            creditPhotograph.setTs((String) obj.get("ts"));

            target.save(creditPhotograph);
        }
    }

    public static Map<String, Object> actCalculateCompensatoryWay(Double bankFeeAmount, Double compensatoryAmount, Integer months) {
        Map<String, Object> map = new HashMap<String, Object>();
        String compensatoryWay = "无贴息";

        int monthsDivisor = months / 12;//年限
        int compensatoryMonth = Integer.parseInt(String.valueOf(Math.round((compensatoryAmount.doubleValue() / bankFeeAmount) * months)));

        //  贴息期限
        double compensatoryYear = monthsDivisor * compensatoryAmount.doubleValue() / bankFeeAmount;
        double compensatoryYearRemainder = monthsDivisor * compensatoryAmount.doubleValue() % bankFeeAmount;

        if (compensatoryYear == 0) {
            compensatoryWay = "无贴息";
        } else if (monthsDivisor - compensatoryYear == 0) {
            compensatoryWay = "全贴息";
        } else {
            //  贴息期限非整
            if (compensatoryYearRemainder > 0) {
                compensatoryWay = "贴息+自付：其他";
            } else {
                compensatoryWay = "贴息+自付：" + (int) compensatoryYear + "+" + ((int) monthsDivisor - (int) compensatoryYear);
            }
        }
        map.put("compensatoryWay", compensatoryWay);
        map.put("compensatoryMonth", compensatoryMonth);
        return map;
    }


    public static void saveBaseBillEntity(BasicDBObject baseOrder, BaseBillEntity b, String transactionId, MongoTemplate target) {
        String ts = baseOrder.get("ts") != null ? (String) baseOrder.get("ts") : null;
        Integer dataStatus = baseOrder.get("dataStatus") != null ? new Double(baseOrder.get("dataStatus").toString()).intValue() : null;
        String comment = baseOrder.get("comment") != null ? (String) baseOrder.get("comment") : null;
        String customerId = baseOrder.get("customerId") != null ? (String) baseOrder.get("customerId") : null;
        String carDealerId = baseOrder.get("carDealerId") != null ? (String) baseOrder.get("carDealerId") : null;
        String employeeId = baseOrder.get("employeeId") != null ? (String) baseOrder.get("employeeId") : null;
        String orginfoId = baseOrder.get("orginfoId") != null ? (String) baseOrder.get("orginfoId") : null;
        String loginUserId = baseOrder.get("loginUserId") != null ? (String) baseOrder.get("loginUserId") : null;
        String cashSourceId = baseOrder.get("cashSourceId") != null ? (String) baseOrder.get("cashSourceId") : null;
        b.setTs(ts);
        b.setDataStatus(dataStatus);
        b.setComment(comment);
        if (baseOrder.get("businessType") != null) {
            com.mongodb.DBRef businessType = (com.mongodb.DBRef) baseOrder.get("businessType");
            ObjectId businessTypeId = (ObjectId) businessType.getId();
            if (businessTypeId.toString().equals("5897ea19722e05ea2511ffa6")) {
                b.setBusinessTypeCode("NC");
            } else if (businessTypeId.toString().equals("58c75860e4b0d35997f3d42f")) {
                b.setBusinessTypeCode("OC");
            }
        }
        b.setCarDealerId(carDealerId);
        b.setCashSourceId(cashSourceId);
        b.setCustomerId(customerId);
        b.setCustomerTransactionId(transactionId);
        b.setEmployeeId(employeeId);
        b.setLoginUserId(loginUserId);
        b.setOrginfoId(orginfoId);
    }

    public static void saveCustomerTransaction(BasicDBObject obj, CustomerTransaction customerTransaction) {
        //业务员
        customerTransaction.setEmployeeId(obj.getString("employeeId"));
        //系统操作人员
        customerTransaction.setLoginUserId(obj.getString("loginUserId"));
        //所属部门
        customerTransaction.setOrginfoId(obj.getString("orginfoId"));
        //业务来源，即4S店
        customerTransaction.setCarDealerId(obj.getString("carDealerId"));
        //客户信息
        customerTransaction.setCustomerId(obj.getString("customerId"));
        //报单行
        customerTransaction.setCashSourceId(obj.getString("cashSourceId"));

        if (obj.containsField("status")) {
            if (0 == obj.getInt("status")) {
                customerTransaction.setStatus(9);
            } else {
                customerTransaction.setStatus(obj.getInt("status"));
            }
        }
        if (obj.containsField("approveStatus")) {
            if (obj.getInt("approveStatus") == 9) { // APPROVE_REJECT
                customerTransaction.setStatus(CustomerTransactionBean.TRANSACTION_STOP);
            }
        }
        customerTransaction.setTs(obj.getString("ts"));
        //业务类型
        com.mongodb.DBRef businessType = (com.mongodb.DBRef) obj.get("businessType");
        org.bson.types.ObjectId businessTypeId = (org.bson.types.ObjectId) businessType.getId();
        if ("5897ea19722e05ea2511ffa6".equals(businessTypeId.toString())) {
            customerTransaction.setBusinessTypeCode("NC");
        } else if ("58c75860e4b0d35997f3d42f".equals(businessTypeId.toString())) {
            customerTransaction.setBusinessTypeCode("OC");
        }
        customerTransaction.setStartTime((String) obj.get("businessTime"));
        //贷款主体信息，同样的客户基本信息
        String creditMasterId = (String) obj.get("creditMasterId");
        customerTransaction.setCustomerId(creditMasterId);

    }

    private static Map<String, List<SignCondition>> getConditions(String id) {

        Map<String, List<SignCondition>> params = new HashMap();

        Map<String, Object> condition = new HashMap<>();
        condition.put(SignCondition.MONGOSORT, "_id"); // paymentPolicy compensatoryInterest
        condition.put("customerTransactionId", id);

        SignCondition isNeedPayment = new SignCondition(condition, "cus_loan", "isNeedPayment", "0");

        SignCondition advancedPayment = new SignCondition(condition, "cus_loan", "advancedPayment", "0");
        SignCondition needCompensatory = new SignCondition("customerTransactionId", id, "so_appoint_swipingcard", "isNeedLoaning", "0");

        List s1 = new ArrayList<>();
        s1.add(isNeedPayment);
        List s2 = new ArrayList<>();
        s2.add(isNeedPayment);
        List s3 = new ArrayList<>();
        s3.add(advancedPayment);
        List s4 = new ArrayList<>();
        s4.add(needCompensatory);
        params.put("A011", s1);
        params.put("A004", s4);
        params.put("A008", s4);
        params.put("A026", s4);
        return params;
    }

}
