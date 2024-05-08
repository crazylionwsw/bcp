package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.FileExpressBean;
import com.fuze.bcp.api.creditcar.service.IFileExpressBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lily on 2017/8/17.
 */
@RestController
@RequestMapping(value = "/json",method = {RequestMethod.GET,RequestMethod.POST})
public class FileExpressController {

    private static final Logger logger = LoggerFactory.getLogger(FileExpressController.class);

    @Autowired
    IFileExpressBizService iFileExpressBizService;

    @RequestMapping(value = "/fileexpresses", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<FileExpressBean> getPageData(
            @RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage) {
        return iFileExpressBizService.actGetFileExpresss(currentPage);
    }

    @RequestMapping(value = "/fileexpress/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<FileExpressBean> searchCarDemands(
            @RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
            @RequestBody CustomerBean customerBean) {
        return iFileExpressBizService.actSearchFileExpresss(currentPage,customerBean);
    }

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/fileexpress/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<FileExpressBean> getOne(@PathVariable("id") String id) {
        return iFileExpressBizService.actGetFileExpress(id);
    }
}
