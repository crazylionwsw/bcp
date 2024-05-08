package com.fuze.bcp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuze.bcp.utils.FormatUtils;
import com.fuze.bcp.utils.NumberHelper;
import com.mongodb.BasicDBObject;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/8/17.
 */
@Service
public class TemplateService {

    private static final Logger logger = LoggerFactory.getLogger(TemplateService.class);

    @Value("${fuze.bcp.filePath}")
    private String filePath;

    @Value("${fuze.bcp.web.server.url}")
    private String webUrl;

    @Autowired
    MetaDataService metaDataService;

    @Autowired
    MessageService messageService;

    public Map getMetaDatas(String transactionId, List<String> metaDatas) {
        Map map = new HashMap<>();

        if (metaDatas.contains(MetaDataService.CUSTOMERTRANSACTION)) {
            map.put(MetaDataService.CUSTOMERTRANSACTION, getTransactionMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CARDEALER)) {
            map.put(MetaDataService.CARDEALER, getCardealerMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.EMPLOYEE)) {
            map.put(MetaDataService.EMPLOYEE, getEmployeeMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.DEALEREMPLOYEE)) {
            map.put(MetaDataService.DEALEREMPLOYEE, getDealerEmployeeMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.ORGINFO)) {
            map.put(MetaDataService.ORGINFO, getOrginfoMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.SUPERORGINFO)) {
            map.put(MetaDataService.SUPERORGINFO, getSuperOrginfoMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.EMPLOYEELEADER)) {
            map.put(MetaDataService.EMPLOYEELEADER, getEmployeeLeaderMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CASHSOURCE)) {
            map.put(MetaDataService.CASHSOURCE, getCashsourceMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CUSTOMER)) {
            map.put(MetaDataService.CUSTOMER, getCustomerMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.MATECUSTOMER)) {
            map.put(MetaDataService.MATECUSTOMER, getMateCustomerMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.PLEDGECUSTOMER)) {
            map.put(MetaDataService.PLEDGECUSTOMER, getPledgeCustomerMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.PLEDGE)) {
            map.put(MetaDataService.PLEDGE, getPledgeCustomerMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.GUARANTEECUSTOMER)) {
            map.put(MetaDataService.GUARANTEECUSTOMER, getGuaranteeCustomerMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CUSTOMERCARD)) {
            map.put(MetaDataService.CUSTOMERCARD, getCustomerCardMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CUSTOMERLOAN)) {
            map.put(MetaDataService.CUSTOMERLOAN, getCustomerLoanMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CUSTOMERCAR)) {
            map.put(MetaDataService.CUSTOMERCAR, getCustomerCarMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CARBRAND)) {
            map.put(MetaDataService.CARBRAND, getCarBrandMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CARMODEL)) {
            map.put(MetaDataService.CARMODEL, getCarModelMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CARTYPE)) {
            map.put(MetaDataService.CARTYPE, getCarTypeMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CARVALUATION)) {
            map.put(MetaDataService.CARVALUATION, getCarValuationMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.CUSTOMERDEMAND)) {
            map.put(MetaDataService.CUSTOMERDEMAND, getCustomerDemandMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.PURCHASECARORDER)) {
            map.put(MetaDataService.PURCHASECARORDER, getPurchasecarMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.BANKCARDAPPLY)) {
            map.put(MetaDataService.BANKCARDAPPLY, getBankCardApplyMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.DECLARATION)) {
            map.put(MetaDataService.DECLARATION, getDeclarationMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.APPOINTPAYMENT)) {
            map.put(MetaDataService.APPOINTPAYMENT, getAppointPaymentMap(transactionId));
        }
        if (metaDatas.contains(MetaDataService.IMAGEPATH)) {
            map.put(MetaDataService.IMAGEPATH, filePath); // 系统默认图片路径
        }
        if (metaDatas.contains(MetaDataService.RECEPTFILE)) {
            map.put(MetaDataService.RECEPTFILE, getReceptFileMap(transactionId)); // 客户资料交接单
        }
        if (metaDatas.contains(MetaDataService.SURVEYRESULT)) {
            map.put(MetaDataService.SURVEYRESULT, getSurveyResultMap(transactionId)); // 该笔交易问卷调查结果
        }
        if (metaDatas.contains(MetaDataService.DECLARATIONHISTORYS)) {
            map.put(MetaDataService.DECLARATIONHISTORYS, getDeclarationHistoryMap(transactionId)); // 该笔交易报批历史
        }
        if(metaDatas.contains(MetaDataService.CANCALORDER)){
            map.put(MetaDataService.CANCALORDER, getCancelOrderMap(transactionId));//该笔交易的取消
        }
        if(metaDatas.contains(MetaDataService.APPROVEUSER)){
            map.put(MetaDataService.APPROVEUSER, getApproveUserMap(transactionId));//该笔交易的审批人
        }
        if(metaDatas.contains(MetaDataService.DMVPLEDGE)){
            map.put(MetaDataService.DMVPLEDGE, getDmvpledgeMap(transactionId));//该笔交易的抵押
        }
        if(metaDatas.contains(MetaDataService.CREDITPHOTOGRAPH)){
            map.put(MetaDataService.CREDITPHOTOGRAPH, getCreditPhotographMap(transactionId));//该笔交易的征信拍照
        }
        return map;
    }

    /**
     * 根据 交易ID 查询 客户交易信息
     *
     * @param transactionId
     * @return
     */
    public Map getTransactionMap(String transactionId) {
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, transactionId, "cus_transaction");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERTRANSACTION"), "transaction", transactionId));
            return (Map) new HashMap().put("transaction",null);
        }
        return obj.toMap();
    }

    /**
     * 根据 交易ID   查询客户签约信息，并通过签约信息获取客户借款信息
     *
     * @param transactionId
     * @return
     */
    public Map getCustomerLoanMap(String transactionId) {
        Map purchasecarMap = getPurchasecarMap(transactionId);
        if (purchasecarMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_PURCHASECARORDER"), "purchasecarorder", transactionId));
            return (Map) new HashMap().put("purchasecarorder",null);
        }
        String customerLoanId = (String) purchasecarMap.get("customerLoanId");
        if ("".equals(customerLoanId) || customerLoanId == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERLOANID"), "customerloan", transactionId, "customerLoanId"));
            return (Map) new HashMap().put("customerloan",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, customerLoanId, "cus_loan");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERLOAN"), "customerloan", transactionId));
            return (Map) new HashMap().put("customerloan",null);
        }
        Map map = obj.toMap();
        map.put("REALPRICE", FormatUtils.parse(String.valueOf(obj.get("realPrice") == null ? 0 : obj.get("realPrice"))));
        map.put("CREDITAMOUNT", FormatUtils.parse(String.valueOf(obj.get("creditAmount") == null ? 0 : obj.get("creditAmount"))));
        map.put("DOWNPAYMENT", FormatUtils.parse(String.valueOf(obj.get("downPayment") == null ? 0 : obj.get("downPayment"))));
        int applyAmount = (int) (obj.get("applyAmount") == null ? 0 : (Double)obj.get("applyAmount"));
        map.put("APPLYAMOUNT", FormatUtils.parse(String.valueOf(applyAmount)));
        map.put("payWayName", "WHOLE".equals(obj.get("chargePaymentWay").toString()) ? "首期支付" : "分期支付");
        map.put("chargePaymentWayName", "WHOLE".equals(obj.get("chargePaymentWay").toString()) ? "趸交" : "分期");
        map.put("applyAmount",applyAmount);
        //  计算 平均年还款
        Double perSalary = 0.0;
        BasicDBObject rateType = (BasicDBObject) obj.get("rateType");
        //      第一种
        perSalary = (Double.valueOf(obj.get("creditAmount").toString()) + Double.valueOf(obj.get("bankFeeAmount").toString()) - Double.valueOf(obj.get("compensatoryAmount").toString())) / ((Integer) rateType.get("months")) * 12;

        map.put("perSalary", Math.floor(perSalary));
        //  每期还款---计算方式：贷款金额 / 贷款期限
        map.put("perMonthlyAmount",Math.floor(Double.valueOf(obj.get("creditAmount").toString()) / (Integer) rateType.get("months")));
        //  计算分期付款手续费率，要考虑贴息的情况---用于 中国工商银行信用卡汽车专项分期付款/担保（抵押、质押及保证）合同
        if ( Double.valueOf(obj.get("compensatoryAmount").toString()) == 0){//无贴息
            //map.put("ratio", (Double) rateType.get("ratio") * 100 + "%");
            map.put("ratio", NumberHelper.round((Double) rateType.get("ratio") * 100,2) + "%");
            map.put("shouxufei",obj.get("bankFeeAmount"));
        } else {//贴息
            DecimalFormat df = new DecimalFormat("#00");
            Double ratio = (Double) rateType.get("ratio") * 100 * Double.valueOf(obj.get("compensatoryMonth").toString()) / (Integer) rateType.get("months");
            String format = df.format(ratio);
            ratio = (Double) rateType.get("ratio") * 100 - Double.valueOf(format);
            map.put("ratio", "");
            map.put("shouxufei","");
        }

        map.put("guaranteeWayName","抵押担保");

        //重新计算担保服务合同首期偿还金额和分期偿还金额
        Double creditAmount = Double.valueOf(obj.get("creditAmount").toString());
        Integer creditMonth = (Integer) rateType.get("months");
        //首先计算平均月还款额(取整)
        Double firstMonthMoney =  Math.floor(creditAmount / Double.parseDouble(String.valueOf((Integer) rateType.get("months"))));
        //首先计算平均月还款额 * 期数-1
        Double chaMoney = firstMonthMoney * (creditMonth - 1);
        //首期还款额 = 贷款金额 - 首先计算平均月还款额 * 期数-1
        Double firstRepaymentMoney = creditAmount - chaMoney;
        //月还款金额
        Double monthlyRepaymentMoney = chaMoney / (creditMonth - 1);

        map.put("firstRepaymentMoney",firstRepaymentMoney);
        map.put("monthlyRepaymentMoney",monthlyRepaymentMoney);

        return map;
    }

    public Map getCustomerLoanMapDemand(String transactionId) {
        Map customerDemandMap = getCustomerDemandMap(transactionId);
        if (customerDemandMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_PURCHASECARORDER"), "purchasecarorder", transactionId));
            return (Map) new HashMap().put("purchasecarorder",null);
        }
        String customerLoanId = (String) customerDemandMap.get("customerLoanId");
        if ("".equals(customerLoanId) || customerLoanId == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERLOANID"), "customerloan", transactionId, "customerLoanId"));
            return (Map) new HashMap().put("customerloan",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, customerLoanId, "cus_loan");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERLOAN"), "customerloan", transactionId));
            return (Map) new HashMap().put("customerloan",null);
        }
        Map map = obj.toMap();
        map.put("REALPRICE", FormatUtils.parse(String.valueOf(obj.get("realPrice") == null ? 0 : obj.get("realPrice"))));
        map.put("CREDITAMOUNT", FormatUtils.parse(String.valueOf(obj.get("creditAmount") == null ? 0 : obj.get("creditAmount"))));
        map.put("DOWNPAYMENT", FormatUtils.parse(String.valueOf(obj.get("downPayment") == null ? 0 : obj.get("downPayment"))));
        int applyAmount = (int) (obj.get("applyAmount") == null ? 0 : (Double)obj.get("applyAmount"));
        map.put("APPLYAMOUNT", FormatUtils.parse(String.valueOf(applyAmount)));
        map.put("payWayName", "WHOLE".equals(obj.get("chargePaymentWay").toString()) ? "首期支付" : "分期支付");
        map.put("chargePaymentWayName", "WHOLE".equals(obj.get("chargePaymentWay").toString()) ? "趸交" : "分期");
        map.put("applyAmount",applyAmount);
        //  计算 平均年还款
        Double perSalary = 0.0;
        BasicDBObject rateType = (BasicDBObject) obj.get("rateType");
        //      第一种
        perSalary = (Double.valueOf(obj.get("creditAmount").toString()) + Double.valueOf(obj.get("bankFeeAmount").toString()) - Double.valueOf(obj.get("compensatoryAmount").toString())) / ((Integer) rateType.get("months")) * 12;

        map.put("perSalary", Math.floor(perSalary));
        //  每期还款---计算方式：贷款金额 / 贷款期限
        map.put("perMonthlyAmount",Math.floor(Double.valueOf(obj.get("creditAmount").toString()) / (Integer) rateType.get("months")));
        //  计算分期付款手续费率，要考虑贴息的情况---用于 中国工商银行信用卡汽车专项分期付款/担保（抵押、质押及保证）合同
        if ( Double.valueOf(obj.get("compensatoryAmount").toString()) == 0){//无贴息
            map.put("ratio", (Double) rateType.get("ratio") * 100 + "%");
            map.put("shouxufei",obj.get("bankFeeAmount"));
        } else {//贴息
            DecimalFormat df = new DecimalFormat("#00");
            Double ratio = (Double) rateType.get("ratio") * 100 * Double.valueOf(obj.get("compensatoryMonth").toString()) / (Integer) rateType.get("months");
            String format = df.format(ratio);
            ratio = (Double) rateType.get("ratio") * 100 - Double.valueOf(format);
            map.put("ratio", "");
            map.put("shouxufei","");
        }

        map.put("guaranteeWayName","抵押担保");

        //重新计算担保服务合同首期偿还金额和分期偿还金额
        Double creditAmount = Double.valueOf(obj.get("creditAmount").toString());
        Integer creditMonth = (Integer) rateType.get("months");
        //首先计算平均月还款额(取整)
        Double firstMonthMoney =  Math.floor(creditAmount / Double.parseDouble(String.valueOf((Integer) rateType.get("months"))));
        //首先计算平均月还款额 * 期数-1
        Double chaMoney = firstMonthMoney * (creditMonth - 1);
        //首期还款额 = 贷款金额 - 首先计算平均月还款额 * 期数-1
        Double firstRepaymentMoney = creditAmount - chaMoney;
        //月还款金额
        Double monthlyRepaymentMoney = chaMoney / (creditMonth - 1);

        map.put("firstRepaymentMoney",firstRepaymentMoney);
        map.put("monthlyRepaymentMoney",monthlyRepaymentMoney);

        return map;
    }


    /**
     * 根据 交易ID   查询客户签约信息，并通过签约信息获取客户购车信息
     *
     * @param transactionId
     * @return
     */
    public Map getCustomerCarMap(String transactionId) {
        Map map = new HashMap();
        Map purchasecarMap = getPurchasecarMap(transactionId);
        if (purchasecarMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_PURCHASECARORDER"), "purchasecarorder", transactionId));
            return (Map) new HashMap().put("purchasecarorder",null);
        }
        String customerCarId = (String) purchasecarMap.get("customerCarId");
        if ("".equals(customerCarId) || customerCarId == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERCARID"), "customercar", transactionId, "customerCarId"));
            return map;
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, customerCarId, "cus_car");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERCAR"), "customercar", transactionId));
            return map;
        }

        map =  obj.toMap();
        return map;
    }

    public Map getCustomerCarMapDemand(String transactionId) {
        Map map = new HashMap();
        Map customerDemandMap = getCustomerDemandMap(transactionId);
        if (customerDemandMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_PURCHASECARORDER"), "customerdemand", transactionId));
            return (Map) new HashMap().put("customerdemand",null);
        }
        String customerCarId = (String) customerDemandMap.get("customerCarId");
        if ("".equals(customerCarId) || customerCarId == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERCARID"), "customercar", transactionId, "customerCarId"));
            return map;
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, customerCarId, "cus_car");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERCAR"), "customercar", transactionId));
            return map;
        }

        map =  obj.toMap();
        return map;
    }

    /**
     * 通过交易ID查询车型信息，在通过车型信息获取车辆品牌信息
     *
     * @param transactionId
     * @return
     */
    public Map getCarBrandMap(String transactionId) {
        Map carTypeMap = getCarTypeMap(transactionId);
        if (carTypeMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARTYPE"), "cartype", transactionId));
            return (Map) new HashMap().put("cartype",null);
        }
        String carBrandId = (String) carTypeMap.get("carBrandId");
        if ("".equals(carBrandId) || carBrandId == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARBRANDID"), "carbrand", transactionId, "carBrandId"));
            return (Map) new HashMap().put("carbrand",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, carBrandId, "bd_carbrand");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARBRAND"), "carbrand", transactionId));
            return (Map) new HashMap().put("carbrand",null);
        }
        return obj.toMap();
    }

    /**
     * 通过交易ID查询车型信息，在通过车型信息获取车系信息
     *
     * @param transactionId
     * @return
     */
    public Map getCarModelMap(String transactionId) {
        Map carTypeMap = getCarTypeMap(transactionId);
        if (carTypeMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARTYPE"), "cartype", transactionId));
            return (Map) new HashMap().put("cartype",null);
        }
        String carModelId = (String) carTypeMap.get("carModelId");
        if ("".equals(carModelId) || carModelId == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARMODELID"), "carmodel", transactionId, "carModelId"));
            return (Map) new HashMap().put("carmodel",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, carModelId, "bd_carmodel");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARMODEL"), "carmodel", transactionId));
            return (Map) new HashMap().put("carmodel",null);
        }
        return obj.toMap();
    }

    /**
     * 根据交易ID获取客户购车信息，从客户购车信息中获取车型ID获取车型数据
     *
     * @param transactionId
     * @return
     */
    public Map getCarTypeMap(String transactionId) {
        Map customerCarMap = getCustomerCarMap(transactionId);
        if (customerCarMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERCAR"), "customercar", transactionId));
            return new HashMap();
        }
        String carTypeId = (String) customerCarMap.get("carTypeId");
        if ("".equals(carTypeId) || carTypeId == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARTYPEID"), "cartype", transactionId, "carTypeId"));
            return (Map) new HashMap().put("cartype",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, carTypeId, "bd_cartype");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARTYPE"), "cartype", transactionId));
            return (Map) new HashMap().put("cartype",null);
        }
        return obj.toMap();
    }

    public Map getCarTypeMapDemand(String transactionId) {
        Map customerCarMap = getCustomerCarMapDemand(transactionId);
        if (customerCarMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERCAR"), "customercar", transactionId));
            return new HashMap();
        }
        String carTypeId = (String) customerCarMap.get("carTypeId");
        if ("".equals(carTypeId) || carTypeId == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARTYPEID"), "cartype", transactionId, "carTypeId"));
            return (Map) new HashMap().put("cartype",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, carTypeId, "bd_cartype");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARTYPE"), "cartype", transactionId));
            return (Map) new HashMap().put("cartype",null);
        }
        return obj.toMap();
    }

    /**
     * 根据交易ID，查询客户购车信息，在根据其中的VIN获取车辆评估信息
     *
     * @param transactionId
     * @return
     */
    public Map getCarValuationMap(String transactionId) {
        Map customerCarMap = getCustomerCarMap(transactionId);
        if (customerCarMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERCAR"), "customercar", transactionId));
            return new HashMap();
        }
        String vin = (String) customerCarMap.get("vin");
        if ("".equals(vin) || vin == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERCAR_VIN"), "carvaluation", transactionId, "vin"));
            return (Map) new HashMap().put("carvaluation",null);
        }
        BasicDBObject obj = metaDataService.getObjMap("vin", vin, "so_car_valuation");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARVALUATION"), "carvaluation", transactionId, vin));
            return (Map) new HashMap().put("carvaluation",null);
        }
        return obj.toMap();
    }

    /**
     * 根据 交易ID   查询客户签约信息，并通过签约信息获取客户卡信息
     *
     * @param transactionId
     * @return
     */
    public Map getCustomerCardMap(String transactionId) {
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "cus_card");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERCARD"), "customercard", transactionId));
            return (Map) new HashMap().put("customercard",null);
        }
        return obj.toMap();
    }

    /**
     * 根据 交易ID 查询 客户交易信息，并获取 分期经理 员工 信息
     *
     * @param transactionId
     * @return
     */
    public Map getEmployeeMap(String transactionId) {
        Map transactionMap = getTransactionMap(transactionId);
        if (transactionMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERTRANSACTION"), "transaction", transactionId));
            return (Map) new HashMap().put("transaction",null);
        }
        String employeeId = (String) transactionMap.get("employeeId");
        if (employeeId == null || "".equals(employeeId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_EMPLOYEEID"), "employee", transactionId, "employeeId"));
            return (Map) new HashMap().put("employee",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, employeeId, "bd_employee");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_EMPLOYEE"), "employee", transactionId));
            return (Map) new HashMap().put("employee",null);
        }
        return obj.toMap();
    }

    /**
     *  根据 交易ID 查询 客户交易信息，并获取 渠道经理 员工 信息
     * @param transactionId
     * @return
     */
    public Map getDealerEmployeeMap(String transactionId) {
        Map cardealerMap = getCardealerMap(transactionId);
        if (cardealerMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARDEALER"), "cardealer", transactionId,"carDealerId"));
            return (Map) new HashMap().put("cardealer",null);
        }
        String employeeId = (String) cardealerMap.get("employeeId");
        if (employeeId == null || "".equals(employeeId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_EMPLOYEEID"), "dealeremployee", transactionId, "employeeId"));
            return (Map) new HashMap().put("dealeremployee",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, employeeId, "bd_employee");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_EMPLOYEE"), "dealeremployee", transactionId));
            return (Map) new HashMap().put("dealeremployee",null);
        }
        return obj.toMap();
    }

    public Map getDealerEmployeeMapDemand(String transactionId) {
        Map cardealerMap = getCardealerMapDemand(transactionId);
        if (cardealerMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARDEALER"), "cardealer", transactionId,"carDealerId"));
            return (Map) new HashMap().put("cardealer",null);
        }
        String employeeId = (String) cardealerMap.get("employeeId");
        if (employeeId == null || "".equals(employeeId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_EMPLOYEEID"), "dealeremployee", transactionId, "employeeId"));
            return (Map) new HashMap().put("dealeremployee",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, employeeId, "bd_employee");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_EMPLOYEE"), "dealeremployee", transactionId));
            return (Map) new HashMap().put("dealeremployee",null);
        }
        return obj.toMap();
    }

    /**
     * 根据交易ID，获取该交易分期经理的部门信息
     *
     * @param transactionId
     * @return
     */
    public Map getOrginfoMap(String transactionId) {
        Map transactionMap = getTransactionMap(transactionId);
        if (transactionMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERTRANSACTION"), "transaction", transactionId));
            return (Map) new HashMap().put("transaction",null);
        }
        String orginfoId = (String) transactionMap.get("orginfoId");
        if (orginfoId == null || "".equals(orginfoId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_ORGINFOID"), "orginfo", transactionId, "orginfoId"));
            return (Map) new HashMap().put("orginfo",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, orginfoId, "bd_orginfo");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_ORGINFO"), "orginfo", transactionId));
            return (Map) new HashMap().put("orginfo",null);
        }
        return obj.toMap();
    }

    /**
     * 根据交易ID，获取该交易分期经理的最上级的部门信息
     *
     * @param transactionId
     * @return
     */
    public Map getSuperOrginfoMap(String transactionId) {
        Map transactionMap = getTransactionMap(transactionId);
        if (transactionMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERTRANSACTION"), "transaction", transactionId));
            return (Map) new HashMap().put("transaction",null);
        }
        String orginfoId = (String) transactionMap.get("orginfoId");
        if (orginfoId == null || "".equals(orginfoId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_SUPERORGINFO"), "superorginfo", transactionId, "orginfoId"));
            return (Map) new HashMap().put("superorginfo",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, orginfoId, "bd_orginfo");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_ORGINFO"), "superorginfo", transactionId, orginfoId));
            return (Map) new HashMap().put("superorginfo",null);
        }
        obj = getSuperOrginfo(obj);
        return obj.toMap();
    }

    /**
     * 根据部门信息，获取最上级部门信息
     *
     * @param orginfo
     * @return
     */
    public BasicDBObject getSuperOrginfo(BasicDBObject orginfo) {
        if ((boolean) orginfo.get("virtual")) {
            BasicDBObject parentOrginfo = metaDataService.getObjMap(MetaDataService.ID, (String) orginfo.get("parentId"), "bd_orginfo");
            getSuperOrginfo(parentOrginfo);
        }
        return orginfo;
    }

    /**
     * 通过交易ID查询,查询
     *
     * @param transactionId
     * @return
     */
    public Map getEmployeeLeaderMap(String transactionId) {
        Map superOrginfoMap = getSuperOrginfoMap(transactionId);
        if (superOrginfoMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_ORGINFO"), "superorginfo", transactionId, null));
            return (Map) new HashMap().put("superorginfo",null);
        }
        String leaderId = (String) superOrginfoMap.get("leaderId");
        if (leaderId == null || "".equals(leaderId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_EMPLOYEELEADERID"), "employeeleader", transactionId, "leaderId"));
            return (Map) new HashMap().put("employeeleader",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, leaderId, "bd_employee");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_EMPLOYEELEADER"), "employeeleader", transactionId, leaderId));
            return (Map) new HashMap().put("employeeleader",null);
        }
        return obj.toMap();
    }

    /**
     * 根据 交易ID 查询 客户交易信息，并获取 客户信息
     *
     * @param transactionId
     * @return
     */
    public Map getCustomerMap(String transactionId) {
        Map transactionMap = getTransactionMap(transactionId);
        if (transactionMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERTRANSACTION"), "transaction", transactionId));
            return (Map) new HashMap().put("transaction",null);
        }
        String customerId = (String) transactionMap.get("customerId");
        if (customerId == null || "".equals(customerId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERID"), "customer", transactionId, "customerId"));
            return (Map) new HashMap().put("customer",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, customerId, "so_customer");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMER"), "customer", transactionId, customerId));
            return (Map) new HashMap().put("customer",null);
        }
        Map map = obj.toMap();
        BasicDBObject customerJob = (BasicDBObject) map.get("customerJob");
        map.put("SALARY", FormatUtils.parse(String.valueOf(customerJob.get("salary") == null ? 0 : customerJob.get("salary"))));
        List<String> cells = (List<String>) map.get("cells");
        map.put("cell",(cells != null && cells.size() > 0) ? cells.get(0):"");

        if(cells != null && cells.size()>0){
            String telephone = cells.get(0);
            map.put("telephone",telephone);
        }
        return map;
    }

    /**
     * 根据 交易ID 获取 客户资质信息，再根据 资质信息获取   客户的配偶信息
     *
     * @param transactionId
     * @return
     */
    public Map getMateCustomerMap(String transactionId) {
        Map customerDemandMap = getCustomerDemandMap(transactionId);
        if (customerDemandMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERDEMAND"), "customerdemand", transactionId));
            return (Map) new HashMap().put("customerdemand",null);
        }
        String mateCustomerId = (String) customerDemandMap.get("mateCustomerId");
        if (mateCustomerId == null || "".equals(mateCustomerId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_MATECUSTOMERID"), "matecustomer", transactionId, "mateCustomerId"));
            return (Map) new HashMap().put("matecustomer",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, mateCustomerId, "so_customer");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_MATECUSTOMER"), "matecustomer", transactionId, mateCustomerId));
            return (Map) new HashMap().put("matecustomer",null);
        }
        return obj.toMap();
    }

    /**
     * 根据 交易ID 获取 客户资质信息，再根据 资质信息获取   客户的抵押人信息
     *
     * @param transactionId
     * @return
     */
    public Map getPledgeCustomerMap(String transactionId) {
        Map map = new HashMap();
        Map customerDemandMap = getCustomerDemandMap(transactionId);
        if (customerDemandMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERDEMAND"), "customerdemand", transactionId));
            return (Map) new HashMap().put("customerdemand",null);
        }
        String pledgeCustomerId = (String) customerDemandMap.get("pledgeCustomerId");
        if (pledgeCustomerId == null || "".equals(pledgeCustomerId)) {
            logger.info(String.format(messageService.getMessage("MSG_METADATA_PLEDGECUSTOMER_IS_CREDITCUSTOMER"),transactionId));
            pledgeCustomerId = (String) customerDemandMap.get("creditMasterId");
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, pledgeCustomerId, "so_customer");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_PLEDGECUSTOMER"), "pledgecustomer", transactionId, pledgeCustomerId));
            return map;
        }
        map = obj.toMap();

        List<String> cells = (List<String>) map.get("cells");
        map.put("cell",(cells != null && cells.size() > 0) ? cells.get(0):"");
        if(cells != null && cells.size()>0){
            String telephone = cells.get(0);
            map.put("telephone",telephone);
        }
        return map;
    }

    /**
     * 根据交易ID获取客户签约信息，再根据签约信息获取客户的担保人信息
     *
     * @param transactionId
     * @return
     */
    public Map getGuaranteeCustomerMap(String transactionId) {
        Map purchasecarMap = getPurchasecarMap(transactionId);
        if (purchasecarMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_PURCHASECARORDER"), "purchasecarorder", transactionId));
            return (Map) new HashMap().put("purchasecarorder",null);
        }
        String counterGuarantorId = (String) purchasecarMap.get("counterGuarantorId");
        if (counterGuarantorId == null || "".equals(counterGuarantorId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_GUARANTEECUSTOMERID"), "guaranteecustomer", transactionId, "counterGuarantorId"));
            return (Map) new HashMap().put("guaranteecustomer",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, counterGuarantorId, "so_customer");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_GUARANTEECUSTOMER"), "guaranteecustomer", transactionId, counterGuarantorId));
            return (Map) new HashMap().put("guaranteecustomer",null);
        }
        return obj.toMap();
    }

    /**
     * 根据 交易ID 查询 客户交易信息，并获取 客户经销商信息
     *
     * @param transactionId
     * @return
     */
    public Map getCardealerMap(String transactionId) {
        Map purchasecarMap = getPurchasecarMap(transactionId);
        if (purchasecarMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_PURCHASECARORDER"), "purchasecarorder", transactionId));
            return (Map) new HashMap().put("purchasecarorder",null);
        }
        String carDealerId = (String) purchasecarMap.get("carDealerId");
        if (carDealerId == null || "".equals(carDealerId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARDEALERID"), "cardealer", transactionId, "carDealerId"));
            return (Map) new HashMap().put("cardealer",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, carDealerId, "bd_cardealer");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARDEALER"), "cardealer", transactionId, carDealerId));
            return (Map) new HashMap().put("cardealer",null);
        }
        return obj.toMap();
    }

    //资质阶段经销商信息
    public Map getCardealerMapDemand(String transactionId) {
        Map customerDemandMap = getCustomerDemandMap(transactionId);
        if (customerDemandMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERDEMAND"), "customerdemand", transactionId));
            return (Map) new HashMap().put("customerdemand",null);
        }
        String carDealerId = (String) customerDemandMap.get("carDealerId");
        if (carDealerId == null || "".equals(carDealerId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARDEALERID"), "cardealer", transactionId, "carDealerId"));
            return (Map) new HashMap().put("cardealer",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, carDealerId, "bd_cardealer");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CARDEALER"), "cardealer", transactionId, carDealerId));
            return (Map) new HashMap().put("cardealer",null);
        }
        return obj.toMap();
    }


    /**
     * 根据 交易ID 查询 客户交易信息，并获取 客户报单行信息
     *
     * @param transactionId
     * @return
     */
    public Map getCashsourceMap(String transactionId) {
        Map transactionMap = getTransactionMap(transactionId);
        if (transactionMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERTRANSACTION"), "transaction", transactionId));
            return (Map) new HashMap().put("transaction",null);
        }
        String cashSourceId = (String) transactionMap.get("cashSourceId");
        if (cashSourceId == null || "".equals(cashSourceId)) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CASHSOURCEID"), "cashsource", transactionId, "cashSourceId"));
            return (Map) new HashMap().put("cashsource",null);
        }
        BasicDBObject obj = metaDataService.getObjMap(MetaDataService.ID, cashSourceId, "bd_cashsource");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CASHSOURCE"), "cashsource", transactionId, cashSourceId));
            return (Map) new HashMap().put("cashsource",null);
        }
        //获取报单行   赋值元数据中的发卡机构和联系人
        BasicDBObject cashSource = metaDataService.getObjMap("code", "DECLARATION_BANKS", "sys_parameter");
        Map map = cashSource.toMap();
        String parameterValue = cashSource.getString("parameterValue");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> cmap = null;
        try {
            cmap = objectMapper.readValue(parameterValue, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map m:cmap) {
            if(m.get("id").equals(cashSourceId)){
                obj.put("bankName",m.get("name"));
                obj.put("bankEmp",m.get("bankpeople"));
                obj.put("bankEmCell",m.get("bpcell"));
            }
        }
        return obj.toMap();
    }

    /**
     * 根据 交易ID  查询客户资质审查信息
     *
     * @param transactionId
     * @return
     */
    public Map getCustomerDemandMap(String transactionId) {
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "so_customerdemand");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERDEMAND"), "customerdemand", transactionId));
            return (Map) new HashMap().put("customerdemand",null);
        }
        Map map = obj.toMap();
        /**
         *      关系解析
         */
        String relation = (String) map.get("relation");
        if (relation!=null && !"".equals(relation)){
            switch (Integer.parseInt(relation)) {
                case 0:
                    map.put("relationName", "本人");
                    break;
                case 1:
                    map.put("relationName", "父子");
                    break;
                case 2:
                    map.put("relationName", "配偶");
                    break;
                case 3:
                    map.put("relationName", "其他");
                    break;
            }
        }
        return map;
    }

    //交易中的签约阶段审批人
    public Map getApproveUserMap(String transactionId){
        Map purchasecarMap = getPurchasecarMap(transactionId);
        if (purchasecarMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_PURCHASECARORDER"), "purchasecarorder", transactionId));
            return (Map) new HashMap().put("purchasecarorder",null);
        }
        String approveUserId = (String) purchasecarMap.get("approveUserId");
        if(approveUserId == null){
            return (Map) new HashMap().put("approveuser",null);
        }
        BasicDBObject obj = metaDataService.getObjMap("loginUserId", approveUserId, "bd_employee");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_APPROVEUSER"), "approveuser", transactionId, approveUserId));
            return (Map) new HashMap().put("approveuser",null);
        }
        return obj.toMap();
    }

    /**
     * 根据  交易ID    查询客户签约信息
     *
     * @param transactionId
     * @return
     */
    public Map getPurchasecarMap(String transactionId) {
        Map map = new HashMap();
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "so_purchasecar");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_PURCHASECARORDER"), "purchasecarorder", transactionId));
            return (Map) new HashMap().put("purchasecarorder",null);
        }
        map = obj.toMap();
        /**
         *      指标状态解析
         */
        Integer indicatorStatus = (Integer) map.get("indicatorStatus");

        if (indicatorStatus != null) {
            switch (indicatorStatus) {
                case 1:
                    map.put("indicatorStatusName", "现标");
                    break;
                case 2:
                    map.put("indicatorStatusName", "外迁");
                    break;
                case 3:
                    map.put("indicatorStatusName", "报废");
                    break;
                case 4:
                    map.put("indicatorStatusName", "本地置换");
                    break;
            }
        }
        return map;
    }

    /**
     * 根据 交易ID 查询 客户 卡业务信息
     *
     * @param transactionId
     * @return
     */
    public Map getBankCardApplyMap(String transactionId) {
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "so_bankcard_apply");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_BANKCARDAPPLY"), "bankcardapply", transactionId));
            return (Map) new HashMap().put("bankcardapply",null);
        }
        return obj.toMap();
    }

    /**
     * 获取银行报批历史数据
     * @param transactionId
     * @return
     */
    public Map getDeclarationHistoryMap(String transactionId){
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "so_declaration_historys");
        if(obj == null){
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_DECLARATION_HISTORY"), "declaration_historys", transactionId));
            return (Map) new HashMap().put("declaration_historys",null);
        }
        return obj.toMap();
    }

    //取消业务
    public Map getCancelOrderMap(String transactionId){
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "so_cancel_order");
        if(obj == null){
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CANCELORDER"), "cancelorder", transactionId));
            return (Map) new HashMap().put("cancelorder",null);
        }
        return obj.toMap();
    }

    //根据交易id获取多条取消业务信息(只取最后一条)
    public Map getCancelOrderMapMore(String transactionId){
        List<BasicDBObject> obj = metaDataService.getObjMapMore("customerTransactionId", transactionId, "so_cancel_order");
        if(obj == null){
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CANCELORDER"), "cancelorder", transactionId));
            return (Map) new HashMap().put("cancelorder",null);
        }
        return obj.get(obj.size() - 1);
    }

    //抵押数据
    public Map getDmvpledgeMap(String transactionId){
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "so_dmvpledge");
        if(obj == null){
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_DMVPLEDGE"), "dmvpledge", transactionId));
            return (Map) new HashMap().put("dmvpledge",null);
        }
        return obj.toMap();
    }

    /**
     * 获取 银行报批的相关数据
     *
     * @param transactionId
     * @return
     */
    public Map getDeclarationMap(String transactionId) {
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "so_declaration");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_DECLARATION"), "declaration", transactionId));
            return (Map) new HashMap().put("declaration",null);
        }
        Map map = obj.toMap();
        Integer customerClass = (Integer) map.get("customerClass");
        switch (customerClass) {
            case 1:
                map.put("customerClass", "一");
                break;
            case 2:
                map.put("customerClass", "二");
                break;
            case 3:
                map.put("customerClass", "三");
                break;
            case 4:
                map.put("customerClass", "四");
                break;
            case 5:
                map.put("customerClass", "五");
                break;
            default:
                map.put("customerClass", "未分类");
                break;
        }
        BasicDBObject paymentToIncome = (BasicDBObject) map.get("paymentToIncome");
        BigDecimal sum = new BigDecimal(0.0);
        List<BasicDBObject> accountList = (List<BasicDBObject>) paymentToIncome.get("accountList");
        for (BasicDBObject incomeAccount : accountList) {
            if (incomeAccount.get("income") != null){
                sum = sum.add(new BigDecimal((Double) incomeAccount.get("income")));
            }
        }
        if (accountList != null){
            int cols = this.ceil(accountList.size(),3);
            map.put("oneNum",cols);
            if(cols > 0){
                List<List<BasicDBObject>> accounts = ListUtils.partition(accountList, cols);
                map.put("oneAccounts",accounts);
            }else{
                map.put("oneAccounts",new ArrayList<>());
            }
        }
        map.put("oneTotalIncome",sum);
        map.put("onePerIncome",accountList.size() > 0 ? sum.doubleValue() / accountList.size() : 0 );
        BigDecimal sum2 = new BigDecimal(0.0);
        List<BasicDBObject> accountList2 = (List<BasicDBObject>) paymentToIncome.get("accountList2");
        for (BasicDBObject incomeAccount : accountList2) {
            if (incomeAccount.get("income") != null){
                sum2 = sum2.add(new BigDecimal((Double) incomeAccount.get("income")));
            }
        }
        if (accountList2 != null){
            int cols2 = this.ceil(accountList2.size(),3);
            map.put("twoNum",cols2);
            if (cols2 > 0){
                List<List<BasicDBObject>> accounts2 = ListUtils.partition(accountList2, cols2);
                map.put("twoAccounts",accounts2);
            } else {
                map.put("twoAccounts",new ArrayList<>());
            }
        }
        map.put("twoTotalIncome",sum2);
        map.put("twoPerIncome",accountList2.size() > 0 ? sum2.doubleValue() / accountList2.size() : 0 );
        //map.put("summary", WordUtil);
        return map;
    }

    /**
     * 获取预约垫资支付信息
     *
     * @param transactionId
     * @return
     */
    public Map getAppointPaymentMap(String transactionId) {
        Map map = new HashMap();
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "so_appoint_payment");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_APPOINTPAYMENT"), "appointpayment", transactionId));
            map.put("APPOINTPAYAMOUNT", "");
            return map;
        }
        map = obj.toMap();
        map.put("APPOINTPAYAMOUNT", FormatUtils.parse(String.valueOf(obj.get("appointPayAmount"))));
        return map;
    }

    //获取征信拍照信息(根据客户Id)
    public Map getCreditPhotographMap(String customerId) {

        Map map = new HashMap();
        BasicDBObject obj = metaDataService.getObjMap("customerId", customerId, "so_credit_photograph");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CREDITPHOTOGRAPH"), "creditphotograph", customerId));
            return map;
        }
        map = obj.toMap();
        return map;
    }

    public Map getCustomerImageFileMap(String transactionId) {
        // TODO: 2017/9/5  参配像取B001 改成存入templateObject；
        Map declarationMap =  getDeclarationMap(transactionId);
        if (declarationMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_DECLARATION"), "declaration", transactionId));
            return (Map) new HashMap().put("declaration",null);
        }
        List<String> imageTypeCode = (List<String>) declarationMap.get("imageTypeCodes");
        List<Map<String,Object>> fileImageList = new ArrayList<Map<String,Object>>();
        for (String ic : imageTypeCode){
            Query query = new Query();
            query.addCriteria(Criteria.where("customerImageTypeCode").is(ic)).addCriteria(Criteria.where("customerTransactionId").is(transactionId));
            BasicDBObject cus_imagefile = metaDataService.mongoTemplate.findOne(query, BasicDBObject.class, "cus_imagefile");
            if (cus_imagefile!=null){
                String finalUrlPath = webUrl;
                Map<String, Object> fileImageMap = new HashMap<String, Object>();
                BasicDBObject customerImageType = metaDataService.getObjMap("code", (String) cus_imagefile.get("customerImageTypeCode"), "bd_customerimagetype");
                List<String> fileIds = (List<String>) cus_imagefile.get("fileIds");
                List<String> fileUrls = new ArrayList<String>();
                List<String> suffixes = (List<String>) customerImageType.get("suffixes");
                if (fileIds.size() > 0 && !suffixes.contains("pdf")) {
                    fileImageMap.put("name", (String) customerImageType.get("name"));
                    for (String fileId : fileIds) {
                        fileUrls.add(String.format("%s/json/file/download/%s", finalUrlPath, fileId));
                    }
                    fileImageMap.put("fileUrls", fileUrls);
                    //添加到列表
                    fileImageList.add(fileImageMap);
                }
            }
        }
        Map map = new HashMap();
        map.put("customerimages", fileImageList); // 客户图片list
        return map;
    }

    public Map getTemplateCustomerImageFilesMap(List<String> imageTypeCodes, String transactionId){
        Map transactionMap = getTransactionMap(transactionId);
        if (transactionMap == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_CUSTOMERTRANSACTION"), "transaction", transactionId));
            return (Map) new HashMap().put("transaction",null);
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("customerImageTypeCode").in(imageTypeCodes)).addCriteria(Criteria.where("customerId").is(transactionMap.get("customerId"))).addCriteria(Criteria.where("customerTransactionId").is(transactionId));
        List<BasicDBObject> objects = metaDataService.mongoTemplate.find(query, BasicDBObject.class, "cus_imagefile");
        List<Map<String,Object>> fileImageList = new ArrayList<Map<String,Object>>();
        if (objects != null && objects.size() > 0) {
            String finalUrlPath = webUrl;
            objects.forEach(basicDBObject -> {
                Map<String, Object> fileImageMap = new HashMap<String, Object>();
                BasicDBObject customerImageType = metaDataService.getObjMap("code", (String) basicDBObject.get("customerImageTypeCode"), "bd_customerimagetype");
                List<String> fileIds = (List<String>) basicDBObject.get("fileIds");
                List<String> fileUrls = new ArrayList<String>();
                List<String> suffixes = (List<String>) customerImageType.get("suffixes");
                if (fileIds.size() > 0 && !suffixes.contains("pdf")) {
                    fileImageMap.put("name", (String) customerImageType.get("name"));
                    for (String fileId : fileIds) {
                        fileUrls.add(String.format("%s/json/file/download/%s", finalUrlPath, fileId));
                    }
                    fileImageMap.put("fileUrls", fileUrls);
                    //添加到列表
                    fileImageList.add(fileImageMap);
                }
            });
        }
        Map map = new HashMap();
        map.put("templateimages", fileImageList);
        return map;
    }

    public Map getReceptFileMap(String transactionId) {
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "so_recept_file");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_RECEPTFILE"), "receptfile", transactionId));
            return (Map) new HashMap().put("receptfile",null);
        }
        return obj.toMap();
    }

    public Map getSurveyResultMap(String transactionId) {
        BasicDBObject obj = metaDataService.getObjMap("customerTransactionId", transactionId, "so_customer_survey_result");
        if (obj == null) {
            logger.error(String.format(messageService.getMessage("MSG_METADATA_NOT_FOUND_SURVEYRESULT"), "surveyresult", transactionId));
            return (Map) new HashMap().put("surveyresult",null);
        }
        Map map = obj.toMap();
        List<Map<String,Object>> newResult = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> result = (List<Map<String,Object>>) map.get("result");
        for (Map<String,Object>  re : result ){
            Map m = new HashMap();
            String questionCode = (String) re.get("questionCode");
            BasicDBObject question = metaDataService.getObjMap("code", questionCode, "bd_questions");
            if (question != null){
                re.put("question",question);
                m.put("questionType",question.get("questionType"));
                m.put("content",question.get("content"));
            }
            newResult.add(re);
            m.put("answerContents",re.get("answerContents"));
            map.put(questionCode,m);
        }
        map.put("result",newResult);
        return map;
    }


    //  取整
    public static int ceil (int v1, int v2){
        int divisor = v1 / v2;
        int remainder = v1 % v2;
        return remainder > 0 ? divisor + 1 : divisor;
    }


}