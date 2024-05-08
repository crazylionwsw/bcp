package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.CustomerSurveyResultBean;
import com.fuze.bcp.api.creditcar.bean.CustomerSurveyTemplateBean;
import com.fuze.bcp.api.creditcar.service.ICustomerSurveyTemplateBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zqw on 2017/9/5.
 */
@RestController
@RequestMapping(value = "/json")
public class CustomerSurveyTemplateController {

    @Autowired
    private ICustomerSurveyTemplateBizService iCustomerSurveyTemplateBizService;

    /**
     * 获取问卷调查模板所有数据(带分页，升序)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/customersurveytemplates", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerSurveyTemplates(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iCustomerSurveyTemplateBizService.actGetCustomerSurveyTemplates(currentPage);
    }

    /**
     * 获取问卷调查模板(可用数据)
     * @return
     */
    @RequestMapping(value = "/customersurveytemplates/lookups", method = RequestMethod.GET)
    public ResultBean lookupCustomerSurveyTemplates() {
        return iCustomerSurveyTemplateBizService.actLookupCustomerSurveyTemplates();
    }

    /**
     * 保存问卷调查模板
     *
     * @param customerSurveyTemplateWay
     * @return
     */
    @RequestMapping(value = "/customersurveytemplate", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCustomerSurveyTemplate(@RequestBody CustomerSurveyTemplateBean customerSurveyTemplateWay) {
        return iCustomerSurveyTemplateBizService.actSaveCustomerSurveyTemplate(customerSurveyTemplateWay);
    }

    /**
     * 删除问卷调查模板
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/customersurveytemplate/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCustomerSurveyTemplate(@PathVariable("id") String id) {
        return iCustomerSurveyTemplateBizService.actDeleteCustomerSurveyTemplate(id);
    }

    /**
     *      根据ID回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/customersurveytemplate/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerSurveyTemplate(@PathVariable("id") String id) {
        return iCustomerSurveyTemplateBizService.actGetCustomerSurveyTemplate(id);
    }

    /**                                          客户问卷调查结果                              **/

    /**
     * 获取问卷调查结果(可用数据)
     * @return
     */
    @RequestMapping(value = "/customersurveyresults/lookups", method = RequestMethod.GET)
    public ResultBean lookupCustomerSurveyResults() {
        return iCustomerSurveyTemplateBizService.actLookupCustomerSurveyResults();
    }

    /**
     * 保存问卷调查，给客户分配调查问卷
     *
     * @param customerSurveyResultBean
     * @return
     */
    @RequestMapping(value = "/customersurveyresult", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCustomerSurveyResult(@RequestBody CustomerSurveyResultBean customerSurveyResultBean) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return iCustomerSurveyTemplateBizService.actSaveCustomerSurveyResult(customerSurveyResultBean,jwtUser.getId());
    }

    /**
     * 删除问卷调查结果
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/customersurveyresult/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCustomerSurveyResult(@PathVariable("id") String id) {
        return iCustomerSurveyTemplateBizService.actDeleteCustomerSurveyResult(id);
    }

    /**
     *      根据ID回显问卷调查结果
     * @param id
     * @return
     */
    @RequestMapping(value = "/customersurveyresult/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerSurveyResult(@PathVariable("id") String id) {
        return iCustomerSurveyTemplateBizService.actGetCustomerSurveyResult(id);
    }

    /**
     *  通过交易ID查询客户调查问卷
     * @param transactionId
     * @return
     */
    @RequestMapping(value = "/customersurveyresult/transaction/{transactionId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerSurveyResultByTransactionId(@PathVariable("transactionId") String transactionId) {
        return iCustomerSurveyTemplateBizService.actGetCustomerSurveyResultByTransactionId(transactionId);
    }
}
