package com.fuze.bcp.msg.repository;

import com.fuze.bcp.msg.domain.MessageSubScribe;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

/**
 * Created by CJ on 2017/7/21.
 */
public interface MessageSubscribeRepository extends BaseRepository<MessageSubScribe, String>{

    List<MessageSubScribe> findByDataStatusAndBusinessType(Integer dataStatus, String business);

}
