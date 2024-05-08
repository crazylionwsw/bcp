package com.fuze.bcp.customer.service;

import com.fuze.bcp.customer.domain.QuestionCategory;
import com.fuze.bcp.service.ITreeDataService;

import java.util.List;

/**
 * Created by GQR on 2017/8/31.
 */
public interface IQuestionCategoryService extends ITreeDataService<QuestionCategory> {

    List<QuestionCategory> getChildrenOrderByDisplayOrderAsc(String parentId);
}
