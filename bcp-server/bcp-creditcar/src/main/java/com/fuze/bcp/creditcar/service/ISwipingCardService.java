package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.SwipingCard;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Lily on 2017/9/22.
 */
public interface ISwipingCardService extends IBaseBillService<SwipingCard> {

    Page<SwipingCard> findAllByStatusOrderByTsDesc(int status, int currentPage);

    Page<SwipingCard> findAllByStatusAndCustomerIds(List<String> customerIds, int status, int currentPage);
}
