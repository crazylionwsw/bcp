package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.ResetOrderBean;
import com.fuze.bcp.api.creditcar.service.IResetOrderBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by admin on 2017/10/23.
 */

/**
 * 重新签约
 */
@RestController
@RequestMapping(value = "/json")
public class ResetOrderController {

    private static final Logger logger = LoggerFactory.getLogger(CancelOrderController.class);

    @Autowired
    private IResetOrderBizService iResetOrderBizService;

    /**
     * 获取重新签约列表 带分页
     * @param currentPage
     * @param approveStatus
     * @return
     */
    @RequestMapping(value = "/resetorders",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<ResetOrderBean> getPageData(
            @RequestParam(value = "currentPage",defaultValue = "0") int currentPage,
            @RequestParam(value = "approveStatus",defaultValue = "1") int approveStatus){

        return iResetOrderBizService.actGetResetOrders(currentPage,approveStatus);
    }

    @RequestMapping(value = "/resetorders/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<ResetOrderBean> search(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                                             @RequestParam(value = "approveStatus", defaultValue = "1") int approveStatus,
                                             @RequestBody CustomerBean customerBean){
        return iResetOrderBizService.actSearchResetOrders(currentPage,approveStatus,customerBean);
    }

    /**
     * 根据id回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/resetorder/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<ResetOrderBean> getOne(@PathVariable(value = "id") String id){

        return iResetOrderBizService.actGetResetOrder(id);
    }

    @RequestMapping(value = "/resetorder/transaction/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<ResetOrderBean> getOneByTransactionId(@PathVariable(value = "id") String transactionId){

        return iResetOrderBizService.actGetResetOrderByTransactionId(transactionId);
    }

    @RequestMapping(value = "/resetorder/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<ResetOrderBean> sign(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iResetOrderBizService.actSignResetOrder(id, signInfo);
    }
}
