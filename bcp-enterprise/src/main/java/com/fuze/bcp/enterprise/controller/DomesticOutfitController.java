package com.fuze.bcp.enterprise.controller;

import com.fuze.bcp.api.credithome.bean.DomesticOutfitSubmissionBean;
import com.fuze.bcp.api.credithome.service.IDomesticOutfitBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ZQW on 2018/3/20.
 */
@RestController
@RequestMapping(value = "/json", method = {RequestMethod.GET, RequestMethod.POST})
public class DomesticOutfitController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DomesticOutfitController.class);

    @Autowired
    IDomesticOutfitBizService iDomesticOutfitBizService;

    /**
     * 【PAD-API】 分页获取loginUserId 的预约垫资
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/domesticoutfits", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAppointPaymentsPage(@RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                             @RequestParam(value = "isPass", required = false, defaultValue = "0" ) boolean isPass) {

        return iDomesticOutfitBizService.actGetDomesticOutfits("59008dd6e4b02c15d7e4f6fa", currentPage, pageSize, isPass);
    }

    /**
     *  【Enterprise WX - API】保存家装分期数据
     * @param domesticOutfitSubmissionBean
     * @return
     */
    @RequestMapping(value="/domesticoutfit",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveDomesticOutfit (@RequestBody DomesticOutfitSubmissionBean domesticOutfitSubmissionBean){
        //domesticOutfitSubmissionBean.setLoginUserId(this.getOperatorId());
        return iDomesticOutfitBizService.actSubmitDomesticOutfit(domesticOutfitSubmissionBean);
    }

    /**
     *  【Enterprise WX - API】根据ID获取家装分期数据
     * @param
     * @return
     */
    @RequestMapping(value="/domesticoutfit/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDomesticOutById(@PathVariable("id") String id){
        return iDomesticOutfitBizService.actRetrieveDomesticOutfitById(id);
    }
}
