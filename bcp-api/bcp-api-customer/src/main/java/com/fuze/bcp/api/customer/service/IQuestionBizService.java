package com.fuze.bcp.api.customer.service;

import com.fuze.bcp.api.customer.bean.QuestionsBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by GQR on 2017/9/1.
 */
public interface IQuestionBizService {
    /**
     * 保存问题
     * @param questionsBean
     * @return
     */
    ResultBean<QuestionsBean> actSaveQuestion(QuestionsBean questionsBean);

    /**
     * 根据Id获取单条问题信息
     * @param questionsId
     * @return
     */
    ResultBean<QuestionsBean> actGetOneQuestion(@NotNull String questionsId);

    //  根据IDS获取问题
    ResultBean<List<QuestionsBean>> actGetQuestions(List<String> questionsIds);

    ResultBean<QuestionsBean> actGetOneQuestionByCode(@NotNull String code);

    /**
     *       获取 所有的问题
     * @return
     */
    ResultBean<QuestionsBean> actQuestions();

    /**
     * 删除问题
     * @param id
     * @return
     */
    ResultBean<QuestionsBean> actDeleteQuestion(@NotNull String id);

    //查找问题
    ResultBean<DataPageBean<QuestionsBean>> actSearchQuestions(Integer currentPage, QuestionsBean questionsBean);




}
