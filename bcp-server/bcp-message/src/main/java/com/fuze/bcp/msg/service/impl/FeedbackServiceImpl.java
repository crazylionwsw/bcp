package com.fuze.bcp.msg.service.impl;

import com.fuze.bcp.msg.domain.Feedback;
import com.fuze.bcp.msg.repository.FeedbackRepository;
import com.fuze.bcp.msg.service.IFeedbackService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/10/23.
 */
@Service
public class FeedbackServiceImpl extends BaseServiceImpl<Feedback, FeedbackRepository> implements IFeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Override
    public Page<Feedback> getAllOrderByTs(Integer currentPage) {
        PageRequest pageRequest = new PageRequest(currentPage, 20);
        return feedbackRepository.findAllByOrderByTsDesc(pageRequest);
    }

//    @Override
//    public Page<Feedback> getAvaliableBaseAll(Integer currentPage) {
//        PageRequest pageRequest = new PageRequest(currentPage, 20);
//        return baseRepository.findAllByLaunch(0, pageRequest);
//    }

//    @Override
//    public List<Feedback> getFeedbackListByRootId(String rootId) {
//        return baseRepository.findByRootId(rootId);
//    }
//
//    @Override
//    public Page<Feedback> getAvaliableBaseAll(Integer currentPage, String loginUsrId) {
//        PageRequest pageRequest = new PageRequest(currentPage, 10);
//        return baseRepository.findAllByLaunchAndLoginUserId(0, loginUsrId, pageRequest);
//    }

}
