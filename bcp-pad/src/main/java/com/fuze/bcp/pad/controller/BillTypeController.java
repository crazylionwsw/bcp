package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/json")
public class BillTypeController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;

    @Autowired
    private ICustomerImageFileBizService iCustomerImageFileBizService;


    /**
     * 【PAD - API】--获取档案类型列表
     *
     * @param billTypeCode
     * @param billid       暂时无用
     * @return
     */
    @RequestMapping(value = "/{billtype}/imagetypes", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getBillTypeImageTypes(@PathVariable("billtype") String billTypeCode, @RequestParam(value = "billid", required = false) String billid,@RequestParam(value = "bizcode", required = false) String bizCode) {
        ResultBean<List<CustomerImageTypeBean>> resultBean = iBaseDataBizService.actGetCustomerImageTypesByBillType(billTypeCode,bizCode);
        if (resultBean.getC() == ResultBean.SUCCEED) {
            List<CustomerImageTypeBean> list = resultBean.getD();
            return ResultBean.getSucceed().setD(list);
        } else {
            return resultBean;
        }

    }

    /**
     * 【PAD - API】--获取客户的档案类型列表
     *
     * @param customerId
     * @param transactionId
     * @param billTypeCode
     * @return
     */
    @RequestMapping(value = "/imagetypes", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerImageTypes(@RequestParam(value = "customerid") String customerId, @RequestParam(value = "transactionid") String transactionId, @RequestParam(value = "billtypecode") String billTypeCode) {
        return iCustomerImageFileBizService.actGetBillImageTypesWithBillTypeCode(customerId, transactionId, billTypeCode);

    }

}
