package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.SwipingCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by zxp on 2017/3/13.
 */
public interface SwipingCardRepository extends BaseBillRepository<SwipingCard,String> {

    Page<SwipingCard> findAllByStatusAndDataStatusOrderByTsDesc(Integer status, Integer dataStatus, Pageable pageable);

    Page<SwipingCard> findAllByDataStatusAndStatusAndCustomerIdInOrderByTsDesc(Integer ds, Integer as, List<String> ids, Pageable pageable);
}
