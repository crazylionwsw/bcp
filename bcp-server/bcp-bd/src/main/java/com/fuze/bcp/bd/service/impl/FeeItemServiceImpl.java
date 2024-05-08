package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.FeeItem;
import com.fuze.bcp.bd.repository.FeeItemRepository;
import com.fuze.bcp.bd.service.IFeeItemService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/6/12.
 */
@Service
public class FeeItemServiceImpl extends BaseDataServiceImpl<FeeItem, FeeItemRepository> implements IFeeItemService {
}
