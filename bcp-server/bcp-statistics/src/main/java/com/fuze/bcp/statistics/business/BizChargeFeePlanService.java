package com.fuze.bcp.statistics.business;

import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.service.ICashSourceBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.creditcar.bean.PoundageSettlementBean;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.service.*;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.statistics.bean.ChargeFeePlanBean;
import com.fuze.bcp.api.statistics.bean.ChargeFeePlanDetailBean;
import com.fuze.bcp.api.statistics.bean.ChargeFeePlanErrorExport;
import com.fuze.bcp.api.statistics.service.IChargeFeePlanBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.statistics.domain.BalanceAccountDetail;
import com.fuze.bcp.statistics.domain.ChargeFeePlan;
import com.fuze.bcp.statistics.domain.ChargeFeePlanDetail;
import com.fuze.bcp.statistics.service.IBalanceAccountDetailService;
import com.fuze.bcp.statistics.service.IChareFeePlanService;
import com.fuze.bcp.utils.CalculatorUtil;
import com.fuze.bcp.utils.SimpleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by GQR on 2017/10/23.
 */
@Service
public class BizChargeFeePlanService implements IChargeFeePlanBizService {

    /**
     * 日志序列化
     */
    private static final Logger logger = LoggerFactory.getLogger(BizChargeFeePlanService.class);

    @Autowired
    IOrderBizService iOrderBizService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ICarRegistryBizService iCarRegistryBizService;

    @Autowired
    ICarTransferBizService iCarTransferBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    IPickupCarBizService iPickupCarBizService;

    @Autowired
    IChareFeePlanService iChargeFeePlanService;

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IPoundageSettlementBizService iPoundageSettlementBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    IBalanceAccountDetailService iBalanceAccountDetailService;

    @Autowired
    ICashSourceBizService iCashSourceBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    /**
     * 通过searchBean来获取列表以及模糊查询
     * @param searchBean
     * @return
     */
    @Override
    public ResultBean<ChargeFeePlanBean> actSearchChargeFeePlans(SearchBean searchBean) {
        Page<ChargeFeePlan> chargefeeplan = iChargeFeePlanService.findAllBySearchBean(ChargeFeePlan.class, searchBean,SearchBean.STAGE_ORDER);
        return ResultBean.getSucceed().setD(mappingService.map(chargefeeplan,ChargeFeePlanBean.class));
    }

    /**
     * 根据客户交易ID信息创建银行收款计划表
     *
     * @param orderId
     * @return
     */
    public ResultBean<ChargeFeePlanBean> actCreatePlan(String orderId) {
        /*PurchaseCarOrderBean purchaseCarOrderBean = iOrderBizService.actGetOrder(orderId).getD();
        String transactionId = purchaseCarOrderBean.getCustomerTransactionId();
        ResultBean<CustomerTransactionBean> transactionBean = iCustomerTransactionBizService.actGetCustomerTransactionById(transactionId);
        CustomerBean d = iCustomerBizService.actGetCustomerById(transactionBean.getD().getCustomerId()).getD();
        if (transactionBean.failed()) {
            deleteErrorDataByTransactionId(transactionId);
            return ResultBean.getFailed().setM(transactionBean.getM());
        }
        CustomerTransactionBean customerTransactionBean = transactionBean.getD();
        if (customerTransactionBean == null) {
            deleteErrorDataByTransactionId(transactionId);
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_NOFINDCUSTOMER"), transactionId));
        }
        if (customerTransactionBean.getStatus() == CustomerTransactionBean.TRANSACTION_STOP) {
            deleteErrorDataByTransactionId(transactionId);
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_REGECT"), d.getName(), d.getIdentifyNo(), transactionId));
        }
        if (customerTransactionBean.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED || customerTransactionBean.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING) {
            deleteErrorDataByTransactionId(transactionId);
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_ALREADYCANCEL"), d.getName(), d.getIdentifyNo(), transactionId));
        }
        BankCardApplyBean cardApply = iBankCardApplyBizService.actFindBankCardApplyByTransactionId(transactionId).getD();
        if (cardApply != null) {
            return createChargeFeePlan(cardApply, transactionBean.getD());
        } else {
            iCustomerTransactionBizService.startBankCard(transactionBean.getD());
            return actCreateErrorChargeFeePlan(transactionBean.getD());
        }*/
        return null;
    }



    /**
     * 核对一条消息
     * @param id
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<ChargeFeePlanBean> actCheckOne(String id,String loginUserId, SignInfo signInfo) {
        ChargeFeePlan rp =iChargeFeePlanService.findOneById(id);
        if (rp != null) {
            if(rp.getStatus() == ChargeFeePlan.STATUS_CHEKCED_PASS){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CHARGEFEEPLAN_CHECKED"));
            }
            if (rp.getStatus() == ChargeFeePlan.STATUS_ERROR || rp.getStatus() == ChargeFeePlan.STATUS_INIT) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CHARGEFEEPLAN_ERROR"));
            }
            signInfo.setFlag(SignInfo.FLAG_COMMENT);
            rp.addSignInfo(signInfo);
            if(signInfo.getResult() == 2){
                rp.setStatus(ChargeFeePlanBean.STATUS_CHEKCED_PASS);
            }
            if(signInfo.getResult() == 3){
                rp.setStatus(ChargeFeePlanBean.STATUS_CHEKCED_ERROR);
            }
            rp.setCheckUserId(loginUserId);
            ChargeFeePlan chargeFeePlan = iChargeFeePlanService.saveOneChargeFeePlan(rp);
            return ResultBean.getSucceed().setD(mappingService.map(chargeFeePlan,ChargeFeePlanBean.class)).setM(messageService.getMessage("MSG_CHARGEFEEPLAN_CHECKSUCCEED"));
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CHARGEFEEPLAN_NOSELECT"));
        }
    }

    /**
     * 核对多条消息
     * @param ids
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<String> actCheck(List<String> ids,String loginUserId, SignInfo signInfo) {
        List<ChargeFeePlan>  list = new ArrayList<ChargeFeePlan>();
        int succeed = 0;
        int failed = 0;
        for (String c:ids) {
            ResultBean res  = actCheckOne(c,loginUserId, signInfo);
            if(res.failed()){
                failed++;
            }else{
                succeed++;
            }
        }
        return ResultBean.getSucceed().setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_CHECKCOUNTS"),succeed,failed));
    }

    /**
     * 取消核对一条消息
     * @param id
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<ChargeFeePlanBean> actUncheckOne(String id, SignInfo signInfo) {
        ChargeFeePlan rp = iChargeFeePlanService.findOneById(id);
        if (rp != null) {
            if (rp.getStatus() == ChargeFeePlan.STATUS_CHEKCED_PASS) {
                List<BalanceAccountDetail> balanceAccountDetails = iBalanceAccountDetailService.findAllBalanceAccountDetail(rp.getCustomerTransactionId());
                if(balanceAccountDetails != null && balanceAccountDetails.size() > 0){
                    return ResultBean.getFailed().setD(ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_CANNOTCANCEL"))));
                }
                rp.addSignInfo(signInfo);
                rp.setStatus(ChargeFeePlan.STATUS_NORMAL);
                return ResultBean.getSucceed().setD(mappingService.map(iChargeFeePlanService.saveOneChargeFeePlan(rp),ChargeFeePlanBean.class)).setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_CANCELSUCCEES")));
            }else if(rp.getStatus() == ChargeFeePlan.STATUS_CHEKCED_ERROR){
                return ResultBean.getFailed().setD(ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_ERRORNOCANCEL"))));
            }
            return ResultBean.getFailed().setD(ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_NOCANCEL"))));
        }
        return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_NOFIND")));
    }

    /**
     * 取消核对多条消息
     * @param
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<String> actUncheck(List<String> ids, SignInfo signInfo) {
        List<ChargeFeePlan>  list = new ArrayList<ChargeFeePlan>();
        int succeed = 0;
        int failed = 0;
        for (String c:ids) {
            ResultBean res  = actUncheckOne(c, signInfo);
            if(res.failed()){
                failed++;
            }else{
                succeed++;
            }
        }
        return ResultBean.getSucceed().setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_CANCELCHECK"),succeed,failed));
    }

    /**
     * 导出错误数据
     * @param
     * @return
     */
    @Override
    public ResultBean<List<ChargeFeePlanErrorExport>> actExportFailedExcel() {
        //导出所有错误数据
        List<ChargeFeePlan> rpList = iChargeFeePlanService.findAllByStatusOrderByCardealerId(ChargeFeePlanBean.STATUS_ERROR);
        if(rpList.size() != 0){

        logger.info(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_NEEDEXPORT"), rpList.size()));
        List<List<Object>> dataSet = new ArrayList<List<Object>>();
        CustomerBean customer = null;
        if (rpList != null && rpList.size() >0 ){

            for (int i = 0; i < rpList.size(); i++)  {
                List<Object>  rowData = new ArrayList<Object>();
                ChargeFeePlan rp = rpList.get(i);
                CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(rp.getCustomerTransactionId()).getD();
                //分期经理
                EmployeeBean employeeBean = iOrgBizService.actGetEmployee(customerTransaction.getEmployeeId()).getD();
                //"贴息/商贷"
                customer = iCustomerBizService.actGetCustomerById(rp.getCustomerId()).getD();
                CustomerLoanBean customerLoanBean = getCustomerLoadByTransaction(customerTransaction.getId());
                rowData.add("NC".equals(customerTransaction.getBusinessTypeCode())? "二手车" : "新车");
                rowData.add(employeeBean.getUsername());
                rowData.add(employeeBean.getCell());
                rowData.add(customerLoanBean.getCompensatoryInterest() == 1 ? "贴息" : "商贷");
                rowData.add(customer.getName());
                rowData.add(customer.getIdentifyNo());
                rowData.add(customer.getCells() != null ? customer.getCells().get(0) : "无" );
                rowData.add(customerTransaction.getTs().substring(0, 10));
                rowData.add(rp.getComment());
                dataSet.add(rowData);
                logger.info(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_SYNCFILE"), i + 1, customer.getName()));
            }
        }


        return ResultBean.getSucceed().setD(dataSet);
        }else {
            return ResultBean.getFailed();
        }
    }

    @Override
    public ResultBean<List<ChargeFeePlanBean>> actSyncAllBankCard(String beforeDate) {
        return null;
    }

    @Override
    public ResultBean<List<ChargeFeePlanBean>> actFindFailedExcel() {

        List<ChargeFeePlan>  rpList = iChargeFeePlanService.findAllByStatusOrderByCardealerId(ChargeFeePlanBean.STATUS_ERROR);
        if(rpList != null){
            return ResultBean.getSucceed().setD(rpList);
        }else {
            return ResultBean.getFailed();
        }
    }

    @Override
    public ResultBean<ChargeFeePlanBean> actChargeFeePlan(String transactionId) {
        ChargeFeePlan chargeFeePlan = iChargeFeePlanService.findByCustomerTransactionId(transactionId);
        if(chargeFeePlan != null){
            return ResultBean.getSucceed().setD(mappingService.map(chargeFeePlan,ChargeFeePlanBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<ChargeFeePlanBean> actSaveChargeFeePlan(ChargeFeePlanBean chargeFeePlanBean) {
        ChargeFeePlan chargeFeePlan = iChargeFeePlanService.save(mappingService.map(chargeFeePlanBean, ChargeFeePlan.class));
        return ResultBean.getSucceed().setD(mappingService.map(chargeFeePlan,ChargeFeePlanBean.class));
    }

    /**
     * 生成还款计划表
     * @return
     */
    public ResultBean<String> actSyncAllOrders(){

        int pageIndex = 0;
        int pageSize = 50;
        int failedCount = 0;
        int succeedCount = 0;
       // Pageable page = new PageRequest(pageIndex, pageSize);
        List<CustomerTransactionBean> transactions = iCustomerTransactionBizService.actFindAllTransactions().getD();
        StringBuffer buffer = new StringBuffer();
        while (transactions != null && transactions.size() > 0) {
            for (CustomerTransactionBean transaction : transactions) {
                ResultBean<ChargeFeePlanBean> res = actCreatePlanByTransaction(transaction.getId(),new SignInfo());
                if (res.failed()) {
                    failedCount++;
                    logger.error(res.getM());
                    buffer.append(res.getM());
                } else {
                    succeedCount++;
                    logger.info(res.getM());
                }
            }
            logger.info(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_REFRESHFINISH"), transactions.size()));
            pageIndex++;
            // page = new PageRequest(pageIndex, pageSize);
            transactions = iCustomerTransactionBizService.actFindAllTransactions().getD();
        }
        logger.info(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_SYNCCOUNT"), failedCount + succeedCount, succeedCount, failedCount));
        return ResultBean.getSucceed().setM(buffer.toString());
    }

    /**
     * 根据某个日期月份年的刷卡数据，计算收款计划表
     * @param date
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<String> actSyncSwingCard(String date,SignInfo signInfo) {
        logger.info(String.format("开始计算%s的收款计划表！",date));
        List<String>    swingTransactionIds = iBankCardApplyBizService.getDailySwipingMoneyTransactionIds(date).getD();
        logger.info(String.format("【%s】总计查询了【%s】条已经刷卡交易需要生成收款计划表！，开始生成收款计划表",date,swingTransactionIds.size()));
        for (String transactionId:swingTransactionIds) {
            ResultBean resultBean = actCreatePlanByTransaction(transactionId,signInfo);
            if(resultBean.failed()){
                logger.error(resultBean.getM());
            }else{

                logger.info(String.format("创建客户交易【%s】的收款计划表成功！",transactionId));
            }
        }
        if(swingTransactionIds.size() == 0){
            logger.info(String.format("计算%s的收款计划表为空！",date));
            return ResultBean.getSucceed().setD(String.format("计算%s的收款计划表为空！",date));
        }else {
            logger.info(String.format("完成计算%s的收款计划表！",date));
            return ResultBean.getSucceed().setD(String.format("完成计算%s的收款计划表！",date));
        }

    }

    @Override
    public ResultBean<List<ChargeFeePlanDetailBean>> actFindOneDetailById(String chargeFeePlanId) {
        List<ChargeFeePlanDetail> oneDetail = iChargeFeePlanService.findOneDetail(chargeFeePlanId);
        if(oneDetail != null){
            return ResultBean.getSucceed().setD(mappingService.map(oneDetail,ChargeFeePlanDetailBean.class));
        }else {
            return ResultBean.getFailed();
        }
    }

    @Override
    public ResultBean<List<ChargeFeePlanDetailBean>> actFindAllDetail() {
        List<ChargeFeePlanDetail> allDetail = iChargeFeePlanService.findAllDetail();
        if(allDetail != null){
            return ResultBean.getSucceed().setD(mappingService.map(allDetail,ChargeFeePlanDetailBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<ChargeFeePlanBean> actGetChargeFeePlanById(String id) {
        ChargeFeePlan chargeFeePlan = iChargeFeePlanService.findOneById(id);
        if(chargeFeePlan != null){
            return  ResultBean.getSucceed().setD(mappingService.map(chargeFeePlan,ChargeFeePlanBean.class));
        } else {
            return ResultBean.getFailed().setM("根据id未找到相应的收款计划");
        }
    }

    /**
     * 查找错误数据信息
     * @param status
     * @return
     */
    @Override
    public ResultBean<List<ChargeFeePlanBean>> actFindErrorStatus(Integer status) {
        List<ChargeFeePlan> chargeFeePlan = iChargeFeePlanService.findByStatus(status);
        if(chargeFeePlan != null){
            return  ResultBean.getSucceed().setD(mappingService.map(chargeFeePlan,ChargeFeePlanBean.class));
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CHARGEFEEPLAN_NOERRORDATA"));
        }
    }

    /*
     *根据交易ID创建还款计划表
     */
    public ResultBean<ChargeFeePlanBean> actCreatePlanByTransaction(String transactionId,SignInfo signInfo){

        ResultBean<CustomerTransactionBean> customerTransactionBean = iCustomerTransactionBizService.actFindCustomerTransactionById(transactionId);
        if (customerTransactionBean.failed()) {
            deleteErrorDataByTransactionId(transactionId);
            return ResultBean.getFailed().setM(customerTransactionBean.getM());
        }
        CustomerTransactionBean transactionBean = customerTransactionBean.getD();
        CustomerBean customerBean = iCustomerBizService.actGetCustomerById(transactionBean.getCustomerId()).getD();
        if (transactionBean == null) {
            deleteErrorDataByTransactionId(transactionId);
            return ResultBean.getFailed().setM(String.format("根据交易ID【%s】查询不到对应的客户订单，无法创建银行收款计划表！", transactionId));
        }
        if (transactionBean.getStatus()== CustomerTransactionBean.TRANSACTION_STOP) {
            deleteErrorDataByTransactionId(transactionId);
            return ResultBean.getFailed().setM(String.format("客户【%s】身份证号码【%s】的交易订单【%s】被拒绝，无法创建银行收款计划表！",customerBean .getName(), customerBean.getIdentifyNo(),transactionId));
        }
        CustomerTransactionBean ct = iCustomerTransactionBizService.actFindCustomerTransactionById(transactionId).getD();
        if (ct.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED || ct.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING) {
            deleteErrorDataByTransactionId(transactionId);
            return ResultBean.getFailed().setM(String.format("客户【%s】身份证号码【%s】的交易订单【%s】已经在取消状态，无法创建银行收款计划表！", customerBean .getName(),  customerBean.getIdentifyNo(), transactionId));
        }
        BankCardApplyBean cardApply = iBankCardApplyBizService.actFindBankCardApplyByTransactionId(transactionId).getD();
        if (cardApply != null) {
            return createChargeFeePlan(cardApply,transactionBean,signInfo);
        } else {
            iBankCardApplyBizService.actStartBankCardByTransaction(transactionBean);
            return actCreateErrorChargeFeePlan(transactionBean,signInfo);
        }
    }


    /**
     * 创建银行收款计划表对象，没有银行放款，状态错误
     *
     * @param
     * @return
     */
    private ResultBean<ChargeFeePlanBean> actCreateErrorChargeFeePlan(CustomerTransactionBean customerTransactionBean,SignInfo signInfo) {
        ChargeFeePlan rp = iChargeFeePlanService.findOneByTransactionId(customerTransactionBean.getId());
        CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerTransactionBean.getCustomerId()).getD();
        if (rp == null) {
            rp = new ChargeFeePlan();
        }
        rp.setCustomerTransactionId(customerTransactionBean.getId());
        String msg = validOrder(customerTransactionBean);
        if (msg != null) {
            return ResultBean.getFailed().setM(msg).setD(mappingService.map(saveError(msg, rp,signInfo),ChargeFeePlanBean.class));
        }
        CustomerLoanBean cus = getCustomerLoadByTransaction(customerTransactionBean.getId());
       // customerTransactionBean = updateOrderData(customerTransactionBean);
        rp.setCardealerId(customerTransactionBean.getCarDealerId());
        rp.setCustomerId(customerTransactionBean.getCustomerId());
        rp.setBankChargeRatio(cus.getRateType().getRatio());
        rp.setChargeAmount(cus.getBankFeeAmount());
        rp.setChargePaymentWay(cus.getChargePaymentWay());
        rp.setCreditMonths(cus.getRateType().getMonths());
        rp.setLimitAmount(cus.getCreditAmount());
        rp.setTotalAmount(cus.getCreditAmount()+cus.getRealityBankFeeAmount());
        rp.setOrderTime(cus.getTs());

        //  1.1 银行手续费按照小数点存储

        msg = String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_NOBANKCARD"),customerBean.getName() , customerBean.getIdentifyNo());
        rp.setComment(msg);
        rp.setStatus(ChargeFeePlan.STATUS_ERROR);
        return ResultBean.getFailed().setD(mappingService.map(iChargeFeePlanService.saveOneChargeFeePlan(rp),ChargeFeePlanBean.class)).setM(msg);
    }


    /**
     * 保存数据计算的状态
     *
     * @param errorMsg
     * @param rp
     * @return
     */
    private ChargeFeePlan saveError(String errorMsg, ChargeFeePlan rp,SignInfo signInfo) {
        rp.setStatus(ChargeFeePlan.STATUS_ERROR);
        rp.setComment(errorMsg);
        rp.setTs(SimpleUtils.getCreateTime());
        signInfo.setResult(ChargeFeePlan.STATUS_ERROR);
        rp.getSignInfos().add(signInfo);
        return iChargeFeePlanService.saveOneChargeFeePlan(rp);
    }

    /**
     * 根据TransactionId获取最后的CustomerLoanBean
     * @param transactionId
     * @return
     */
    private CustomerLoanBean getCustomerLoadByTransaction(String transactionId){
        PurchaseCarOrderBean orderBean =   iOrderBizService.actGetOrderByTransactionId(transactionId).getD();
        if(orderBean == null)   return null;
        return iCustomerBizService.actGetCustomerLoanById(orderBean.getCustomerLoanId()).getD();
    }

    /**
     * 校验订单数据的逻辑
     *
     * @param
     * @return
     */
    private String validOrder(CustomerTransactionBean customerTransactionBean) {
        CustomerLoanBean cusLoan = getCustomerLoadByTransaction(customerTransactionBean.getId());
        CustomerBean customerBean = iCustomerBizService.actGetCustomerById(cusLoan.getCustomerId()).getD();
        if (cusLoan.getCreditAmount() == null) {
            return String.format("客户【%s】身份证号码【%s】的签约订单【%s】中的贷款信息creditAmount为空了，请补充数据口进行再次计算！", customerBean.getName(), customerBean.getIdentifyNo(),customerTransactionBean.getId());
        }
        PoundageSettlementBean poundageSettlementBean = iPoundageSettlementBizService.actGetOneByCustomerTransactionId(customerTransactionBean.getId()).getD();
        if(poundageSettlementBean != null && poundageSettlementBean.getSettlementCashSourceId() == null){
            return String.format("客户【%s】身份证号码【%s】的签约订单【%s】中的代码行信息为空了，请在客户签约页面检查数据后再次计算！", customerBean.getName(), customerBean.getIdentifyNo(),customerTransactionBean.getId());
        }
        //一年期限，3万起贷，手续费最低是1200元
        double minCharge = 1200;
        double minChargeRatio = 4.0;
        if ((cusLoan.getBankFeeAmount() == null || cusLoan.getBankFeeAmount() < minCharge
        ) && ( cusLoan.getRateType().getRatio()== null || cusLoan.getRateType().getRatio() < minChargeRatio)) {

            return String.format("客户【%s】身份证号码【%s】的签约订单【%s】中的借款信息【SalePrice】中手续费【charge应该大于等于1200元】和手续费率【fuzeChargeRatio应该大于等于大于等于4】都不符合实际业务最低值或者为空，请补充客户数据后进行再次计算！", customerBean.getName(), customerBean.getIdentifyNo(), customerTransactionBean.getId());
        }
        return null;
    }

    /**
     * 创建收款计划
     * @param cardApply
     * @param transactionBean
     * @param signInfo
     * @return
     */
    private ResultBean<ChargeFeePlanBean> createChargeFeePlan(BankCardApplyBean cardApply, CustomerTransactionBean transactionBean,SignInfo signInfo) {
        ChargeFeePlan chargeFeePlan = iChargeFeePlanService.findOneByTransactionId(transactionBean.getId());
        CustomerBean customer = iCustomerBizService.actGetCustomerById(transactionBean.getCustomerId()).getD();
        if(chargeFeePlan == null){
            chargeFeePlan = new ChargeFeePlan();
        }else {
            if(chargeFeePlan.getStatus() == ChargeFeePlan.STATUS_CHEKCED_PASS){
                String msg = String.format("客户【%s】收款计划表已经复核完成，不需要再重新生成！", customer.getName());
                return ResultBean.getFailed().setM(msg).setD(mappingService.map(chargeFeePlan,ChargeFeePlanBean.class));
            }
        }
        chargeFeePlan.setCustomerTransactionId(transactionBean.getId());
        chargeFeePlan.setCardealerId(transactionBean.getCarDealerId());
        chargeFeePlan.setCustomerId(transactionBean.getCustomerId());
        chargeFeePlan.setEmployeeId(transactionBean.getEmployeeId());
        String msg = validOrder(transactionBean);
        if(msg != null){
            return ResultBean.getFailed().setM(msg).setD(mappingService.map(saveError(msg,chargeFeePlan,signInfo),ChargeFeePlanBean.class));
        }

        CustomerLoanBean cus = getCustomerLoadByTransaction(transactionBean.getId());
        //  1.1 银行手续费按照小数点存储
        chargeFeePlan.setBankChargeRatio(cus.getRateType().getRatio());
        chargeFeePlan.setChargeAmount(cus.getBankFeeAmount());
        chargeFeePlan.setChargePaymentWay(cus.getChargePaymentWay());
        chargeFeePlan.setCreditMonths(cus.getRateType().getMonths());
        chargeFeePlan.setLimitAmount(cus.getCreditAmount());
        chargeFeePlan.setTotalAmount(cus.getCreditAmount()+cus.getRealityBankFeeAmount());
        chargeFeePlan.setOrderTime(cus.getTs());
        //计算该笔业务的返佣政策
        msg = validaBankCard(cardApply, transactionBean);
        if(msg != null){
            return ResultBean.getFailed().setM(msg).setD(mappingService.map(saveError(msg,chargeFeePlan,signInfo),ChargeFeePlanBean.class));
        }
        if(cardApply.getFirstReimbursement() != null){
            //首次收款日期
          // chargeFeePlan.setFirstRepaymentDate(cardApply.getFirstReimbursement());
            //最后一次收款日期
           // chargeFeePlan.setEndRepaymentDate(SimpleUtils.getOffsetMonthToday(chargeFeePlan.getFirstRepaymentDate(),chargeFeePlan.getCreditMonths()-1));
        }
        //刷卡日期
        chargeFeePlan.setSwingCardDate(cardApply.getSwipingShopTime()==null?cardApply.getSwipingTrusteeTime() : cardApply.getSwipingShopTime());
        chargeFeePlan.setComment(transactionBean.getComment());
        chargeFeePlan.setStatus(chargeFeePlan.STATUS_NORMAL);
        signInfo.setResult(chargeFeePlan.STATUS_NORMAL);
        chargeFeePlan.getSignInfos().add(signInfo);
        chargeFeePlan.setDefaultReimbursement(cardApply.getDefaultReimbursement());
        //实际刷卡费率-基准费率（特殊店的利率）
        double chargeCheckRatio =  getCheckChargeRatio(transactionBean.getBusinessTypeCode(),chargeFeePlan);
        if(chargeCheckRatio<=0.0){
            msg = String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_ERRORCOUNT"),transactionBean.getBusinessTypeCode(),chargeFeePlan.getCardealerId(),chargeFeePlan.getCreditMonths(),chargeFeePlan.getCustomerTransactionId(),chargeCheckRatio);
            return ResultBean.getFailed().setM(msg).setD(mappingService.map(saveError(msg, chargeFeePlan,signInfo),ChargeFeePlanBean.class));
        }else{
            logger.info(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_RIGHTCOUNT"),transactionBean.getBusinessTypeCode(),chargeFeePlan.getCardealerId(),chargeFeePlan.getCreditMonths(),chargeFeePlan.getCustomerTransactionId(),chargeCheckRatio));
        }
        chargeFeePlan.setChargeCheckRatio(chargeCheckRatio);
        chargeFeePlan.setTs(SimpleUtils.getCreateTime());
        if(StringUtils.isEmpty(chargeFeePlan.getId())) {
            chargeFeePlan = iChargeFeePlanService.saveOneChargeFeePlan(chargeFeePlan);
        }
        chargeFeePlan = updatePlanDetail(chargeFeePlan);
        chargeFeePlan = iChargeFeePlanService.saveOneChargeFeePlan(chargeFeePlan);

        return ResultBean.getSucceed().setD(mappingService.map(chargeFeePlan,ChargeFeePlanBean.class)).setM(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_CREATEFINISH"), customer.getName()));
    }

    private void deleteErrorDataByTransactionId(String transactionId) {
        ChargeFeePlan chargeFeePlan = iChargeFeePlanService.findOneByTransactionId(transactionId);
        if(chargeFeePlan != null){
            iChargeFeePlanService.deleteOneChargeFeePlan(chargeFeePlan);
        }
        BankCardApplyBean bankCardApply = iBankCardApplyBizService.actFindBankCardApplyByTransactionId(transactionId).getD();
        if(bankCardApply != null){
            iBankCardApplyBizService.actDeleteBankCardApply(bankCardApply);
        }

    }

    /**
     * 计算银行收款计划表明细
     *
     * @param rp
     * @return
     */
    private ChargeFeePlan updatePlanDetail(ChargeFeePlan rp) {
        if (rp.getStatus() == ChargeFeePlan.STATUS_ERROR) {
            return rp;
        }
        rp.clearDetail();
        if(rp.getId() != null){
            iChargeFeePlanService.deleteDetailByChargeFeePlanId(rp.getId());
        }
        Double firstAmount = CalculatorUtil.calculatorFirstPayment(rp.getLimitAmount(), rp.getCreditMonths());
        Double mongthlyAmount = CalculatorUtil.calculatorMonthlyPayments(rp.getLimitAmount(), rp.getCreditMonths());

        Double firstCharge = rp.getChargeAmount();
        Double monthlyCharge = 0.0;

        if (rp.getChargePaymentWay().equals("STAGES")) {
            firstCharge = CalculatorUtil.calculatorFirstPayment(rp.getChargeAmount(), rp.getCreditMonths());
            monthlyCharge = CalculatorUtil.calculatorMonthlyPayments(rp.getChargeAmount(), rp.getCreditMonths());
        }
        //添加首月收款计划
        String firstDate = rp.getSwingCardDate();
        rp.addOneDetail(rp,1, firstDate, firstAmount, firstCharge);
        if (rp.getChargePaymentWay().equals("STAGES")) {
            //添加每个月的收款计划
            for (int i = 2; i <= rp.getCreditMonths(); i++) {
                rp.addOneDetail(rp,i, SimpleUtils.getOffsetMonthToday(firstDate, i - 1), mongthlyAmount, monthlyCharge);
            }
        }

        //保存明细数据
        rp.setDetailList(iChargeFeePlanService.saveDetail(rp.getDetailList()));
        return rp;
    }



    /**
     * 校验卡业务处理的日期是否正确
     *
     * @param cardApply
     * @return
     */
    private String validaBankCard(BankCardApplyBean cardApply, CustomerTransactionBean  transactionBean) {
        CustomerBean customer = iCustomerBizService.actGetCustomerById(cardApply.getCustomerId()).getD();
//        if(cardApply.getStatus() != 6 && cardApply.getStatus() != 5  ){
//            return  String.format("客户【%s】身份证号码【%s】的银行卡业务处理状态不正确，还不是已刷卡状态！",customer.getName(),customer.getIdentifyNo());
//        }
        if (cardApply.getSwipingShopTime() == null && cardApply.getSwipingTrusteeTime() == null) {
            return String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_NOSWINGDATE"), customer.getName(), customer.getIdentifyNo(), transactionBean.getTs());
        }
//        String swingCardDate = cardApply.getSwipingShopTime() == null ? cardApply.getSwipingTrusteeTime() : cardApply.getSwipingShopTime();
//        if (swingCardDate != null && cardApply.getFirstReimbursement() != null) {
//            int days = SimpleUtils.daysBetween(swingCardDate, cardApply.getFirstReimbursement());
//            if (days < 5) {
//                return String.format("客户【%s】身份证号码【%s】的银行卡业务出现异常，刷卡日期【%s】晚于首次还款日期【%s】，请尽快核对进行修改！", customer.getName(), customer.getIdentifyNo(), swingCardDate, cardApply.getFirstReimbursement());
//            }
//        }
        return null;
    }

   /**
     *
     * @param businessTypeCode
     * @param rp
     * @return
     **/
    private Double getCheckChargeRatio(String businessTypeCode,ChargeFeePlan rp){
        //获取银行需要返给我们的贷款额的百分比
        Double bankCommissionRatio = getRatio("BANK_COMMISSION_RATIO",businessTypeCode,rp.getCreditMonths());
        //根据这个额度除以业务实际收取的手续费率，就是我们获取的百分比
        Double baseCommissionRatioOfCarDealer = SimpleUtils.formatDigit(bankCommissionRatio/rp.getBankChargeRatio(),4);
        return baseCommissionRatioOfCarDealer;
    }

    /**
     * 获取渠道的手续费返佣比例（如果渠道没有设置特殊的返佣比例【参数编码：BANK_CARDEALER_COMMISSION_RATIO】，则依据银行手续费返佣比例【参数编码：BANK_COMMISSION_RATIO】查询
     * @param carDealerId 渠道ID
     * @param creditMonths  贷款期限
     * @param businessTypeCode 业务类型编码
     * @return
     */
    private Double getCommissionRatioOfCarDealer(String carDealerId,Integer creditMonths,String businessTypeCode){

        Double commissionRatio = getRatio("BANK_COMMISSION_RATIO",businessTypeCode,creditMonths);

        Map mapCardealerBaseRatio = iParamBizService.actGetMap("BANK_CARDEALER_COMMISSION_RATIO").getD();
        if(mapCardealerBaseRatio != null)
        {
            Map cardealerMap = (Map) mapCardealerBaseRatio.get(carDealerId);
            if(cardealerMap != null){
                Map map = (Map)cardealerMap.get(businessTypeCode);
                if(map != null){
                    String str = (String)map.get(creditMonths.toString());
                    if(str != null) {
                        commissionRatio = Double.parseDouble(str);
                    }
                }
            }
        }
        return commissionRatio;
    }


    /**
     * 获取客户的银行结费率参数
     * @param pcode
     * @param businessTypeCode
     * @param creditMonths
     * @return
     */

    private Double getRatio(String pcode,String businessTypeCode,Integer creditMonths){
        Double ratio = 0.0;
        Map mapBaseRatio = iParamBizService.actGetMap(pcode).getD();
        if(mapBaseRatio == null){
            logger.error(String.format(messageService.getMessage("MSG_CHARGEFEEPLAN_NOMESSAGE"),pcode));
        }else{
            Map map =  (Map)mapBaseRatio.get(businessTypeCode);
            String str = (String) map.get(creditMonths.toString());
            ratio = Double.parseDouble(str);
        }
        return ratio;
    }


    /**
     * 重新生成银行收款计划单
     *
     * @param chargeFeePlanId 银行收款计划单数据
     * @return
     */
    public ResultBean<ChargeFeePlanBean> actRecreateChargeFeePlan(String chargeFeePlanId,SignInfo signInfo) {
        ChargeFeePlan rp = iChargeFeePlanService.findOneById(chargeFeePlanId);
        return actCreatePlanByTransaction(rp.getCustomerTransactionId(),signInfo);
    }



}
