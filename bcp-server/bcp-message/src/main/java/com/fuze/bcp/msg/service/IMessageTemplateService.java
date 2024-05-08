package com.fuze.bcp.msg.service;

import com.fuze.bcp.msg.domain.MessageTemplate;
import com.fuze.bcp.service.IBaseService;
import freemarker.cache.TemplateLoader;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by CJ on 2017/7/19.
 */
public interface IMessageTemplateService extends IBaseService<MessageTemplate>, TemplateLoader {
    List<MessageTemplate> getTemplateByDataStatusAndBusinessType(String messageType);

    Page<MessageTemplate> getAllOrderByBusinessType(Integer currentPage);
}
