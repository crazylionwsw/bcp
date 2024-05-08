package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.GroupSharing;
import com.fuze.bcp.service.IBaseService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Lily on 2017/9/23.
 */
public interface IGroupSharingService extends IBaseService<GroupSharing> {

    GroupSharing getByMonthAndGroup(String month, String groupId);

    List<GroupSharing> getByGroup(String groupId);

    List<GroupSharing> getByMonthAndGroupId(String month, String groupId);

    Page<GroupSharing> getGroupSharing(String groupId, Integer currentPage);

    GroupSharing getGroupSharingByGroupSharingDetailsContaining(String sharingDetailId);

    GroupSharing getGroupSharingByGroupId(String groupId,String saleMonth);

    Page<GroupSharing> getAvaliablePageData(Integer currentPage, String month, String groupId);
}
