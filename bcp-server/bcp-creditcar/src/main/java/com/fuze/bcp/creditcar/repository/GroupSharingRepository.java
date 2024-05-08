package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.GroupSharing;
import com.fuze.bcp.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupSharingRepository extends BaseRepository<GroupSharing,String> {

    GroupSharing findOneByMonthAndDealerGroupId(String month, String groupId);

    List<GroupSharing> findAllByMonthAndDealerGroupId(String month, String groupId);

    Page<GroupSharing> findAllByDealerGroupId(String groupId, Pageable page);

    List<GroupSharing> findAllByDealerGroupId(String groupId);

    GroupSharing findByDataStatusAndGroupSharingDetailsContaining(Integer save, String sharingDetailId);

    GroupSharing findByDataStatusAndDealerGroupIdAndMonth(Integer save,String groupId,String saleMonth);
}
