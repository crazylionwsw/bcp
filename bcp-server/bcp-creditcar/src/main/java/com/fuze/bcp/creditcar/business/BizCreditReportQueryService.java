package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.creditcar.bean.CreditPhotographBean;
import com.fuze.bcp.api.creditcar.bean.CreditReportQueryBean;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.creditcar.service.ICreditReportQueryBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.CreditPhotograph;
import com.fuze.bcp.creditcar.domain.CreditReportQuery;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.creditcar.service.ICreditPhotographService;
import com.fuze.bcp.creditcar.service.ICreditReportQueryService;
import com.fuze.bcp.creditcar.service.ICustomerDemandService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * 征信查询微服务
 * Created by Lily on 2017/7/19.
 */
@Service
public class BizCreditReportQueryService implements ICreditReportQueryBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCreditReportQueryService.class);

    @Autowired
    MappingService mappingService;

    @Autowired
    ICreditReportQueryService iCreditReportQueryService;

    @Autowired
    ICustomerDemandService iCustomerDemandService;
    @Autowired

    IBaseDataBizService iBaseDataBizService;
    @Autowired

    IAmqpBizService iAmqpBizService;
    @Autowired

    ICustomerBizService iCustomerBizService;
    @Autowired

    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    ICreditPhotographService iCreditPhotographService;


    /**
     * 接收消息，创建征信查询
     * @param id
     * @param content
     * @return
     */
    @Override
    public ResultBean<CreditReportQueryBean> actCreateCreditReportQuery(String id, String content) {
        //通过资质Id查询客户的征信查询是否存在(存在则不创建，不存在则创建)
        CustomerDemand customerDemand = iCustomerDemandService.getOne(id);
        return this.actCreateCreditReportQuery(customerDemand);
    }

    /**
     * 解析客户需求，创建征信查询
     * @param customerDemand
     * @return
     */
    private ResultBean<CreditReportQueryBean> actCreateCreditReportQuery(CustomerDemand customerDemand) {
        //通过客户ID查询征信
        CreditReportQuery creditReportQuery = iCreditReportQueryService.findByCustomerIdOrderByTsDesc(customerDemand.getCustomerId());
        if(creditReportQuery != null){ //如果该客户存在征信查询记录
            //TODO 设置业务类型
            creditReportQuery.setBusinessTypeCode(customerDemand.getBusinessTypeCode());

            BillTypeBean billTypeBean = iBaseDataBizService.actGetBillType(creditReportQuery.getBillTypeCode()).getD();
            //TODO
            if(creditReportQuery.getSubmitTime() != null){ //征信报告已提交
                try{
                    //征信报告可以使用30天，30天后需要重新查征信
                    if(DateTimeUtils.daysBetweenToday(creditReportQuery.getSubmitTime()) < 30){
                        logger.info("征信报告未过期,不用创建征信拍照和征信查询!");

                        //复制档案资料
                        iCustomerImageFileBizService.actCopyCustomerImages(customerDemand.getCustomerId(),
                                billTypeBean.getRequiredImageTypeCodes(),
                                creditReportQuery.getCustomerTransactionId(),
                                customerDemand.getCustomerTransactionId());

                        return ResultBean.getSucceed().setD(iCreditReportQueryService.save(creditReportQuery));
                    }
                }catch (ParseException e){
                    logger.error("", e);
                    return ResultBean.getFailed().setM("");
                }
            } else { //未提交征信报告
                //复制档案资料
                iCustomerImageFileBizService.actCopyCustomerImages(customerDemand.getCustomerId(),
                        billTypeBean.getRequiredImageTypeCodes(),
                        creditReportQuery.getCustomerTransactionId(),
                        customerDemand.getCustomerTransactionId());

                return ResultBean.getSucceed().setD(iCreditReportQueryService.save(creditReportQuery));
            }
        }

        //如果不存在或征信报告大于30天，创建征信查询单据
        CreditReportQuery cre = this.createCreditReport(customerDemand).getD();
        if(cre != null){
            //创建征信拍照
            this.createCreditPhotograph(cre);
            return ResultBean.getSucceed().setM("征信拍照已创建");
        }
        return ResultBean.getSucceed().setM("征信报告已创建");

    }

    /**
     * 创建征信拍照
     * @param creditReportQuery
     * @return
     */

    private ResultBean<CreditPhotographBean> createCreditPhotograph(CreditReportQuery creditReportQuery) {
        CreditPhotograph creditPhotograph = new CreditPhotograph();
        creditPhotograph.setCustomerId(creditReportQuery.getCustomerId());
        creditPhotograph.setUploadFinish(false);
        creditPhotograph.setCashSourceId(creditReportQuery.getCashSourceId());
        creditPhotograph.setStatus(CreditPhotographBean.UPLOAD_INIT);
        creditPhotograph.setCustomerTransactionId(creditReportQuery.getCustomerTransactionId());
        //创建征信拍照
        iCreditPhotographService.save(creditPhotograph);
        System.out.println("征信拍照已创建,操作开始执行");

        //判断资质审核的配偶信息是否存在
        CustomerDemand demand = iCustomerDemandService.findByCustomerId(creditReportQuery.getCustomerId());
        //配偶信息存在则保存配偶信息
        if(demand != null && demand.getMateCustomerId() != null){
            CreditPhotograph mateCreditPhotograph = new CreditPhotograph();
            mateCreditPhotograph.setCustomerId(demand.getMateCustomerId());
            mateCreditPhotograph.setUploadFinish(false);
            mateCreditPhotograph.setCashSourceId(demand.getCashSourceId());
            mateCreditPhotograph.setStatus(CreditPhotographBean.UPLOAD_INIT);
            creditPhotograph.setCustomerTransactionId(creditReportQuery.getCustomerTransactionId());
            //创建征信拍照
            iCreditPhotographService.save(mateCreditPhotograph);
        }
        return ResultBean.getSucceed().setM("征信拍照创建成功");
    }

    /**
     * 创建征信查询
     * @param customerDemand
     * @return
     */
    private ResultBean<CreditReportQuery> createCreditReport(CustomerDemand customerDemand) {
        CreditReportQuery creditReport = new CreditReportQuery();
        creditReport.setDataStatus(DataStatus.SAVE);
        creditReport.setLoginUserId(customerDemand.getLoginUserId());

        //创建征信报告查询单绑定业务人员Id、业务类型、经销商Id
        creditReport.setEmployeeId(customerDemand.getEmployeeId());
        creditReport.setCarDealerId(customerDemand.getCarDealerId());
        creditReport.setBusinessTypeCode(customerDemand.getBusinessTypeCode());

        creditReport.setCustomerId(customerDemand.getCustomerId());
        creditReport.setCustomerTransactionId(customerDemand.getCustomerTransactionId());

        //把客户资质审查信息中的报单银行ID保存进征信查询报告中
        creditReport.setCashSourceId(customerDemand.getCashSourceId());
        CreditReportQuery cre = iCreditReportQueryService.save(creditReport);
        return ResultBean.getSucceed().setD(cre);
    }

    @Override
    public ResultBean<CreditReportQueryBean> actSave(CreditReportQueryBean creditReportQueryBean) {
        CreditReportQuery creditReportQuery = iCreditReportQueryService.save(mappingService.map(creditReportQueryBean, CreditReportQuery.class));
        return ResultBean.getSucceed().setD(mappingService.map(creditReportQuery,CreditReportQueryBean.class));
    }

    @Override
    public ResultBean<CreditReportQueryBean> actFindByCustomerId(String customerId) {
        //通过客户ID获取最近一次征信查询
        CreditReportQuery creditReportQuery = iCreditReportQueryService.findByCustomerIdOrderByTsDesc(customerId);
        if (creditReportQuery == null)
            return ResultBean.getSucceed();

        return ResultBean.getSucceed().setD(mappingService.map(creditReportQuery,CreditReportQueryBean.class));
    }

    @Override
    public ResultBean<CreditReportQueryBean> actParseReportImage(CustomerImageFileBean customerImageFileBean) {
        return null;
    }

    @Override
    public ResultBean<CreditReportQueryBean> actFindCreditReportQueries(int currentPage, Integer approveStatus) {
        if(approveStatus == -1){
            Page<CreditReportQuery> creditReportQueries = iCreditReportQueryService.findAllByOrderByTsDesc(currentPage);
            return ResultBean.getSucceed().setD(mappingService.map(creditReportQueries,CreditReportQueryBean.class));
        }else{
            Page<CreditReportQuery> creditReportQueries = iCreditReportQueryService.findAllByApproveStatusOrderByTsDesc(approveStatus,currentPage);
            return ResultBean.getSucceed().setD(mappingService.map(creditReportQueries,CreditReportQueryBean.class));
        }
    }

    @Override
    public ResultBean<CreditReportQueryBean> actFindCreditReportQueryById(String id) {
        CreditReportQuery creditReportQuery = iCreditReportQueryService.getOne(id);
        String code = creditReportQuery.getBillTypeCode();
        //通过编码获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();

        CreditReportQueryBean creditReportQueryBean = mappingService.map(creditReportQuery, CreditReportQueryBean.class);
        creditReportQueryBean.setBillType(billType);
        return ResultBean.getSucceed().setD(creditReportQueryBean);
    }

    @Override
    public ResultBean<CreditReportQueryBean> actFindCreditReportQueriesByCustomerIds(int currentPage, List<String> ids) {
        return null;
    }

    @Override
    public ResultBean<CreditReportQueryBean> actSign(CreditReportQueryBean creditReportQuery) {
        return null;
    }

    @Override
    public ResultBean deleteCreditReportQuery(String id) {
        return null;
    }
}
