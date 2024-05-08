package com.fuze.bcp.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.bean.CashSourceEmployeeBean;
import com.fuze.bcp.api.bd.bean.SourceRateBean;
import com.fuze.bcp.api.bd.service.ICashSourceBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/json")
public class CashSourceController {

    @Autowired
    private ICashSourceBizService iCashSourceBizService;

    /**
     * 获取资金提供方类型
     *
     * @return
     */
    @RequestMapping(value = "/cashsources/types", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAllCashSourceType() {
        return iCashSourceBizService.actGetAllCashSourceType();
    }

    /**
     * 保存资金提供方
     *
     * @param cashSourceBean
     * @return
     */

    @RequestMapping(value = "/cashsource", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCashSource(@RequestBody CashSourceBean cashSourceBean) {
        return iCashSourceBizService.actSaveCashSource(cashSourceBean);
    }


    /**
     * 删除资金提供方
     *
     * @param id
     * @return
     */

    @RequestMapping(value = "/cashsource/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCashSource(@PathVariable("id") String id) {
        return iCashSourceBizService.actDeleteCashSource(id);
    }

    /**
     * 获取可用资金提供方
     *
     * @return
     */
    @RequestMapping(value = "/cashsources/lookups", method = RequestMethod.GET)
    public ResultBean lookupCashSources() {
        return iCashSourceBizService.actLookupCashSources();
    }


    /**
     * 获取下级资金提供方
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cashsource/{id}/cashsources", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCashSources(@PathVariable("id") String id) {
        return iCashSourceBizService.actGetCashSources(id);
    }


    /**
     * 获取单条资金提供方信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cashsource/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCashSource(@PathVariable("id") String id) {
        return iCashSourceBizService.actGetCashSource(id);
    }

    /**
     * 获取所有的支行
     * @return
     */
    @RequestMapping(value = "/cashsources/childBanks", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<CashSourceBean>> getCashSourceChild() {
        // 北京分行的code
        String YH_GSYH_BJ_CODE = "YH-GSYH-BJ-";
        return iCashSourceBizService.actFindChildBank(YH_GSYH_BJ_CODE);
    }

    /********************************************************协作用户********************************************************/
    /**
     * 保存协作用户
     *
     * @param cashSourceEmployee
     * @return
     */
    @RequestMapping(value = "/cashsource/employee", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCashSourceEmployee(@RequestBody CashSourceEmployeeBean cashSourceEmployee) {
        return iCashSourceBizService.actSaveCashSourceEmployee(cashSourceEmployee);
    }


    /**
     * 删除协作用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cashsource/employee/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCashSourceEmployee(@PathVariable("id") String id) {
        return iCashSourceBizService.actDeleteCashSourceEmployee(id);
    }

    /**
     * 获取指定资金机构的协作用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cashsource/{id}/employees", method = RequestMethod.GET)
    public ResultBean getCashSourceEmployees(@PathVariable("id") String id) {
        return iCashSourceBizService.actGetCashSourceEmployees(id);
    }

    /**
     * 获取单个协作用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cashsource/employee/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCashSourceEmployee(@PathVariable("id") String id) {
        return iCashSourceBizService.actGetCashSourceEmployee(id);
    }

    //获取所有协作用户
    @RequestMapping(value = "/cashsource/employees/avaliable",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAvaliableCashSourceEmployee(){
        return iCashSourceBizService.actGetAvaliableCashSourceEmployee();
    }

    /********************************************************资金利率********************************************************/

    /**
     * 保存资金利率
     *
     * @param sourceRateBean
     * @return
     */
    @RequestMapping(value = "/cashsource/sourcerate", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveSourceRate(@RequestBody SourceRateBean sourceRateBean) {
        return iCashSourceBizService.actSaveSourceRate(sourceRateBean);
    }


    /**
     * 删除资金利率
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cashsource/sourcerate/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteSourceRate(@PathVariable("id") String id) {
        return iCashSourceBizService.actDeleteSourceRate(id);
    }


    /**
     * 获取指定资金机构的资金利率列表
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cashsource/{id}/sourcerates", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSourceRates(@PathVariable("id") String id) {
        return iCashSourceBizService.actGetSourceRates(id);
    }

    /**
     * 获取可用的资金利率
     *
     * @return
     */
    @RequestMapping(value = "/cashsource/sourcerates", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupSourceRates() {
        return iCashSourceBizService.actLookupSourceRates();
    }

    /*
       * 获取单条资金利率
       * @param id
       * @return
      */
    @RequestMapping(value = "/cashsource/sourcerate/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSourceRate(@PathVariable("id") String id) {
        return iCashSourceBizService.actGetSourceRate(id);
    }
}
