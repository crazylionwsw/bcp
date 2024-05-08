package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.creditcar.bean.CustomerSurveyResultBean;
import com.fuze.bcp.api.creditcar.bean.CustomerSurveyTemplateBean;
import com.fuze.bcp.api.creditcar.bean.declaration.DeclarationBean;
import com.fuze.bcp.api.creditcar.service.ICustomerSurveyTemplateBizService;
import com.fuze.bcp.api.creditcar.service.IDeclarationBizService;
import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.api.customer.service.IQuestionBizService;
import com.fuze.bcp.api.customer.service.IQuestionCategoryBizService;
import com.fuze.bcp.api.file.bean.DocumentTypeBean;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.push.bean.PushDataBean;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.CustomerSurveyResult;
import com.fuze.bcp.creditcar.domain.CustomerSurveyTemplate;
import com.fuze.bcp.creditcar.service.ICustomerSurveyResultService;
import com.fuze.bcp.creditcar.service.ICustomerSurveyTemplateService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by zqw on 2017/9/5.
 */
@Service
public class BizCustomerSurveyTemplateService implements ICustomerSurveyTemplateBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCustomerSurveyTemplateService.class);

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    ICustomerSurveyTemplateService iCustomerSurveyTemplateService;

    @Autowired
    ICustomerSurveyResultService iCustomerSurveyResultService;

    @Autowired
    IQuestionBizService iQuestionBizService;

    @Autowired

    IQuestionCategoryBizService iQuestionCategoryBizService;

    @Autowired
    IDeclarationBizService iDeclarationBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    ITemplateBizService iTemplateBizService;

    @Autowired
    IAmqpBizService iAmqpBizService;


    @Override
    public ResultBean<List<CustomerSurveyTemplateBean>> actGetCustomerSurveyTemplates() {
        List<CustomerSurveyTemplate> customerSurveyTemplateList = iCustomerSurveyTemplateService.getAll();
        if (customerSurveyTemplateList != null && customerSurveyTemplateList.size() > 0){
            return ResultBean.getSucceed().setD(mappingService.map(customerSurveyTemplateList,CustomerSurveyTemplateBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<CustomerSurveyTemplateBean>> actGetCustomerSurveyTemplates(@NotNull @Min(0L) Integer currentPage) {
        Page<CustomerSurveyTemplate> customerSurveyTemplatePage = iCustomerSurveyTemplateService.getAllOrderByCodeAsc(currentPage);
        if ( customerSurveyTemplatePage != null ){
            return ResultBean.getSucceed().setD(mappingService.map(customerSurveyTemplatePage,CustomerSurveyTemplateBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupCustomerSurveyTemplates() {
        List<CustomerSurveyTemplate> customerSurveyTemplateList = iCustomerSurveyTemplateService.getAvaliableAll();
        if (customerSurveyTemplateList != null && customerSurveyTemplateList.size() > 0){
            return ResultBean.getSucceed().setD(mappingService.map(customerSurveyTemplateList,CustomerSurveyTemplateBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerSurveyTemplateBean> actSaveCustomerSurveyTemplate(CustomerSurveyTemplateBean customerSurveyTemplateBean) {

        CustomerSurveyTemplate customerSurveyTemplate = mappingService.map(customerSurveyTemplateBean, CustomerSurveyTemplate.class);
        customerSurveyTemplate = iCustomerSurveyTemplateService.save(customerSurveyTemplate);
        return ResultBean.getSucceed().setD(mappingService.map(customerSurveyTemplate,CustomerSurveyTemplateBean.class));
    }

    @Override
    public ResultBean<CustomerSurveyTemplateBean> actDeleteCustomerSurveyTemplate(@NotNull String customerSurveyTemplateId) {
        CustomerSurveyTemplate customerSurveyTemplate = iCustomerSurveyTemplateService.getOne(customerSurveyTemplateId);
        if ( customerSurveyTemplate != null){
            customerSurveyTemplate = iCustomerSurveyTemplateService.delete(customerSurveyTemplateId);
            return ResultBean.getSucceed().setD(mappingService.map(customerSurveyTemplate,CustomerSurveyTemplateBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerSurveyTemplateBean> actGetCustomerSurveyTemplate(@NotNull String customerSurveyTemplateId) {
        CustomerSurveyTemplate customerSurveyTemplate = iCustomerSurveyTemplateService.getOne(customerSurveyTemplateId);
        if ( customerSurveyTemplate != null){
            return ResultBean.getSucceed().setD(mappingService.map(customerSurveyTemplate,CustomerSurveyTemplateBean.class));
        }
        return ResultBean.getFailed();
    }


    public ResultBean<CustomerSurveyBean> actGetTransactionSurvey(String transactionId) {

        //获取答案
        CustomerSurveyResult result = iCustomerSurveyResultService.getByTransactionId(transactionId);
        if (result == null)
            return ResultBean.getSucceed();

        //获取模板
        CustomerSurveyTemplate template = iCustomerSurveyTemplateService.getOne(result.getCustomerSurveyTemplateId());
        if (template == null){
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CUSTOMERSURVEYTEMPLATE_NOTFIND")));
        }

        //获取模板的问题列表
        List<QuestionsBean> questions = iQuestionBizService.actGetQuestions(template.getQuestionIds()).getD();

        CustomerSurveyBean customerSurvey = new CustomerSurveyBean();
        customerSurvey.setId(result.getId());
        customerSurvey.setCustomerTransactionId(transactionId);

        //  问题分类ID    分类问题实体
		Map<String,CategoryQuestionResultBean> cats = new HashMap<String,CategoryQuestionResultBean>();
        for (QuestionsBean question: questions) {
			String catId = question.getQuestionCategoryId();
			if (!cats.containsKey(catId)) {
				//通过categoryId获取分类
				QuestionCategoryBean cat = iQuestionCategoryBizService.actGetQuestionCategory(catId).getD();
				//创建CategoryQuestionResultBean
				CategoryQuestionResultBean categoryQuestionResultBean = mappingService.map(cat, CategoryQuestionResultBean.class);
				cats.put(catId, categoryQuestionResultBean);			
			}
			CategoryQuestionResultBean categoryQuestionResultBean = cats.get(catId);
			
			QuestionResultBean questionResult = mappingService.map(question, QuestionResultBean.class);
			categoryQuestionResultBean.getQuestionsList().add(questionResult);
        }

        Iterator iter = cats.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            customerSurvey.getCategoryQuestions().add((CategoryQuestionResultBean)entry.getValue());
        }

        return ResultBean.getSucceed().setD(customerSurvey);
    }

    @Override
    public ResultBean<CustomerSurveyBean> actSaveTransactionSurvey(CustomerSurveyBean customerSurvey) {
        //获取答案
        CustomerSurveyResult result = iCustomerSurveyResultService.getByTransactionId(customerSurvey.getCustomerTransactionId());
        if (result == null)
            return ResultBean.getFailed().setM("未找到调查问卷");

        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        for (QuestionResultBean questionResult: customerSurvey.getQuestionsList()) {
            Map<String, Object> newResult = new HashMap<String, Object>();
            newResult.put("questionId", questionResult.getId());
            newResult.put("answer", questionResult.getResult());
            results.add(newResult);
        }

        //result.setResult(results);

        return ResultBean.getSucceed().setD(result);
    }

    @Override
    public ResultBean actSaveTransactionSurvey(String transactionId, List<SurveyOption> surveyOptions) {
        //获取答案
        CustomerSurveyResult result = iCustomerSurveyResultService.getByTransactionId(transactionId);
        if (result == null)
            return ResultBean.getFailed().setM("未找到调查问卷");

        result.setResult(surveyOptions);

        result = iCustomerSurveyResultService.save(result);
        return ResultBean.getSucceed().setM("提交成功！");
    }


    @Override
    public ResultBean<List<SurveyOption>> actGetTransactionSurveyResult(String transactionId) {
        //获取答案
        CustomerSurveyResult result = iCustomerSurveyResultService.getByTransactionId(transactionId);
        if (result == null)
            return ResultBean.getFailed().setM("未找到调查问卷");

        return ResultBean.getSucceed().setD(result.getResult());
    }

    /**                         问卷调查结果                                ****/
    @Override
    public ResultBean<List<APILookupBean>> actLookupCustomerSurveyResults() {
        List<CustomerSurveyResult> customerSurveyResultList = iCustomerSurveyResultService.getAvaliableAll();
        if ( customerSurveyResultList != null && customerSurveyResultList.size() > 0 ){
            return ResultBean.getSucceed().setD(mappingService.map(customerSurveyResultList,CustomerSurveyResult.class));
        }
        return ResultBean.getFailed();
    }

    //TODO：  分配调查问卷，发送通知
    @Override
    public ResultBean<CustomerSurveyResultBean> actSaveCustomerSurveyResult(CustomerSurveyResultBean customerSurveyResultBean, String loginUserId) {
        CustomerSurveyResult customerSurveyResult = mappingService.map(customerSurveyResultBean, CustomerSurveyResult.class);
        DeclarationBean declarationBean = iDeclarationBizService.actGetTransactionDeclaration(customerSurveyResultBean.getCustomerTransactionId()).getD();
        if (declarationBean != null){
            String documentCode = iParamBizService.actGetString("DECLARATION_SURVEY_CONTRACT_CODE").getD();
            if (documentCode != null) {
                DocumentTypeBean documentTypeBean = iTemplateBizService.actGetDocumentTypeByCode(documentCode).getD();
                if (documentTypeBean != null) {
                    declarationBean.getDocumentIds().add(documentTypeBean.getId());
                    iDeclarationBizService.actCompleteDeclaration(declarationBean);
                } else {
                    logger.error(String.format(messageService.getMessage("MSG_DOCUMENT_NOTFIND_NAME"), "银行报批问卷调查"));
                }
            } else {
                logger.error(String.format(messageService.getMessage("MSG_SYSPARAM_NOTFIND_CODE"), "DECLARATION_SURVEY_CONTRACT_CODE"));
            }
        }
        // 给分期经理发送业务事件
        Map sendMap = new HashMap<>();
        List<String> toList = new ArrayList<>();
        if (declarationBean.getEmployeeId() != null) {
            toList.add(declarationBean.getEmployeeId());
        }
        sendMap.put("bd_employee", toList);
        Map ctrlMap = new HashMap<>();
        ctrlMap.put("afterOpenAction", PushDataBean.go_activity); // go activity
        ctrlMap.put("go_activity", "com.fzfq.fuzecredit.home.activity.WebViewActivity");
        Map<String, String> extraFields = new HashMap<>();
        extraFields.put("key_url", iParamBizService.actGetWebServerUrl().getD() + "/#/survey/" + customerSurveyResult.getCustomerTransactionId());
        extraFields.put("messageType", "type_3");
        extraFields.put("key_tittle", "问卷调查");
        ctrlMap.put("extraFields", extraFields);
        MsgRecordBean msgRecordBean = new MsgRecordBean("A015_DECLARATION_CREATE_SURVEY", customerSurveyResultBean.getCustomerTransactionId(), new HashMap(), null, sendMap);
        msgRecordBean.setPushCtrlMap(ctrlMap);
        msgRecordBean.setSenderId(loginUserId);
        iAmqpBizService.actSendMq("A015_DECLARATION_CREATE_SURVEY", null, msgRecordBean);
        customerSurveyResult = iCustomerSurveyResultService.save(customerSurveyResult);
        return ResultBean.getSucceed().setD(mappingService.map(customerSurveyResult,CustomerSurveyResultBean.class));
    }

    @Override
    public ResultBean<CustomerSurveyResultBean> actDeleteCustomerSurveyResult(@NotNull String customerSurveyResultId) {
        CustomerSurveyResult customerSurveyResult = iCustomerSurveyResultService.getOne(customerSurveyResultId);
        if ( customerSurveyResult != null ){
            customerSurveyResult = iCustomerSurveyResultService.delete(customerSurveyResultId);
            return ResultBean.getSucceed().setD(mappingService.map(customerSurveyResult,CustomerSurveyResultBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerSurveyResultBean> actGetCustomerSurveyResult(@NotNull String customerSurveyResultId) {
        CustomerSurveyResult customerSurveyResult = iCustomerSurveyResultService.getOne(customerSurveyResultId);
        if ( customerSurveyResult != null ){
            return ResultBean.getSucceed().setD(mappingService.map(customerSurveyResult,CustomerSurveyResultBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<Map<String,String>> actGetCustomerSurveyResultByTransactionId(@NotNull String customerTransactionId) {
        Map<String,String> map= new HashMap<String,String>();

        CustomerSurveyResult customerSurveyResult = iCustomerSurveyResultService.getByTransactionId(customerTransactionId);
        if ( customerSurveyResult != null ){
            map.put("id",customerSurveyResult.getId());
            map.put("customerSurveyTemplateId",customerSurveyResult.getCustomerSurveyTemplateId());
            return ResultBean.getSucceed().setD(map);
        }
        return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CUSTOMERSURVEYRESULT_NULL_TRANSACTIONID"),customerTransactionId));
    }
}
