package com.fuze.bcp.msg.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.entity.StringTemplateSource;
import com.fuze.bcp.msg.domain.MessageTemplate;
import com.fuze.bcp.msg.repository.MessageTemplateRepository;
import com.fuze.bcp.msg.service.IMessageTemplateService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * Created by CJ on 2017/7/19.
 */
@Service
public class MessageTemplateServiceImpl extends BaseServiceImpl<MessageTemplate, MessageTemplateRepository> implements IMessageTemplateService {


    @Autowired
    MessageTemplateRepository messageTemplateRepository;

    private Logger logger = LoggerFactory.getLogger(MessageTemplateServiceImpl.class);

    @Override
    public Object findTemplateSource(String id) {
        try {
            MessageTemplate template = this.getOne(id);
            if (template == null) {
                return null;
            }
            return new StringTemplateSource(id, template.getTemplateContent(), DateTimeUtils.getSimpleDateFormat().parse(template.getTs()).getTime());
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public long getLastModified(Object templateSource) {
        return ((StringTemplateSource) templateSource).getLastModified();
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) {
        return new StringReader(((StringTemplateSource) templateSource).getSource());
    }

    @Override
    public void closeTemplateSource(Object templateSource) {
        //do nothing
    }

    @Override
    public List<MessageTemplate> getTemplateByDataStatusAndBusinessType(String businessType) {
        return messageTemplateRepository.findByDataStatusAndBusinessType(DataStatus.SAVE, businessType);
    }

    @Override
    public Page<MessageTemplate> getAllOrderByBusinessType(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20, new Sort(Sort.Direction.ASC, "businessType"));
        return messageTemplateRepository.findAll(pr);
    }

}
