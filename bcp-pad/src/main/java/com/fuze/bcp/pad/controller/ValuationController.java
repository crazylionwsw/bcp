package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICustomerImageTypeBizService;
import com.fuze.bcp.api.creditcar.bean.CarValuationBean;
import com.fuze.bcp.api.creditcar.bean.ValuationInfo;
import com.fuze.bcp.api.creditcar.bean.ValuationListBean;
import com.fuze.bcp.api.creditcar.bean.ValuationSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarValuationBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/json")
public class ValuationController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;
    @Autowired
    private ICarValuationBizService iCarValuationBizService;

    @Autowired
    private ICustomerImageTypeBizService iCustomerImageTypeBizService;


    /**
     * 【PAD-API】-- 获取评估列表
     * @return
     */
    @RequestMapping(value = "/valuations",method = RequestMethod.GET)
    public ResultBean<DataPageBean<ValuationListBean>> getValuations(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer page, @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer size){
        return iCarValuationBizService.actGetValuations(page, size);
    }

    @RequestMapping(value = "/valuation/imagetypes", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getBillCustomerImageTypes() {
        //获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType("A021").getD();
        //档案类型编码
        List<String> imageTypeCodes = billType.getRequiredImageTypeCodes();

        if (imageTypeCodes.size() > 0) {
            List<CustomerImageTypeBean> customerImageTypes = iCustomerImageTypeBizService.actFindCustomerImageTypesByCodes(imageTypeCodes).getD();
            return ResultBean.getSucceed().setD(customerImageTypes);
        }

        return ResultBean.getSucceed();
    }

    /**
     * 【PAD-API】-- 获取评估列表(通过经销商ID）
     * @return
     */
    @RequestMapping(value = "/cardealer/{id}/valuations",method = RequestMethod.GET)
    public ResultBean getCarDealerValuations(@PathVariable("id") String id){
        return iCarValuationBizService.actFindCarValuationsByCarDealerId(id);
    }

    /**
     * 【PAD-API】-- 获取评估列表(通过经销商ID,评估单Id）
     * 业务调整时单独使用
     */
    @RequestMapping(value = "/cardealer/{id}/{vid}/valuations",method = RequestMethod.GET)
    public ResultBean getCarDealerValuationsOnBusinessExchange(@PathVariable("id") String id,@PathVariable String vid){
        return iCarValuationBizService.actGetCarValuationsOnBusinessExchange(id,vid);
    }

    /**
     * 【PAD-API】-- 提交二手车信息并进入评估工作流
     * @return
     */
    @RequestMapping(value = "/valuation",method = RequestMethod.POST)
    public ResultBean submitValuation(@RequestBody ValuationSubmissionBean carInfoQuery){
        return iCarValuationBizService.actSubmitValuation(carInfoQuery);
    }


    /**
     * 【PAD-API】-- 获取评估单
     * @return
     */
    @RequestMapping(value = "/valuation/{id}",method = RequestMethod.GET)
    public ResultBean getValuation(@PathVariable("id") String id){

        return this.getValuationById(id);

    }

    private ResultBean getValuationById(String id) {
        ValuationSubmissionBean carInfoQuery = iCarValuationBizService.actFindValuationById(id).getD();
        if (carInfoQuery == null) {
            return ResultBean.getFailed().setM("评估单ID无效！");
        }

        return ResultBean.getSucceed().setD(carInfoQuery);
    }


    /**
     * 【PAD-API】-- 根据Vin获取一条评估信息
     * @return
     */
    @RequestMapping(value = "/valuation/{vin}/vin",method = RequestMethod.GET)
    public ResultBean getValuationByVin(@PathVariable("vin") String vin){
        return iCarValuationBizService.actGetValuationByVin(vin);
    }

}
