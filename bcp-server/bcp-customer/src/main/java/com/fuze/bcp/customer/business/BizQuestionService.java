package com.fuze.bcp.customer.business;


import com.fuze.bcp.api.customer.bean.QuestionsBean;
import com.fuze.bcp.api.customer.service.IQuestionBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.customer.domain.Questions;
import com.fuze.bcp.customer.service.IQuestionService;
import com.fuze.bcp.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by GQR on 2017/9/1.
 */
@Service
public class BizQuestionService implements IQuestionBizService {

    @Autowired
    IQuestionService iQuestionService;

    @Autowired
    MappingService mappingService;

    @Autowired
    public MongoTemplate mongoTemplate;

    @Override
    public ResultBean<QuestionsBean> actSaveQuestion(QuestionsBean questionsBean) {
        Questions questions = mappingService.map(questionsBean, Questions.class);
        questions = iQuestionService.save(questions);
        return ResultBean.getSucceed().setD(mappingService.map(questions,QuestionsBean.class));
    }

    @Override
    public ResultBean<QuestionsBean> actGetOneQuestion(String questionsId) {
        Questions questions = iQuestionService.getOne(questionsId);
        if(questions !=null){
            return ResultBean.getSucceed().setD(mappingService.map(questions,QuestionsBean.class));
        }
        return  ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<QuestionsBean>> actGetQuestions(List<String> questionsIds) {
        List<Questions> questions = iQuestionService.getAvaliableList(questionsIds);
        if(questions !=null){
            return ResultBean.getSucceed().setD(mappingService.map(questions,QuestionsBean.class));
        }
        return  ResultBean.getFailed();
    }

    @Override
    public ResultBean<QuestionsBean> actGetOneQuestionByCode(String code) {
        Questions questions = iQuestionService.getOneByCode(code);
        if(questions !=null){
            return ResultBean.getSucceed().setD(mappingService.map(questions,QuestionsBean.class));
        }
        return  ResultBean.getFailed();
    }

    @Override
    public ResultBean<QuestionsBean> actQuestions(){
        List<Questions> questionsList = iQuestionService.getAvaliableAll();
        if (questionsList!=null&&questionsList.size()>0){
            return ResultBean.getSucceed().setD(mappingService.map(questionsList,QuestionsBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<QuestionsBean> actDeleteQuestion(@NotNull String id) {
        Questions question = iQuestionService.getOne(id);
        if (question != null) {
            question = iQuestionService.delete(id);
            return ResultBean.getSucceed().setD(mappingService.map(question, QuestionsBean.class));
        }
        return ResultBean.getFailed();
    }

    /**
     * 模糊查询
     * @param currentPage
     * @param questionsBean
     * @return
     */
    @Override
    public ResultBean<DataPageBean<QuestionsBean>> actSearchQuestions(Integer currentPage, QuestionsBean questionsBean) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").gt(DataStatus.TEMPSAVE));
        if (!StringUtils.isEmpty(questionsBean.getQuestionCategoryId()))
            query.addCriteria(Criteria.where("questionCategoryId").is(questionsBean.getQuestionCategoryId()));
        if (!StringUtils.isEmpty(questionsBean.getContent()))
            query.addCriteria(Criteria.where("content").regex(Pattern.compile("^.*"+ questionsBean.getContent() +".*$", Pattern.CASE_INSENSITIVE)));
        if (!StringUtils.isEmpty(questionsBean.getCode()))
            query.addCriteria(Criteria.where("code").regex(Pattern.compile("^.*"+ questionsBean.getCode() +".*$", Pattern.CASE_INSENSITIVE)));

        Pageable pageable = new PageRequest(currentPage,20);
        query.with(pageable);
        List list = mongoTemplate.find(query,Questions.class);
        Page<Questions> page  = new PageImpl(list,pageable, mongoTemplate.count(query,Questions.class));
        return ResultBean.getSucceed().setD(mappingService.map(page, QuestionsBean.class));
    }
}
