package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICustomerImageTypeBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.creditcar.bean.EnhancementBean;
import com.fuze.bcp.api.creditcar.bean.enhancement.EnhancementListBean;
import com.fuze.bcp.api.creditcar.bean.enhancement.EnhancementSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.creditcar.service.IEnhancementBizService;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.TransactionSummaryBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.Enhancement;
import com.fuze.bcp.creditcar.service.IEnhancementService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.Collections3;
import com.fuze.bcp.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 资料补全
 * Created by GQR on 2017/8/18.
 */
@Service
public class BizEnhancementService implements IEnhancementBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizEnhancementService.class);

    @Autowired
    MappingService mappingService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IEnhancementService iEnhancementService;

    @Autowired
    private IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    ICustomerImageTypeBizService iCustomerImageTypeBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    @Override
    public ResultBean<EnhancementBean> actCreateEnhancement(EnhancementBean enhancementBean) {

        CustomerTransactionBean transaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(enhancementBean.getCustomerTransactionId()).getD();
        if (transaction == null) return ResultBean.getFailed().setM("当前业务不可用。");
        Enhancement enhancement = mappingService.map(enhancementBean, Enhancement.class);
        enhancement.setBusinessTypeCode(transaction.getBusinessTypeCode());
        enhancement.setCustomerId(transaction.getCustomerId());
        enhancement.setCarDealerId(transaction.getCarDealerId());
        enhancement.setLoginUserId(transaction.getLoginUserId());
        enhancement.setEmployeeId(transaction.getEmployeeId());
        enhancement.setOrginfoId(transaction.getOrginfoId());
        enhancement = iEnhancementService.save(enhancement);
        enhancement = this.startEnhancement(enhancement).getD();
        if (enhancement == null){
            logger.error(String.format(messageService.getMessage("MSG_ENHANCEMENT_CREATE_ERROR"),enhancementBean.getCustomerTransactionId()));
            iEnhancementService.deleteOneByCustomerTransactionId(enhancementBean.getCustomerTransactionId());
            return ResultBean.getFailed();
        }
        enhancement.setApproveStatus(ApproveStatus.APPROVE_INIT);
        iEnhancementService.save(enhancement);
        return ResultBean.getSucceed().setD(mappingService.map(enhancement,EnhancementBean.class));
    }

    public ResultBean<EnhancementSubmissionBean> actSaveEnhancement(EnhancementSubmissionBean submission) {
        Enhancement enhancement = iEnhancementService.getAvailableOne(submission.getId());
        if (enhancement.getApproveStatus() == ApproveStatus.APPROVE_ONGOING) // 审批中不可编辑
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERDEMAND_NOEDIT"));
        //处理档案资料
        List<ImageTypeFileBean> images = submission.getCustomerImages();
        images = dealWithEnhancementImageFiles(enhancement,images);
        List<CustomerImageFileBean> customerImages = iCustomerImageFileBizService.actSaveCustomerImages(images).getD();
        List<String> customerImageIds = new ArrayList<String>();
        for (CustomerImageFileBean imageFile: customerImages) {
            customerImageIds.add(imageFile.getId());
        }
        enhancement.setCustomerImageIds(customerImageIds);
        enhancement = iEnhancementService.save(enhancement);
        return ResultBean.getSucceed().setD(mappingService.map(enhancement, EnhancementSubmissionBean.class));
    }

    public List<ImageTypeFileBean> dealWithEnhancementImageFiles(Enhancement enhancement, List<ImageTypeFileBean> images){
        //  该客户该笔交易已经上传的档案资料
        List<CustomerImageFileBean>  existenceCustomerImageFiles = iCustomerImageFileBizService.actGetTransactionImages(enhancement.getCustomerId(),enhancement.getCustomerTransactionId(),enhancement.getCustomerImageTypeCodes()).getD();
        for (ImageTypeFileBean imageTypeFileBean : images){
            for (CustomerImageFileBean customerImageFileBean : existenceCustomerImageFiles){
                if (imageTypeFileBean.getCode().equals(customerImageFileBean.getCustomerImageTypeCode())){
                    //方法功能：删除分期经理在PAD端删除的旧的档案资料【将本次资料补全上传的档案资料的文件ID和系统中本就存在的档案资料的文件ID求交集，作为系统中本就存在的档案资料的文件ID】
                    customerImageFileBean.setFileIds(Collections3.intersection(imageTypeFileBean.getFileIds(),customerImageFileBean.getFileIds()));
                    imageTypeFileBean.getFileIds().removeAll(customerImageFileBean.getFileIds());
                    iCustomerImageFileBizService.actSaveCustomerImage(customerImageFileBean);
                }
            }
        }
        return images;
    }

    @Override
    public ResultBean<EnhancementBean> actSubmitEnhancement(String id, String comment) {
        Enhancement enhancement = iEnhancementService.getAvailableOne(id);
        if (enhancement.getApproveStatus() == ApproveStatus.APPROVE_ONGOING) // 审批中不可编辑
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_SIGN_NOSUBMIT"));
        SignInfo signInfo = new SignInfo(enhancement.getLoginUserId(), enhancement.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, comment);
        try {
            iWorkflowBizService.actSignBill(id, signInfo);
        } catch (Exception e) {
            logger.error("提交资料补全出错。", e);
        }
//        //启动工作流
//        enhancement = this.startEnhancement(enhancement, comment).getD();
        // TODO: 2018/1/10 用户提交
        return ResultBean.getSucceed().setD(mappingService.map(enhancement, EnhancementBean.class)).setM(messageService.getMessage("MSG_ENHANCEMENT_SUBMIT"));
    }

    @Override
    public ResultBean<EnhancementBean> actSignEnhancement(String id, SignInfo signInfo) {
        //提交审核任务
        try {
            ResultBean<WorkFlowBillBean> resultBean = iWorkflowBizService.actSignBill(id, signInfo);
            if(resultBean.failed()){
                return ResultBean.getFailed().setM(resultBean.getM());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAILED_SIGN"));
        }
        Enhancement enhancement = iEnhancementService.getOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(enhancement,EnhancementBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
    }

    private ResultBean<Enhancement> startEnhancement(Enhancement enhancement) {
        EmployeeBean employeeBean = iOrgBizService.actFindEmployeeByLoginUserId(enhancement.getUserId()).getD();
        String employeeId = null;
        if (employeeBean != null) {
            employeeId = employeeBean.getId();
        }
        SignInfo signInfo = new SignInfo(enhancement.getUserId(), employeeId, SignInfo.SIGN_PASS, SignInfo.FLAG_COMMENT, enhancement.getComment());
        signInfo.setFromSalesman(false);
        //进行审批
        String collectionMame = null;
        try {
            collectionMame = Enhancement.getMongoCollection(enhancement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResultBean resultBean = iWorkflowBizService.actSubmit(enhancement.getBusinessTypeCode(), enhancement.getId(), enhancement.getBillTypeCode(), signInfo, collectionMame, null, enhancement.getCustomerTransactionId());
        if (resultBean != null) {
            if(resultBean.isSucceed()){
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    enhancement = iEnhancementService.getOne(enhancement.getId());
                }else{
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            }else if(resultBean.failed()){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(enhancement);
    }

    @Override
    public ResultBean<EnhancementBean> actGetEnhancement(String id) {
        Enhancement enhancement = iEnhancementService.getOne(id);
        if ( enhancement != null){
            String code = enhancement.getBillTypeCode();
            //通过编码获取单据类型
            BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
            EnhancementBean enhancementBean = mappingService.map(enhancement, EnhancementBean.class);
            enhancementBean.setBillType(billType);
            return ResultBean.getSucceed().setD(enhancementBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<EnhancementBean> actSearchEnhancements(String userId, SearchBean searchBean) {
        Page<Enhancement> enhancements = iEnhancementService.findAllBySearchBean(Enhancement.class,searchBean,SearchBean.STAGE_TRANSACTION, userId);
        return ResultBean.getSucceed().setD(mappingService.map(enhancements, EnhancementBean.class));
    }

    public ResultBean<List<EnhancementListBean>> actGetEnhancements(Boolean isPass, String loginUserId, Integer currentPage, Integer currentSize, String customerTransactionId) {
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_ENHANCEMENT_LOGINUSERID_ID_NULL"), loginUserId));
        }

        Page<Enhancement> enhancementPage = null;
        List<Integer> as = new ArrayList<Integer>();
        if(isPass){
            as.add(ApproveStatus.APPROVE_PASSED);
            enhancementPage = iEnhancementService.findByLoginUserIdAndApproveStatusIn(loginUserId, as, currentPage, currentSize,customerTransactionId);
        } else {
            as.add(ApproveStatus.APPROVE_INIT);
            as.add(ApproveStatus.APPROVE_ONGOING);
            as.add(ApproveStatus.APPROVE_REAPPLY);
            enhancementPage = iEnhancementService.findByLoginUserIdAndApproveStatusIn( loginUserId, as, currentPage, currentSize,customerTransactionId);
        }

        EnhancementListBean enhancementListBean = null;
        DataPageBean<EnhancementListBean> destination = new DataPageBean<EnhancementListBean>();
        destination.setPageSize(enhancementPage.getSize());
        destination.setTotalCount(enhancementPage.getTotalElements());
        destination.setTotalPages(enhancementPage.getTotalPages());
        destination.setCurrentPage(enhancementPage.getNumber());
        for (Enhancement enhancement: enhancementPage.getContent()) {
            if(enhancement != null){
                enhancementListBean = mappingService.map(enhancement, EnhancementListBean.class);
                //获取交易概览
                TransactionSummaryBean transactionSummary = iCarTransactionBizService.actGetTransactionSummary(enhancement.getCustomerTransactionId()).getD();
                transactionSummary.setApproveStatus(enhancement.getApproveStatus());
                enhancementListBean.setTransactionSummary(transactionSummary);
            }

            destination.getResult().add(enhancementListBean);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<EnhancementBean> actGetByCustomerTransactionId(String customerTransactionId) {
        Enhancement enhancement = iEnhancementService.findByCustomerTransactionId(customerTransactionId);
        if ( enhancement != null ){
            return ResultBean.getSucceed().setD(mappingService.map(enhancement,EnhancementBean.class));
        }
        return ResultBean.getFailed();
    }

    public ResultBean<EnhancementSubmissionBean> actGetSubmissionById(String id) {
        Enhancement enhancement = iEnhancementService.getOne(id);
        if (enhancement != null) {
             EnhancementSubmissionBean enhancementSubmissionBean = mappingService.map(enhancement, EnhancementSubmissionBean.class);

            //档案类型
            List<CustomerImageFileBean> customerImages = new ArrayList<CustomerImageFileBean>();
            if (enhancement.getCustomerImageIds() != null){
                //  本次资料补全上传的档案资料回显
                customerImages = iCustomerImageFileBizService.actFindCustomerImagesByIds(enhancement.getCustomerImageIds()).getD();
            }

            //  将要补全的档案资料的原本就已经存在的档案资料给回显出来
            List<CustomerImageFileBean> existenceCustomerImageFiles = iCustomerImageFileBizService.actGetTransactionImages(enhancement.getCustomerId(),enhancement.getCustomerTransactionId(),enhancement.getCustomerImageTypeCodes()).getD();
            List<ImageTypeFileBean> imageTypeFiles = new ArrayList<ImageTypeFileBean>();
            for (String imageTypeCode : enhancement.getCustomerImageTypeCodes()) {
                CustomerImageTypeBean imageType = iBaseDataBizService.actGetCustomerImageType(imageTypeCode).getD();
                ImageTypeFileBean imageTypeFile = mappingService.map(imageType, ImageTypeFileBean.class);
                imageTypeFile.setId(null);
                for (CustomerImageFileBean customerImage : customerImages) {
                    if (imageTypeCode.equals(customerImage.getCustomerImageTypeCode())) {
                        imageTypeFile.setId(customerImage.getId());
                        imageTypeFile.setFileIds(customerImage.getFileIds());
                        break;
                    }
                }
                for (CustomerImageFileBean customerImage : existenceCustomerImageFiles) {
                    if (imageTypeCode.equals(customerImage.getCustomerImageTypeCode())) {
                        imageTypeFile.getFileIds().addAll(customerImage.getFileIds());
                        if (enhancement.getAllowDeleteCustomerImage() == 0){
                            imageTypeFile.setCanNotDeleteFileIds(customerImage.getFileIds());
                        }
                        break;
                    }
                }
                imageTypeFiles.add(imageTypeFile);
            }

            enhancementSubmissionBean.setCustomerImages(imageTypeFiles);

            return ResultBean.getSucceed().setD(enhancementSubmissionBean);
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_ENHANCEMENT_NOT_FIND"));
    }

    @Override
    public ResultBean<List<CustomerImageFileBean>> actGetEnhancementImages(String id) {
        Enhancement enhancement = iEnhancementService.getOne(id);
        List<CustomerImageFileBean> imageTypeFiles = new ArrayList<CustomerImageFileBean>();
        List<CustomerImageFileBean> enhancementImages = iCustomerImageFileBizService.actFindCustomerImagesByIds(enhancement.getCustomerImageIds()).getD();
        List<CustomerImageFileBean> existenceCustomerImageFiles = iCustomerImageFileBizService.actGetTransactionImages(enhancement.getCustomerId(), enhancement.getCustomerTransactionId(), enhancement.getCustomerImageTypeCodes()).getD();
        for (String imageTypeCode : enhancement.getCustomerImageTypeCodes()) {
            CustomerImageFileBean customerImageFile = new CustomerImageFileBean();
            customerImageFile.setCustomerId(enhancement.getCustomerId());
            customerImageFile.setCustomerTransactionId(enhancement.getCustomerTransactionId());
            customerImageFile.setCustomerImageTypeCode(imageTypeCode);
            //  如果资料补全，没有通过，就展示本次资料补全上传的档案资料
            if (enhancement.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
                for (CustomerImageFileBean customerImageFileBean : enhancementImages){
                    if (imageTypeCode.equals(customerImageFileBean.getCustomerImageTypeCode())){
                        customerImageFile.setFileIds(customerImageFileBean.getFileIds());
                    }
                }
            }
            for (CustomerImageFileBean customerImageFileBean : existenceCustomerImageFiles){
                if (imageTypeCode.equals(customerImageFileBean.getCustomerImageTypeCode())){
                    customerImageFile.getFileIds().addAll(customerImageFileBean.getFileIds());
                }
            }
            imageTypeFiles.add(customerImageFile);
        }
        return ResultBean.getSucceed().setD(mappingService.map(imageTypeFiles, CustomerImageFileBean.class));
    }

    @Override
    public void actFinnishEnhancement(String id){
        Enhancement enhancement = iEnhancementService.getOne(id);
        //资料补全审批通过，同步档案资料
        if (enhancement.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
            //  获取本次资料补全上传的档案资料
            List<CustomerImageFileBean> customerImages = iCustomerImageFileBizService.actFindCustomerImagesByIds(enhancement.getCustomerImageIds()).getD();
            for (CustomerImageFileBean customerImageFileBean : customerImages ){
                CustomerImageFileBean customerImageFile = iCustomerImageFileBizService.actGetCustomerImageFile(enhancement.getCustomerId(), enhancement.getCustomerTransactionId(), customerImageFileBean.getCustomerImageTypeCode()).getD();
                if (customerImageFile != null){
                    customerImageFile.getFileIds().addAll(customerImageFileBean.getFileIds());
                    iCustomerImageFileBizService.actSaveCustomerImage(customerImageFile);
                } else {
                    customerImageFileBean.setCustomerId(enhancement.getCustomerId());
                    customerImageFileBean.setCustomerTransactionId(enhancement.getCustomerTransactionId());
                    iCustomerImageFileBizService.actSaveCustomerImage(customerImageFileBean);
                }
            }
            enhancement.setCustomerImageIds(new ArrayList<>());
            iEnhancementService.save(enhancement);
            return;
        }
        logger.error(String.format(messageService.getMessage("MSG_ENHANCEMENT_NOT_PASSED"), id));
    }
}
