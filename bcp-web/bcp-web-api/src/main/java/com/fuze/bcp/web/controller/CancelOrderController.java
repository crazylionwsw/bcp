package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.CancelOrderBean;
import com.fuze.bcp.api.creditcar.service.ICancelOrderBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by GQR on 2017/8/19.
 */

/**
 * 取消业务
 */
@RestController
@RequestMapping(value = "/json")
public class CancelOrderController {

    @Autowired
    private ICancelOrderBizService iCancelOrderBizService;

    /**
     * 模糊查询
     * @param searchBean
     * @return
     */
    @RequestMapping(value = "/cancelorders/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CancelOrderBean> searchCancelorder(@RequestBody SearchBean searchBean) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iCancelOrderBizService.actSearchCancelOrders(userId, searchBean);
    }

    /**
     * 根据id获取
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancelorder/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CancelOrderBean> getOne(@PathVariable("id") String id){
        return iCancelOrderBizService.actGetCancelOrder(id);
    }

    /**
     * 根据id获取
     * @param transactionId
     * @return
     */
    @RequestMapping(value = "/cancelorder/transaction/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CancelOrderBean> getOneByTransactionId(@PathVariable("id") String transactionId){
        return iCancelOrderBizService.actGetCancelOrderByTransactionId(transactionId);
    }

    /**
     *  审核
     * @param id
     * @param signInfo
     * @return
     */
    @RequestMapping(value = "/cancelorder/{id}/sign",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CancelOrderBean> getOneByTransactionId(@PathVariable("id") String id, @RequestBody SignInfo signInfo){
        return iCancelOrderBizService.actSignCancelOrder(id,signInfo);
    }

}
