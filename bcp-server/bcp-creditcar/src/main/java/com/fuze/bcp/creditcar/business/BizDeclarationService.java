package com.fuze.bcp.creditcar.business;


import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.CashSourceEmployeeBean;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.HistoryRecord;
import com.fuze.bcp.api.bd.service.*;
import com.fuze.bcp.api.creditcar.bean.CustomerContractBean;
import com.fuze.bcp.api.creditcar.bean.declaration.*;
import com.fuze.bcp.api.creditcar.service.ICustomerContractBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerSurveyTemplateBizService;
import com.fuze.bcp.api.creditcar.service.IDeclarationBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.file.bean.DocumentTypeBean;
import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.bean.TemplateObjectBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ImageTypeFileBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.creditcar.domain.Declaration;
import com.fuze.bcp.creditcar.domain.DeclarationHistorys;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.creditcar.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.service.TemplateService;
import com.fuze.bcp.utils.DateTimeUtils;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zqw on 2017/8/5.
 */
@Service
public class BizDeclarationService implements IDeclarationBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizDeclarationService.class);

    @Autowired
    IDeclarationService iDeclarationService;

    @Autowired
    IDeclarationHistorysService iDeclarationHistorysService;

    @Autowired
    ICustomerDemandService iCustomerDemandService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerContractBizService iCustomerContractBizService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    ICashSourceBizService iCashSourceBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ITemplateBizService iTemplateBizService;

    @Autowired
    ICustomerImageTypeBizService iCustomerImageTypeBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    IFileBizService iFileBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    ICustomerSurveyTemplateBizService iCustomerSurveyTemplateBizService;

    @Autowired
    ICustomerContractService iCustomerContractService;

    @Autowired
    IMessageBizService iMessageBizService;

    private static final String DEFAULT_ENCODING = "utf-8";

    private Template template;

    @Autowired
    TemplateService templateService;

    @Autowired
    public MongoTemplate mongoTemplate;

    /**
     * 列表分页
     *
     * @param currentPage
     * @return
     */
    @Override
    public ResultBean<DeclarationBean> actGetDeclarations(Integer currentPage) {
        Page<Declaration> declarations = iDeclarationService.findAllByOrderByTsDesc(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(declarations, DeclarationBean.class));
    }

    //  模糊查询
    @Override
    public ResultBean<DeclarationBean> actSearchDeclaration(DeclarationBean declarationBean, String name, int currentPage) {
        List<String> customerIds = new ArrayList<String>();
        if(!StringUtils.isEmpty(name) && !"undefined".equals(name)){
            CustomerBean customerBean = new CustomerBean();
            customerBean.setName(name);
            List<CustomerBean> customerBeanList = iCustomerBizService.actSearchCustomer(customerBean).getD();
            for (CustomerBean cb : customerBeanList) {
                customerIds.add(cb.getId());
            }
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").gt(DataStatus.TEMPSAVE));
        query.addCriteria(Criteria.where("customerId").in(customerIds));
        if (!StringUtils.isEmpty(declarationBean.getCustomerClass()))
            query.addCriteria(Criteria.where("customerClass").is(declarationBean.getCustomerClass()));
        if (!StringUtils.isEmpty(declarationBean.getStatus()) && declarationBean.getStatus()!= -1)
            query.addCriteria(Criteria.where("status").is(declarationBean.getStatus()));
        if (!StringUtils.isEmpty(declarationBean.getTs()))
            query.addCriteria(Criteria.where("ts").regex(Pattern.compile("^.*"+ declarationBean.getTs() +".*$", Pattern.CASE_INSENSITIVE)));

        Pageable pageable = new PageRequest(currentPage, 20);
        query.with(pageable);
        List list = mongoTemplate.find(query,Declaration.class);
        Page<Declaration> page  = new PageImpl(list,pageable, mongoTemplate.count(query,Declaration.class));
        return ResultBean.getSucceed().setD(mappingService.map(page, DeclarationBean.class));
    }

    /**
     * 通过  交易ID      查询 银行报批数据
     *
     * @param transactionId
     * @return
     */
    @Override
    public ResultBean<DeclarationBean> actGetTransactionDeclaration(String transactionId) {
        Declaration declaration = iDeclarationService.findByCustomerTransactionId(transactionId);
        if (declaration == null) {
            CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(transactionId);
            declaration = new Declaration();
            if (customerDemand != null) {
                declaration.setCustomerId(customerDemand.getCustomerId());
                declaration.setBusinessTypeCode(customerDemand.getBusinessTypeCode());
                declaration.setLoginUserId(customerDemand.getLoginUserId());
                declaration.setEmployeeId(customerDemand.getEmployeeId());
                declaration.setCarDealerId(customerDemand.getCarDealerId());
                declaration.setOrginfoId(customerDemand.getOrginfoId());
                declaration.setCashSourceId(customerDemand.getCashSourceId());
            }
            declaration.setCustomerTransactionId(transactionId);
            declaration.setDataStatus(DataStatus.SAVE);
            declaration.setApproveStatus(ApproveStatus.APPROVE_INIT);
            declaration = iDeclarationService.save(declaration);
        }
        return ResultBean.getSucceed().setD(mappingService.map(declaration, DeclarationBean.class));
    }

    /**
     * 通过  银行报批 ID      查询 银行报批数据
     *
     * @param id
     * @return
     */
    @Override
    public ResultBean<DeclarationBean> actGetDeclaration(String id) {
        Declaration declaration = iDeclarationService.getOne(id);
        if (declaration == null) {
            return ResultBean.getFailed();
        }
        String code = declaration.getBillTypeCode();
        //通过编码获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
        DeclarationBean declarationBean = mappingService.map(declaration, DeclarationBean.class);
        declarationBean.setBillType(billType);
        return ResultBean.getSucceed().setD(declarationBean);
    }

    //  生成银行报批专项分期情况说明
    @Override
    public ResultBean<DeclarationBean> actUpdateDeclarationStageApplication(String id) {
        Declaration declaration = iDeclarationService.getOne(id);
        TemplateObjectBean t = iTemplateBizService.actGetTemplateByCode("customersummary.ftl").getD();
        Map paramMap = templateService.getMetaDatas(declaration.getCustomerTransactionId(), t.getMetaDatas());
        BigDecimal sum = new BigDecimal(0.0);
        PaymentToIncome paymentToIncome = declaration.getPaymentToIncome();
        List<IncomeAccount> accountList = paymentToIncome.getAccountList();
        List<IncomeAccount> accountList2 = paymentToIncome.getAccountList2();
        for (IncomeAccount incomeAccount : accountList) {
            if (incomeAccount.getIncome() != null) {
                sum = sum.add(new BigDecimal(incomeAccount.getIncome()));
            }
        }
        for (IncomeAccount incomeAccount : accountList2) {
            if (incomeAccount.getIncome() != null) {
                sum = sum.add(new BigDecimal(incomeAccount.getIncome()));
            }
        }
        Integer monthCount = accountList.size() + accountList2.size();
        paramMap.put("totalIncome", sum.doubleValue());
        paramMap.put("perIncome", monthCount > 0 ? sum.doubleValue() / monthCount : 0);
        ResultBean<String> r = iTemplateBizService.actCreateStrByTemplate(t.getId(), declaration.getCustomerTransactionId(), paramMap);
        if (r.isSucceed()) {
            declaration.setStageApplication(r.getD());
        }
        declaration = iDeclarationService.save(declaration);
        return ResultBean.getSucceed().setD(mappingService.map(declaration, DeclarationBean.class));
    }

    /**
     * 发送银行报批
     *
     * @param id
     * @return
     */
    @Override
    public ResultBean<DeclarationBean> actSubmitDeclaration(String id, String loginUserId) {
        Declaration declaration = iDeclarationService.getAvailableOne(id);
        String transactionId = declaration.getCustomerTransactionId();
        DeclarationHistorys declarationHistorys = iDeclarationHistorysService.findOneByCustomerTransactionId(transactionId);
        DeclarationRecord declarationRecord = new DeclarationRecord();
        if (declarationHistorys == null){
            declarationHistorys = new DeclarationHistorys();
            declarationHistorys.setCustomerId(declaration.getCustomerId());
            declarationHistorys.setCustomerTransactionId(transactionId);
        }
        Map<String, Object> map = new HashMap<>();
        BigDecimal sum = new BigDecimal(0.0);
        PaymentToIncome paymentToIncome = declaration.getPaymentToIncome();
        List<IncomeAccount> accountList = paymentToIncome.getAccountList();
        List<IncomeAccount> accountList2 = paymentToIncome.getAccountList2();
        for (IncomeAccount incomeAccount : accountList) {
            if (incomeAccount.getIncome() != null){
                sum = sum.add(new BigDecimal(incomeAccount.getIncome()));
            }
        }
        for (IncomeAccount incomeAccount : accountList2) {
            if (incomeAccount.getIncome() != null){
                sum = sum.add(new BigDecimal(incomeAccount.getIncome()));
            }
        }
        Integer monthCount = accountList.size() + accountList2.size();
        map.put("totalIncome", sum.doubleValue());
        map.put("stageApplication", declaration.getStageApplication());
        map.put("perIncome", monthCount > 0 ? sum.doubleValue() / monthCount : 0);
        try {
            // 发送地址
            List<String> list = new ArrayList<>();
            //从交易中查询，保单行ID
            CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(declaration.getCustomerTransactionId()).getD();
            if (customerTransaction ==null){
                return ResultBean.getFailed().setM(String.format(messageService.getMessage("TRANSACTION_NOTFOUN_ID"), declaration.getCustomerTransactionId()));
            }
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerTransaction.getCustomerId()).getD();
            String cashSourceId = customerTransaction.getCashSourceId();
            if (cashSourceId == null) {
                logger.error(String.format(messageService.getMessage("MSG_CUSTOMERTRANSACTION_CASHSOUCEID_NULL"), declaration.getCustomerTransactionId()));
            }
            List<CashSourceEmployeeBean> cashSourceEmployees = iCashSourceBizService.actGetCashSourceEmployees(cashSourceId).getD();
            if (cashSourceEmployees != null && cashSourceEmployees.size() > 0) {
                for (CashSourceEmployeeBean cashSourceEmployeeBean : cashSourceEmployees) {
                    if (DataStatus.SAVE == cashSourceEmployeeBean.getDataStatus()) {
                        list.add(cashSourceEmployeeBean.getId());
                    }
                }
            } else {
                logger.error(String.format(messageService.getMessage("MSG_DECLARATION_SUBMIT_CASHSOURCEEMPLOYEE_NULL"), declaration.getId()));
            }
            //获取银行报批要发送的合同或资料(青云文件IDS)
            List<String> fileIds = new ArrayList<String>();
            List<String> documentIds = declaration.getDocumentIds();
            for (String documentId :documentIds){
                CustomerContractBean contractBean = iCustomerContractBizService.actGetTransactionContract(customerTransaction.getCustomerId(), customerTransaction.getId(), documentId).getD();
                if (contractBean == null){
                    contractBean = new CustomerContractBean();
                    contractBean.setCustomerId(customerTransaction.getCustomerId());
                    contractBean.setCustomerTransactionId(customerTransaction.getId());
                    contractBean.setDocumentId(documentId);
                }
                HistoryRecord historyRecord = new HistoryRecord();
                historyRecord.setLoginUserId(loginUserId);
                historyRecord.setTs(DateTimeUtils.getCreateTime());
                contractBean.getDownloadRecords().add(historyRecord);
                contractBean = iCustomerContractBizService.actCreateCustomerContract(true, contractBean).getD();
                if (contractBean != null){
                    fileIds.add(contractBean.getFileId());
                } else {
                    logger.error(String.format(messageService.getMessage("MSG_DOCUMENT_BULL_TYPECODE_CUSTOMER"),documentId,customerBean.getName()));
                }
            }

            List<ImageTypeFileBean> customerImageFileBeans = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(customerTransaction.getCustomerId(),customerTransaction.getId(), declaration.getImageTypeCodes()).getD();
            for(ImageTypeFileBean imageTypeFileBean: customerImageFileBeans) {
                List<String> suffixes = imageTypeFileBean.getSuffixes();
                if (suffixes != null && suffixes.contains("pdf")){
                    if (imageTypeFileBean.getFileIds().size() > 0){
                        String fileId = imageTypeFileBean.getFileIds().get(0);
                        String fileName = imageTypeFileBean.getName();
                        fileIds.add(fileId);
                        FileBean file = iFileBizService.actGetFile(fileId).getD();
                        if (file != null) {
                            file.setFileName(fileName + ".pdf");
                            iFileBizService.actSaveFile(file);
                        }
                    }
                } else if (suffixes == null || (suffixes != null && suffixes.size() == 0)) {
                    return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CUSTOMERIMAGETYPE_CONFIG_SUFFIX"),imageTypeFileBean.getName()));
                }
            }

            Map<String, List<String>> sendMap = new HashMap<>();
            sendMap.put("bd_cashsourceemployee", list);
            MsgRecordBean msgRecordBean = new MsgRecordBean("NC_A015_Declaration_Submit", transactionId, map,fileIds, sendMap);
            msgRecordBean.setSenderId(loginUserId);
            Map result = iAmqpBizService.actSendMq("NC_A015_Declaration_Submit", null, msgRecordBean).getD();
            if (result.get("message") instanceof MsgRecordBean) {
                declarationRecord.setLoginUserId(loginUserId);
                declarationRecord.setEmailTime(DateTimeUtils.getCreateTime());
                declarationRecord.setResultId(((MsgRecordBean) result.get("message")).getId());
            }
            declarationHistorys.getHistoryRecords().add(declarationRecord);
            iDeclarationHistorysService.save(declarationHistorys);
            declaration.setTs(DateTimeUtils.getCreateTime());
            declaration.setStatus(DeclarationBean.STATUS_DECLARATION_GOING);
            declaration = iDeclarationService.save(declaration);

            return ResultBean.getSucceed().setD(mappingService.map(declaration, DeclarationBean.class));
        } catch (Exception e) {
            logger.error("发送银行报批邮件错误",e);
            throw e;
        }
    }

    //  保存银行报批反馈
    @Override
    public ResultBean<DeclarationBean> actSaveDeclarationFeedBack(String id, DeclarationResult declarationResult) {

        Declaration declaration = iDeclarationService.getOne(id);
        if (declaration != null) {

            CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(declaration.getCustomerTransactionId()).getD();
            EmployeeBean employee = iOrgBizService.actGetEmployee(customerTransaction.getEmployeeId()).getD();
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(declaration.getCustomerTransactionId());
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            DeclarationHistorys declarationHistorys = iDeclarationHistorysService.findOneByCustomerTransactionId(declaration.getCustomerTransactionId());
            // 将报批反馈历史保存到最新的发送报批历史记录中去
            if (declarationHistorys != null && declarationHistorys.getHistoryRecords() != null  && declarationHistorys.getHistoryRecords().size() > 0){
                //  获取最新的报批记录
                DeclarationRecord declarationRecord = declarationHistorys.getHistoryRecords().get(declarationHistorys.getHistoryRecords().size() - 1);
                declarationResult.setTs(DateTimeUtils.getCreateTime());
                declarationRecord.setDeclarationResult(declarationResult);//保存报批反馈结果
                List<DeclarationRecord> historyRecords = declarationHistorys.getHistoryRecords();
                historyRecords.set(declarationHistorys.getHistoryRecords().size() - 1,declarationRecord);
                iDeclarationHistorysService.save(declarationHistorys);
            }

            //  保存报批反馈的结果
            if (declarationResult.getResult() == DeclarationResult.RESULT_PASS) {//通过
                declaration.setStatus(DeclarationBean.STATUS_HAVE_DECLARATUION_SUCCESS);
            } else if (declarationResult.getResult() == DeclarationResult.RESULT_REAPPLY) {//驳回
                declaration.setStatus(DeclarationBean.STATUS_HAVE_DECLARATUION_ADJUST);
            } else if (declarationResult.getResult() == DeclarationResult.RESULT_REJECT) {//拒绝
                declaration.setStatus(DeclarationBean.STATUS_HAVE_DECLARATUION_REJECT);
            }
            List<String> receivers = new ArrayList<String>();
            receivers.add(employee.getId());

            declaration = iDeclarationService.save(declaration);

            // 批付额度 小于 贷款金额,通知分期经理
            Map<String, List<String>> sendMap = new HashMap<>();
            sendMap.put("bd_employee", receivers);
            MsgRecordBean msgRecordBean = new MsgRecordBean("NC_A015_Declaration_FeedBack", declaration.getCustomerTransactionId(), new HashMap(), null , sendMap);
            Map result = iAmqpBizService.actSendMq("NC_A015_Declaration_FeedBack", new Object[]{declaration.getCustomerTransactionId()}, msgRecordBean).getD();

            return ResultBean.getSucceed().setD(mappingService.map(declaration, DeclarationBean.class));
        }
        logger.error(messageService.getMessage("MSG_DECLARATION_NULL"));
        return ResultBean.getFailed();
    }

    /**
     * 补充报批资料，用于Web页面，银行报批数据保存
     *
     * @param declarationBean
     * @return
     */
    @Override
    public ResultBean<DeclarationBean> actCompleteDeclaration(DeclarationBean declarationBean) {
        Declaration declaration = mappingService.map(declarationBean, Declaration.class);
        if (declaration.getId() == null) {
            return ResultBean.getFailed();
        }
        declaration = iDeclarationService.save(declaration);
        return ResultBean.getSucceed().setD(mappingService.map(declaration, DeclarationBean.class));
    }

    /**
     * 资质审查提交之后，创建报批数据
     * @param customerDemandId;
     */
    @Override
    public void actCreateDeclaration(String customerDemandId) {
        CustomerDemand customerDemand = iCustomerDemandService.getOne(customerDemandId);
        String transactionId = customerDemand.getCustomerTransactionId();
        Declaration declaration = iDeclarationService.findByCustomerTransactionId(transactionId);
        if (declaration == null) {
            declaration = new Declaration();
        }
        declaration.setBusinessTypeCode(customerDemand.getBusinessTypeCode());
        declaration.setCustomerId(customerDemand.getCustomerId());
        declaration.setCustomerTransactionId(customerDemand.getCustomerTransactionId());
        declaration.setLoginUserId(customerDemand.getLoginUserId());
        declaration.setEmployeeId(customerDemand.getEmployeeId());
        declaration.setCarDealerId(customerDemand.getCarDealerId());
        declaration.setOrginfoId(customerDemand.getOrginfoId());
        declaration.setCashSourceId(customerDemand.getCashSourceId());
        declaration.setDocumentIds(this.saveDocumentIds(transactionId));
        iDeclarationService.save(declaration);
    }

    //  签约数据创建之后，更新报批数据
    public void actUpdateDeclaration(String orderId) {
        PurchaseCarOrder purchaseCarOrder = iOrderService.getAvailableOne(orderId);
        String transactionId = purchaseCarOrder.getCustomerTransactionId();
        Declaration declaration = iDeclarationService.findByCustomerTransactionId(transactionId);
        if (declaration != null) {
            declaration.setLoginUserId(purchaseCarOrder.getLoginUserId());
            declaration.setEmployeeId(purchaseCarOrder.getEmployeeId());
            declaration.setCarDealerId(purchaseCarOrder.getCarDealerId());
            declaration.setOrginfoId(orderId);
            declaration.setDocumentIds(this.saveDocumentIds(transactionId));
            iDeclarationService.save(declaration);
        }
    }

    //  保存银行报批要生成的文档IDs
    public List<String> saveDocumentIds(String customerTransactionId) {
        List<String> documentIds = new ArrayList<String>();
        //      所有的客户都有信用卡分期业务审核表\信用卡分期业务审核材料清单
        List<String> documentCodes = (List<String>) iParamBizService.actGetList("DECLARATION_DOCUMENT_CODES").getD();
        if (documentCodes != null) {
            for (String code : documentCodes){
                DocumentTypeBean documentTypeBean = iTemplateBizService.actGetDocumentTypeByCode(code).getD();
                if (documentTypeBean != null) {
                    documentIds.add(documentTypeBean.getId());
                } else {
                    logger.error(String.format(messageService.getMessage("MSG_DOCUMENT_NOTFIND_NAME"), "银行报批信用卡分期业务审核表或信用卡分期业务审核材料清单"));
                }
            }
        } else {
            logger.error(String.format(messageService.getMessage("MSG_SYSPARAM_NOTFIND_CODE"), "DECLARATION_DOCUMENT_CODES"));
        }
        //      判断该笔交易，客户是否分配调查问卷
        ResultBean<Map<String,String>> stringResultBean = iCustomerSurveyTemplateBizService.actGetCustomerSurveyResultByTransactionId(customerTransactionId);
        if (stringResultBean.isSucceed()){
            String documentCode = iParamBizService.actGetString("DECLARATION_SURVEY_CONTRACT_CODE").getD();
            if (documentCode != null) {
                DocumentTypeBean documentTypeBean = iTemplateBizService.actGetDocumentTypeByCode(documentCode).getD();
                if (documentTypeBean != null) {
                    documentIds.add(documentTypeBean.getId());
                } else {
                    logger.error(String.format(messageService.getMessage("MSG_DOCUMENT_NOTFIND_NAME"), "银行报批问卷调查"));
                }
            } else {
                logger.error(String.format(messageService.getMessage("MSG_SYSPARAM_NOTFIND_CODE"), "DECLARATION_SURVEY_CONTRACT_CODE"));
            }
        }
        return documentIds;
    }

    //  报批反馈通过，发送档案资料--批贷函
    @Override
    public void actSendCreditAmountDocument(String transactionId) {
        Declaration declaration = iDeclarationService.findAvailableOneByCustomerTransactionId(transactionId);
        if (declaration == null){
            logger.error(String.format(messageService.getMessage("MSG_DECLARATION_NOTFIND_CUSTOMERTRANSACTIONID"), transactionId));
            return;
        }
        DeclarationHistorys declarationHistorys = iDeclarationHistorysService.findOneByCustomerTransactionId(declaration.getCustomerTransactionId());
        if (declarationHistorys == null && declarationHistorys.getHistoryRecords().size() == 0){
            logger.error(String.format(messageService.getMessage("MSG_DECLARATION_NOTFIND_HISTORYS"), declaration.getCustomerTransactionId()));
            return;
        }
        if (declarationHistorys.getHistoryRecords().get(declarationHistorys.getHistoryRecords().size()-1).getDeclarationResult().getResult() != DeclarationResult.RESULT_PASS){
            logger.error(String.format(messageService.getMessage("MSG_DECLARATION_FEEDBACK_NOTPASS"), declaration.getId()));
            return;
        }
        CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(transactionId);
        String paramName = null;
        //   判断业务是否抵贷不一
        if (!StringUtils.isEmpty(customerDemand.getPledgeCustomerId()) && !customerDemand.getCreditMasterId().equals(customerDemand.getPledgeCustomerId())) {  //抵贷不一
            paramName = "PURCHASECARORDER_DOCUMENT_CODE_OTHER";
        } else {  //抵贷一致
            paramName = "PURCHASECARORDER_DOCUMENT_CODE_SELF";
        }
        String documentCode = iParamBizService.actGetString(paramName).getD();
        if (!StringUtils.isEmpty(documentCode)) {
            List<String> contractCodes = new ArrayList<>();
            contractCodes.add(documentCode);
            iDeclarationService.sendImagesAndContractsToEmployee(declaration, new ArrayList<String>(), contractCodes);
        } else {
            logger.error(String.format(messageService.getMessage("MSG_SYSPARAM_NOTFIND_CODE"), "PURCHASECARORDER_DOCUMENT_CODE"));
        }
    }

    @Override
    public ResultBean<DeclarationHistorysBean> actGetDeclarationHistorys(String transactionId) {
        DeclarationHistorys declarationHistorys = iDeclarationHistorysService.findOneByCustomerTransactionId(transactionId);
        if (declarationHistorys == null){
            return ResultBean.getFailed();
        }
        return ResultBean.getSucceed().setD(mappingService.map(declarationHistorys, DeclarationHistorysBean.class));
    }

    @Override
    public ResultBean<DeclarationBean> actSaveDeclarationInfo(DeclarationBean declaration) {
        Declaration declaration1 = iDeclarationService.save(mappingService.map(declaration, Declaration.class));
        return ResultBean.getSucceed().setD(mappingService.map(declaration1,DeclarationBean.class));
    }
}