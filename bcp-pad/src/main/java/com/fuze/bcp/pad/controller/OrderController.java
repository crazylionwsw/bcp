package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.OrderSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IOrderBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.ResultBean;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/json")
public class OrderController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    IOrderBizService iOrderBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;


    /**
     * 【PAD-API】-- 提交订单信息（临时保存，不进审批流）
     *
     * @param orderSubmissionBean
     * @return
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveOrder(@RequestBody OrderSubmissionBean orderSubmissionBean) {
        orderSubmissionBean.setLoginUserId(super.getOperatorId());
        logger.error("OrderSubmissionBean PAD create!==============================================================================================");
        return iOrderBizService.actSaveOrder(orderSubmissionBean);
    }

    /**
     * 【PAD-API】-- 提交订单信息(正式保存，同时提交进审批流)
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/order/{orderid}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean submitOrder(@PathVariable("orderid") String orderId, @RequestBody String body) {
        //TODO 查询资质是否通过
        JSONObject obj = new JSONObject(body);
        String comment = (String) obj.get("comment");
        return iOrderBizService.actSubmitOrder(orderId, comment);
    }


    /**
     * 【PAD-API】-- 根据 transactionId  初始化 客户签约 order 的基本信息
     *
     * @param transactionId
     * @return
     */
    @RequestMapping(value = "/order/{transactionid}/init", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<OrderSubmissionBean> getOrderByTransactionId(@PathVariable("transactionid") String transactionId) {
        return iOrderBizService.actGetTransactionOrder(transactionId);
    }


    /**
     * 【PAD-API】-- 根据 id 获取客户签约
     *
     * @param orderid
     * @return
     */
    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOrder(@PathVariable("id") String orderid) {
        return iOrderBizService.actGetOrder(orderid);
    }

    /**
     * 【PAD-API】-- 根据 id 获取客户签约信息
     *
     * @param pageIndex 当前页面
     * @return
     */
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOrdersByLoginUserId(@RequestParam(value = "pageindex", defaultValue = "0", required = false) Integer pageIndex, @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pageSize, @RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed) {
        return iOrderBizService.actGetOrders(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

    /**
     *获取签约阶段的审核状态,判断是否可以进行业务调整
     */
    @RequestMapping(value = "/order/{id}/approvestatus", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOrderApproveStatus(@PathVariable("id") String transactionId) {
        return iOrderBizService.actGetOrderApproveStatus(transactionId);
    }

    /**
     * 获取组装所有缴费项
     */
    @RequestMapping(value = "/order/{id}/fees",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAllFeesOnOrder(@PathVariable("id") String transactionId){
        return iOrderBizService.actGetAllFeesOnOrder(transactionId);
    }

}
