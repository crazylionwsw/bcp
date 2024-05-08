package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.PromotionPolicy;
import com.fuze.bcp.bd.domain.SalesPolicy;
import com.fuze.bcp.bd.repository.SalesPolicyRepository;
import com.fuze.bcp.bd.service.ISalesPolicyService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2017/7/10.
 */
@Service
public class SalesPolicyServiceImpl extends BaseDataServiceImpl<SalesPolicy,SalesPolicyRepository> implements ISalesPolicyService{
}
