package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/json")
public class CarBrandController {


    @Autowired
    private ICarTypeBizService iCarTypeBizService;

    @Autowired
    private ICarDealerBizService iCarDealerBizService;

    /**
     * 【PAD-API】--获取所有车辆品牌信息列表
     * @return
     */
    @RequestMapping(value = "/carbrands",method = RequestMethod.GET)
    public ResultBean lookupCarBrands(){
        return iCarTypeBizService.actLookupCarBrands();
    }

    /**
     * 【PAD-API】--获取渠道经营的车辆品牌信息列表
     * @return
     */
    @RequestMapping(value = "/carbrands/{cardealerid}/cardealer",method = RequestMethod.GET)
    public ResultBean lookupCarBrands(@PathVariable("cardealerid") String cardealerId){
        return iCarDealerBizService.actGetCarBrandByCardealer(cardealerId);
    }


}
