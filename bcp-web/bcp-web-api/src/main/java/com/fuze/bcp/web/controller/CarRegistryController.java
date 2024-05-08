package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistryBean;
import com.fuze.bcp.api.creditcar.service.ICarRegistryBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zqw on 2017/8/23.
 */
@RestController
@RequestMapping("/json")
public class CarRegistryController {

    @Autowired
    private ICarRegistryBizService iCarRegistryBizService;

    @RequestMapping(value = "/carregistries/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CarRegistryBean> searchCarDemands(@RequestBody SearchBean searchBean) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iCarRegistryBizService.actSearchCarRegistries(userId, searchBean);
    }

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/carregistry/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CarRegistryBean> getOne(@PathVariable("id") String id) {
        return iCarRegistryBizService.actGetCarRegistry(id);
    }

    /**
     *      根据     客户交易ID       查询车辆上牌数据
     * @param id    客户交易ID
     * @return
     */
    @RequestMapping(value = "/carregistry/transaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CarRegistryBean> getOneByTransactionId(@PathVariable("id") String id) {
        return iCarRegistryBizService.actGetByCustomerTransactionId(id);
    }

    /**
     * 审批
     */
    @RequestMapping(value = "/carregistry/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CarRegistryBean> sign(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iCarRegistryBizService.actSignCarRegistry(id, signInfo);
    }

    /**
     * 保存 资质信息
     */
    @RequestMapping(value = "/carregistry", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CarRegistryBean> save(@RequestBody CarRegistryBean carRegistryBean) {
        return iCarRegistryBizService.actSaveCarRegistry(carRegistryBean);
    }

}
