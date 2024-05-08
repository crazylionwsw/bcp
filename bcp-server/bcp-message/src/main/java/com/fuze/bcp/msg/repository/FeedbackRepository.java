package com.fuze.bcp.msg.repository;

import com.fuze.bcp.msg.domain.Feedback;
import com.fuze.bcp.msg.domain.MessageLog;
import com.fuze.bcp.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by CJ on 2017/10/23.
 */
public interface FeedbackRepository extends BaseRepository<Feedback, String> {

//    Page<Feedback> findAllByLaunch(int i, Pageable pageRequest);
//
//    List<Feedback> findByRootId(String rootId);
//
//    Page<Feedback> findAllByLaunchAndLoginUserId(int i, String loginUsrId, Pageable pageRequest);

}
