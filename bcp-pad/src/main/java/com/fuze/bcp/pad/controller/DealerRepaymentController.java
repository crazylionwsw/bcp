package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IDealerRepaymentBizService;
import com.fuze.bcp.api.creditcar.service.ISwipingCardBizService;
import com.fuze.bcp.bean.ResultBean;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 渠道还款
 * Created by user on 2017/7/3.
 */
@RestController
@RequestMapping(value = "/json")
public class DealerRepaymentController extends BaseController {

    @Autowired
    IDealerRepaymentBizService iDealerRepaymentBizService;


    /**************************************渠道还款***************************************/
    /**
     * 【PAD-API】 分页获取loginUserId 的渠道还款
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/dealerrepayments", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getswipingcardsPage(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "5") Integer pageSize,@RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed) {
        return iDealerRepaymentBizService.actGetDealerRepayments(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

    /**
     * 【PAD-API】 根据交易ID获取预约刷卡
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/transaction/{transactionid}/dealerrepayment", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAppointSwipingCard(@PathVariable("transactionid") String transactionId) {
        return this.iDealerRepaymentBizService.actInitDealerRepaymentByTransactionId(transactionId);
    }

    /**
     * 【PAD-API】临时保存预约刷卡(整体提交)
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/dealerrepayment", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveAppointSwipingCard(@RequestBody DealerRepaymentSubmissionBean dealerRepaymentSubmissionBean) {
        return this.iDealerRepaymentBizService.actSaveDealerRepayment(dealerRepaymentSubmissionBean);
    }

    /**
     * 【PAD-API】 提交保存预约刷卡(进工作流)
     * 00
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/dealerrepayment/{dealerrepaymentid}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean submitAppointSwipingCard(@PathVariable("dealerrepaymentid") String dealerrepaymentId, @RequestBody String body) {
        return this.iDealerRepaymentBizService.actSubmitDealerRepayment(dealerrepaymentId, (String) new JSONObject(body).get("comment"));
    }

//    /**
//     * 临时生成数据使用
//     *
//     * @return ResultBean
//     */
//    @RequestMapping(value = "/swipingcard/{oederid}/init", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultBean initAppointSwipingCard(@PathVariable("oederid") String oederId) {
//        ResultBean resultBean = iSwipingCardBizService.(oederId);
//        if (resultBean.isSucceed()) {
//            return resultBean;
//        } else {
//            return ResultBean.getFailed().setM("创建失败");
//        }
//    }

    /**************************************刷卡***************************************/

}
