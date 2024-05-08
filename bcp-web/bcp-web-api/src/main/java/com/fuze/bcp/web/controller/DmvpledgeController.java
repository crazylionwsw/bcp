package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.DMVPledgeBean;
import com.fuze.bcp.api.creditcar.service.IDmvpledgeBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by GQR on 2017/8/24.
 */
@RestController
@RequestMapping(value = "/json")
public class DmvpledgeController extends BaseController {


    @Autowired
    private IDmvpledgeBizService iDmvpledgeBizService;


    /**
     * 分页列表
     * @param currentPage
     * @param status
     * @return
     */
    @RequestMapping(value = "/dmvpledges", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDmvpledges(@RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage,
                                    @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {
        return iDmvpledgeBizService.actGetDMVPledges(currentPage,status);
    }

    /**
     * 模糊查询列表
     */
    @RequestMapping(value = "/dmvpledges/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<DMVPledgeBean> searchReturnInfo(@RequestBody SearchBean searchBean) {
        return iDmvpledgeBizService.actSearchPromoteDMVPledges(searchBean);
    }

    /**
     * 根据ID回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/dmvpledge/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DMVPledgeBean> getOne(@PathVariable("id") String id){
        return iDmvpledgeBizService.actGetDmvpledge(id);
    }

    /**
     *  通过交易ID查询车辆抵押数据
     * @param transactionId
     * @return
     */
    @RequestMapping(value = "/dmvpledge/transaction/{transactionId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DMVPledgeBean> getOneByCustomerTransactionId(@PathVariable("transactionId") String transactionId){
        return iDmvpledgeBizService.actGetDmvpledgeByCustomerTransactionId(transactionId);
    }

    @RequestMapping(value = "/dmvpledge", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<DMVPledgeBean> save(@RequestBody DMVPledgeBean dmvPledgeBean){
        return iDmvpledgeBizService.actSaveDMVPledge(dmvPledgeBean);
    }

    /**
     * 完成当前任务
     * @param dmvPledgeBean
     * @returnbank
     */
    @RequestMapping(value = "/dmvpledge/sign",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<DMVPledgeBean> approvedDmvpledge(@RequestBody DMVPledgeBean dmvPledgeBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        String loginUserId = jwtUser.getId();
        return iDmvpledgeBizService.actApprovedDmvpledge(dmvPledgeBean,loginUserId);
    }


}

