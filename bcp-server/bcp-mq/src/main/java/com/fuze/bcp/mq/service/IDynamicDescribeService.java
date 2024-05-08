package com.fuze.bcp.mq.service;

import com.fuze.bcp.mq.domain.DynamicDescribe;
import com.fuze.bcp.mq.domain.TaskDescribe;
import com.fuze.bcp.mq.repository.DynamicDescribeRepository;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by CJ on 2017/7/12.
 */
public interface IDynamicDescribeService extends IBaseDataService<DynamicDescribe> {
    List<DynamicDescribe> findByTaskId(String id);

    DynamicDescribe findByNeededResource(String id);
}
