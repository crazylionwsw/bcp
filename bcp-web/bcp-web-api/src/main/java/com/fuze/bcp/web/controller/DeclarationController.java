package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.declaration.DeclarationBean;
import com.fuze.bcp.api.creditcar.bean.declaration.DeclarationResult;
import com.fuze.bcp.api.creditcar.service.IDeclarationBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zqw on 2017/8/5.
 */
@RestController
@RequestMapping("/json")
public class DeclarationController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDemandController.class);

    @Autowired
    private IDeclarationBizService iDeclarationBizService;

    /**
     * 列表带分页
     *
     * @param currenPage
     * @return
     */
    @RequestMapping(value = "/declarations", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DeclarationBean> getDataByPage(@RequestParam(value = "currentPage", defaultValue = "0") Integer currenPage) {
        return iDeclarationBizService.actGetDeclarations(currenPage);
    }

    /**
     * 模糊查询
     * @param declarationBean
     * @param name
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/declaration/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean searchDeclaration(@RequestBody DeclarationBean declarationBean,@RequestParam(value = "name") String name,@RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage){
        return iDeclarationBizService.actSearchDeclaration(declarationBean,name,currentPage);
    }

    /**
     * 根据  银行报批数据ID        查询数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/declaration/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DeclarationBean> getOne(@PathVariable("id") String id) {
        return iDeclarationBizService.actGetDeclaration(id);
    }

    /**
     * 根据   customerTransactionId   查询数据
     *
     * @param id 交易ID
     * @return
     */
    @RequestMapping(value = "/declaration/transaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DeclarationBean> getOneByCustomerTransactionId(@PathVariable("id") String id) {
        return iDeclarationBizService.actGetTransactionDeclaration(id);
    }

    /**
     * 完善银行报批信息
     */
    @RequestMapping(value = "/declaration", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<DeclarationBean> completeDeclaration(@RequestBody DeclarationBean declarationBean) {
        return iDeclarationBizService.actCompleteDeclaration(declarationBean);
    }

    /**
     *   更新银行报批专项分期说明
     * @param id
     * @return
     */
    @RequestMapping(value = "/declaration/{id}/update",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DeclarationBean> updateDeclarationStageApplication(@PathVariable("id") String id){
        return iDeclarationBizService.actUpdateDeclarationStageApplication(id);
    }

    /**
     * 报批 银行报批
     */
    @RequestMapping(value = "/declaration/{id}/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<DeclarationBean> submit(@PathVariable("id") String id) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return iDeclarationBizService.actSubmitDeclaration(id, jwtUser.getId());
    }

    //  保存银行反馈
    @RequestMapping(value = "/declaration/{id}/feedback", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean feedBackDeclaration(@PathVariable("id") String id,@RequestBody DeclarationResult declarationResult) {
        return iDeclarationBizService.actSaveDeclarationFeedBack(id, declarationResult);
    }

    @RequestMapping(value = "/declaration/{id}/historys",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDeclarationHistorys(@PathVariable("id") String transactionId){
        return iDeclarationBizService.actGetDeclarationHistorys(transactionId);
    }
}
