package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.decompressBill.DecompressBillBean;
import com.fuze.bcp.api.creditcar.bean.overdueRecord.OverdueRecordBean;
import com.fuze.bcp.api.creditcar.service.IOverdueRecordBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ${Liu} on 2018/3/10.
 * 逾期记录
 */
@RestController
@RequestMapping(value = "/json")
public class OverdueRecordController {

    @Autowired
    IOverdueRecordBizService iOverdueRecordBizService;

    /**
     *获取逾期记录(含查询)
     */
    @RequestMapping(value = "/overduerecord/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<OverdueRecordBean> searchOverdueRecord(@RequestBody SearchBean searchBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iOverdueRecordBizService.actSearchOverdueRecord(userId, searchBean);
    }

    /**
     *获取逾期记录
     */
    @RequestMapping(value = "/overduerecord/{id}/{dataStatus}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOverdueRecord(@PathVariable("id")String overduerecordId,@PathVariable("dataStatus")Integer dataStatus ) {
        return iOverdueRecordBizService.actGetOverdueRecord(overduerecordId, dataStatus);
    }

    /**
     *保存逾期记录
     */
    @RequestMapping(value = "/overduerecord", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveOverdueRecord(@RequestBody OverdueRecordBean overdueRecordBean) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iOverdueRecordBizService.actSaveOverdueRecord(overdueRecordBean,userId);
    }

    /**
     * 发送逾期提醒
     */
    @RequestMapping(value = "/send/overduerecord/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean sendOverdueRecord(@PathVariable("id")String overduerecordId) {
        return iOverdueRecordBizService.actSendRemindMsg(overduerecordId);
    }

    /**
     * 查询当月是否已有记录
     */
    @RequestMapping(value = "/overduerecord/month", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOverdueRecordByMonth(@RequestParam String transactionId,@RequestParam String month) {
        return iOverdueRecordBizService.actGetOverdueRecordByMonth(transactionId,month);
    }

    /**
     *创建逾期记录
     */
    @RequestMapping(value = "/overduerecord/create/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean createOverdueRecord(@PathVariable("id") String transactionId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
       return iOverdueRecordBizService.actCreateOverdueRecord(transactionId,userId);
    }

    /**
     *根据交易获取逾期记录次数
     * @param customerTransactionId
     * @return
     */
    @RequestMapping(value = "/overduerecord/transaction/{customerTransactionId}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOverduerecordByTransaction(@PathVariable("customerTransactionId")String customerTransactionId){
        return iOverdueRecordBizService.actGetOverdueRecordCount(customerTransactionId);
    }

}
