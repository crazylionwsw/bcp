package com.fuze.wechat.controller;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.LoanQuery;
import com.fuze.wechat.service.ILoanQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by CJ on 2018/4/23.
 */
@RestController
@RequestMapping("/json")
public class LoanQueryController {

    Logger logger = LoggerFactory.getLogger(LoanQueryController.class);

    @Autowired
    private ILoanQueryService iLoanQueryService;

    @RequestMapping(value = "/loanQuery/save", method = RequestMethod.POST)
    public ResultBean saveLoanQuery(@RequestBody LoanQuery loanQuery) {
        return iLoanQueryService.actSaveLoanQuery(loanQuery);
    }

    @RequestMapping(value = "/loanQuery/{id}", method = RequestMethod.GET)
    public ResultBean getLoanQuery(@PathVariable("id") String id) {
        return iLoanQueryService.actGetLoanQuery(id);
    }

}
