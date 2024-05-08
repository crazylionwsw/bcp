package com.fuze.bcp.customer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fuze.bcp.customer.domain.Questions;
import com.fuze.bcp.customer.repository.QuestionRepository;
import com.fuze.bcp.customer.service.IQuestionService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by GQR on 2017/9/1.
 */
@Service
public class QuestionServiceImpl extends BaseDataServiceImpl<Questions,QuestionRepository> implements IQuestionService{


    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Page<Questions> findByQuestionCategoryId(Integer currentPage, String questionCategoryId) {
        PageRequest pr = new PageRequest(currentPage,20);
        return questionRepository.findAllByQuestionCategoryIdOrderByDisplayOrderAsc(questionCategoryId,pr);
    }

    @Override
    public List<Questions> findAllByQuestionCategoryId(String questionCategoryId) {
        return questionRepository.findAllByQuestionCategoryIdOrderByDisplayOrderAsc(questionCategoryId);
    }

    @Override
    public Page<Questions> getAllOrderByDisplayOrderAsc(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage,20);
        return questionRepository.findAllByOrderByDisplayOrderAsc(pr);
    }
}
