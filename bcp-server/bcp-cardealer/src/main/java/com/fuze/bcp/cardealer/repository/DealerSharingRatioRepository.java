package com.fuze.bcp.cardealer.repository;

import com.fuze.bcp.cardealer.domain.DealerSharingRatio;
import com.fuze.bcp.repository.BaseRepository;


public interface DealerSharingRatioRepository extends BaseRepository<DealerSharingRatio, String> {

    DealerSharingRatio findOneByCarDealerId(String carDealerId);

}
