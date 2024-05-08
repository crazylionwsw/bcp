package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.EnhancementBean;
import com.fuze.bcp.api.creditcar.service.IEnhancementBizService;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by gqr on 2017/8/18.
 */
@RestController
@RequestMapping(value = "/json")
public class EnhancementController {

    @Autowired
    private IEnhancementBizService iEnhancementBizService;

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/enhancement/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<EnhancementBean> getOne(@PathVariable("id") String id) {
        return iEnhancementBizService.actGetEnhancement(id);
    }

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/enhancement/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<EnhancementBean> sign(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iEnhancementBizService.actSignEnhancement(id,signInfo);
    }

    @RequestMapping(value = "/enhancements/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<EnhancementBean> searchEnhancements(@RequestBody SearchBean searchBean) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iEnhancementBizService.actSearchEnhancements(userId, searchBean);
    }

    /**
     * 保存 资质信息---用于PC端发起资料补全请求
     */
    @RequestMapping(value = "/enhancement", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<EnhancementBean> save(@RequestBody EnhancementBean enhancementBean) {
        return iEnhancementBizService.actCreateEnhancement(enhancementBean);
    }

    /**
     *      根据     客户交易ID       查询资质审查数据
     * @param id    客户交易ID
     * @return
     */
    @RequestMapping(value = "/enhancement/transaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<EnhancementBean> getOneByTransactionId(@PathVariable("id") String id) {
        return iEnhancementBizService.actGetByCustomerTransactionId(id);
    }

    @RequestMapping(value = "/enhancement/{id}/images", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<CustomerImageFileBean>> getEnhancementImages(@PathVariable("id") String id) {
        return iEnhancementBizService.actGetEnhancementImages(id);
    }


}
