package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.DemandSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICustomerDemandBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerSurveyTemplateBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class CustomerDemandController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(CustomerDemandController.class);

    @Autowired
    ICustomerDemandBizService iCustomerDemandBizService;

    @Autowired
    ICustomerSurveyTemplateBizService iCustomerSurveyTemplateBizService;

    @Autowired
    IParamBizService iParamBizService;

    /**
     * 【PAD - API】-- 新车业务 - 新增／修改购车需求接口
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/customerdemand", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean postCustomerDemand(@RequestBody DemandSubmissionBean demandSubmission) {
        return iCustomerDemandBizService.actSubmitCustomerDemand(demandSubmission);
    }

    /**
     * 【PAD - API】-- 新车业务 - 获取购车需求
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/customerdemand/{transactionid}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerDeamand(@PathVariable("transactionid") String transactionId) {
        return iCustomerDemandBizService.actRetrieveCustomerDemand(transactionId);
    }

    @RequestMapping(value = "/customerdemand/{transactionid}/survey", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerSurvey(@PathVariable("transactionid") String transactionId) {

        ResultBean resultBean = iCustomerSurveyTemplateBizService.actGetTransactionSurvey(transactionId);
        if (resultBean.isSucceed() && resultBean.getD() != null) {
            return ResultBean.getSucceed().setD(iParamBizService.actGetWebServerUrl().getD() + "/#/survey/" + transactionId);
        } else {
            //约定，Ｄ为空的时不跳转
            return ResultBean.getFailed().setM("该笔交易未分配调查问卷！如需分配请联系风控人员，在资质审查页面分配！");
        }
    }


    /**
     * 【PAD - API】-- 分页获取当前登陆用户的资质
     *
     * @param   pageIndex
     * @param   pageSize
     * @param   isPassed    1 已审核完成   0 未完成
     * @return
     */
    @RequestMapping(value = "/customerdemands", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getMyCustomerDeamand(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pageSize,@RequestParam(value = "ispassed",required = false, defaultValue = "0") boolean isPassed) {
        return iCustomerDemandBizService.actGetCustomerDemands(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

}
