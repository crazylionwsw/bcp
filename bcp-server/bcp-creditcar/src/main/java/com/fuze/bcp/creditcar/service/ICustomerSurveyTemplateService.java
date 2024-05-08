package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.CustomerSurveyTemplate;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

/**
 * Created by zqw on 2017/9/5.
 */
public interface ICustomerSurveyTemplateService extends IBaseDataService<CustomerSurveyTemplate> {

    Page<CustomerSurveyTemplate> getAllOrderByCodeAsc(Integer currentPage);
}
