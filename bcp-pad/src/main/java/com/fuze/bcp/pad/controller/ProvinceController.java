package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.bean.ProvinceBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/json")
public class ProvinceController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;


    /**
     * 获取下级地区列表（parentId为0时表示获取顶级地区列表
     * @param id
     * @return
     */
    @RequestMapping(value = "/province/{id}/provinces",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getProvinces(@PathVariable("id") String id){
        return iBaseDataBizService.actGetChildProvinces(id);
    }


    /**
     * 获取地区列表(仅可用数据)
     * @return
     */
    @RequestMapping(value = "/provinces/lookups",method = RequestMethod.GET)
    public  ResultBean lookupProvinces(){
        return iBaseDataBizService.actLookupProvinces();
    }


    /**
     * 保存地区
     * @param province
     * @return
     */
    @RequestMapping(value = "/province",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveProvince(@RequestBody ProvinceBean province){
        return  iBaseDataBizService.actSaveProvince(province);
    }


    /**
     * 删除地区
     * @param id
     * @return
     */
    @RequestMapping(value = "/province/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public  ResultBean deleteProvince(@PathVariable("id") String id){
        return  iBaseDataBizService.actDeleteProvince(id);
    }



}
