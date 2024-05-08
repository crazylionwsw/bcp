package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.CarValuationBean;
import com.fuze.bcp.api.creditcar.service.ICarValuationBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zqw on 2017/9/2.
 */
@RestController
@RequestMapping(value = "/json")
public class CarValuationController {

    @Autowired
    private ICarValuationBizService iCarValuationBizService;

    /**
     *          通过二手车车架号码(vin)获取二手车评估信息
     * @param vin
     * @return
     */
    @RequestMapping(value = "/carvaluation/{vin}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarTypeById(@PathVariable("vin") String vin) {
        return iCarValuationBizService.actGetValuationByVin(vin);
    }

    //获取所有评估信息
    @RequestMapping(value = "/carvaluations",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarValutions(@RequestParam(value = "currentPage",defaultValue = "0")Integer currentPage,
                                      @RequestParam(value = "approveStatus", required = false, defaultValue = "1") int approveStatus){
        return iCarValuationBizService.actGetCarValuations(currentPage,approveStatus);
    }

    //获取单条评估信息(根据Id)
    @RequestMapping(value = "/carvaluation/valuation/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarValution(@PathVariable("id")String id){
        return iCarValuationBizService.actFindCarValuationById(id);
    }

    //确认评估信息
    @RequestMapping(value = "/carvaluation/pass",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean savePassCarValution(@RequestBody CarValuationBean carValuationBean){
        return iCarValuationBizService.actSavePassCarValuation(carValuationBean);
    }

    //取消确认评估信息
    @RequestMapping(value = "/carvaluation/cancel",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCancelCarValution(@RequestBody CarValuationBean carValuationBean){
        return iCarValuationBizService.actCancelCarValuation(carValuationBean);
    }

    //保存评估信息
    @RequestMapping(value = "/carvaluation",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCarValution(@RequestBody CarValuationBean carValuationBean){
        return iCarValuationBizService.actSaveCarValuation(carValuationBean);
    }

    //删除评估信息
    @RequestMapping(value = "/carvaluation/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCarValuation(@PathVariable("id") String id){
        return iCarValuationBizService.actDeleteCarValuation(id);
    }

    //查询
    @RequestMapping(value = "/carvaluation/search")
    @ResponseBody
    public ResultBean searchCarValuation(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,@RequestBody CarValuationBean carValuationBean){
        return iCarValuationBizService.actSearchCarValuations(currentPage,carValuationBean);
    }

    //根据评估单Id判断是否已被签约使用
    @RequestMapping(value = "/carvaluation/finish/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarValuationFinshOrder(@PathVariable("id") String id){
       return iCarValuationBizService.actGetCarValuationFinshOrder(id);
    }

}
