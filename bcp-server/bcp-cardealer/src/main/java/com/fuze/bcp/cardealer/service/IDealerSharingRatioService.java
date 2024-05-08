package com.fuze.bcp.cardealer.service;

import com.fuze.bcp.cardealer.domain.DealerSharingRatio;
import com.fuze.bcp.service.IBaseService;

/**
 * Created by user on 2017/7/10.
 */
public interface IDealerSharingRatioService extends IBaseService<DealerSharingRatio> {
    /**
     * 通过经销商ID获取返佣比例
     * @param carDealerId
     * @return
     */
    DealerSharingRatio getCarDealerRatio(String carDealerId);
}
