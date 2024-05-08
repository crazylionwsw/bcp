package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.ReceptFileBean;
import com.fuze.bcp.api.creditcar.service.IOrderBizService;
import com.fuze.bcp.api.creditcar.service.IReceptFileBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.TEMSignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lily on 2017/8/4.
 */
@RestController
@RequestMapping(value = "/json")
public class OrderController {

    @Autowired
    private IOrderBizService iOrderBizService;

    @Autowired
    private IReceptFileBizService iReceptFileBizService;

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PurchaseCarOrderBean> getOne(@PathVariable("id") String id) {
        return iOrderBizService.actGetOrder(id);
    }

    @RequestMapping(value = "/orders/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<PurchaseCarOrderBean> searchOrders(@RequestBody SearchBean searchBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iOrderBizService.actSearchOrders(userId, searchBean);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<PurchaseCarOrderBean> save(@RequestBody PurchaseCarOrderBean purchaseCarOrderBean) {
        return iOrderBizService.actSaveOrder(purchaseCarOrderBean);
    }

    @RequestMapping(value = "/order/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<PurchaseCarOrderBean> sign(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iOrderBizService.actSignOrder(id,signInfo);
    }

    /**
     *  新审查、审批
     */
    @RequestMapping(value = "/order/{id}/temsign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<PurchaseCarOrderBean> sign(@PathVariable("id") String id,@RequestBody TEMSignInfo temSignInfo) {
        return iOrderBizService.actSignOrder(id, temSignInfo);
    }

    /**
     * 根据     transactionId      回显
     * @param id            交易ID
     * @return
     */
    @RequestMapping(value = "/order/transaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PurchaseCarOrderBean> getOneByTransactionId(@PathVariable("id") String id) {
        return iOrderBizService.actGetOrderByTransactionId(id);
    }

    @RequestMapping(value = "/receptfile/transaction/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getReceptFile(@PathVariable("id") String id){
        return iReceptFileBizService.actGetReceptFile(id);
    }

    @RequestMapping(value = "/receptfile",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveReceptFile(@RequestBody ReceptFileBean receptFileBean){
        return iReceptFileBizService.actSaveReceptFile(receptFileBean);
    }

    @RequestMapping(value = "/order/compare/{transactionId}/{propname}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean compareData(@PathVariable("propname") String propname, @PathVariable("transactionId") String transactionId){
        return iOrderBizService.actCompareData(propname,transactionId);
    }

}
