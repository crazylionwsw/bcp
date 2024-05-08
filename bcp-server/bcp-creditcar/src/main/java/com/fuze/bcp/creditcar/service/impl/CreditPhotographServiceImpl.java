package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.CreditPhotograph;
import com.fuze.bcp.creditcar.repository.CreditPhotographRepository;
import com.fuze.bcp.creditcar.service.ICreditPhotographService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lily on 2017/7/19.
 */
@Service
public class CreditPhotographServiceImpl extends BaseBillServiceImpl<CreditPhotograph, CreditPhotographRepository> implements ICreditPhotographService {

    @Autowired
    CreditPhotographRepository creditPhotographRepository;

    @Override
    public Page<CreditPhotograph> findAllByUploadFinishOrderByTsDesc(Boolean uploadFinish, Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage,20);
        return creditPhotographRepository.findAllByUploadFinishOrderByTsDesc(uploadFinish, pr);
    }

    @Override
    public Page<CreditPhotograph> findAllByUploadFinishAndCustomerIdIn(Boolean uploadFinish, List<String> customerIds, Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage,20);
        return creditPhotographRepository.findAllByUploadFinishAndCustomerIdIn(uploadFinish,customerIds,pr);
    }

    @Override
    public Page<CreditPhotograph> findAllFinished(String bankid, Integer pageIndex, Integer pageSize) {
        PageRequest pr = new PageRequest(pageIndex,pageSize,new Sort(Sort.Direction.DESC, "ts"));
        return creditPhotographRepository.findAllByCashSourceIdAndUploadFinish(bankid,true,pr);
    }

    @Override
    public List<CreditPhotograph> findAllUnFinished(String bankid) {
        return creditPhotographRepository.findAllByCashSourceIdAndUploadFinish(bankid,false,CreditPhotograph.getTsSort());
    }

}
