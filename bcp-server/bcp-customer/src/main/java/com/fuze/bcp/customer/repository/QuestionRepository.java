package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.Questions;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by GQR on 2017/9/1.
 */
public interface QuestionRepository extends BaseDataRepository<Questions,String> {

    Page<Questions> findAllByQuestionCategoryId(String questionCategoryId, Pageable pageable);

    List<Questions> findAllByQuestionCategoryIdOrderByDisplayOrderAsc(String questionCategoryId);

    Page<Questions> findAllByQuestionCategoryIdOrderByDisplayOrderAsc(String questionCategoryId, Pageable pageable);

    Page<Questions> findAllByOrderByDisplayOrderAsc(Pageable pageable);
}
