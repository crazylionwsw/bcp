package com.fuze.bcp.msg.service.impl;

import com.fuze.bcp.api.msg.bean.NoticeBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.msg.domain.Notice;
import com.fuze.bcp.msg.repository.NoticeRepository;
import com.fuze.bcp.msg.service.INoticeService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/10/30.
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<Notice, NoticeRepository> implements INoticeService {
    @Override
    public Page<Notice> findByTypeAndSendTypeAndLoginUserName(Integer currentPage, Integer pageSize, Integer status, String type, String sendType, String loginUserName) {
        Pageable pageable = new PageRequest(currentPage, pageSize, new Sort(Sort.Direction.DESC, "sendTime")); // Containing
        return baseRepository.findByDataStatusAndStatusAndTypeAndSendTypeAndLoginUserNamesContaining(DataStatus.SAVE, status, type, sendType, loginUserName, pageable);
    }

    public Page<Notice> findByDataStatusAndStatusAndTypeAndSendType(Integer currentPage, Integer pageSize, Integer status, String type, String sendType){
        Pageable pageable = new PageRequest(currentPage, pageSize, new Sort(Sort.Direction.DESC, "sendTime"));
        return baseRepository.findByDataStatusAndStatusAndTypeAndSendType(DataStatus.SAVE, status, type, sendType, pageable);
    }

    @Override
    public Page getAllByTsDesc(Integer currentPage) {
        Pageable pageable = new PageRequest(currentPage, 20, new Sort(Sort.Direction.DESC, "ts"));
        return baseRepository.findByDataStatus(DataStatus.SAVE, pageable);
    }

}
