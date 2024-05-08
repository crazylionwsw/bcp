package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICustomerImageTypeBizService;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.CreditCustomerBean;
import com.fuze.bcp.api.creditcar.bean.CreditPhotographBean;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.creditcar.service.ICreditPhotographBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.utils.FileBeanUtils;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.CreditPhotograph;
import com.fuze.bcp.creditcar.domain.CreditReportQuery;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.creditcar.service.ICreditPhotographService;
import com.fuze.bcp.creditcar.service.ICreditReportQueryService;
import com.fuze.bcp.creditcar.service.ICustomerDemandService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.NumberHelper;
import com.fuze.bcp.utils.QingStorUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lily on 2017/7/18.
 */
@Service
public class BizCreditPhotographService implements ICreditPhotographBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCreditPhotographService.class);

    @Autowired
    ICreditPhotographService iCreditPhotographService;

    @Autowired
    ICreditReportQueryService iCreditReportQueryService;

    @Autowired
    ICustomerDemandService iCustomerDemandService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    ICustomerImageTypeBizService iCustomerImageTypeBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    IFileBizService iFileBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    MessageService messageService;

    /**
     * 未上传征信拍照列表
     *
     * @param bankid
     * @return
     */
    @Override
    public ResultBean<List<CreditPhotographBean>> actFindAllUnFinished(String bankid) {
        List<CreditPhotograph> creditPhotographs = iCreditPhotographService.findAllUnFinished(bankid);
        if (creditPhotographs != null) {
            List<CreditPhotographBean> creditPhotographBeens = mappingService.map(creditPhotographs, CreditPhotographBean.class);
            creditPhotographBeens = this.attachCreditCustomer(creditPhotographs, creditPhotographBeens);
            return ResultBean.getSucceed().setD(creditPhotographBeens);
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }

    }

    private List<CreditPhotographBean> attachCreditCustomer(List<CreditPhotograph> creditPhotographs, List<CreditPhotographBean> creditPhotographBeens) {
        for (int i = 0; i < creditPhotographs.size(); i++) {
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(creditPhotographBeens.get(i).getCustomerId()).getD();
            if (customerBean != null) {
                CreditCustomerBean creditCustomerBean = new CreditCustomerBean();
                creditCustomerBean.setId(customerBean.getId());
                creditCustomerBean.setName(customerBean.getName());
                creditCustomerBean.setIdentifyNo(customerBean.getIdentifyNo());
                creditPhotographBeens.get(i).setCreditCustomerBean(creditCustomerBean);
            } else {
                logger.error("Can't find customer [" + creditPhotographBeens.get(i).getCustomerId() + "]");
            }
        }

        return creditPhotographBeens;
    }

    /**
     * 已上传征信拍照列表
     *
     * @param bankid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ResultBean<DataPageBean<CreditPhotographBean>> actFindAllFinished(String bankid, Integer pageIndex, Integer pageSize) {
        Page<CreditPhotograph> creditPhotograph = iCreditPhotographService.findAllFinished(bankid, pageIndex, pageSize);
        List<CreditPhotograph> list = creditPhotograph.getContent();
        List<CreditPhotographBean> creditPhotographBeens = mappingService.map(list, CreditPhotographBean.class);
        DataPageBean<CreditPhotographBean> destination = new DataPageBean<CreditPhotographBean>();
        destination.setPageSize(creditPhotograph.getSize());
        destination.setTotalCount(creditPhotograph.getTotalElements());
        destination.setTotalPages(creditPhotograph.getTotalPages());
        destination.setCurrentPage(creditPhotograph.getNumber());
        destination.setResult(this.attachCreditCustomer(list, creditPhotographBeens));
        return ResultBean.getSucceed().setD(destination);
    }

    /**
     * 确认图片是否上传成功
     *
     * @param customerId
     * @return
     */
    @Override
    public ResultBean<CreditPhotographBean> actFinishOneCustomer(String customerId) {
        CreditPhotograph creditPhotograph = iCreditPhotographService.findByCustomerId(customerId);
        if (creditPhotograph == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_NONENTITY_CREDITPHOTOGRAPH"));
        } else {
            if (creditPhotograph.getImageFileIds().size() > 0) {
                creditPhotograph.setUploadFinish(true);
                creditPhotograph.setSubmitTime(DateTimeUtils.getCreateTime());
                CreditPhotograph photograph = iCreditPhotographService.save(creditPhotograph);
                CreditPhotographBean creditPhotographBean = this.actMqCreateSavePdf(photograph.getId()).getD();
                /*try {
                    if (photograph != null) {
                        MsgRecordBean msgRecordBean = new MsgRecordBean("NC_A016_Creditreport_Upload", null, null, null, null);
                        iAmqpBizService.actSendMq("NC_A016_Creditreport_Upload", new Object[]{photograph.getId()}, msgRecordBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                return ResultBean.getSucceed().setD(creditPhotographBean);
            }
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_UPLOADIMG"));
    }

    @Override
    public ResultBean<CreditPhotographBean> actSave(CreditPhotographBean creditPhotographBean) {
        CreditPhotograph creditPhotograph = mappingService.map(creditPhotographBean, CreditPhotograph.class);
        creditPhotograph = iCreditPhotographService.save(creditPhotograph);
        return ResultBean.getSucceed().setD(mappingService.map(creditPhotograph, CreditPhotographBean.class)).setM(messageService.getMessage("MSG_SUCESS_SAVE"));
    }

    @Override
    public ResultBean<CreditPhotographBean> actFindCreditPhotographs(Integer currentPage, Boolean uploadFinish) {
        Page<CreditPhotograph> creditPhotographs = iCreditPhotographService.findAllByUploadFinishOrderByTsDesc(uploadFinish, currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(creditPhotographs, CreditPhotographBean.class));
    }

    @Override
    public ResultBean<CreditPhotographBean> actFindCreditPhotographById(String id) {
        CreditPhotograph creditPhotograph = iCreditPhotographService.getOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(creditPhotograph, CreditPhotographBean.class));
    }

    @Override
    public ResultBean<CreditPhotographBean> actFindAllByuploadFinishAndCustomerIds(Integer currentPage, Boolean uploadFinish, List<String> customerIds) {
        Page<CreditPhotograph> creditPhotographs = iCreditPhotographService.findAllByUploadFinishAndCustomerIdIn(uploadFinish, customerIds, currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(creditPhotographs, CreditPhotographBean.class));
    }

    @Override
    public ResultBean<CreditPhotographBean> actFindCreditPhotographByCustomerId(String customerId) {
        CreditPhotograph creditPhotograph = iCreditPhotographService.findByCustomerId(customerId);
        if (creditPhotograph != null) {
            return ResultBean.getSucceed().setD(mappingService.map(creditPhotograph, CreditPhotographBean.class));
        }
        return ResultBean.getFailed().setD(String.format(messageService.getMessage("MSG_CREDITPHOTOGRAPH_CUSTOMERIDNULL"), customerId));
    }

    @Override
    public ResultBean<CreditPhotographBean> actCreateSavePdf(CreditPhotographBean creditPhotographBean) {
        CreditPhotograph creditPhotograph = mappingService.map(creditPhotographBean, CreditPhotograph.class);
        //保存上传的图片内容
        creditPhotograph = iCreditPhotographService.save(creditPhotograph);
        /*try {
            MsgRecordBean msgRecordBean = new MsgRecordBean("NC_A016_Creditreport_Upload", null, null, null, null);
            iAmqpBizService.actSendMq("NC_A016_Creditreport_Upload", new Object[]{creditPhotographBean.getId()}, msgRecordBean);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //发送MQ消息
        return ResultBean.getSucceed().setD(creditPhotograph).setM(messageService.getMessage("MSG_CREDITPHOTOGRAPH_ONGING"));
    }

    /**
     * 后台手动生成征信报告
     * @param creditPhotographBean
     * @return
     */
    public ResultBean<CreditPhotographBean> actCreateCreditReportManually(CreditPhotographBean creditPhotographBean) {
        CreditPhotograph creditPhotograph = mappingService.map(creditPhotographBean, CreditPhotograph.class);
        //保存上传的图片内容
        creditPhotograph = iCreditPhotographService.save(creditPhotograph);

        creditPhotographBean = this.actMqCreateSavePdf(creditPhotograph.getId()).getD();

        return ResultBean.getSucceed().setD(creditPhotographBean);
    }

    public ResultBean<CreditPhotographBean> actMqCreateSavePdf(String id) {
        CreditPhotograph creditPhotograph = iCreditPhotographService.getOne(id);
        CreditPhotograph credit = null;
        if (creditPhotograph.getImageFileIds().size() > 0) {
            credit = this.createPdf(creditPhotograph).getD();
            if (credit != null && credit.getComment() != null) {
                if (credit.getComment().equals("true")) {
                    //删除已经生成的图片资料
                    List<String> fileIds = credit.getImageFileIds();
                    iFileBizService.actDeleteFileByIds(fileIds);

                    credit.clearImageFiles();
                    credit = iCreditPhotographService.save(credit);
                }
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(credit, CreditPhotographBean.class)).setM(messageService.getMessage("MSG_SUCCESS_CREATEPDF"));
    }

    /**
     * 解析客户需求，创建征信拍照
     *
     * @param customerDemandId
     * @return
     */
    public void actCreateCreditPhotograph(String customerDemandId) {
        CustomerDemand customerDemand = iCustomerDemandService.getOne(customerDemandId);
        if (customerDemand == null) {
            logger.error(messageService.getMessage("MSG_FAIL_NUll"));
        }

        //贷款主体征信拍照
        CreditPhotograph customerCreditPhoto = this.createCreditPhotograph(customerDemand.getCustomerId(),
                customerDemand.getCustomerTransactionId(),
                customerDemand.getCashSourceId());
        if (customerCreditPhoto != null && customerCreditPhoto.getPadFileId() != null) {
            //附加征信报告
            this.attachCreditReport(customerDemand, "B013", customerCreditPhoto.getPadFileId());
        }


        if (customerDemand.getMateCustomerId() != null) { //配偶征信拍照
            CreditPhotograph mateCreditPhoto = this.createCreditPhotograph(customerDemand.getMateCustomerId(),
                    customerDemand.getCustomerTransactionId(),
                    customerDemand.getCashSourceId());
            if (mateCreditPhoto != null && mateCreditPhoto.getPadFileId() != null) {
                //附加征信报告
                this.attachCreditReport(customerDemand, "B014", mateCreditPhoto.getPadFileId());
            }
        }

        //除本人和配偶外抵押人的征信报告
        if((!customerDemand.getRelation() .equals("0") && !customerDemand.getRelation() .equals("2"))||(customerDemand.getRelation() .equals("2")) && customerDemand.getMateCustomerId() == null){
            CreditPhotograph mateCreditPhoto = this.createCreditPhotograph(customerDemand.getPledgeCustomerId(),
                    customerDemand.getCustomerTransactionId(),
                    customerDemand.getCashSourceId());
            if (mateCreditPhoto != null && mateCreditPhoto.getPadFileId() != null) {
                //附加征信报告
                this.attachCreditReport(customerDemand, "B035", mateCreditPhoto.getPadFileId());
            }
        }
    }

    private void attachCreditReport(CustomerDemand customerDemand, String imageTypeCode, String fileId) {

        List<String> imageTypeCodes = new ArrayList<String>();
        imageTypeCodes.add(imageTypeCode);

        List<CustomerImageFileBean> beans = iCustomerImageFileBizService.actGetTransactionImages(customerDemand.getCustomerId(), customerDemand.getCustomerTransactionId(), imageTypeCodes).getD();
        //保存征信报告的PDF文件
        CustomerImageFileBean customerImage = new CustomerImageFileBean();
        if(beans !=null && beans.size()==1){
            customerImage = beans.get(0);
        }
        if(!StringUtils.isEmpty(fileId)){
            List<String>    fileIds = new ArrayList<String>();
            fileIds.add(fileId);
            customerImage.setFileIds(fileIds);
        }
        customerImage.setCustomerId(customerDemand.getCustomerId());
        customerImage.setCustomerTransactionId(customerDemand.getCustomerTransactionId());
        customerImage.setCustomerImageTypeCode(imageTypeCode);
        customerImage.setLoginUserId(customerDemand.getLoginUserId());
        customerImage.setDataStatus(DataStatus.SAVE);

        iCustomerImageFileBizService.actSaveCustomerImage(mappingService.map(customerImage, CustomerImageFileBean.class)).getD();
    }

    /**
     * 创建征信拍照
     *
     * @param customerId
     * @param transactionId
     * @param cashSourceId
     * @return
     */
    private CreditPhotograph createCreditPhotograph(String customerId, String transactionId, String cashSourceId) {

        CreditPhotograph creditPhotograph = iCreditPhotographService.findByCustomerId(customerId);

        if (creditPhotograph != null) {
            if (creditPhotograph.getSubmitTime() != null) { //征信报告已提交
                try {
                    //征信报告可以使用30天，30天后需要重新查征信
                    if (DateTimeUtils.daysBetweenToday(creditPhotograph.getSubmitTime()) < 30) {
                        logger.info(messageService.getMessage("MSG_CREDITPHOTOGRAPH_NOOVERDUE"));

                        return creditPhotograph;// ResultBean.getSucceed().setD(mappingService.map(creditPhotograph, CreditPhotographBean.class));
                    } else {

                        creditPhotograph.setSubmitTime(null);
                        //creditPhotograph.setCustomerTransactionId(transactionId);
                        creditPhotograph.setUploadFinish(false);
                        creditPhotograph.setCashSourceId(cashSourceId);
                        creditPhotograph.setStatus(CreditPhotographBean.UPLOAD_INIT);
                        iCreditPhotographService.save(creditPhotograph);

                        return creditPhotograph;// ResultBean.getSucceed().setD(mappingService.map(creditPhotograph, CreditPhotographBean.class))
                    }
                } catch (ParseException e) {
                    logger.error("", e);
                    return null;
                }
            } else {
                return creditPhotograph;//ResultBean.getSucceed().setD(mappingService.map(creditPhotograph, CreditPhotographBean.class));
            }
        }

        creditPhotograph = new CreditPhotograph();
        creditPhotograph.setCustomerId(customerId);
        //creditPhotograph.setCustomerTransactionId(transactionId);
        creditPhotograph.setUploadFinish(false);
        creditPhotograph.setCashSourceId(cashSourceId);
        creditPhotograph.setStatus(CreditPhotographBean.UPLOAD_INIT);

        //创建征信拍照
        creditPhotograph = iCreditPhotographService.save(creditPhotograph);
        logger.info(messageService.getMessage("MSG_CREDITPHOTOGRAPH_SAVE"));
        return creditPhotograph;
    }


    /**
     * 根据上传的文件开始生成PDF文件
     *
     * @return
     */
    public ResultBean<CreditPhotograph> createPdf(final CreditPhotograph creditPhotograph) {

        String pdfFileId = null;
        List<CustomerDemand> demands = iCustomerDemandService.findAllByCustomerId(creditPhotograph.getCustomerId());
        if (demands != null) {
            for (CustomerDemand demand: demands) {
                if (demand.getApproveStatus() != ApproveStatus.APPROVE_REJECT && demand.getApproveStatus() != ApproveStatus.APPROVE_PASSED) {
                    pdfFileId = createPdfFile(creditPhotograph.getImageFileIds(), creditPhotograph.getCustomerId());
                    if (pdfFileId == null) {
                        return ResultBean.getFailed().setM(messageService.getMessage("MSG_CREDITPHOTOGRAPH_ERROR"));
                    }
                    this.attachCreditReport(demand, "B013", pdfFileId);
                }
            }
        }

        demands = iCustomerDemandService.findAllByMateCustomerId(creditPhotograph.getCustomerId());
        if (demands != null) {
            for (CustomerDemand demand: demands) {
                if (demand.getApproveStatus() != ApproveStatus.APPROVE_REJECT && demand.getApproveStatus() != ApproveStatus.APPROVE_PASSED) {
                    pdfFileId = createPdfFile(creditPhotograph.getImageFileIds(), creditPhotograph.getCustomerId());
                    if (pdfFileId == null) {
                        return ResultBean.getFailed().setM(messageService.getMessage("MSG_CREDITPHOTOGRAPH_ERROR"));
                    }
                    this.attachCreditReport(demand, "B014", pdfFileId);
                }
            }
        }

        demands = iCustomerDemandService.findAllBypledgeCustomerId(creditPhotograph.getCustomerId());
        if (demands != null ) {
            for (CustomerDemand demand: demands) {
                if((!demand.getRelation() .equals("0") && !demand.getRelation() .equals("2"))||((demand.getRelation() .equals("2")) && demand.getMateCustomerId() == null)){
                    if (demand.getApproveStatus() != ApproveStatus.APPROVE_REJECT && demand.getApproveStatus() != ApproveStatus.APPROVE_PASSED) {
                        pdfFileId = createPdfFile(creditPhotograph.getImageFileIds(), creditPhotograph.getCustomerId());
                        if (pdfFileId == null) {
                            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CREDITPHOTOGRAPH_ERROR"));
                        }
                        this.attachCreditReport(demand, "B035", pdfFileId);
                    }
                }

            }
        }

        //crq.getCustomerImageIds().add(customerImage.getId());
        //iCreditReportQueryService.save(crq);
        if(pdfFileId != null){
            creditPhotograph.setPadFileId(pdfFileId);
        }
        creditPhotograph.setUploadFinish(true);
        creditPhotograph.setStatus(CreditPhotographBean.UPLOAD_SUCCEED);
        CreditPhotograph upload = iCreditPhotographService.save(creditPhotograph);

        return ResultBean.getSucceed().setD(upload);
    }

    /**
     * 根据客户的ID，查询客户对应的征信查询，如果是配偶，则需要根据资质审核的对象查询客户ＩＤ
     *
     * @param customerId
     * @return
     */
    private CreditReportQuery getCreditReportQueryByCustomerId(String customerId) {
        CreditReportQuery crq = iCreditReportQueryService.findByCustomerId(customerId);
        if (crq == null) {
            //可能是配偶的客户ID
            CustomerDemand carDemand = iCustomerDemandService.findCarDemandByMateCustomerId(customerId);
            if (carDemand == null) return null;
            crq = iCreditReportQueryService.findByCustomerId(carDemand.getCustomerId());
        }
        return crq;
    }

    /**
     * 在MongoDB中生成PDF文件
     *
     * @param fileIds
     * @param customerId
     * @return
     */
    private String createPdfFile(List<String> fileIds, String customerId) {
        String pdfFileId = null;
        try {
            String path = System.getProperty("java.io.tmpdir") + customerId + File.pathSeparator;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = path + "creditreport.pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            float width = PageSize.A4.getWidth() - PageSize.A4.getBorderWidthLeft() - PageSize.A4.getBorderWidthRight();
            float height = (PageSize.A4.getHeight() - PageSize.A4.getBorderWidthBottom() - PageSize.A4.getBorderWidthTop()) / 2;

            for (int i = 0; i < fileIds.size(); i++) {
                String fileId = fileIds.get(i);
                document.newPage();
                addImage2Pdf(document, width, height, fileId);
                //添加第二张图片
                i++;
                if (i < fileIds.size()) {
                    fileId = fileIds.get(i);
                    addImage2Pdf(document, width, height, fileId);
                }
            }

            document.close();
            File pdf = new File(fileName);
            Long fileLength = pdf.length();
            FileBean fileBean = new FileBean();
            fileBean.setContentLength(fileLength);
            fileBean.setFileName(NumberHelper.getRundomCode() + ".pdf");
            fileBean.setBucketName(QingStorUtils.DEFAULT_BUCKET);
            fileBean.setZoneName(QingStorUtils.DEFAULT_ZOO);
            fileBean.setObjectname(NumberHelper.getRundomCode() + ".pdf");
            fileBean.setUrl("http://" + fileBean.getBucketName() + "." + fileBean.getZoneName() + ".qingstor.com/" + fileBean.getObjectname());
            pdfFileId = iFileBizService.actPackageSaveFile(fileBean, pdf).getD().getId();
            //dfFileId = iImageFileService.saveFile(new FileInputStream(fileName), "creditreport.pdf").getD();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(messageService.getMessage("MSG_CREDITPHOTOGRAPH_PDFERROR"), ex);
        }
        return pdfFileId;
    }

    private void addImage2Pdf(Document document, float width, float height, String fileId) throws Exception {
        InputStream inputStream = null;
        try {
            FileBean fileBean = iFileBizService.actGetFile(fileId).getD();
            inputStream = FileBeanUtils.downloadFile(fileBean);
            if (fileBean != null) {
                Image image = Image.getInstance(file2Byte(inputStream));
                image.scaleToFit(width, height);
                image.setAlignment(Image.ALIGN_MIDDLE);
                document.add(image);
            } else {
                logger.error(String.format(messageService.getMessage("MSG_CREDITPHOTOGRAPH_FILENULL"), fileId));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(messageService.getMessage("MSG_CREDITPHOTOGRAPH_PDFERROR"), ex);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 根据客户的ID判断客户是配偶的征信报告档案类型
     *
     * @param customerImageTypeCode
     * @return
     */
    private CustomerImageTypeBean getCustomerImageTypeByCustomerID(String customerImageTypeCode) {
        return iCustomerImageTypeBizService.actFindCustomerImageTypeByCode(customerImageTypeCode).getD();
    }


    /**
     * 将输入流转换为字节数组
     *
     * @param inputStream
     * @return
     */
    public static byte[] file2Byte(InputStream inputStream) {
        byte[] buffer = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            inputStream.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


}
