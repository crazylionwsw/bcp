package com.fuze.bcp.mq.service;

import com.fuze.bcp.mq.domain.TaskDescribe;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by CJ on 2017/7/12.
 */
public interface ITaskDescribeService extends IBaseDataService<TaskDescribe> {
    List<TaskDescribe> getByDataStatusAndType(String type);

    TaskDescribe getByDynamicDescribeId(String id);

    Page<TaskDescribe> getAllOrderByType(Integer currentPage);
}
