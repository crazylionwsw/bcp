package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/json")
public class CarModelController {

    @Autowired
    private ICarTypeBizService iCarTypeBizService;

    /**
     * 【PAD-API】-- 品牌Id获取车系列表信息列表
     *
     * @return
     */
    @RequestMapping(value = "/carmodels/{brandid}/brand", method = RequestMethod.GET)
    public ResultBean getCarModelsByBrand(@PathVariable("brandid") String brandId) {
        return iCarTypeBizService.actGetCarModels(brandId);
    }

}
