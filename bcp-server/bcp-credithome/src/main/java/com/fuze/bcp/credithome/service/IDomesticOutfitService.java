package com.fuze.bcp.credithome.service;

import com.fuze.bcp.credithome.domain.DomesticOutfit;
import org.springframework.data.domain.Page;

/**
 * Created by ZQW on 2018/3/19.
 */
public interface IDomesticOutfitService  extends IHomeBillService<DomesticOutfit>{

    DomesticOutfit findByCustomerTransactionId(String id);

    Page<DomesticOutfit> findAllByLoginUser(String loginUserId, Integer currentPage, Integer pageSize, Boolean isPass);
}
