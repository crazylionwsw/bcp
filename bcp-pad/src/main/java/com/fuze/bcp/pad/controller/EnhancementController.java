package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.enhancement.EnhancementSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IEnhancementBizService;
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
public class EnhancementController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    IEnhancementBizService iEnhancementBizService;

    /**************************************资料补充***************************************/
    /**
     * 【PAD-API】 分页获取loginUserId 的资料补充
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/enhancements", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getEnhancementsPage(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex,
                                          @RequestParam(value = "pagesize", required = false, defaultValue = "5") Integer pageSize,
                                          @RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed,
                                          @RequestParam(value = "transactionId", defaultValue = "", required = false) String transactionId) {
        return iEnhancementBizService.actGetEnhancements(isPassed, this.getOperatorId(), pageIndex, pageSize, transactionId);
    }

    /**
     * 【PAD-API】 根据交易ID获取资料补充
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/enhancement/{id}/", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getEnhancement(@PathVariable("id") String id) {
        return this.iEnhancementBizService.actGetSubmissionById(id);
    }

    /**
     * 【PAD-API】临时保存资料补充(整体提交)
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/enhancement", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveEnhancement(@RequestBody EnhancementSubmissionBean enhancementSubmissionBean) {
        enhancementSubmissionBean.setLoginUserId(this.getOperatorId());



        iEnhancementBizService.actSaveEnhancement(enhancementSubmissionBean);
        //提交进工作流
        String comment = enhancementSubmissionBean.getComment();
        return this.iEnhancementBizService.actSubmitEnhancement(enhancementSubmissionBean.getId(), comment);
    }

    /**
     * 【PAD-API】 提交保存资料补充(进工作流)
     * 00
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/enhancement/{id}/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean submitEnhancement(@PathVariable("id") String id, @RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        String comment = (String) obj.get("comment");
        return this.iEnhancementBizService.actSubmitEnhancement(id, comment);
    }

}
