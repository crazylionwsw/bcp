package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.GuaranteeWay;
import com.fuze.bcp.bd.repository.GuaranteeWayRepository;
import com.fuze.bcp.bd.service.IGuaranteeWayService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/6/12.
 */
@Service
public class GuaranteeWayServiceImpl extends BaseDataServiceImpl<GuaranteeWay, GuaranteeWayRepository> implements IGuaranteeWayService {

    @Autowired
    GuaranteeWayRepository guaranteeWayRepository;

    /**
     * 担保方式编码升序排列
     * @param currentPage
     * @return
     */
    @Override
    public Page<GuaranteeWay> getAllOrderByAsc(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage,20);
        return guaranteeWayRepository.findAllByOrderByCodeAsc(pr);
    }
}
