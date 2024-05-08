package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.RejectCustomerBean;
import com.fuze.bcp.api.creditcar.service.IRejectCustomerBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class RejectCustomerController {

    @Autowired
    private IRejectCustomerBizService iRejectCustomerBizService;

    @RequestMapping(value = "/rejectcustomers/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<RejectCustomerBean> searchRejectCustomers(@RequestBody SearchBean searchBean) {
        return iRejectCustomerBizService.actSearchRejectCustomers(searchBean);
    }
}
