package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.SwipingCardTrustee;
import com.fuze.bcp.creditcar.repository.SwipingCardTrusteeRepository;
import com.fuze.bcp.creditcar.service.ISwipingCardTrusteeService;
import org.springframework.stereotype.Service;

/**
 * Created by Lily on 2017/10/16.
 */
@Service
public class SwipingCardTrusteeServiceImpl extends BaseBillServiceImpl<SwipingCardTrustee,SwipingCardTrusteeRepository> implements ISwipingCardTrusteeService{
}
