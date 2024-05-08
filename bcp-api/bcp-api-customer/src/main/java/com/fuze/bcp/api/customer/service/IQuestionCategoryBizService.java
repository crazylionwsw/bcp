package com.fuze.bcp.api.customer.service;

import com.fuze.bcp.api.customer.bean.QuestionCategoryBean;
import com.fuze.bcp.api.customer.bean.QuestionsBean;
import com.fuze.bcp.bean.APITreeLookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 问题的查询接口
 */

public interface IQuestionCategoryBizService {

    /**
     *获取问题分类列表，不带分页
     * @param
     * @return
     */
    ResultBean<List<APITreeLookupBean>> actLookupQuestionCategory();

    /**
     *      根据      ID      回显
     * @param id
     * @return
     */
    ResultBean<QuestionCategoryBean> actGetQuestionCategory(@NotNull String id);

    /**
     * 获取下级问题分类
     *
     * @param parentId
     * @return
     */
    ResultBean<List<QuestionCategoryBean>> actGetQuestionCategories(@NotNull String parentId);

    /**
     * 获取某问题分类下所有问题
     * 注意：问题分类为树型结构，如果该问题分类有子级问题分类，要返回时子级问题分类的问题
     *
     * @param currentPage
     * @param questionCategoryId
     * @return
     */
    ResultBean<DataPageBean<QuestionsBean>> actGetQuestionsPageByQuestionCategoryId(@NotNull @Min(0) Integer currentPage, @NotNull String questionCategoryId);

    /**
     *      获取当前问题分类下的所有的问题
     * @param questionCategoryId
     * @return
     */
    ResultBean<List<QuestionsBean>> actGetQuestionsByQuestionCategoryId(@NotNull String questionCategoryId);

    /**
     * 保存问题分类
      * @return
     */
    ResultBean<QuestionCategoryBean> actSaveQuestionCategory(QuestionCategoryBean questionCategoryBean);


}
