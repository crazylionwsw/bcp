package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ISwipingCardBizService;
import com.fuze.bcp.bean.ResultBean;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 渠道刷卡
 * Created by user on 2017/7/3.
 */
@RestController
@RequestMapping(value = "/json")
public class SwipingCardController extends BaseController {

    @Autowired
    ISwipingCardBizService iSwipingCardBizService;


    /**************************************渠道刷卡***************************************/
    /**
     * 【PAD-API】 分页获取loginUserId 的渠道刷卡
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/swipingcards", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getswipingcardsPage(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "5") Integer pageSize,@RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed) {
        return iSwipingCardBizService.actGetSwipingCard(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

    /**
     * 【PAD-API】 根据交易ID获取渠道刷卡
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/transaction/{transactionid}/swipingcard", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAppointSwipingCard(@PathVariable("transactionid") String transactionId) {
        return this.iSwipingCardBizService.actInitSwipingCardByTransactionId(transactionId);
    }

    /**
     * 【PAD-API】临时保存渠道刷卡(整体提交)
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/swipingcard", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveAppointSwipingCard(@RequestBody SwipingCardSubmissionBean awipingCardSubmissionBean) {
        awipingCardSubmissionBean.setLoginUserId(super.getOperatorId());
        return this.iSwipingCardBizService.actSavePadSwipingCard(awipingCardSubmissionBean);
    }

    /**
     * 【PAD-API】 提交保存渠道刷卡(进工作流)
     *
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/swipingcard/{swipingcardid}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean submitAppointSwipingCard(@PathVariable("swipingcardid") String swipingCardId,@RequestBody String body) {
        return this.iSwipingCardBizService.actSubmitSwipingCard(swipingCardId ,(String) new JSONObject(body).get("comment"));
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
