package com.fuze.bcp.msg.service;

import com.fuze.bcp.msg.domain.Feedback;
import com.fuze.bcp.service.IBaseService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by CJ on 2017/10/23.
 */
public interface IFeedbackService extends IBaseService<Feedback> {

//    Page<Feedback> getAvaliableBaseAll(Integer currentPage);

//    List<Feedback> getFeedbackListByRootId(String rootId);
//
//    Page<Feedback> getAvaliableBaseAll(Integer currentPage, String loginUsrId);

    Page<Feedback> getAllOrderByTs(Integer currentPage);
}
