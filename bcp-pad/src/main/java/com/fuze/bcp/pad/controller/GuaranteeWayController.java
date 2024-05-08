package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.bean.GuaranteeWayBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/json")
public class GuaranteeWayController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;


    /**
     * 获取担保方式所有数据(带分页)
     * @return
     */
    @RequestMapping(value = "/guaranteeways",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getGuaranteeWays(@RequestParam(value = "currentPage",defaultValue = "0")Integer currentPage){
        return  iBaseDataBizService.actGetGuaranteeWays(currentPage);
    }

    /**
     * 获取担保方式的可用数据
     * @return
     */
    @RequestMapping(value = "/guaranteeways/lookups",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupGuaranteeWays(){
        return iBaseDataBizService.actLookupGuaranteeWays();
    }

    /**
     * 保存担保方式
     * @param guaranteeWay
     * @return
     */
    @RequestMapping(value = "/guaranteeway",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveGuaranteeWay(@RequestBody GuaranteeWayBean guaranteeWay){
        return iBaseDataBizService.actSaveGuaranteeWay(guaranteeWay);
    }

    /**
     *删除担保方式
     * @param id
     * @return
     */
    @RequestMapping(value = "/guaranteeway/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteGuaranteeWay(@PathVariable("id") String id){
        return  iBaseDataBizService.actDeleteGuaranteeWay(id);
    }
}
