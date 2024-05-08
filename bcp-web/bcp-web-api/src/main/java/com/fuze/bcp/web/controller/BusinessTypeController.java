package com.fuze.bcp.web.controller;


import com.fuze.bcp.api.bd.bean.BusinessTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/json")
public class BusinessTypeController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;


    /**
     * 获取业务类型列表(带分页)
     *
     * @return
     */
    @RequestMapping(value = "/businesses", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getBusinessTypes(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iBaseDataBizService.actGetBusinessTypes(currentPage);
    }

    /**
     * 获取可用的业务类型列表
     *
     * @return
     */
    @RequestMapping(value = "/businesstypes/lookups", method = RequestMethod.GET)
    public ResultBean lookupBusinessTypes() {
        return iBaseDataBizService.actLookupBusinessTypes();
    }

    /**
     * 获取可用的业务类型列表
     *
     * @return
     */
    @RequestMapping(value = "/businesstypes/lookup/rates", method = RequestMethod.GET)
    public ResultBean lookupBusinessRates() {
        return iBaseDataBizService.actLookupBusinessRates();
    }

    /**
     * 保存业务类型
     *
     * @param businessType
     * @return
     */
    @RequestMapping(value = "/businesstype", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveBusinessType(@RequestBody BusinessTypeBean businessType) {
        return iBaseDataBizService.actSaveBusinessType(businessType);
    }

    /**
     * 删除业务类型
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/businesstype/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteBusinessType(@PathVariable("id") String id) {
        return iBaseDataBizService.actDeleteBusinessType(id);
    }

    @RequestMapping(value = "/businesstype/pickup", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getBussinessByCodes(@RequestBody List<String> codes){
        return iBaseDataBizService.actGetBusinessByCodes(codes);
    }

}
