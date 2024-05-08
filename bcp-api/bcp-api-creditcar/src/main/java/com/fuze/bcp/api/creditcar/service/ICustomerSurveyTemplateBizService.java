package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.customer.bean.CustomerSurveyBean;
import com.fuze.bcp.api.creditcar.bean.CustomerSurveyResultBean;
import com.fuze.bcp.api.creditcar.bean.CustomerSurveyTemplateBean;
import com.fuze.bcp.api.customer.bean.SurveyOption;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by zqw on 2017/9/5.
 */
public interface ICustomerSurveyTemplateBizService {

    /**
     * 获取问卷调查模板数据
     * @return
     */
    ResultBean<List<CustomerSurveyTemplateBean>> actGetCustomerSurveyTemplates();

    /**
     * 获取问卷调查模板列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CustomerSurveyTemplateBean>> actGetCustomerSurveyTemplates(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取问卷调查模板列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCustomerSurveyTemplates();

    /**
     * 保存问卷调查模板
     *
     * @param customerSurveyTemplateBean
     * @return
     */
    ResultBean<CustomerSurveyTemplateBean> actSaveCustomerSurveyTemplate(CustomerSurveyTemplateBean customerSurveyTemplateBean);

    /**
     * 删除问卷调查模板
     *
     * @param customerSurveyTemplateId
     * @return
     */
    ResultBean<CustomerSurveyTemplateBean> actDeleteCustomerSurveyTemplate(@NotNull String customerSurveyTemplateId);

    /**
     *      根据ID    回显
     * @param customerSurveyTemplateId
     * @return
     */
    ResultBean<CustomerSurveyTemplateBean> actGetCustomerSurveyTemplate(@NotNull String customerSurveyTemplateId);

    /****                            调查问卷                                          ***/

    /**
     * 通过交易ID获取调查问卷
     * @param transactionId
     * @return
     */
    ResultBean<CustomerSurveyBean> actGetTransactionSurvey(String transactionId);

    /**
     * 保存调查问卷
     * @param customerSurvey
     * @return
     */
    ResultBean<CustomerSurveyBean> actSaveTransactionSurvey(CustomerSurveyBean customerSurvey);
    ResultBean actSaveTransactionSurvey(String transactionId, List<SurveyOption> surveyOptions);

    /**
     * 获取问卷结果
     * @param transactionId
     * @return
     */
    ResultBean<List<SurveyOption>> actGetTransactionSurveyResult(String transactionId);

    /****                               客户问卷调查结果                           ****/

    /**
     * 获取问卷调查结果列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCustomerSurveyResults();

    /**
     * 保存问卷调查结果
     *
     * @param customerSurveyResultBean
     * @return
     */
    ResultBean<CustomerSurveyResultBean> actSaveCustomerSurveyResult(CustomerSurveyResultBean customerSurveyResultBean,String loginUserId);

    /**
     * 删除问卷调查结果
     *
     * @param customerSurveyResultId
     * @return
     */
    ResultBean<CustomerSurveyResultBean> actDeleteCustomerSurveyResult(@NotNull String customerSurveyResultId);

    /**
     *      根据ID回显问卷调查结果
     * @param customerSurveyResultId
     * @return
     */
    ResultBean<CustomerSurveyResultBean> actGetCustomerSurveyResult(@NotNull String customerSurveyResultId);

    /**
     *      根据交易ID查询客户调查结果
     * @param customerTransactionId
     * @return
     */
    ResultBean<Map<String,String>> actGetCustomerSurveyResultByTransactionId(@NotNull String customerTransactionId);

}
