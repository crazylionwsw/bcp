package com.fuze.bcp.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.bd.bean.CompensatoryPolicyBean;
import com.fuze.bcp.api.bd.bean.CompensatoryPolicyFormulaBean;
import com.fuze.bcp.api.bd.service.IProductBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class CompensatoryPolicyController {

    @Autowired
    private IProductBizService iProductBizService;

    /**
     * 获取贴息产品数据(带分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/compensatorypolicys", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCompensatoryPolicies(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iProductBizService.actGetCompensatoryPolicies(currentPage);
    }

    /**
     * 保存贴息产品数据
     *
     * @param compensatoryPolicy
     * @return
     */
    @RequestMapping(value = "/compensatorypolicy", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean save(@RequestBody CompensatoryPolicyBean compensatoryPolicy) {
        return iProductBizService.actSaveCompensatoryPolicy(compensatoryPolicy);
    }

    /**
     * 删除贴息产品
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/compensatorypolicy/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean delete(@PathVariable("id") String id) {
        return iProductBizService.actDeleteCompensatoryPolicy(id);
    }

    /**
     * 获取贴息数据(仅可用)
     *
     * @return
     */
    @RequestMapping(value = "/compensatorypolicys/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupCompensatoryPolicies() {
        return iProductBizService.actLookupCompensatoryPolicies();
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/compensatorypolicy/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOneCompensatoryPolicies(@PathVariable("id") String id) {
        return iProductBizService.actGetCompensatoryPolicy(id);
    }

    /***
     * 贴息方式 (列表)
     */
    @RequestMapping(value = "/compensatorypolicy/formulas",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCompensatoryPolicyFormulas(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage){
        return iProductBizService.actGetFormulas(currentPage);
    }

    /**
     *保存贴息公式
     */
    @RequestMapping(value = "/compensatorypolicy/formula",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCompensatoryPolicyFormula(@RequestBody CompensatoryPolicyFormulaBean compensatoryPolicyFormulaBean){
        return iProductBizService.actSaveFormula(compensatoryPolicyFormulaBean);
    }

    /*
    *删除贴息公式
     */
    @RequestMapping(value = "/compensatorypolicy/formula/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean delateCompensatoryPolicyFormula(@PathVariable("id") String id){
        return iProductBizService.actDeleteFormula(id);
    }

    /**
     * 获取单条
     */
    @RequestMapping(value = "/compensatorypolicy/formula/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCompensatoryPolicyFormula(@PathVariable("id") String id){
        return iProductBizService.actGetFormula(id);
    }

    /**
     * 贴息列表
     * @return
     */
    @RequestMapping(value = "/formulas",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCompensatoryPolicyFormulas(){
        return iProductBizService.actGetFormulas();
    }
}
