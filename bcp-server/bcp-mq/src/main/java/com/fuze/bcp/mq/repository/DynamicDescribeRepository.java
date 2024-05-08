package com.fuze.bcp.mq.repository;

import com.fuze.bcp.mq.domain.DynamicDescribe;
import com.fuze.bcp.repository.BaseDataRepository;

import java.util.List;

/**
 * Created by CJ on 2017/7/12.
 */
public interface DynamicDescribeRepository extends BaseDataRepository<DynamicDescribe, String> {
    List<DynamicDescribe> findByTaskId(String id);

    DynamicDescribe findByNeededResourceId(String id);
}
