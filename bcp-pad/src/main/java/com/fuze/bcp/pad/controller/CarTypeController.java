package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.bean.CarBrandBean;
import com.fuze.bcp.api.bd.bean.CarModelBean;
import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.creditcar.service.ICarValuationBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
@RequestMapping(value = "/json")
public class CarTypeController {

    @Autowired
    private ICarTypeBizService iCarTypeBizService;

    @Autowired
    private ICarValuationBizService iCarValuationBizService;

    /**
     * 【PAD-API】-- 车系Id获取车型数据信息列表
     * @return
     */
    @RequestMapping(value = "/cartypes/{modelid}/model",method = RequestMethod.GET)
    public ResultBean getCarTypesByCarModel(@PathVariable("modelid") String modelId){
        return iCarTypeBizService.actGetCarTypes(modelId);
    }


    /**
     * 【PAD-API】-- 品牌Id获取车型数据信息列表
     * @return
     */
    @RequestMapping(value = "/cartypes/{brandid}/brand",method = RequestMethod.GET)
    public ResultBean getCaTypesByCarBrand(@PathVariable("brandid") String brandId){
        return iCarTypeBizService.actGetCarTypeByCarBrand(brandId);
    }


    /**
     * 【PAD-API】-- Id 获取车型数据详情
     * @return
     */
    @RequestMapping(value = "/cartype/{cartypeid}",method = RequestMethod.GET)
    public ResultBean getCarTypeById(@PathVariable("cartypeid") String cartypeId){
        return iCarTypeBizService.actGetCarTypeById(cartypeId);
    }


    /**
     * 【PAD-API】-- 根据车型Id 获取该车型的品牌，车系，车型所有信息
     * @return
     */
    @RequestMapping(value = "/cartype/{cartypeid}/full",method = RequestMethod.GET)
    public ResultBean geFullByCarTypeId(@PathVariable("cartypeid") String cartypeId) {
        ResultBean resultBean = iCarTypeBizService.actGetCarTypeById(cartypeId);
        HashMap map = new HashMap<>();
        if (resultBean.isSucceed()) {
            CarTypeBean carTypeBean  = (CarTypeBean) resultBean.getD();
            if(carTypeBean!=null){
                map.put("ml",carTypeBean.getMl());
                map.put("carTypeId",carTypeBean.getId());
                map.put("carTypeName",carTypeBean.getName());
                map.put("price",carTypeBean.getPrice());
                CarBrandBean carBrandBean  = iCarTypeBizService.actGetCarBrandById(carTypeBean.getCarBrandId()).getD();

                if(carBrandBean != null){
                    map.put("carBrandId",carBrandBean.getId());
                    map.put("carBrandName",carBrandBean.getName());
                }
                CarModelBean carModelBean = iCarTypeBizService.actGetCarModelById(carTypeBean.getCarModelId()).getD();
                    if(carModelBean!=null){
                        map.put("carModelId",carModelBean.getId());
                        map.put("carModelName",carModelBean.getName());
                }
                return ResultBean.getSucceed().setD(map);
            }else{
                return ResultBean.getFailed().setM("车型数据为空！");
            }
        } else {
            return resultBean;
        }
    }
}
