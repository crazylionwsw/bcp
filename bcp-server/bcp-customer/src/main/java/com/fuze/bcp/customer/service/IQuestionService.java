package com.fuze.bcp.customer.service;

import com.fuze.bcp.customer.domain.Questions;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by GQR on 2017/8/31.
 */
public interface IQuestionService extends IBaseDataService<Questions> {

    Page<Questions> findByQuestionCategoryId(Integer currentPage, String questionCategoryId);

    List<Questions> findAllByQuestionCategoryId(String questionCategoryId);

    Page<Questions> getAllOrderByDisplayOrderAsc(Integer currentPage);

}