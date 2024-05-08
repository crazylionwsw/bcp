package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.CarValuation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Lily on 2017/8/14.
 */
public interface CarValuationRepository extends BaseBillRepository<CarValuation,String> {

    CarValuation findAllByCarDealerIdAndFinishOrder(String carDealerId, boolean b);

    CarValuation findOneByVin(String vin);

    CarValuation findOneByVinOrderByTsDesc(String vin);

    CarValuation findOneByVinAndDataStatus(String vin, Integer ds);

    List<CarValuation> findAllByCarDealerIdAndFinishOrderAndDataStatus(String carDealerId, boolean b, Integer ds);

    List<CarValuation> findAllByCarDealerIdAndApproveStatusIn(String carDealerId,List<Integer> as);

    Page<CarValuation> findAllByFinishOrderAndDataStatus(boolean b, Integer ds, Pageable pageable);

    Page<CarValuation> findAllByFinishOrderAndDataStatusAndLoginUserId(boolean b, Integer ds, String loginUserId, Pageable pageable);

    Page<CarValuation> findAllByApproveStatusInAndLoginUserId(List<Integer> as, String loginUserId, Pageable pageable);

    CarValuation findOneByCarTypeIdAndVin(String carTypeId, String vin);

    Page<CarValuation> findAllByApproveStatusOrderByTsDesc(Integer approveStatus,Pageable pageable);
}
