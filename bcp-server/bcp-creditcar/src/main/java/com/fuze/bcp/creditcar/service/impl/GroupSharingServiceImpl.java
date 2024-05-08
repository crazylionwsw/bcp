package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.DealerSharing;
import com.fuze.bcp.creditcar.domain.GroupSharing;
import com.fuze.bcp.creditcar.repository.GroupSharingRepository;
import com.fuze.bcp.creditcar.service.IGroupSharingService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupSharingServiceImpl extends BaseServiceImpl<GroupSharing,GroupSharingRepository> implements IGroupSharingService {


    @Override
    public GroupSharing getByMonthAndGroup(String month, String groupId) {
        return baseRepository.findOneByMonthAndDealerGroupId(month, groupId);
    }

    @Override
    public List<GroupSharing> getByGroup(String groupId) {
        return baseRepository.findAllByDealerGroupId(groupId);
    }

    @Override
    public List<GroupSharing> getByMonthAndGroupId(String month, String groupId) {
        return baseRepository.findAllByMonthAndDealerGroupId(month, groupId);
    }

    @Override
    public Page<GroupSharing> getGroupSharing(String groupId, Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20, GroupSharing.getSortDESC("month"));
        return baseRepository.findAllByDealerGroupId(groupId, pr);
    }

    @Override
    public GroupSharing getGroupSharingByGroupSharingDetailsContaining(String sharingDetailId) {
        return baseRepository.findByDataStatusAndGroupSharingDetailsContaining(DataStatus.SAVE, sharingDetailId);
    }

    @Override
    public GroupSharing getGroupSharingByGroupId(String groupId,String saleMonth) {
        return baseRepository.findByDataStatusAndDealerGroupIdAndMonth(DataStatus.SAVE,groupId,saleMonth);
    }

    @Override
    public Page<GroupSharing> getAvaliablePageData(Integer currentPage, String month, String groupId) {
        Criteria criteria = new Criteria();
        if (month != null && !"".equals(month)) {
            criteria.and("month").is(month);
        }
        if (groupId != null && !"".equals(groupId)) {
            criteria.and("dealerGroupId").is(groupId);
        }
        Query query = new Query();
        Pageable p = new PageRequest(currentPage, 20);
        query.addCriteria(criteria).with(p);
        List<GroupSharing> data = mongoTemplate.find(query, GroupSharing.class);
        return new PageImpl<GroupSharing>(data, p, mongoTemplate.count(query, GroupSharing.class));
    }
}
