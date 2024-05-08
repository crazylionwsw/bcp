package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/json")
public class BillTypeController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;

    /**
     * 获取单据类型列表(带分页，升序排列)
     * @param currentPage
     * @return
     */
    @RequestMapping( value = "/billtypes",method = RequestMethod.GET)
    public ResultBean getBillTypes(@RequestParam(value = "currentPage",defaultValue = "0")Integer currentPage){
        return iBaseDataBizService.actGetBillTypeOrderBy(currentPage);
    }

    /**
     * 获取可用单据类型
     * @return
     */
    @RequestMapping(value = "/billtypes/lookups",method = RequestMethod.GET)
    public ResultBean lookupBillTypes(){
        return iBaseDataBizService.actLookupBillTypes();
    }

    /**
     * 保存单据类型
     * @param billType
     * @return
     */
    @RequestMapping(value = "/billtype",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveBillType(@RequestBody BillTypeBean billType){
        return iBaseDataBizService.actSaveBillType(billType);
    }

    /**
     * 删除单据类型
     * @param id
     * @return
     */
    @RequestMapping(value = "/billtype/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteBillType(@PathVariable("id") String id){
        return iBaseDataBizService.actDeleteBillType(id);
    }

    /**
     *      通过  单据类型编码  获取到 单据数据
     * @param code
     * @return
     */
    @RequestMapping(value = "/billtype/{code}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<BillTypeBean> actGetBillType(@PathVariable("code") String code){
        return iBaseDataBizService.actGetBillType(code);
    }

}
