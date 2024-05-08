package com.fuze.bcp.mq.repository;

import com.fuze.bcp.mq.domain.TaskDescribe;
import com.fuze.bcp.repository.BaseDataRepository;

import java.util.List;

/**
 * Created by CJ on 2017/7/12.
 */
public interface TaskDescribeRepository extends BaseDataRepository<TaskDescribe,String> {
    List<TaskDescribe> findByType(String type);

    List<TaskDescribe> findByDataStatusAndType(Integer save, String type);

    TaskDescribe findByDynamicDescribeId(String id);
}
