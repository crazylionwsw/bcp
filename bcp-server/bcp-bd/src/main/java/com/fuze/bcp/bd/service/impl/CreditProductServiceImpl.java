package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.CreditProduct;
import com.fuze.bcp.bd.repository.CreditProductRepository;
import com.fuze.bcp.bd.service.ICreditProductService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/6/12.
 */
@Service
public class CreditProductServiceImpl extends BaseDataServiceImpl<CreditProduct, CreditProductRepository> implements ICreditProductService {
}
