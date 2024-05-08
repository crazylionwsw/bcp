package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.AppointSwipingCard;
import com.fuze.bcp.creditcar.repository.AppointSwipingCardRepository;
import com.fuze.bcp.creditcar.service.IAppointSwipingCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gqr on 2017/8/17.
 */
@Service
public class AppointSwipingCardServiceImpl  extends BaseBillServiceImpl<AppointSwipingCard, AppointSwipingCardRepository> implements IAppointSwipingCardService {

    @Autowired
    AppointSwipingCardRepository appointSwipingCardRepository;

    @Override
    public Page<AppointSwipingCard> findAllByStatusOrderByTsDesc(int status, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return appointSwipingCardRepository.findAllByStatusAndDataStatusOrderByTsDesc(status, DataStatus.SAVE, page);
    }

    @Override
    public Page<AppointSwipingCard> findAllByStatusAndCustomerIds(List<String> customerIds, int status, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return appointSwipingCardRepository.findAllByDataStatusAndStatusAndCustomerIdInOrderByTsDesc(DataStatus.SAVE, status, customerIds, page);
    }
}
