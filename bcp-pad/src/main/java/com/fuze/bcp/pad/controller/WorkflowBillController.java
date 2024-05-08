package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.workflow.bean.BillSignInfo;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by admin on 2017/5/27.
 */
@RestController
@RequestMapping(value = "/json")
public class WorkflowBillController {

    @Autowired
    private IWorkflowBizService iWorkflowBizService;


    /**
     * 获取单据的签批信息
     *
     * @return
     */
    @RequestMapping(value = "/{flowcode}/{sourceid}/signInfos", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<SignInfo>> getWorkfolwBillByFlowCodeAndSourceId(@PathVariable("flowcode") String flowCode, @PathVariable("sourceid") String sourceId) {
        return iWorkflowBizService.actGetSingInfosByFlowCodeAndSourceId(flowCode, sourceId);
    }

    /**
     * 获取交易的签批信息
     *
     * @return
     */
    @RequestMapping(value = "/transaction/{id}/signinfos", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<BillSignInfo>> getTransactionSignInfos(@PathVariable("id") String id) {
        List<BillSignInfo> billSignInfos = iWorkflowBizService.actGetSingInfosByTransaction(id).getD();

        if (billSignInfos == null || billSignInfos.isEmpty()) {
            return ResultBean.getFailed().setM("暂无签批信息");
        } else {
            return ResultBean.getSucceed().setD(billSignInfos);
        }
    }

    /**
     * 发送交易的签批信息
     *
     * @return
     */
    @RequestMapping(value = "/{flowcode}/{sourceid}/signInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveSignInfo(@PathVariable("flowcode") String flowCode, @PathVariable("sourceid") String sourceId, @RequestBody SignInfo signInfo) {
        return iWorkflowBizService.actSaveWorkflowBillBySourceIdAndFlowCode(flowCode, sourceId, signInfo);
    }


}
