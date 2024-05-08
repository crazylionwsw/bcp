package com.fuze.bcp.app.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.bd.bean.EmployeeLookupBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppAppointSwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.service.IAppointSwipingCardBizService;
import com.fuze.bcp.api.creditcar.service.IBankCardApplyBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * app预约刷卡审批接口
 * Created by Lily on 2017/12/26.
 */
@RestController
@RequestMapping(value = "/json/app", method = {RequestMethod.GET, RequestMethod.POST})
public class AppointSwipingCardController {

    @Autowired
    IAppointSwipingCardBizService iAppointSwipingCardBizService;

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @RequestMapping(value = "/appointswipingcard/{tid}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<AppAppointSwipingCardBean> signAppointPayment(@PathVariable("tid") String tid) {
        return iAppointSwipingCardBizService.actGetAppAppointSwipingCardByCustomerTransactionId(tid);
    }

    @RequestMapping(value = "/appointswipingcard/{tid}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<AppointSwipingCardBean> signAppointPayment(@PathVariable("tid") String tid, @RequestBody SignInfo signInfo,
                                                                  @RequestParam(value = "receiveCardName",required = false,defaultValue = "")String receiveCardName) {
        AppointSwipingCardBean appointSwipingCardBean = iAppointSwipingCardBizService.actGetAppointSwipingCardByCustomerTransactionId(tid).getD();
            if(appointSwipingCardBean != null && appointSwipingCardBean.getApproveStatus() == ApproveStatus.APPROVE_ONGOING){
                if(!receiveCardName.equals("")){
                    //保存领卡人
                    BankCardApplyBean bankCardApplyBean = iBankCardApplyBizService.actsaveReceiveCardName(tid, receiveCardName).getD();
                }
                JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
                String loginUserId = jwtUser.getId();
                EmployeeLookupBean employee = iOrgBizService.actGetEmployeeByLogin(loginUserId).getD();
                if(employee != null){
                    signInfo.setEmployeeId(employee.getId());
                }
                signInfo.setUserId(loginUserId);
                return iAppointSwipingCardBizService.actSignAppointSwipingCard(appointSwipingCardBean.getId(),signInfo);
            }else{
                return ResultBean.getFailed().setM("操作任务失败，单据不存在！");
            }
    }
}
