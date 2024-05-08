package com.fuze.bcp.mq.service.impl;

import com.fuze.bcp.mq.domain.MsgRecord;
import com.fuze.bcp.mq.repository.MsgRecordRepository;
import com.fuze.bcp.mq.service.IMsgRecordService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/7/12.
 */
@Service
public class MsgRecordServiceImpl extends BaseServiceImpl<MsgRecord, MsgRecordRepository> implements IMsgRecordService {
}
