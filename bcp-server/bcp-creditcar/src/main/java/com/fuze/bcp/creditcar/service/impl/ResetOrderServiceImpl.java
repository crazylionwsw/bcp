package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.ResetOrder;
import com.fuze.bcp.creditcar.repository.ResetOrderRepository;
import com.fuze.bcp.creditcar.service.IResetOrderService;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2017/10/23.
 */
@Service
public class ResetOrderServiceImpl extends BaseBillServiceImpl<ResetOrder,ResetOrderRepository> implements IResetOrderService {
}
