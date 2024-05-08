package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.CustomerSurveyTemplate;
import com.fuze.bcp.creditcar.repository.CustomerSurveyTemplateRepository;
import com.fuze.bcp.creditcar.service.ICustomerSurveyTemplateService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Created by jinglu on 2017/9/5.
 */
@Service
public class CustomerSurveyTemplateServiceImpl extends BaseDataServiceImpl<CustomerSurveyTemplate, CustomerSurveyTemplateRepository> implements ICustomerSurveyTemplateService {

    @Autowired
    CustomerSurveyTemplateRepository customerSurveyTemplateRepository;

    @Override
    public Page<CustomerSurveyTemplate> getAllOrderByCodeAsc(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage,20);
        return customerSurveyTemplateRepository.findAllByOrderByCodeAsc(pr);
    }
}
