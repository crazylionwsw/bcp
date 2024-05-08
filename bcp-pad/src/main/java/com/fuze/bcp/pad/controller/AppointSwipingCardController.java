package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IAppointSwipingCardBizService;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.ResultBean;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 预约刷卡，刷卡
 * Created by user on 2017/7/3.
 */
@RestController
@RequestMapping(value = "/json")
public class AppointSwipingCardController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppointSwipingCardController.class);

    @Autowired
    IAppointSwipingCardBizService iAppointSwipingCardBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    /**************************************预约刷卡***************************************/
    /**
     * 【PAD-API】 分页获取loginUserId 的预约刷卡
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/appointswipingcards", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAppointSwipingCardsPage(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "5") Integer pageSize,@RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed) {
        return iAppointSwipingCardBizService.actGetAppointSwipingCards(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

    /**
     * 【PAD-API】 根据交易ID获取预约刷卡
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/transaction/{transactionid}/appointswipingcard", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAppointSwipingCard(@PathVariable("transactionid") String transactionId) {
        return this.iAppointSwipingCardBizService.actGetAppointSwipingCardByTransactionId(transactionId);
    }

    /**
     * 【PAD-API】临时保存预约刷卡(整体提交)
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/appointswipingcard", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveAppointSwipingCard(@RequestBody AppointSwipingCardSubmissionBean appointSwipingCardSubmissionBean) {
        logger.error("AppointSwipingCardSubmissionBean PAD create!==============================================================================================");
        appointSwipingCardSubmissionBean.setLoginUserId(super.getOperatorId());
        return this.iAppointSwipingCardBizService.actSaveAppointSwipingCard(appointSwipingCardSubmissionBean);
    }

    /**
     * 【PAD-API】 提交保存预约刷卡(进工作流)
     * 00
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/appointswipingcard/{appointswipingcardid}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean submitAppointSwipingCard(@PathVariable("appointswipingcardid") String appointSwipingCardId, @RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        String comment = (String) obj.get("comment");
        return this.iAppointSwipingCardBizService.actSubmitAppointSwipingCard(appointSwipingCardId, comment);
    }

    /**
     * 临时生成数据使用
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/appointswipingcard/{oederid}/init", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean initAppointSwipingCard(@PathVariable("oederid") String oederId) {
        ResultBean resultBean = iAppointSwipingCardBizService.actCreateAppointSwipingCard(oederId);
        if (resultBean.isSucceed()) {
            return resultBean;
        } else {
            return ResultBean.getFailed().setM("创建失败");
        }
    }


    /**
     * 【PAD-API】 transactionId 获取交易信息
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/transaction/{transactionid}/summary", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTransactionSummary(@PathVariable("transactionid") String transactionId) {
        return this.iCarTransactionBizService.actGetTransactionSummary(transactionId);
    }


    /**************************************刷卡***************************************/

}
