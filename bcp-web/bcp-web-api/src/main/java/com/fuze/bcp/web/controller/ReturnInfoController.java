package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.ReturnInfoBean;
import com.fuze.bcp.api.creditcar.service.IReturnInfoBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by GQR on 2017/8/24.
 */
@RestController
@RequestMapping(value = "/json")
public class ReturnInfoController {

    @Autowired
    private IReturnInfoBizService iReturnInfoBizService;


    /**
     * 获取资料归还列表数据(带分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/returninfos", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getReturnInfo(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage) {
        return iReturnInfoBizService.actGetReturnInfos(currentPage);
    }

    /**
     * 模糊查询列表
     * @param currentPage
     * @param customerBean
     * @return
     */
    @RequestMapping(value = "/returninfos/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<ReturnInfoBean> searchReturnInfo(
            @RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
            @RequestBody CustomerBean customerBean) {
        return iReturnInfoBizService.actSearchReturnInfos(currentPage,customerBean);
    }

    /**
     * 根据id回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/returninfo/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<ReturnInfoBean> getOne(@PathVariable("id") String id){
        return iReturnInfoBizService.actGetReturnInfo(id);
    }
}
