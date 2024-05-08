package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.QuestionCategory;
import com.fuze.bcp.repository.TreeDataRepository;

import java.util.List;

/**
 * Created by GQR on 2017/8/31.
 */
public interface QuestionCategoryRepository extends TreeDataRepository<QuestionCategory,String> {


    List<QuestionCategory> findByParentIdOrderByDisplayOrderAsc(String parentId);
}
