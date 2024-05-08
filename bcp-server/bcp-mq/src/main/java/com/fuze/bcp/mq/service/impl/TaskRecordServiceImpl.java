package com.fuze.bcp.mq.service.impl;

import com.fuze.bcp.mq.domain.TaskRecord;
import com.fuze.bcp.mq.repository.TaskRecordRepository;
import com.fuze.bcp.mq.service.ITaskRecordService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/7/12.
 */
@Service
public class TaskRecordServiceImpl extends BaseServiceImpl<TaskRecord, TaskRecordRepository> implements ITaskRecordService {
}
