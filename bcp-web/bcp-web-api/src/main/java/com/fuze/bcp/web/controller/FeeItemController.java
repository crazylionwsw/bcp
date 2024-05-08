package com.fuze.bcp.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.bd.bean.FeeItemBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/json")
public class FeeItemController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;

    /**
     * 获取收费项列表(带分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/feeitems", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetFeeItems(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iBaseDataBizService.actGetFeeItems(currentPage);
    }


    /**
     * 收费项列表(仅可用数据)
     *
     * @return
     */
    @RequestMapping(value = "/feeitems/lookups", method = RequestMethod.GET)
    public ResultBean actLookupFeeItems() {
        return iBaseDataBizService.actLookupFeeItems();
    }


    /**
     * 保存收费项类型
     *
     * @param feeItem
     * @return
     */
    @RequestMapping(value = "/feeitem", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean actSaveFeeItem(@RequestBody FeeItemBean feeItem) {
        return iBaseDataBizService.actSaveFeeItem(feeItem);
    }


    /**
     * 删除收费项类型
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/feeitem/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean actDeleteFeeItem(@PathVariable("id") String id) {
        return iBaseDataBizService.actDeleteFeeItem(id);
    }

    /**
     *    根据  ID  回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/feeitem/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetFeeItem(@PathVariable("id") String id) {
        return iBaseDataBizService.actGetFeeItem(id);
    }
}
