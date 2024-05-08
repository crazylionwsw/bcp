package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.HistoryRecord;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICashSourceBizService;
import com.fuze.bcp.api.bd.service.ICustomerImageTypeBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.creditcar.bean.CustomerContractBean;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.service.ICustomerContractBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.creditcar.service.IOrderBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.file.bean.DocumentTypeBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.CustomerContract;
import com.fuze.bcp.creditcar.service.ICustomerContractService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 客户合同信息处理类
 * Created by zqw on 2017/8/10.
 */
@Service
public class BizCustomerContractService implements ICustomerContractBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCustomerContractService.class);

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    private IOrgBizService iOrgBizService;

    @Autowired
    private IFileBizService iFileBizService;

    @Autowired
    private IBaseDataBizService iBaseDataBizService;

    @Autowired
    private IParamBizService iParamBizService;

    @Autowired
    ICustomerContractService iCustomerContractService;

    @Autowired
    ITemplateBizService iTemplateBizService;

    @Autowired
    private ICashSourceBizService iCashSourceBizService;

    @Autowired
    private ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    private ICustomerImageTypeBizService iCustomerImageTypeBizService;

    @Autowired
    private ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    IOrderBizService iOrderBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    MongoTemplate mongoTemplate;


    @Override
    public ResultBean<CustomerContractBean> actSaveCustomerContract(CustomerContractBean customerContractBean) {
        CustomerContract customerContract = mappingService.map(customerContractBean, CustomerContract.class);
        customerContract = iCustomerContractService.save(customerContract);
        return ResultBean.getSucceed().setD(mappingService.map(customerContract, CustomerContractBean.class));
    }

    @Override
    public ResultBean<List<CustomerContractBean>> actGetTransactionContracts(String customerId, String customerTransactionId) {
        List<CustomerContract> customerContractList = iCustomerContractService.getCustomerTransactionContracts(customerId, customerTransactionId);
        if (customerContractList == null)
            customerContractList = new ArrayList<CustomerContract>();
        return ResultBean.getSucceed().setD(mappingService.map(customerContractList, CustomerContractBean.class));
    }

    @Override
    public ResultBean<CustomerContractBean> actGetTransactionContract(String customerId, String customerTransactionId, String documentId) {
        CustomerContract customerContract = iCustomerContractService.getCustomerTransactionContract(customerId, customerTransactionId, documentId);
        if (customerContract != null){
            return ResultBean.getSucceed().setD(mappingService.map(customerContract, CustomerContractBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<CustomerContractBean>> actGetTransactionContracts(String customerId, String customerTransactionId, List<String> documentIdsList) {
        List<CustomerContract> customerContractList = iCustomerContractService.getCustomerTransactionContracts(customerId, customerTransactionId, documentIdsList);
        if (customerContractList == null)
            customerContractList = new ArrayList<CustomerContract>();
        return ResultBean.getSucceed().setD(mappingService.map(customerContractList, CustomerContractBean.class));
    }

    /**
     * 生成客户合同      todo
     *
     * @param force
     * @param customerContractBean
     * @return
     */
    @Override
    public ResultBean<CustomerContractBean> actCreateCustomerContract(Boolean force, CustomerContractBean customerContractBean) {

        CustomerContract customerContract = mappingService.map(customerContractBean, CustomerContract.class);
        if (customerContract.getId() == null || force) {

            //删除旧的 客户合同
            if (customerContract.getId() != null && customerContract.getFileId() != null) {
                iFileBizService.actDeleteFileById(customerContract.getFileId());
                iCustomerContractService.deleteReal(customerContract.getId());
            }
            Map tempMap = new HashMap<>();

            List<HistoryRecord> downloadRecords = customerContract.getDownloadRecords();
            if (downloadRecords.size() > 0) {
                HistoryRecord historyRecord = downloadRecords.get(downloadRecords.size() - 1);
                if (historyRecord != null) {
                    String loginUserId = historyRecord.getLoginUserId();
                    EmployeeBean employeeBean = iOrgBizService.actFindEmployeeByLoginUserId(loginUserId).getD();
                    if (employeeBean != null) {
                        tempMap.put("operatename", employeeBean.getUsername());
                    } else {
                        tempMap.put("operatename", "");
                    }
                }
            }

            String customerTransactionId = customerContract.getCustomerTransactionId();
            CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(customerTransactionId).getD();
            if (customerTransaction == null) {
                return ResultBean.getFailed().setM(String.format(messageService.getMessage("TRANSACTION_NOTFOUN_ID"), customerTransactionId));
            }
            CashSourceBean cashSourceBean = iCashSourceBizService.actGetCashSource(customerTransaction.getCashSourceId()).getD();
            if (cashSourceBean == null) {
                return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CASHSOURCE_NOTFOUND_ID"), customerTransaction.getCashSourceId()));
            }
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerTransaction.getCustomerId()).getD();
            DocumentTypeBean contractBean = iTemplateBizService.actGetDocumentType(customerContract.getDocumentId()).getD();
            if (contractBean == null) {
                return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CONTRACT_NOTFIND_ID"), customerContract.getDocumentId()));
            }
            if (contractBean.getTemplateObjectId() == null) {
                logger.info(String.format(messageService.getMessage("MSG_CONTRACT_TEMPLATEOBJECTID_NULL"), contractBean.getName()));
            }

            //      生成 合同编号
            Date date = new Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
            String time = f.format(date);
            Integer codeNum = this.getTodayCountByContract(contractBean.getId()).getD();
            codeNum += 1;
            String s = codeNum.toString();
            for (int j = codeNum; j < 4; j++) {
                s = "0" + s;
            }
            String contractNo = String.format("%s-%s-%s", contractBean.getCodeRule(), time, s);

            tempMap.put("contractNo", contractNo);
            customerContract.setContractNo(contractNo);

            //      当前时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            String nowTime = format.format(new Date());
            tempMap.put("date", nowTime);

            //      当前时间
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            String nowTime1 = format1.format(new Date());
            tempMap.put("dateTime", nowTime1);

            //当前时间的年月 专项分期合同封面时间
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM");
            String yearAndMonth = format2.format(new Date());
            ResultBean<PurchaseCarOrderBean> purchaseCarOrderBeanResultBean = iOrderBizService.actGetOrderByTransactionId(customerTransactionId);
            String contractNumber = this.actGetCreatContractNumber(purchaseCarOrderBeanResultBean.getD().getTs());
            StringBuilder ss = new StringBuilder();
            String contractNumberFull  = ss.append(yearAndMonth).append(contractNumber).toString();
            tempMap.put("cNumber",contractNumberFull);
            //  TODO:获取印章
            List<Map<String, String>> bankSealList = (List<Map<String, String>>) iParamBizService.actGetList("BANK_SEAL").getD();
            for (Map<String, String> bankSeal : bankSealList) {
                if (bankSeal.containsKey(cashSourceBean.getCode())) {
                    tempMap.put("bankImageName", bankSeal.get(cashSourceBean.getCode()));
                }
            }

            //      根据  模板ID    客户交易ID  生成文件
            String fileId = null;
            if (StringUtils.isEmpty(contractBean.getTemplateObjectId())){
                fileId = iTemplateBizService.actCreateFileByParam(String.format("客户【%s】的【%s】",customerBean.getName(),contractBean.getName()) ,customerTransactionId, tempMap, contractBean.getFileType()).getD();
            } else {
                fileId = iTemplateBizService.actCreateFileByTemplate(contractBean.getTemplateObjectId(), customerTransactionId, tempMap, contractBean.getFileType()).getD();
            }
            if (fileId == null) {
                return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_FILE_CREATE_ERROR_CONTRACTNAME"), contractBean.getName()));
            }
            ResultBean<DocumentTypeBean> documentTypeBean = iTemplateBizService.actGetDocumentType(customerContractBean.getDocumentId());
            if(documentTypeBean.getD().getImageTypeCodes() != null){
                for (String imgCode : documentTypeBean.getD().getImageTypeCodes()) {
                    CustomerImageFileBean customerImageFile =  iCustomerImageFileBizService.actGetCustomerImageFile(customerTransaction.getCustomerId(),customerTransaction.getId(),imgCode).getD();
                    if (customerImageFile == null ){
                        customerImageFile = new CustomerImageFileBean();
                        customerImageFile.setCustomerId(customerTransaction.getCustomerId());
                        customerImageFile.setCustomerTransactionId(customerTransaction.getId());
                        customerImageFile.setCustomerImageTypeCode(imgCode);
                    }
                    customerImageFile.getFileIds().add(fileId);
                    iCustomerImageFileBizService.actSaveCustomerImage(customerImageFile);
                }
            }
            customerContract.setFileId(fileId);
            customerContract.setLatestDownloadTime(DateTimeUtils.getCreateTime());
            customerContract = iCustomerContractService.save(customerContract);

        } else {
            customerContract.setLatestDownloadTime(DateTimeUtils.getCreateTime());
            customerContract = iCustomerContractService.save(customerContract);
        }
        return ResultBean.getSucceed().setD(mappingService.map(customerContract, CustomerContractBean.class));
    }

    /**
     * 获取  当天  该合同 生成的次数
     *
     * @param documentId
     * @return
     */
    public ResultBean<Integer> getTodayCountByContract(String documentId) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = f.format(date);
        return ResultBean.getSucceed().setD(iCustomerContractService.countByContractAndTsStartingWith(documentId, today));
    }

    @Override
    public ResultBean<CustomerContractBean> actDeleteCustomerContractById(String id){
        CustomerContract customerContract = iCustomerContractService.getOne(id);
        if ( customerContract != null ){
            customerContract = iCustomerContractService.delete(id);
            return ResultBean.getSucceed().setD(customerContract);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actGetMergedCustomerContract(CustomerImageFileBean customerImageFileBean) {
        String customerId = customerImageFileBean.getCustomerId();
        String customerImageTypeCode = customerImageFileBean.getCustomerImageTypeCode();
        CustomerImageTypeBean customerImageTypeBean = iCustomerImageTypeBizService.actFindCustomerImageTypeByCode(customerImageTypeCode).getD();
        if (customerImageTypeBean == null){
            return ResultBean.getFailed().setD(String.format(messageService.getMessage("MSG_CUSTOMERIMAGETYPE_NULL_CODE"),customerImageTypeCode));
        }
        String mergeTemplateId = customerImageTypeBean.getMergeTemplateId();
        String customerTransactionId = customerImageFileBean.getCustomerTransactionId();
        CustomerContract customerContract = iCustomerContractService.getCustomerTransactionContract(customerId, customerTransactionId, mergeTemplateId);
        if (customerContract!=null){
            return ResultBean.getSucceed().setD(mappingService.map(customerContract,CustomerContractBean.class));
        }
        return ResultBean.getFailed();
    }




    public String actGetCreatContractNumber(String ts){
        String firstDay = DateTimeUtils.actGetFirstDate();
        Query query  = new Query();
        query.addCriteria(Criteria.where("approveStatus").is(ApproveStatus.APPROVE_PASSED).and("ts").gt(firstDay).lte(ts));
        Long count = mongoTemplate.count(query,"so_purchasecar");
        String strCount = String.format("%03d", count.intValue());
        return strCount;
    }

}
