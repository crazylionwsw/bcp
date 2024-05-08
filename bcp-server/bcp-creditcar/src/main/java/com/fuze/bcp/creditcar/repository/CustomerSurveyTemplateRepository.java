package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.CustomerSurveyTemplate;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by zqw on 2017/9/5.
 */
public interface CustomerSurveyTemplateRepository extends BaseDataRepository<CustomerSurveyTemplate,String> {

    Page<CustomerSurveyTemplate> findAllByOrderByCodeAsc(Pageable pageable);
}
