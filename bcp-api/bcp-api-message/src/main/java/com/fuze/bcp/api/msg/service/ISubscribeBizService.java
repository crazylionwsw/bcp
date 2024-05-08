package com.fuze.bcp.api.msg.service;

import com.fuze.bcp.api.msg.bean.MessageSubScribeBean;
import com.fuze.bcp.api.msg.bean.SubSribeSourceBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;
import java.util.Set;

/**
 * 消息订阅接口
 * Created by sean on 2017/5/23.
 */
public interface ISubscribeBizService {

    /**
     * 订阅的保存、修改功能
     *
     * @param messageSubScribeBean
     * @return
     */
    ResultBean<MessageSubScribeBean> actSaveSubscribe(MessageSubScribeBean messageSubScribeBean);

    ResultBean<MessageSubScribeBean> actGetMessageSubScribe(String subScribeId);

    ResultBean<DataPageBean<MessageSubScribeBean>> actGetMessageSubScribes(Integer currentPage);

    ResultBean<MessageSubScribeBean> actDeleteSubscribe(String subScribeId);

    ResultBean<List<MessageSubScribeBean>> actGetMessageSubScribeByBusinessType(String business);

    ResultBean<SubSribeSourceBean> actSaveSubScribeSource(SubSribeSourceBean subSribeSourceBean);

    ResultBean<List<SubSribeSourceBean>> actGetSubscribeSources();

    ResultBean<SubSribeSourceBean> actGetSubscribeSource(String id);

    ResultBean<DataPageBean<SubSribeSourceBean>> actGetSubscribeSources(Integer currentPage);

    ResultBean<SubSribeSourceBean> actDeleteSubSribeSource(String id);

    ResultBean<List<SubSribeSourceBean>> actLookupSubSribeSources();

    ResultBean<Set<String>> actLookupSubSribeSourcesGruop();

    ResultBean<List<SubSribeSourceBean>> actGetSourceByGroup(String groupName);

    /**
     * 根据ID列表获取
     *
     * @param ids
     * @return
     */
    ResultBean<List<SubSribeSourceBean>> actGetSubSribeSourceByIds(List<String> ids);


}
