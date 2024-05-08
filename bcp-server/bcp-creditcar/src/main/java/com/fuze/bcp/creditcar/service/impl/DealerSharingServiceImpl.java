package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.DealerSharing;
import com.fuze.bcp.creditcar.repository.DealerSharingRepository;
import com.fuze.bcp.creditcar.service.IDealerSharingService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Created by Lily on 2017/9/23.
 */
@Service
public class DealerSharingServiceImpl extends BaseServiceImpl<DealerSharing, DealerSharingRepository> implements IDealerSharingService {

    @Override
    public DealerSharing getByMonthAndCarDealer(String month, String carDealerId) {
        return baseRepository.findOneByMonthAndCarDealerId(month, carDealerId);
    }

    @Override
    public List<DealerSharing> getByMonthAndChannelIds(String month, List<String> channelIds) {
        return baseRepository.findByMonthAndChannelIdInOrderByChannelId(month, channelIds);
    }

    @Override
    public List<DealerSharing> getByMonth(String month) {
        return baseRepository.findByMonthOrderByChannelId(month);
    }

    @Override
    public DealerSharing getDealerSharingBySharingDetailIdsContaining(String sharingDetailId) {
        return baseRepository.findByDataStatusAndSharingDetailIdsContaining(DataStatus.SAVE, sharingDetailId);
    }

    @Override
    public List<DealerSharing> getAvaliableAllByMonth(String month) {
        return baseRepository.findAllByDataStatusAndMonth(DataStatus.SAVE, month);
    }

    @Override
    public List<DealerSharing> getAvaliableAllByMonthAndCarDealerId(String month, String cardealerId) {
        return baseRepository.findAllByDataStatusAndMonthAndCarDealerId(DataStatus.SAVE, month, cardealerId);
    }

    @Override
    public Collection<? extends DealerSharing> getAvaliableAllByMonthAndCarDealerIdIn(String month, List cardealerIds) {
        return baseRepository.findAllByDataStatusAndMonthAndCarDealerIdIn(DataStatus.SAVE, month, cardealerIds);
    }

    @Override
    public Page<DealerSharing> getAvaliablePageData(Integer currentPage, String month, List carDealerIds) {

        Criteria criteria = new Criteria();
        if (month != null && !"".equals(month)) {
            criteria.and("month").is(month);
        }
        if (carDealerIds != null && carDealerIds.size() > 0) {
            criteria.and("carDealerId").in(carDealerIds);
        }
        Query query = new Query();
        query.addCriteria(criteria);
        Pageable p = new PageRequest(currentPage, 20);
        query.with(p);
        List<DealerSharing> data = mongoTemplate.find(query, DealerSharing.class);
        return new PageImpl<DealerSharing>(data, p, mongoTemplate.count(query, DealerSharing.class));
    }

//    @Override
//    public List<DealerSharing> getByMonthAndGroup(String month, String groupId) {
//        return baseRepository.findByMonthAndDealerGroupId(month, groupId);
//    }
}
