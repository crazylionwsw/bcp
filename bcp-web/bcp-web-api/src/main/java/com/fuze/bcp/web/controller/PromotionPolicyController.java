package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.bd.bean.PromotionPolicyBean;
import com.fuze.bcp.api.bd.service.IProductBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class PromotionPolicyController {

    @Autowired
    private IProductBizService iProductBizService;

    /**
     * 获取促销数据(带分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/promotionpolicys", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getPromotionPolicys(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iProductBizService.actGetPromotionPolicies(currentPage);
    }

    /**
     * 保存促销数据
     *
     * @param promotionPolicy
     * @return
     */
    @RequestMapping(value = "/promotionpolicy", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean savePromotionPolicy(@RequestBody PromotionPolicyBean promotionPolicy) {
        return iProductBizService.actSavePromotionPolicy(promotionPolicy);
    }

    /**
     * 删除促销数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/promotionpolicy/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deletePromotionPolicy(@PathVariable("id") String id) {
        return iProductBizService.actDeletePromotionPolicy(id);
    }

    /**
     * 获取促销数据
     *
     * @return
     */
    @RequestMapping(value = "/promotionpolicy/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupPromotionPolicys() {
        return iProductBizService.actLookupPromotionPolicies();
    }

}
