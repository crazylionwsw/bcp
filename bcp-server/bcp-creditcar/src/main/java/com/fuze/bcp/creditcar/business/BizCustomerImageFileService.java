package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.api.bd.bean.HistoryRecord;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICustomerImageTypeBizService;
import com.fuze.bcp.api.creditcar.bean.CustomerContractBean;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.creditcar.service.ICustomerContractBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.file.bean.DocumentTypeBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ImageTypeFileBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.CustomerImageFile;
import com.fuze.bcp.creditcar.service.ICustomerImageFileService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/7/19.
 */
@Service
public class BizCustomerImageFileService implements ICustomerImageFileBizService {

    @Value("${fuze.bcp.web.server.url}")
    private String webUrl;

    private static final Logger logger = LoggerFactory.getLogger(BizCustomerImageFileService.class);

    @Autowired
    private ICustomerImageFileService iCustomerImageFileService;

    @Autowired
    private ICustomerImageTypeBizService iCustomerImageTypeBizService;

    @Autowired
    private IBaseDataBizService iBaseDataBizService;

    @Autowired
    private MappingService mappingService;

    @Autowired
    private IFileBizService iFileBizService;

    @Autowired
    private IParamBizService iParamBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    private ICustomerContractBizService iCustomerContractBizService;

    @Autowired
    ITemplateBizService iTemplateBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;


    @Override
    public ResultBean<CustomerImageFileBean> actSaveCustomerImage(CustomerImageFileBean customerImageFileBean) {
        CustomerImageFile customerImageFile = mappingService.map(customerImageFileBean, CustomerImageFile.class);
        customerImageFile = iCustomerImageFileService.save(customerImageFile);
        return ResultBean.getSucceed().setD(mappingService.map(customerImageFile, CustomerImageFileBean.class));
    }

    @Override
    public ResultBean<List<CustomerImageFileBean>> actSaveCustomerImages(String customerId, String transactionId, List<ImageTypeFileBean> customerImageFileBeans) {

        List<CustomerImageFile> customerImages = new ArrayList<CustomerImageFile>();
        for (ImageTypeFileBean imageTypeFile : customerImageFileBeans) {
            CustomerImageFile customerImage = iCustomerImageFileService.getCustomerImageFile(customerId,transactionId,imageTypeFile.getCode());
            if (customerImage == null){
                customerImage = new CustomerImageFile();
                customerImage.setCustomerId(customerId);
                customerImage.setCustomerTransactionId(transactionId);
                customerImage.setCustomerImageTypeCode(imageTypeFile.getCode());
            }
            customerImage.setFileIds(imageTypeFile.getFileIds());
            customerImage.setDataStatus(DataStatus.SAVE);
            customerImage.setTs(DateTimeUtils.getCreateTime());
            customerImages.add(customerImage);
        }

        List<CustomerImageFile> customerImageFiles = iCustomerImageFileService.saveCustomerImages(customerId, transactionId, customerImages);

        return ResultBean.getSucceed().setD(mappingService.map(customerImageFiles, CustomerImageFileBean.class));
    }

    @Override
    public ResultBean<List<CustomerImageFileBean>> actSaveCustomerImages(List<ImageTypeFileBean> customerImageFileBeans) {
        List<CustomerImageFile> customerImages = new ArrayList<CustomerImageFile>();
        for (ImageTypeFileBean imageTypeFile : customerImageFileBeans) {
            CustomerImageFile customerImage = new CustomerImageFile();
            customerImage.setCustomerImageTypeCode(imageTypeFile.getCode());
            customerImage.setFileIds(imageTypeFile.getFileIds());
            customerImage.setDataStatus(DataStatus.SAVE);
            customerImage.setId(imageTypeFile.getId());
            customerImages.add(customerImage);
        }

        List<CustomerImageFile> customerImageFiles = iCustomerImageFileService.saveCustomerImages(customerImages);

        return ResultBean.getSucceed().setD(mappingService.map(customerImageFiles, CustomerImageFileBean.class));
    }

    @Override
    public ResultBean actDeleteCustomerImage(String id) {
        CustomerImageFile customerImageFile = iCustomerImageFileService.getOne(id);
        if (customerImageFile != null) {
            //先删除远端存储图片
            for (String fileId : customerImageFile.getFileIds()) {
                if (fileId != null) iFileBizService.actDeleteFileById(fileId);
            }
            //再删除本地关系
            iCustomerImageFileService.delete(id);
        }
        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<CustomerImageFileBean> actFindCustomerImageById(String id) {
        CustomerImageFile customrImage = iCustomerImageFileService.getOne(id);
        if(customrImage != null){
            return ResultBean.getSucceed().setD(mappingService.map(customrImage,CustomerImageFileBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actCopyCustomerImages(String customerId, List<String> imageTypeIds, String fromTransId, String toTransId) {

        //首先删除旧的档案资料
        List<CustomerImageFile> customerImageFiles2 = iCustomerImageFileService.getCustomerTransactionImages(customerId, toTransId, imageTypeIds);
        List<String> customerImageFileIds = new ArrayList<String>();
        for (CustomerImageFile customerImage : customerImageFiles2) {
            customerImageFileIds.add(customerImage.getId());
        }
        iCustomerImageFileService.deleteRealByIds(customerImageFileIds);

        //开始复制档案资料
        List<CustomerImageFile> customerImageFiles = iCustomerImageFileService.getCustomerTransactionImages(customerId, fromTransId, imageTypeIds);
        List<CustomerImageFile> copiedCustomerImageFiles = new ArrayList<CustomerImageFile>();
        for (CustomerImageFile customerImage : customerImageFiles) {
            customerImage.setCustomerTransactionId(toTransId);
            customerImage.setId(null);
            copiedCustomerImageFiles.add(customerImage);
        }

        if (copiedCustomerImageFiles.size() > 0)
            iCustomerImageFileService.save(copiedCustomerImageFiles);

        return ResultBean.getSucceed();
    }

    public ResultBean actCopyCustomerImage(String customerId, String imageTypeCode, String fromTransId, String toTransId) {
        List<String> imageTypeCodes = new ArrayList<String>();
        imageTypeCodes.add(imageTypeCode);

        //首先删除旧的档案资料
        List<CustomerImageFile> customerImageFiles2 = iCustomerImageFileService.getCustomerTransactionImages(customerId, toTransId, imageTypeCodes);
        List<String> customerImageFileIds = new ArrayList<String>();
        for (CustomerImageFile customerImage : customerImageFiles2) {
            customerImageFileIds.add(customerImage.getId());
        }
        iCustomerImageFileService.deleteRealByIds(customerImageFileIds);



        return this.actCopyCustomerImages(customerId, imageTypeCodes, fromTransId, toTransId);
    }

    @Override
    public ResultBean<List<CustomerImageFileBean>> actFindByCustomerIdAndCustomerImageType(String customerId, String customerImageTypeCode) {
        List<CustomerImageFile> customerImageFiles = iCustomerImageFileService.findByCustomerIdAndCustomerImageType(customerId, customerImageTypeCode);
        return ResultBean.getSucceed().setD(mappingService.map(customerImageFiles, CustomerImageFileBean.class));
    }

    @Override
    public ResultBean<CustomerImageFileBean> actFindByCustomerIdAndCustomerImageTypeAndCustomerTransactionId(String customerId,String transactionId,String customerImageTypeCode) {
        CustomerImageFile customerImageFile = iCustomerImageFileService.getCustomerImageFile(customerId, transactionId, customerImageTypeCode);
        return ResultBean.getSucceed().setD(mappingService.map(customerImageFile,CustomerImageFileBean.class));
    }

    @Override
    public ResultBean<List<CustomerImageFileBean>> actGetTransactionImages(String customerId, String customerTransactionId) {
        List<CustomerImageFile> imageFiles = iCustomerImageFileService.getCustomerTransactionImages(customerId, customerTransactionId);
        if (imageFiles == null)
            imageFiles = new ArrayList<CustomerImageFile>();

        return ResultBean.getSucceed().setD(mappingService.map(imageFiles, CustomerImageFileBean.class));
    }

    @Override
    public ResultBean<List<CustomerImageFileBean>> actGetTransactionImages(String customerId, String customerTransactionId, List<String> imageTypeCodeList) {
        List<CustomerImageFile> imageFiles = iCustomerImageFileService.getCustomerTransactionImages(customerId, customerTransactionId, imageTypeCodeList);
        if (imageFiles == null)
            imageFiles = new ArrayList<CustomerImageFile>();
        return ResultBean.getSucceed().setD(mappingService.map(imageFiles, CustomerImageFileBean.class));
    }

    @Override
    public ResultBean<List<CustomerImageFileBean>> actGetImageByIdAndByImageTypeCodeList(String id, List<String> imageTypeCodeList) {
        List<CustomerImageFile> imageFiles = iCustomerImageFileService.getImages(id,imageTypeCodeList);
        if(imageFiles == null)
            imageFiles = new ArrayList<CustomerImageFile>();
        return ResultBean.getSucceed().setD(mappingService.map(imageFiles,CustomerImageFileBean.class));
    }

    @Override
    public ResultBean<List<ImageTypeFileBean>> actGetBillImageTypesWithFiles(String customerId, String customerTransactionId, List<String> imageTypeCodeList) {
        List<CustomerImageFile> customerImages = iCustomerImageFileService.getCustomerTransactionImages(customerId, customerTransactionId, imageTypeCodeList);
        List<ImageTypeFileBean> imageTypeFiles = new ArrayList<ImageTypeFileBean>();
        for (String imageTypeCode : imageTypeCodeList) {
            CustomerImageTypeBean imageType = iBaseDataBizService.actGetCustomerImageType(imageTypeCode).getD();
            if(imageType == null){
                continue;
            }
            ImageTypeFileBean imageTypeFile = mappingService.map(imageType, ImageTypeFileBean.class);
            for (CustomerImageFile customerImage : customerImages) {
                if (imageTypeCode.equals(customerImage.getCustomerImageTypeCode())) {
                    imageTypeFile.setFileIds(customerImage.getFileIds());
                    break;
                }
            }
            imageTypeFiles.add(imageTypeFile);
        }
        return ResultBean.getSucceed().setD(imageTypeFiles);
    }

    /**
     * 从系统参数获取档案类型
     * @param customerId
     * @param customerTransactionId
     * @param bizCode
     * @param billTypeCode
     * @return
     */
    @Override
    public ResultBean<List<ImageTypeFileBean>> actGetBillImageTypesWithFiles(String customerId, String customerTransactionId, String bizCode, String billTypeCode) {
        //读取系统参数
        Map<?, ?> padImages = iParamBizService.actGetMap("PAD_IMAGES_ORDER").getD();
        Map<?, ?> padBizImages = (Map<?, ?>) padImages.get(bizCode);
        List<String> imageTypeCodeList = (List<String>)padBizImages.get(billTypeCode);
        if(imageTypeCodeList == null){
            List<ImageTypeFileBean> imageFiles = new ArrayList<ImageTypeFileBean>();
            return ResultBean.getSucceed().setD(imageFiles);
        }
        return this.actGetBillImageTypesWithFiles(customerId, customerTransactionId, imageTypeCodeList);
    }

    @Override
    public ResultBean<List<ImageTypeFileBean>> actGetBillImageTypesWithBillTypeCode(String customerId, String customerTransactionId, String billTypeCode) {
        BillTypeBean billType = iBaseDataBizService.actGetBillType(billTypeCode).getD();
        //档案类型编码
        if (billType == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BILLTYPE_CODE_NOT_FIND"), billTypeCode));
        }
        return this.actGetBillImageTypesWithFiles(customerId, customerTransactionId, billType.getRequiredImageTypeCodes());
    }

    @Override
    public ResultBean<List<ImageTypeFileBean>> actGetBillImageTypesWithFiles(List<String> customerImageIds) {
        List<CustomerImageFile> customerImages = iCustomerImageFileService.getAvaliableList(customerImageIds);
        List<ImageTypeFileBean> imageTypeFiles = new ArrayList<ImageTypeFileBean>();
        for (CustomerImageFile customerImage : customerImages) {
            CustomerImageTypeBean imageType = iBaseDataBizService.actGetCustomerImageType(customerImage.getCustomerImageTypeCode()).getD();
            ImageTypeFileBean imageTypeFile = mappingService.map(imageType, ImageTypeFileBean.class);
            imageTypeFile.setFileIds(customerImage.getFileIds());
            imageTypeFile.setId(customerImage.getId());
            imageTypeFiles.add(imageTypeFile);
        }
        return ResultBean.getSucceed().setD(imageTypeFiles);
    }

    @Override
    public ResultBean<List<CustomerImageFileBean>> actFindCustomerImagesByIds(List<String> ids) {
        List<CustomerImageFile> customerImageFiles = iCustomerImageFileService.findCustomerImagesByIds(ids);
        List<CustomerImageFileBean> customerImageFileBeans = mappingService.map(customerImageFiles, CustomerImageFileBean.class);
        List<CustomerImageFileBean> returnCIFB = new ArrayList<CustomerImageFileBean>();
        for (CustomerImageFileBean customerImageFile : customerImageFileBeans) {
            if (customerImageFile.getCustomerImageTypeCode() != null) {
                CustomerImageTypeBean customerImageTypeBean = iCustomerImageTypeBizService.actFindCustomerImageTypeByCode(customerImageFile.getCustomerImageTypeCode()).getD();
                if (customerImageTypeBean != null) {
                    customerImageFile.setCustomerImageType(customerImageTypeBean);
                }
            }
            returnCIFB.add(customerImageFile);
        }
        return ResultBean.getSucceed().setD(returnCIFB);
    }

    @Override
    public ResultBean<List<CustomerImageFileBean>> actFindCustomerImageByCustomer(String id) {
        List<CustomerImageFile> customerImages = iCustomerImageFileService.getCustomerImageByCustomer(id);
        if (customerImages == null)
            customerImages = new ArrayList<CustomerImageFile>();
        return ResultBean.getSucceed().setD(mappingService.map(customerImages, CustomerImageFileBean.class));
    }

    @Override
    public void actDeleteCustomerImages(List<String> customerImageIds) {

    }

    @Override
    public void actAssignImagesToTransaction(List<String> customerImageIds, String customerId, String transactionId) {
        List<CustomerImageFile> customerImages = iCustomerImageFileService.getAvaliableList(customerImageIds);
        for (CustomerImageFile customerImage : customerImages) {
            customerImage.setCustomerId(customerId);
            customerImage.setCustomerTransactionId(transactionId);
        }

        iCustomerImageFileService.save(customerImages);
    }

    @Override
    public ResultBean actMergeImages(Boolean force, CustomerImageFileBean ci, String loginUserId){

        String customerId = ci.getCustomerId();
        String customerImageTypeCode = ci.getCustomerImageTypeCode();
        CustomerImageTypeBean customerImageTypeBean = iCustomerImageTypeBizService.actFindCustomerImageTypeByCode(customerImageTypeCode).getD();
        if (customerImageTypeBean == null){
            logger.error(String.format(messageService.getMessage("MSG_CUSTOMERIMAGETYPE_NULL_CODE"),customerImageTypeCode));
            return ResultBean.getFailed();
        }

        //  根据客户ID 交易ID 文档ID    查询客户当前交易的该档案数据是否存在
        if ("".equals(customerImageTypeBean.getMergeTemplateId())){
            logger.error(String.format(messageService.getMessage("MSG_CUSTOMERIMAGETYPE_NOT_CONFIG_MERGETEMPLATEID"),customerImageTypeBean.getName()));
            return ResultBean.getFailed();
        }
        DocumentTypeBean documentTypeBean = iTemplateBizService.actGetDocumentType(customerImageTypeBean.getMergeTemplateId()).getD();
        if (documentTypeBean==null){
            logger.error(String.format(messageService.getMessage("MSG_DOCUMENT_NOTFIND_ID"),customerImageTypeBean.getMergeTemplateId()));
            return ResultBean.getFailed();
        }
        CustomerContractBean customerContractBean = iCustomerContractBizService.actGetTransactionContract(customerId, ci.getCustomerTransactionId(), customerImageTypeBean.getMergeTemplateId()).getD();

        List<HistoryRecord> downloadRecords = new ArrayList<HistoryRecord>();
        if(customerContractBean == null  || force){
            //删除旧的
            if (customerContractBean != null) {
                iFileBizService.actDeleteFileById(customerContractBean.getFileId());
                iCustomerContractBizService.actDeleteCustomerContractById(customerContractBean.getId());
            }

            //生成新的
            Map htmlData = new HashMap<String,Object>();
            String urlPath = String.format("%s/json/file/download/", webUrl);
            htmlData.put("urlPath", urlPath);
            htmlData.put("fileIds",ci.getFileIds());


            //      根据  模板ID    客户交易ID  生成文件
            String fileId = iTemplateBizService.actCreateFileByTemplate(documentTypeBean.getTemplateObjectId(), ci.getCustomerTransactionId(), htmlData, documentTypeBean.getFileType()).getD();
            if (fileId == null) {
                return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_FILE_CREATE_ERROR_CONTRACTNAME"), customerImageTypeBean.getName()));
            }
            customerContractBean = new CustomerContractBean();
            customerContractBean.setCustomerId(ci.getCustomerId());
            customerContractBean.setFileId(fileId);
            customerContractBean.setDataStatus(DataStatus.SAVE);
        }else{
            downloadRecords = customerContractBean.getDownloadRecords();
        }

        HistoryRecord historyRecord = new HistoryRecord();
        historyRecord.setLoginUserId(loginUserId);
        downloadRecords.add(historyRecord);
        customerContractBean = iCustomerContractBizService.actSaveCustomerContract(customerContractBean).getD();

        return ResultBean.getSucceed().setD(customerContractBean);
    }

    @Override
    public ResultBean<CustomerImageFileBean> actGetCustomerImageFile(String customerId, String transactionId, String imgCode) {
        CustomerImageFile customerImageFile = iCustomerImageFileService.getCustomerImageFile(customerId,transactionId,imgCode);
        if(customerImageFile != null){
            return ResultBean.getSucceed().setD(mappingService.map(customerImageFile,CustomerImageFileBean.class));
        }
        return ResultBean.getFailed();
    }

    public ResultBean<DataPageBean<ImageTypeFileBean>> actGetCustomerImagesByTransactionId(String transactionId, Integer pageIndex, Integer pageSize){
        CustomerTransactionBean customerTransactionBean = iCustomerTransactionBizService.actFindCustomerTransactionById(transactionId).getD();
        DataPageBean<CustomerImageTypeBean> imageTypeBeanDataPageBean = iCustomerImageTypeBizService.actGetCustomerImageTypes(customerTransactionBean.getBusinessTypeCode(), pageIndex, pageSize).getD();
        DataPageBean<ImageTypeFileBean> imageTypeFileBeanDataPageBean = new DataPageBean<ImageTypeFileBean>();
        imageTypeFileBeanDataPageBean.setPageSize(imageTypeBeanDataPageBean.getPageSize());
        imageTypeFileBeanDataPageBean.setTotalCount(imageTypeBeanDataPageBean.getTotalCount());
        imageTypeFileBeanDataPageBean.setTotalPages(imageTypeBeanDataPageBean.getTotalPages());
        imageTypeFileBeanDataPageBean.setCurrentPage(imageTypeBeanDataPageBean.getCurrentPage());
        imageTypeFileBeanDataPageBean.setResult(this.mergeImages(imageTypeBeanDataPageBean.getResult(),transactionId));
        return ResultBean.getSucceed().setD(imageTypeFileBeanDataPageBean);
    }

    private List<ImageTypeFileBean> mergeImages(List<CustomerImageTypeBean> customerImageTypeBeans, String transactionId){
        List<ImageTypeFileBean> imageTypeFileBeans = new ArrayList<ImageTypeFileBean>();
        for (CustomerImageTypeBean customerImageTypeBean : customerImageTypeBeans){
            ImageTypeFileBean imageTypeFileBean = mappingService.map(customerImageTypeBean,ImageTypeFileBean.class);
            List<CustomerImageFile> customerImageFiles = iCustomerImageFileService.getCustomerImagesByTransactionIdAndImageTypeCode(transactionId,customerImageTypeBean.getCode());
            for (CustomerImageFile customerImageFile : customerImageFiles){
                if (customerImageFile.getFileIds().size() > 0){
                    imageTypeFileBean.setFileIds(customerImageFile.getFileIds());
                }
            }
            imageTypeFileBeans.add(imageTypeFileBean);
        }
        return imageTypeFileBeans;
    }

    @Override
    public ResultBean<List<CustomerImageFileBean>> actFindByCustomerIdAndCustomerImageTypesAndCustomerTransactionId(String customerId, String transactionId, List<String> customerImageTypeCodes) {
        List<CustomerImageFile> customerTransactionImages = iCustomerImageFileService.getCustomerTransactionImages(customerId, transactionId, customerImageTypeCodes);
        return ResultBean.getSucceed().setD(mappingService.map(customerTransactionImages,CustomerImageFileBean.class));
    }
}
