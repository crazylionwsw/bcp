package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.PromotionPolicy;
import com.fuze.bcp.bd.repository.PromotionPolicyRepository;
import com.fuze.bcp.bd.service.IPromotionPolicyService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by ${Liu} on 2017/7/31.
 */
@Service
public class PromotionPolicyServiceImpl extends BaseDataServiceImpl<PromotionPolicy,PromotionPolicyRepository> implements IPromotionPolicyService{
}
