package com.fuze.bcp.mq.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.mq.domain.TaskDescribe;
import com.fuze.bcp.mq.repository.TaskDescribeRepository;
import com.fuze.bcp.mq.service.ITaskDescribeService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/7/12.
 */
@Service
public class TaskDescribeServiceImpl extends BaseDataServiceImpl<TaskDescribe, TaskDescribeRepository> implements ITaskDescribeService{

    @Autowired
    TaskDescribeRepository taskDescribeRepository;

    @Override
    public List<TaskDescribe> getByDataStatusAndType(String type) {
        return taskDescribeRepository.findByDataStatusAndType(DataStatus.SAVE, type);
    }

    @Override
    public TaskDescribe getByDynamicDescribeId(String id) {
        return taskDescribeRepository.findByDynamicDescribeId(id);
    }

    @Override
    public Page<TaskDescribe> getAllOrderByType(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20, new Sort(Sort.Direction.ASC, "type"));
        return taskDescribeRepository.findAll(pr);
    }
}
