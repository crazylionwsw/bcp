package com.fuze.bcp.msg.repository;

import com.fuze.bcp.msg.domain.MessageTemplate;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

/**
 * Created by CJ on 2017/7/19.
 */
public interface MessageTemplateRepository extends BaseRepository<MessageTemplate, String> {
    List<MessageTemplate> findByDataStatusAndBusinessType(Integer save, String businessType);
}
