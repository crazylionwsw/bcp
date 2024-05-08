package com.fuze.bcp.credithome.repository;

import com.fuze.bcp.credithome.domain.DomesticOutfit;

/**
 * Created by ZQW on 2018/3/19.
 */
public interface DomesticOutfitRepository extends HomeBillRepository<DomesticOutfit,String>{

    DomesticOutfit findOneByCustomerTransactionId(String id);

    DomesticOutfit findOneByDataStatusAndId(Integer ds, String id);
}
