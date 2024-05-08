package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.customer.bean.QuestionCategoryBean;
import com.fuze.bcp.api.customer.bean.QuestionsBean;
import com.fuze.bcp.api.customer.service.IQuestionBizService;
import com.fuze.bcp.api.customer.service.IQuestionCategoryBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by GQR on 2017/8/31.
 */
@RestController
@RequestMapping(value = "/json")
public class QuestionCategoryController {

    @Autowired
    IQuestionCategoryBizService iQuestionCategoryBizService;

    @Autowired
    IQuestionBizService iQuestionBizService;

    /**
     * 获取可用问题分类
     * @return
     */
    @RequestMapping(value = "/questioncategory/lookups", method = RequestMethod.GET)
    public ResultBean lookupQuestionCategories() {
        return iQuestionCategoryBizService.actLookupQuestionCategory();
    }

    /**
     * 获取下级问题分类
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/questioncategory/{id}/questioncategorys", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetQuestionCategories(@PathVariable("id") String id) {
        return iQuestionCategoryBizService.actGetQuestionCategories(id);
    }

    /**
     *保存问题分类
     * @param questionCategoryBean
     * @return
     */
    @RequestMapping(value = "/questioncategory",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveQuestionCategories(@RequestBody QuestionCategoryBean questionCategoryBean){
        return iQuestionCategoryBizService.actSaveQuestionCategory(questionCategoryBean);
    }

    /**
     * 获取指定问题分类
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/questioncategory/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getQuestionCategory(@PathVariable("id") String id) {
        return iQuestionCategoryBizService.actGetQuestionCategory(id);
    }

    /**
     * 获取某问题分类下的所有问题
     *
     * @param currentPage
     * @param id
     * @return
     */
    @RequestMapping(value = "/questioncategory/{id}/questionspage", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getQuestionsPage(@RequestParam(value = "currentPage") Integer currentPage, @PathVariable("id") String id) {
        return iQuestionCategoryBizService.actGetQuestionsPageByQuestionCategoryId(currentPage, id);
    }

    @RequestMapping(value = "/questioncategory/{id}/questions", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getQuestions(@PathVariable("id") String id) {
        return iQuestionCategoryBizService.actGetQuestionsByQuestionCategoryId(id);
    }

    /***                                问题                                                **/

    /**
     * 获取可用问题
     * @return
     */
    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public ResultBean lookupQuestions() {
        return iQuestionBizService.actQuestions();
    }

    /**
     * 保存问题
     * @param question
     * @return
     */
    @RequestMapping(value = "/question", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveQuestion(@RequestBody QuestionsBean question){
       return iQuestionBizService.actSaveQuestion(question);
   }

    /**
     * 根据Id获取单条问题
     * @param questionId
     * @return
     */
    @RequestMapping(value = "/question/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOneQuestion(@PathVariable("id") String questionId){
        return iQuestionBizService.actGetOneQuestion(questionId);
    }

    @RequestMapping(value = "/question/code/{code}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOneQuestionByCode(@PathVariable("code") String code){
        return iQuestionBizService.actGetOneQuestionByCode(code);
    }

    /**
     * 废弃问题
     */
    @RequestMapping(value = "/questioncategory/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteQuestion(@PathVariable("id") String id){
        return iQuestionBizService.actDeleteQuestion(id);
    }

    /**
     * 查找问题
     * @param currentPage
     * @param
     * @return
     */
    @RequestMapping(value = "/question/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean searchQuestions(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,@RequestBody QuestionsBean questionsBean){

        return iQuestionBizService.actSearchQuestions(currentPage,questionsBean);
    }

}