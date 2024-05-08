package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.AppointSwipingCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by gqr on 2017/8/17.
 */
public interface AppointSwipingCardRepository extends BaseBillRepository<AppointSwipingCard,String> {

    Page<AppointSwipingCard> findAllByDataStatusAndApproveStatusAndCustomerIdIn(Integer save, int approveStatus, List<String> customerIds, Pageable page);

    Page<AppointSwipingCard> findAllByStatusAndDataStatusOrderByTsDesc(Integer status, Integer dataStatus, Pageable pageable);

    Page<AppointSwipingCard> findAllByDataStatusAndStatusAndCustomerIdInOrderByTsDesc(Integer ds, Integer as, List<String> ids, Pageable pageable);


}
