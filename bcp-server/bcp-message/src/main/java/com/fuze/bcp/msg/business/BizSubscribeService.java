package com.fuze.bcp.msg.business;

import com.fuze.bcp.api.msg.bean.MessageSubScribeBean;
import com.fuze.bcp.api.msg.bean.SubSribeSourceBean;
import com.fuze.bcp.api.msg.service.ISubscribeBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.msg.domain.MessageSubScribe;
import com.fuze.bcp.msg.domain.SubSribeSource;
import com.fuze.bcp.msg.service.IMessageSubscribeService;
import com.fuze.bcp.msg.service.ISubScribeSourceService;
import com.fuze.bcp.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by CJ on 2017/7/21.
 */
@Service
public class BizSubscribeService implements ISubscribeBizService {

    @Autowired
    MappingService mappingService;

    @Autowired
    IMessageSubscribeService iMessageSubscribeService;

    @Autowired
    ISubScribeSourceService iSubScribeSourceService;

    @Override
    public ResultBean<MessageSubScribeBean> actSaveSubscribe(MessageSubScribeBean messageSubScribeBean) {
        MessageSubScribe messageSubScribe = mappingService.map(messageSubScribeBean, MessageSubScribe.class);
        messageSubScribe.setUserChannel(messageSubScribeBean.getUserChannel());
        messageSubScribe = iMessageSubscribeService.save(messageSubScribe);
        MessageSubScribeBean bean = mappingService.map(messageSubScribe, MessageSubScribeBean.class);
        bean.setUserChannel(messageSubScribe.getUserChannel());
        return ResultBean.getSucceed().setD(bean);
    }

    @Override
    public ResultBean<MessageSubScribeBean> actGetMessageSubScribe(String subScribeId) {
        MessageSubScribe messageSubScribe = iMessageSubscribeService.getOne(subScribeId);
        if (messageSubScribe != null) {
            MessageSubScribeBean bean = mappingService.map(messageSubScribe, MessageSubScribeBean.class);
            bean.setUserChannel(messageSubScribe.getUserChannel());
            return ResultBean.getSucceed().setD(bean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<MessageSubScribeBean>> actGetMessageSubScribes(Integer currentPage) {
        Page<MessageSubScribe> messageSubScribes = iMessageSubscribeService.getAll(currentPage);
        Page<MessageSubScribeBean> messageSubScribeBean = messageSubScribes.map(new Converter<MessageSubScribe, MessageSubScribeBean>() {
            @Override
            public MessageSubScribeBean convert(MessageSubScribe messageSubScribe) {
                MessageSubScribeBean messageSubScribeBean = mappingService.map(messageSubScribe, MessageSubScribeBean.class);
                messageSubScribeBean.setUserChannel(messageSubScribe.getUserChannel());
                return messageSubScribeBean;
            }
        });
        DataPageBean<MessageSubScribeBean> destination = new DataPageBean<MessageSubScribeBean>();
        destination.setPageSize(messageSubScribeBean.getSize());
        destination.setTotalCount(messageSubScribeBean.getTotalElements());
        destination.setTotalPages(messageSubScribeBean.getTotalPages());
        destination.setCurrentPage(messageSubScribeBean.getNumber());
        for (MessageSubScribeBean t : messageSubScribeBean.getContent()) {
            destination.getResult().add(t);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<MessageSubScribeBean> actDeleteSubscribe(String subScribeId) {
        MessageSubScribe messageSubScribe = iMessageSubscribeService.delete(subScribeId);
        MessageSubScribeBean messageSubScribeBean = mappingService.map(messageSubScribe, MessageSubScribeBean.class);
        messageSubScribeBean.setUserChannel(messageSubScribe.getUserChannel());
        return ResultBean.getSucceed().setD(messageSubScribeBean);
    }

    @Override
    public ResultBean<List<MessageSubScribeBean>> actGetMessageSubScribeByBusinessType(String businessType) {
        List<MessageSubScribe> subScribes = iMessageSubscribeService.getListByBusinessType(businessType);
        List<MessageSubScribeBean> beans = new ArrayList<>();
        for (int i = 0; i < subScribes.size(); i++) {
            MessageSubScribe messageSubScribe = subScribes.get(i);
            MessageSubScribeBean bean = mappingService.map(messageSubScribe, MessageSubScribeBean.class);
            bean.setUserChannel(messageSubScribe.getUserChannel());
            beans.add(bean);
        }
        return ResultBean.getSucceed().setD(beans);
    }

    public ResultBean<SubSribeSourceBean> actSaveSubScribeSource(SubSribeSourceBean subSribeSourceBean) {
        SubSribeSource subSribeSource = mappingService.map(subSribeSourceBean, SubSribeSource.class);
        subSribeSource = iSubScribeSourceService.save(subSribeSource);
        return ResultBean.getSucceed().setD(mappingService.map(subSribeSource, SubSribeSourceBean.class));
    }

    public ResultBean<List<SubSribeSourceBean>> actGetSubscribeSources() {
        List<SubSribeSource> subSribeSources = iSubScribeSourceService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(subSribeSources, SubSribeSourceBean.class));
    }

    public ResultBean<SubSribeSourceBean> actGetSubscribeSource(String id) {
        SubSribeSource subSribeSource = iSubScribeSourceService.getAvailableOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(subSribeSource, SubSribeSourceBean.class));
    }

    @Override
    public ResultBean<DataPageBean<SubSribeSourceBean>> actGetSubscribeSources(Integer currentPage) {
        Page<SubSribeSource> subSribeSource = iSubScribeSourceService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(subSribeSource, SubSribeSourceBean.class));
    }

    @Override
    public ResultBean<SubSribeSourceBean> actDeleteSubSribeSource(String id) {
        SubSribeSource subSribeSource = iSubScribeSourceService.delete(id);
        SubSribeSourceBean subSribeSourceBean = mappingService.map(subSribeSource, SubSribeSourceBean.class);
        return ResultBean.getSucceed().setD(subSribeSourceBean);
    }

    @Override
    public ResultBean<List<SubSribeSourceBean>> actLookupSubSribeSources() {
        List<SubSribeSource> subSribeSources = iSubScribeSourceService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(subSribeSources, SubSribeSourceBean.class));
    }

    @Override
    public ResultBean<Set<String>> actLookupSubSribeSourcesGruop() {
        List<SubSribeSource> subSribeSources = iSubScribeSourceService.getAvaliableAll();
        Set<String> set = new HashSet<>();
        for (SubSribeSource s : subSribeSources) {
            set.add(s.getGroupName());
        }
        return ResultBean.getSucceed().setD(set);

    }

    @Override
    public ResultBean<List<SubSribeSourceBean>> actGetSourceByGroup(String groupName) {
        List<SubSribeSource> subSribeSources = iSubScribeSourceService.getByGroupName(groupName);
        return ResultBean.getSucceed().setD(mappingService.map(subSribeSources, SubSribeSourceBean.class));
    }

    @Override
    public ResultBean<List<SubSribeSourceBean>> actGetSubSribeSourceByIds(List<String> ids) {

        List names = iSubScribeSourceService.getAvaliableList(ids);

        return ResultBean.getSucceed().setD(mappingService.map(names, SubSribeSourceBean.class));
    }


}
