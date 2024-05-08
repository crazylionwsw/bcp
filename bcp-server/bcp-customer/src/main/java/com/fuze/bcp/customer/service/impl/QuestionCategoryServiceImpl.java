package com.fuze.bcp.customer.service.impl;

import com.fuze.bcp.customer.domain.QuestionCategory;
import com.fuze.bcp.customer.repository.QuestionCategoryRepository;
import com.fuze.bcp.customer.service.IQuestionCategoryService;
import com.fuze.bcp.service.impl.TreeDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by GQR on 2017/8/31.
 */
@Service
public class QuestionCategoryServiceImpl extends TreeDataServiceImpl<QuestionCategory, QuestionCategoryRepository> implements IQuestionCategoryService{

    @Autowired
    QuestionCategoryRepository questionCategoryRepository;

    @Override
    public List<QuestionCategory> getChildrenOrderByDisplayOrderAsc(String parentId) {
        return questionCategoryRepository.findByParentIdOrderByDisplayOrderAsc(parentId);
    }
}
