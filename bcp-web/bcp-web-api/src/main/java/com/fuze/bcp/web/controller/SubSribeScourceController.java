package com.fuze.bcp.web.controller;


import com.fuze.bcp.api.msg.bean.SubSribeSourceBean;
import com.fuze.bcp.api.msg.service.ISubscribeBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/json")
public class SubSribeScourceController {

    @Autowired
    private ISubscribeBizService iSubscribeBizService;


    /**
     * 获取订阅源列表(带分页)
     *
     * @return
     */
    @RequestMapping(value = "/subsribesources", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSbuSribeSources(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iSubscribeBizService.actGetSubscribeSources(currentPage);
    }

    /**
     * 保存订阅源
     * @param subSribeSourceBean
     * @return
     */
    @RequestMapping(value = "/subsribesource", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveSubSribeSource(@RequestBody SubSribeSourceBean subSribeSourceBean) {
        return iSubscribeBizService.actSaveSubScribeSource(subSribeSourceBean);
    }

    /**
     *删除订阅源
     * @param id
     * @return
     */
    @RequestMapping(value = "/subsribesource/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteSubSribeSource(@PathVariable("id") String id){
        return iSubscribeBizService.actDeleteSubSribeSource(id);
    }

    /**
     * 获取可用的订阅源
     * @return
     */
    @RequestMapping(value = "/subsribesources/lookups",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupSubSribeSource(){
        return iSubscribeBizService.actLookupSubSribeSources();
    }

}
