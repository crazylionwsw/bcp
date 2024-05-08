package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.CreditPhotograph;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Lily on 2017/7/19.
 */
public interface ICreditPhotographService extends IBaseBillService<CreditPhotograph> {

    Page<CreditPhotograph> findAllByUploadFinishOrderByTsDesc(Boolean uploadFinish, Integer currentPage);

    Page<CreditPhotograph> findAllByUploadFinishAndCustomerIdIn(Boolean uploadFinish, List<String> customerIds, Integer currentPage);

    Page<CreditPhotograph> findAllFinished(String bankid, Integer pageIndex, Integer pageSize);

    List<CreditPhotograph> findAllUnFinished(String bankid);
}
