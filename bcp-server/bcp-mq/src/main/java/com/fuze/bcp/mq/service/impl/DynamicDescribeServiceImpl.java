package com.fuze.bcp.mq.service.impl;

import com.fuze.bcp.mq.domain.DynamicDescribe;
import com.fuze.bcp.mq.repository.DynamicDescribeRepository;
import com.fuze.bcp.mq.service.IDynamicDescribeService;
import com.fuze.bcp.mq.service.ITaskDescribeService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/7/12.
 */
@Service
public class DynamicDescribeServiceImpl extends BaseDataServiceImpl<DynamicDescribe, DynamicDescribeRepository> implements IDynamicDescribeService {

    @Autowired
    DynamicDescribeRepository dynamicDescribeRepository;


    @Override
    public List<DynamicDescribe> findByTaskId(String id) {
        return dynamicDescribeRepository.findByTaskId(id);
    }

    @Override
    public DynamicDescribe findByNeededResource(String id) {
        return dynamicDescribeRepository.findByNeededResourceId(id);
    }
}
