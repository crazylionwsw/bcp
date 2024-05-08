package com.fuze.bcp.pad.controller;

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
      * 收费项列表(仅可用数据)
      * @return
      */
    @RequestMapping(value = "/feeitems",method = RequestMethod.GET)
    public ResultBean actLookupFeeItems(){
        return iBaseDataBizService.actLookupFeeItems();
    }

}
