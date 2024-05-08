package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistrySubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarRegistryBizService;
import com.fuze.bcp.bean.ResultBean;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 车辆上牌
 * Created by user on 2017/7/3.
 */
@RestController
@RequestMapping(value = "/json")
public class CarRegistryController extends BaseController {

    @Autowired
    ICarRegistryBizService iCarRegistryBizService;

    /**
     * 【PAD-API】 分页获取loginUserId 的车辆上牌
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/carregistrys", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarRegistrysPage(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "5") Integer pageSize,@RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed) {
        return iCarRegistryBizService.actGetCarRegistrys(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

    /**
     * 【PAD-API】 根据ID获取我的车辆上牌
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/transaction/{transactionid}/carregistry", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarRegistry(@PathVariable("transactionid") String transactionId) {
        return iCarRegistryBizService.actInitCarRegistryByTransactionId(transactionId);
    }

    /**
     * 【PAD-API】临时保存车辆上牌(整体提交)
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/carregistry", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCarRegistry(@RequestBody CarRegistrySubmissionBean carRegistrySubmission) {
        carRegistrySubmission.setLoginUserId(super.getOperatorId());
        return iCarRegistryBizService.actSaveCarRegistry(carRegistrySubmission);
    }

    /**
     * 【PAD-API】 提交保存车辆上牌(进工作流)
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/carregistry/{carregistryid}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean submitCarRegistry(@PathVariable("carregistryid") String carRegistryId, @RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        String comment = (String) obj.get("comment");
        return iCarRegistryBizService.actSubmitCarRegistry(carRegistryId, comment);
    }


    /***************************************************临时接口，创建***********************************************8/
     *//**
     * 【PAD-API】 临时接口，创建
     *
     * @return ResultBean
     *//*
    @RequestMapping(value = "/carregistry/{orderid}/init", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean creatrCarRegistryByOrderId(@PathVariable("orderid") String orderId) {
        return iCarRegistryBizService.actCreateCarRegistry(orderId);
    }*/


}
