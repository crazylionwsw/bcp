package com.fuze.bcp.enterprise.controller;

import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICustomerImageTypeBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/json")
public class ImageTypeController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;

    @Autowired
    private ICustomerImageTypeBizService iCustomerImageTypeBizService;


    @RequestMapping(value = "/imagetypes/list", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<CustomerImageTypeBean>> getCustomerImageTypes(@RequestParam(value = "bizcode") String bizCode, @RequestParam(value = "billtype") String billTypeCode) {
        return iCustomerImageTypeBizService.actGetCustomerImageTypesByBillType(bizCode, billTypeCode);
    }

}
