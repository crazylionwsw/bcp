package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.SwipingCard;
import com.fuze.bcp.creditcar.repository.SwipingCardRepository;
import com.fuze.bcp.creditcar.service.ISwipingCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lily on 2017/9/22.
 */
@Service
public class SwipingCardServiceImpl extends BaseBillServiceImpl<SwipingCard,SwipingCardRepository> implements ISwipingCardService{

    @Autowired
    SwipingCardRepository swipingCardRepository;

    @Override
    public Page<SwipingCard> findAllByStatusOrderByTsDesc(int status, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return swipingCardRepository.findAllByStatusAndDataStatusOrderByTsDesc(status, DataStatus.SAVE, page);
    }

    @Override
    public Page<SwipingCard> findAllByStatusAndCustomerIds(List<String> customerIds, int status, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return swipingCardRepository.findAllByDataStatusAndStatusAndCustomerIdInOrderByTsDesc(DataStatus.SAVE, status, customerIds, page);
    }
}
