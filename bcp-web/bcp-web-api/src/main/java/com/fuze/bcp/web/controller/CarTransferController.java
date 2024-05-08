package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferBean;
import com.fuze.bcp.api.creditcar.service.ICarTransferBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 转移过户
 * Created by zqw on 2017/9/20.
 */
@RestController
@RequestMapping("/json")
public class CarTransferController {
    private static final Logger logger = LoggerFactory.getLogger(CarTransferController.class);

    @Autowired
    private ICarTransferBizService iCarTransferBizService;

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/cartransfer/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CarTransferBean> getOne(@PathVariable("id") String id) {
        return iCarTransferBizService.actGetCarTransfer(id);
    }

    /**
     * 模糊查询  转移过户
     * @param searchBean
     * @return
     */
    @RequestMapping(value = "/cartransfers/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CarTransferBean> searchCarDemands(@RequestBody SearchBean searchBean) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iCarTransferBizService.actSearchCarTransfers(userId, searchBean);
    }

    /**
     * 保存 转移过户信息
     */
    @RequestMapping(value = "/cartransfer", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CarTransferBean> save(@RequestBody CarTransferBean carTransferBean) {
        return iCarTransferBizService.actSaveCarTransfer(carTransferBean);
    }

    /**
     *      根据     客户交易ID       查询转移过户数据
     * @param id    客户交易ID
     * @return
     */
    @RequestMapping(value = "/cartransfer/transaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CarTransferBean> getOneByTransactionId(@PathVariable("id") String id) {
        return iCarTransferBizService.actGetByCustomerTransactionId(id);
    }

    @RequestMapping(value = "/cartransfer/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CarTransferBean> sign(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iCarTransferBizService.actSignCarTransfer(id, signInfo);
    }
}
