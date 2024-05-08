package com.fuze.bcp.cardealer.service.impl;

import com.fuze.bcp.cardealer.domain.DealerSharingRatio;
import com.fuze.bcp.cardealer.repository.DealerSharingRatioRepository;
import com.fuze.bcp.cardealer.service.IDealerSharingRatioService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2017/7/10.
 */
@Service
public class DealerSharingRatioServiceImpl extends BaseServiceImpl<DealerSharingRatio, DealerSharingRatioRepository> implements IDealerSharingRatioService {

    @Override
    public DealerSharingRatio save(DealerSharingRatio t) {
        DealerSharingRatio exist = baseRepository.findOneByCarDealerId(t.getCarDealerId());
        if (exist != null)
            t.setId(exist.getId());
        return super.save(t);
    }


    public DealerSharingRatio getCarDealerRatio(String carDealerId) {
        return baseRepository.findOneByCarDealerId(carDealerId);
    }
}
