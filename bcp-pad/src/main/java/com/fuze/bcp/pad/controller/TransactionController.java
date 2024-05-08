package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.service.ICancelOrderBizService;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.PadCustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class TransactionController extends BaseController {

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICancelOrderBizService iCancelOrderBizService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    /**
     * 【PAD - API】--我的在办业务列表 （待修改）
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getMyTransactions(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "ispassed", required = false, defaultValue = "0") boolean isPassed) {
        System.out.println(super.getOperatorId());
        List<Integer> tmpList = new ArrayList<>();
        if (isPassed) {
            tmpList.add(CustomerTransactionBean.TRANSACTION_CANCELLED);
            tmpList.add(CustomerTransactionBean.TRANSACTION_STOP);
            tmpList.add(CustomerTransactionBean.TRANSACTION_FINISH);
            tmpList.add(CustomerTransactionBean.TRANSACTION_LOAN);
        } else {
            tmpList.add(CustomerTransactionBean.TRANSACTION_TRANSFERED);
            tmpList.add(CustomerTransactionBean.TRANSACTION_INIT);
            tmpList.add(CustomerTransactionBean.TRANSACTION_PROCESSING);
            tmpList.add(CustomerTransactionBean.TRANSACTION_ORDER);
            tmpList.add(CustomerTransactionBean.TRANSACTION_CANCELLING);
        }
        return this.iCarTransactionBizService.actGetTransactions(super.getOperatorId(), tmpList, pageIndex, pageSize);
    }

    /**
     * 【PAD API】-- 我的全部业务 （待修改）
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/user/{loginuserid}/transactions/all", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getMyAllTransactions(@PathVariable("loginuserid") String loginUserId, @RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pageSize) {
        DataPageBean<PadCustomerTransactionBean> page = iCarTransactionBizService.actGetAllTransactionsByLoginUserId(loginUserId, pageIndex, pageSize).getD();
        return ResultBean.getSucceed().setD(page);
    }

    @RequestMapping(value = "/transaction/query", method = RequestMethod.GET)
    public ResultBean<List<PadCustomerTransactionBean>> actGetCustomerQuery(@RequestParam("inputstr") String inputStr) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return iCarTransactionBizService.actGetTransactionQuery(jwtUser.getId(), inputStr);
    }

    /**
     * 【PAD-API】-- 取消交易
     *
     * @return
     */
    @RequestMapping(value = "/transaction/{id}/cancel", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean cancelTransaction(@PathVariable("id") String id, @RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        String comment = (String) obj.get("comment");
        return iCancelOrderBizService.actSubmitCancelOrder(id, comment);
    }

}
