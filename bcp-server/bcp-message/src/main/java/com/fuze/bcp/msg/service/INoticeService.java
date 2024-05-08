package com.fuze.bcp.msg.service;

import com.fuze.bcp.msg.domain.Notice;
import com.fuze.bcp.service.IBaseService;
import org.springframework.data.domain.Page;

/**
 * Created by CJ on 2017/10/30.
 */
public interface INoticeService extends IBaseService<Notice> {

    Page<Notice> findByTypeAndSendTypeAndLoginUserName(Integer currentPage, Integer pageSize, Integer status, String type, String sendType, String loginUserName);

    Page<Notice> findByDataStatusAndStatusAndTypeAndSendType(Integer currentPage, Integer pageSize, Integer status, String type, String sendType);

    Page getAllByTsDesc(Integer currentPage);
}
