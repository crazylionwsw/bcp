package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.AppointSwipingCard;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by gqr on 2017/8/17.
 */
public interface IAppointSwipingCardService extends IBaseBillService<AppointSwipingCard> {

    Page<AppointSwipingCard> findAllByStatusOrderByTsDesc(int status, int currentPage);

    Page<AppointSwipingCard> findAllByStatusAndCustomerIds(List<String> customerIds, int status, int currentPage);

}