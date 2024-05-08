package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ImageTypeFileBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/json")
public class ImagesController {

    @Autowired
    private ICustomerImageFileBizService iCustomerImageFileBizService;


    @RequestMapping(value = "/transaction/{id}/images", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DataPageBean<ImageTypeFileBean>> getCustomerImages(@PathVariable("id") String transactionId,
                                                                         @RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex,
                                                                         @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pageSize) {
        return iCustomerImageFileBizService.actGetCustomerImagesByTransactionId(transactionId, pageIndex, pageSize);
    }
}
