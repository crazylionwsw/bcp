package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.service.ICashSourceBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/json")
public class CashSourceController {

    @Autowired
    private ICashSourceBizService iCashSourceBizService;

    /**
     * 【PAD API】--根据主键获取资金来源的信息
     *
     * @return
     */
    @RequestMapping(value = "/cashsource/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAllCashSourceType(@PathVariable("id") String id) {
        return iCashSourceBizService.actGetCashSource(id);
    }

    /**
     * 【PAD API】--获取渠道来源中支行信息接口
     *              当前只获取北京分行的支行
     * @param type type 1 : 报单行  0 取全部
     * @return
     */

    @RequestMapping(value = "/cashsources", method = RequestMethod.GET)
    public ResultBean getCashSourceList(@RequestParam(value = "type", required = false, defaultValue = "0") Integer type) {
        if (type == null) {
            type = 0;
        }
        //弃用
//        if (type == 1) {
//            return iCashSourceBizService.actGetCashSourceBanks();
//        } else {
//            return iCashSourceBizService.actGetCashSources();
//        }
        // 北京分行的code
        String YH_GSYH_BJ_CODE = "YH-GSYH-BJ";
        List<CashSourceBean> cashSourceBeanList = iCashSourceBizService.actFindChildBank(YH_GSYH_BJ_CODE).getD();
        if(cashSourceBeanList.size() == 0){
            return ResultBean.getFailed().setM("获取的资金来源为空");
        }
        List<CashSourceBean> cashSources = new ArrayList<CashSourceBean>();
        for (CashSourceBean cashSource: cashSourceBeanList) {
            if (!cashSource.getCode().equals(YH_GSYH_BJ_CODE)) {
                cashSources.add(cashSource);
            }
        }
        return ResultBean.getSucceed().setD(cashSources);
    }

}
