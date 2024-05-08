package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.DealerSharing;
import com.fuze.bcp.service.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * Created by Lily on 2017/9/23.
 */
public interface IDealerSharingService extends IBaseService<DealerSharing> {

    DealerSharing getByMonthAndCarDealer(String month, String carDealerId);

    List<DealerSharing> getByMonthAndChannelIds(String month, List<String> channelIds);

    List<DealerSharing> getByMonth(String month);

    DealerSharing getDealerSharingBySharingDetailIdsContaining(String sharingDetailId);

    List<DealerSharing> getAvaliableAllByMonth(String month);

    Collection<? extends DealerSharing> getAvaliableAllByMonthAndCarDealerId(String month, String cardealerId);

    Collection<? extends DealerSharing> getAvaliableAllByMonthAndCarDealerIdIn(String month, List cardealerIds);

    Page<DealerSharing> getAvaliablePageData(Integer currentPage, String month, List carDealerIds);


//    List<DealerSharing> getByMonthAndGroup(String month, String groupId);

}
