package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.bd.bean.BusinessEventTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by CJ on 2017/8/10.
 */
@RestController
@RequestMapping(value = "/json")
public class BusinessEventTypeController {

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    /**
     * 获取业务事件类型列表
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/businesseventtypes", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DataPageBean<BusinessEventTypeBean>> getBusinessEventTypes(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iBaseDataBizService.actGetBusinessEventTypes(currentPage);
    }

    /**
     * 可用事件类型
     * @return
     */
    @RequestMapping(value = "/businesseventtype/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<BusinessEventTypeBean>> lookupBusinessEventType() {
        return iBaseDataBizService.actLookupBusinessEventTypes();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/businesseventtype/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteBusinessEventType(@PathVariable("id") String id){
        return iBaseDataBizService.actDeleteBusinessEventType(id);
    }

    /**
     * 保存事件类型
     * @param businessEventType
     * @return
     */
    @RequestMapping(value = "/businesseventtype",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveBusinessEventType(@RequestBody BusinessEventTypeBean businessEventType){
        return iBaseDataBizService.actSaveBusinessEventType(businessEventType);
    }

    /**
     *
     * @param businessType
     * @return
     */
    @RequestMapping(value = "/businesseventtype/{businessType}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getBusinessEventType(@PathVariable("businessType") String businessType){
        return  iBaseDataBizService.actGetOneBusinessEventType(businessType);
    };




}
