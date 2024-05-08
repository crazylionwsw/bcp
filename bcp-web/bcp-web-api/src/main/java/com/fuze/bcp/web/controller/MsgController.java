package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.msg.bean.Channels;
import com.fuze.bcp.api.msg.bean.MessageSubScribeBean;
import com.fuze.bcp.api.msg.bean.MessageTemplateBean;
import com.fuze.bcp.api.msg.bean.NoticeBean;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.api.msg.service.ISubscribeBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/7/21.
 */
@RestController
@RequestMapping(value = "/json")
public class MsgController {

    @Value("${web.websocket.url}")
    private String webSocketUrl;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ISubscribeBizService iSubscribeBizService;

    @Autowired
    IMessageBizService iMessageBizService;

    @RequestMapping(value = "/messageTemplate/test", method = RequestMethod.GET)
    public ResultBean getMessageByTemplate(){
        return null;
    }

    /**
     * 获取所有的订阅组
     * @return
     */
    @RequestMapping(value = "/msg/subscribe/groups", method = RequestMethod.GET)
    public ResultBean getSubScribeGroups(){
        return iSubscribeBizService.actLookupSubSribeSourcesGruop();
    }

    /**
     * 获取订阅组下相应的订阅源
     * @param groupName
     * @return
     */
    @RequestMapping(value = "/msg/subscribe/source", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSubScribeSourceByGroups(@RequestParam(value = "groupName", defaultValue = "") String groupName){
        return iSubscribeBizService.actGetSourceByGroup(groupName);
    }

    @RequestMapping(value = "/msg/subscribe/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveSubscribe(@RequestBody MessageSubScribeBean messageSubScribeBean) {
        return iSubscribeBizService.actSaveSubscribe(messageSubScribeBean);
    }

    @RequestMapping(value = "/msg/subscribe/{id}", method = RequestMethod.GET)
    public ResultBean<MessageSubScribeBean> getMessageSubscribe(@PathVariable("id") String id) {
        return iSubscribeBizService.actGetMessageSubScribe(id);
    }

    @RequestMapping(value = "/msg/subscribe/{id}", method = RequestMethod.DELETE)
    public ResultBean<MessageSubScribeBean> deleteMessageSubscribe(@PathVariable("id") String id) {
        return iSubscribeBizService.actDeleteSubscribe(id);
    }

    @RequestMapping(value = "/msg/subscribes", method = RequestMethod.GET)
    public ResultBean<DataPageBean<MessageSubScribeBean>> getMessageSubscribe(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iSubscribeBizService.actGetMessageSubScribes(currentPage);
    }

    //订阅维护查询
    @RequestMapping(value = "/msg/subscribe/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean searchMessageSubscribe(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,@RequestBody MessageSubScribeBean messageSubScribeBean){
        return iMessageBizService.searchMessageSubScribes(currentPage,messageSubScribeBean);
    }


    @RequestMapping(value = "/msg/messageTemplate/{messageTemplateId}", method = RequestMethod.DELETE)
    public ResultBean deleteMessageTempLates(@PathVariable String messageTemplateId) {
        return iMessageBizService.actDeleteMessageTemplate(messageTemplateId);
    }

    /**
     * 消息模版(带分页)
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/msg/messageTemplates", method = RequestMethod.GET)
    public ResultBean getMessageTempLates(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iMessageBizService.actGetMessageTemplates(currentPage);
    }

    @RequestMapping(value = "/msg/messageTemplate/{messageTemplateId}", method = RequestMethod.GET)
    public ResultBean getMessageTempLate(@PathVariable String messageTemplateId) {
        return iMessageBizService.actGetMessageTemplate(messageTemplateId);
    }

    @RequestMapping(value = "/msg/messageTemplate/save", method = RequestMethod.POST)
    public ResultBean saveMessageTempLate(@RequestBody MessageTemplateBean messageTemplateBean) {
        return iMessageBizService.actSaveMessageTemplate(messageTemplateBean);
    }

    @RequestMapping(value = "/msg/messageTemplate/lookupTaskType", method = RequestMethod.GET)
    public ResultBean getBusinessEventType(){
        return iBaseDataBizService.actLookupBusinessEventTypes();
    }


    @RequestMapping(value = "/msg/messageTemplate/lookupchannels", method = RequestMethod.GET)
    public ResultBean lookUpChannels(){
        List list = new ArrayList<>();
        Map map = new HashMap<>();
        map.put("label", "WEB页面消息");
        map.put("code", Channels.WEBSOCKET);
        list.add(map);

        map = new HashMap<>();
        map.put("label", "邮箱");
        map.put("code", Channels.EMAIL);
        list.add(map);

        map = new HashMap<>();
        map.put("label", "移动APP");
        map.put("code", Channels.MOBILE);
        list.add(map);

        map = new HashMap<>();
        map.put("label", "PAD推送");
        map.put("code",Channels.PAD);
        list.add(map);

        map = new HashMap<>();
        map.put("label", "短信");
        map.put("code",Channels.SMS);
        list.add(map);

        map = new HashMap<>();
        map.put("label", "微信");
        map.put("code",Channels.WECHAT);
        list.add(map);

        return ResultBean.getSucceed().setD(list);
    }

    /**
     * 根据多个ID获取多个对象
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/subsribesources/pickup", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getSubsribesourceByIds(@RequestBody List<String> ids) {
        return iSubscribeBizService.actGetSubSribeSourceByIds(ids);
    }

    @RequestMapping(value = "/msg/log/{id}", method = RequestMethod.GET)
    public ResultBean getMessageLogs(@PathVariable("id") String id) {
        return iMessageBizService.actGetMessageLog(id);
    }

    @RequestMapping(value = "/msg/logs/{ids}", method = RequestMethod.GET)
    public ResultBean getMessageLogs(@PathVariable("ids") List<String> ids) {
        return iMessageBizService.actGetMessageLogs(ids);
    }

    //意见反馈列表(分页)
    @RequestMapping(value = "/feedbacks",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getFeedBacks(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage){
        return iMessageBizService.actGetFeedBacks(currentPage);
    }

    //删除意见反馈
    @RequestMapping(value = "/feedback/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteFeedBack(@PathVariable String id){
        return iMessageBizService.actDeleteFeedback(id);
    }

    /**
     *通知管理(分页)
     */
    @RequestMapping(value = "/notices",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getNotics(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage){
        return iMessageBizService.actGetNotices(currentPage);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/notice",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveNotice(@RequestBody NoticeBean noticeBean){
        return iMessageBizService.actSaveNotice(noticeBean);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/notice/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteNotice(@PathVariable("id") String id){
        return iMessageBizService.actDeleteNotice(id);
    }

    @RequestMapping(value = "/websocket", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerSurvey() {
        return ResultBean.getSucceed().setD(webSocketUrl);
    }
}
