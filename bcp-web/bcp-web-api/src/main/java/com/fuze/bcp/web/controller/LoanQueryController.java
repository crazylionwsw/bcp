package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.wechat.bean.LoanQueryBean;
import com.fuze.bcp.api.wechat.service.ILoanQueryBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ${Liu} on 2018/4/18.
 */
@RestController
@RequestMapping(value = "/json")
public class LoanQueryController {

    @Autowired
    private ILoanQueryBizService iLoanQueryBizService;

    @RequestMapping(value = "/loanquery", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<LoanQueryBean> saveLoanQuery(@RequestBody LoanQueryBean loanQueryBean) {
        return iLoanQueryBizService.actSaveLoanQuery(loanQueryBean);
    }
}
