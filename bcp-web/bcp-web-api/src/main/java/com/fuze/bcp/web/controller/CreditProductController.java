package com.fuze.bcp.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.bd.bean.CreditProductBean;
import com.fuze.bcp.api.bd.service.IProductBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/json")
public class CreditProductController {

    @Autowired
    private IProductBizService iProductBizService;


    /**
     * 获取分期产品(带分页)
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/creditproducts",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCreditProducts(@RequestParam(value = "currentPage",defaultValue = "0")Integer currentPage){
        return iProductBizService.actGetCreditProducts(currentPage);
    }

    /**
     *获取分期产品信息(仅可用数据)
     * @return
     */
    @RequestMapping(value = "/creditproducts/lookups",method = RequestMethod.GET)
    @ResponseBody
    public  ResultBean lookupCreditProducts(){
        return  iProductBizService.actLookupCreditProducts();
    }

    /**
     * 保存分期产品
     * @param creditProduct
     * @return
     */
    @RequestMapping(value = "/creditproduct",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCreditProduct(@RequestBody CreditProductBean creditProduct){
        return  iProductBizService.actSaveCreditProduct(creditProduct);
    }

    /**
     * 删除分期产品
     * @return
     */
    @RequestMapping(value = "/creditproduct/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCreditProduct(@PathVariable("id") String id){
        return iProductBizService.actDeleteCreditProduct(id);
    }

    /**
     * 获取分期产品
     * @return
     */
    @RequestMapping(value = "/creditproduct/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCreditProduct(@PathVariable("id") String id){
        return iProductBizService.actGetCreditProduct(id);
    }

}
