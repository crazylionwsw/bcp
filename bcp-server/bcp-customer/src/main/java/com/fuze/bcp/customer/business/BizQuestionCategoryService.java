package com.fuze.bcp.customer.business;

import com.alibaba.dubbo.config.annotation.Service;
import com.fuze.bcp.api.customer.bean.QuestionCategoryBean;
import com.fuze.bcp.api.customer.bean.QuestionsBean;
import com.fuze.bcp.api.customer.service.IQuestionCategoryBizService;
import com.fuze.bcp.bean.APITreeLookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.customer.domain.QuestionCategory;
import com.fuze.bcp.customer.domain.Questions;
import com.fuze.bcp.customer.service.IQuestionCategoryService;
import com.fuze.bcp.customer.service.IQuestionService;
import com.fuze.bcp.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by GQR on 2017/8/31.
 */
@Service
public class BizQuestionCategoryService implements IQuestionCategoryBizService {

    @Autowired
    IQuestionCategoryService iQuestionCategoryService;

    @Autowired
    private IQuestionService iQuestionService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<List<APITreeLookupBean>> actLookupQuestionCategory() {
        List<QuestionCategory> questionCategories = iQuestionCategoryService.getLookups(null, 0);
        return ResultBean.getSucceed().setD(mappingService.map(questionCategories, APITreeLookupBean.class));
    }

    @Override
    public ResultBean<QuestionCategoryBean> actGetQuestionCategory(@NotNull String id) {
        QuestionCategory questionCategory = iQuestionCategoryService.getOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(questionCategory, QuestionCategoryBean.class));
    }

    @Override
    public ResultBean<List<QuestionCategoryBean>> actGetQuestionCategories(String parentId) {

        List<QuestionCategory> questionCategoryList = iQuestionCategoryService.getChildrenOrderByDisplayOrderAsc(parentId);

        return ResultBean.getSucceed().setD(mappingService.map(questionCategoryList, QuestionCategoryBean.class));
    }

    @Override
    public ResultBean<DataPageBean<QuestionsBean>> actGetQuestionsPageByQuestionCategoryId(Integer currentPage, String questionCategoryId) {

        Page<Questions> questionsPage = null;
        if (questionCategoryId == "0") {
            questionsPage = iQuestionService.getAll(currentPage);
        } else {
            questionsPage = iQuestionService.findByQuestionCategoryId(currentPage, questionCategoryId);
        }
        return ResultBean.getSucceed().setD(mappingService.map(questionsPage, QuestionsBean.class));
    }

    @Override
    public ResultBean<List<QuestionsBean>> actGetQuestionsByQuestionCategoryId(String questionCategoryId) {

        List<Questions> questionsList = null;
        if (questionCategoryId == "0") {
            questionsList = iQuestionService.getAll();
        } else {
            questionsList = iQuestionService.findAllByQuestionCategoryId(questionCategoryId);
        }
        return ResultBean.getSucceed().setD(mappingService.map(questionsList, QuestionsBean.class));
    }

    @Override
    public ResultBean<QuestionCategoryBean> actSaveQuestionCategory(QuestionCategoryBean questionCategoryBean) {
        QuestionCategory questionCategory = iQuestionCategoryService.save(mappingService.map(questionCategoryBean,QuestionCategory.class));
        return ResultBean.getSucceed().setD(mappingService.map(questionCategory,QuestionCategoryBean.class));
    }




}
