package com.fuze.bcp.web.controller;

/*import com.fuze.bcp.api.workflow.bean.GroupBean;
import com.fuze.bcp.api.workflow.service.IWorkflowService;*/


import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.workflow.bean.GroupBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.TaskBean;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.APIBillBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by admin on 2017/5/27.
 */
@RestController
@RequestMapping(value = "/json/workflow")
public class WorkflowController {
    @Autowired
    private IWorkflowBizService iWorkflowBizService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;


    /**
     * 通过单据ID获取工作流对象
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/bill/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<WorkFlowBillBean> actGetBillWorkflow(@PathVariable("id") String id) {

        return iWorkflowBizService.actGetBillWorkflow(id);
    }

    /**
     * 通过单据ID获取工作流对象
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/bill/temsign/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<WorkFlowBillBean> actGetBillWorkflowTEM(@PathVariable("id") String id) {

        return iWorkflowBizService.actGetBillWorkflowTEM(id);
    }

    @RequestMapping(value = "/bill/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<WorkFlowBillBean> actSaveBillWorkflow(@PathVariable("id") String id, @RequestBody SignInfo signInfo) {
        try {
            return iWorkflowBizService.actSignBill(id, signInfo);
        } catch (Exception e) {
            return ResultBean.getFailed().setM("签批失败");
        }
    }

    @RequestMapping(value = "/bill/reset", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<WorkFlowBillBean> resetBillWorkflow(@RequestBody APIBillBean baseBill) {
        try {
            return iWorkflowBizService.actReset(baseBill);
        } catch (Exception e) {
            return ResultBean.getFailed().setM("签批失败");
        }
    }

    /**
     * 获取当前任务
     */
    @RequestMapping(value = "/currentgroup", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTaskCurrentGroup(@RequestParam(value = "businessKey") String businessKey) {
        List<TaskBean> taskBeen = iWorkflowBizService.actGetBillTasks(businessKey).getD();
        if (taskBeen != null && taskBeen.size() > 0) {
            return ResultBean.getSucceed().setD(taskBeen.get(0).getName());
        }
        return ResultBean.getFailed().setM("当前任务为空!");
    }

    /**
     * 获取当前任务列表
     */
    @RequestMapping(value = "/currentgroups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTaskCurrentGroups(@RequestParam(value = "businessKey") String businessKey) {
        List<TaskBean> taskBeen = iWorkflowBizService.actGetBillTasks(businessKey).getD();
        if (taskBeen != null && taskBeen.size() > 0) {
            return ResultBean.getSucceed().setD(taskBeen);
        }
        return ResultBean.getFailed().setM("当前任务为空!");
    }

    /**
     * 我的任务
     *
     * @return
     */
    @RequestMapping(value = "/mytask", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getMyTasks(@RequestParam(value = "userid") String userid,
                                 @RequestParam(value = "businessKey") String businessKey) {

        LoginUserBean loginUser = iAuthenticationBizService.actGetLoginUser(userid).getD();
        String activitiUserId = loginUser.getActivitiUserId();

        return iWorkflowBizService.actGetUserBillTask(activitiUserId, businessKey);
    }

    /**
     * 获取全部工作流角色
     *
     * @return
     */
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<GroupBean>> getGroups() {
        return iWorkflowBizService.actGetAllGroups();
    }

    /**
     * 给用户分配工作流角色
     *
     * @return
     */
    @RequestMapping(value = "/user/{username}/groups", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean assignGroups(@PathVariable("username") String username, @RequestBody List<String> groupIds) {
        return iWorkflowBizService.actAssignUserGroups(username, groupIds);
    }


    /**
     * 获取用户工作流角色
     *
     * @return
     */
    @RequestMapping(value = "/user/{username}/groups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<GroupBean>> getUserGroups(@PathVariable("username") String username) {
        return iWorkflowBizService.actGetUserGroups(username);
    }

    @RequestMapping(value = "/user/groups/{activitiuserid}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<GroupBean>> getUserGroupsByActivitiUserId(@PathVariable("activitiuserid") String activitiuserid) {
        return iWorkflowBizService.actGetUserGroupsByActivitiUserId(activitiuserid);
    }

    /**
     * 根据单据ID  简单保存
     *
     * @param id
     * @param signInfo
     * @return
     */
    @RequestMapping(value = "/bill/{id}/comment", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<WorkFlowBillBean> actSaveWorkflowBill(@PathVariable("id") String id, @RequestBody SignInfo signInfo) {
        try {
            return iWorkflowBizService.actSaveWorkflowBill(id, signInfo);
        } catch (Exception e) {
            return ResultBean.getFailed().setM("签批失败");
        }
    }

    /**
     * TODO:     重走流程
     *
     * @return
     */
    @RequestMapping(value = "/bill/{businessTypeCode}/{billTypeCode}/{billId}/again", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<WorkFlowBillBean> actAgainWorkFlowBill(@PathVariable("businessTypeCode") String businessTypeCode,
                                                             @PathVariable("billTypeCode") String billTypeCode,
                                                             @PathVariable("billId") String billId,
                                                             @RequestBody SignInfo signInfo) {
        WorkFlowBillBean w = iWorkflowBizService.actGetBillWorkflow(billId).getD();
        if (w == null) {
            return ResultBean.getFailed().setM("");
        }
        return iWorkflowBizService.actSubmit(businessTypeCode, billId, billTypeCode, signInfo, w.getCollectionName(), null, w.getTransactionId());
    }

}
