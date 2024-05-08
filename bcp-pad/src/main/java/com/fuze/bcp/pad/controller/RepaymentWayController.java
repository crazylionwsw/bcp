package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.bean.RepaymentWayBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/json")
public class RepaymentWayController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;


    /**
     * 获取还款方式所有数据(带分页)
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/repaymentways",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getRepaymentWays(@RequestParam(value = "currentPage",defaultValue = "0")Integer currentPage){
        return iBaseDataBizService.actGetRepaymentWays(currentPage);
    }

    /**
     * 获取还款方式(可用数据)
     * @return
     */
    @RequestMapping(value = "/repaymentways/lookups",method = RequestMethod.GET)
    public ResultBean lookupRepaymentWays(){
        return iBaseDataBizService.actLookupRepaymentWays();
    }

    /**
     * 保存还款方式
     * @param repaymentWay
     * @return
     */
    @RequestMapping(value = "/repaymentway",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveRepaymentWay(@RequestBody RepaymentWayBean repaymentWay){
        return iBaseDataBizService.actSaveRepaymentWay(repaymentWay);
    }

    /**
     * 删除还款方式
     * @param id
     * @return
     */
    @RequestMapping(value = "/repaymentway/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteRepaymentWay(@PathVariable("id") String id){
        return iBaseDataBizService.actDeleteRepaymentWay(id);
    }
}
