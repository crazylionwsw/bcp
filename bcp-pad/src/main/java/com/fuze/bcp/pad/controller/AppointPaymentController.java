package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IAppointPaymentBizService;
import com.fuze.bcp.bean.ResultBean;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 预约垫资，垫资
 * Created by user on 2017/7/3.
 */
@RestController
@RequestMapping(value = "/json")
public class AppointPaymentController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    IAppointPaymentBizService iAppointPaymentBizService;

    /**
     * 【PAD-API】 分页获取loginUserId 的预约垫资
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/appointpayments", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAppointPaymentsPage(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "5") Integer pageSize,@RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed) {
        return iAppointPaymentBizService.actGetAppointPayments(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

    /**
     * 【PAD-API】 根据ID获取我的垫资
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/transaction/{transactionid}/appointpayment", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAppointPayment(@PathVariable("transactionid") String transactionId) {
        return iAppointPaymentBizService.actInitAppointPaymentsByTransactionId(transactionId);
    }

    /**
     * 【PAD-API】非贴息的情况下计算垫资额
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/transaction/{transactionid}/appointpayment/amount/{chargeParty}", method = RequestMethod.GET)
    public ResultBean getAppointPaymentAmount(@PathVariable("transactionid") String transactionId, @PathVariable("chargeParty") String chargeParty) {
        return iAppointPaymentBizService.actGetAppointPaymentAmount(transactionId, chargeParty);
    }

    /**
     * 【PAD-API】临时保存预约垫资(整体提交)
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/appointpayment", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveAppointPayment(@RequestBody AppointPaymentSubmissionBean appointPaymentSubmissionBean) {
        logger.error("AppointSwipingCardSubmissionBean PAD create!==============================================================================================");
        appointPaymentSubmissionBean.setLoginUserId(super.getOperatorId());
        return iAppointPaymentBizService.actSavePadAppointPayment(appointPaymentSubmissionBean);
    }

    /**
     * 【PAD-API】 提交保存预约垫资(进工作流)
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/appointpayment/{appointpaymentid}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean submitAppointPayment(@PathVariable("appointpaymentid") String appointPaymentId, @RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        String comment = (String) obj.get("comment");
        return iAppointPaymentBizService.actSubmitAppointPayment(appointPaymentId, comment);
    }


    /***************************************************
     * 临时接口，创建***********************************************8/
     * /**
     * 【PAD-API】 临时接口，创建
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/appointpayment/{orderid}/init", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean creatrAppointPaymentByOrderId(@PathVariable("orderid") String orderId) {
        return iAppointPaymentBizService.actCreateAppointPayment(orderId);
    }


}
