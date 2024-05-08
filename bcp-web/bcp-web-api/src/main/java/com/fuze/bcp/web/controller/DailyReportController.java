package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.*;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistryBean;
import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferBean;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardBean;
import com.fuze.bcp.api.creditcar.service.*;
import com.fuze.bcp.api.customer.bean.BusinessDataBean;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.ResultBean;
//import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by GQR on 2017/10/29.
 */
@RestController
@RequestMapping(value = "/json")
public class DailyReportController {


    private static final Logger logger = LoggerFactory.getLogger(DailyReportController.class);

    @Autowired
    private ICarRegistryBizService iCreditReportService;
    @Autowired
    private ICustomerDemandBizService iCustomerDemandBizService;
    @Autowired
    private IOrderBizService iOrderBizService;
    @Autowired
    private IPickupCarBizService iPickupCarBizService;
    @Autowired
    private IAppointPaymentBizService iAppointPaymentBizService;
    @Autowired
    private ICarRegistryBizService iCarRegistryBizService;
    @Autowired
    private IDmvpledgeBizService iDmvpledgeBizService;
    @Autowired
    private IBankCardApplyBizService iBankCardApplyBizService;
    @Autowired
    private ICarTransferBizService iCarTransferBizService;
    @Autowired
    ISwipingCardBizService iSwipingCardBizService;

    /**
     * 计算每天数量
     * @return
     */
    @RequestMapping(value = "/dailyreport/{billtypecode}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDailyCount(
            @PathVariable("billtypecode")String billTypeCode,
            @RequestParam(value="searchdate",required = false)String searchDate,
            @RequestParam(value="orgid",required = false)String orgid){
        if(StringUtils.isEmpty(orgid) || orgid.equals("-1")) {
            orgid = null;
        }
        if (billTypeCode.equals("A001")) {
            return iCustomerDemandBizService.getDailyReport(orgid,searchDate, new CustomerDemandBean());
        }
        if (billTypeCode.equals("A002")) {
            return iOrderBizService.getDailyReport(orgid,searchDate, new PurchaseCarOrderBean());
        }
        if (billTypeCode.equals("A003")) {
            return iPickupCarBizService.getDailyReport(orgid,searchDate, new PickupCarBean());
        }
        if (billTypeCode.equals("A004")) {
            return iAppointPaymentBizService.getDailyReport(orgid,searchDate, new AppointPaymentBean());
        }
        if (billTypeCode.equals("A005")) {
            return iCarRegistryBizService.getDailyReport(orgid,searchDate, new CarRegistryBean());
        }
        if (billTypeCode.equals("A008")) {
            return iDmvpledgeBizService.getDailyReport(orgid,searchDate, new DMVPledgeBean());
        }
        if (billTypeCode.equals("A011")) {
            return iBankCardApplyBizService.getDailyReport(orgid,searchDate, new BankCardApplyBean());
        }
        if (billTypeCode.equals("A023")) {
            return iCarTransferBizService.getDailyReport(orgid,searchDate, new CarTransferBean());
        }
        if (billTypeCode.equals("A019")) {
            return iSwipingCardBizService.getDailyReport(orgid,searchDate, new SwipingCardBean());
        }
        return ResultBean.getFailed().setM("输入参数无法查询到数据！");
    }
    /**
     * 计算客户信息
     * @return
     */

    @RequestMapping(value = "/dailyreport/{billtypecode}/reapply",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDailyCountReapplyList(@PathVariable("billtypecode") String billTypeCode){
        if(billTypeCode.equals("A001")){
            return iCustomerDemandBizService.getAllCustomerByApproveStatus(ApproveStatus.APPROVE_REAPPLY);
        }
        if(billTypeCode.equals("A002")){
            return iOrderBizService.getAllCustomerByApproveStatus(ApproveStatus.APPROVE_REAPPLY);
        }
        if(billTypeCode.equals("A004")){
            return iAppointPaymentBizService.getAllCustomerByApproveStatus(ApproveStatus.APPROVE_REAPPLY);
        }
        return null;
    }
}

