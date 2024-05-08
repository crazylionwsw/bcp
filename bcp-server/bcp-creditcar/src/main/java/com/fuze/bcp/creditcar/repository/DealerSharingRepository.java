package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.DealerSharing;
import com.fuze.bcp.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface DealerSharingRepository extends BaseRepository<DealerSharing,String> {

    DealerSharing findOneByMonthAndCarDealerId(String month, String carDealerId);

    List<DealerSharing> findByMonthAndCarDealerId(String month, String carDealerId);

    List<DealerSharing> findByMonthAndChannelIdInOrderByChannelId(String month, List<String> employeeIds);

    List<DealerSharing> findByMonthOrderByChannelId(String month);

    DealerSharing findByDataStatusAndSharingDetailIdsContaining(Integer save, String sharingDetailId);

    List<DealerSharing> findAllByDataStatusAndMonth(Integer save, String month);

    List<DealerSharing> findAllByDataStatusAndMonthAndCarDealerId(Integer save, String month, String cardealerId);

    Collection<? extends DealerSharing> findAllByDataStatusAndMonthAndCarDealerIdIn(Integer save, String month, List cardealerIds);

    Page<DealerSharing> findAllByDataStatusAndMonthAndCarDealerIdIn(Integer save, String month, List cardealerIds, Pageable pageable);
}
