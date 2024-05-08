package com.fuze.bcp.msg.business;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.fuze.bcp.api.msg.bean.Channels;
import com.fuze.bcp.api.msg.bean.FeedbackBean;
import com.fuze.bcp.api.msg.bean.NoticeBean;
import com.fuze.bcp.api.msg.bean.SubSribeSourceBean;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.api.msg.service.ISubscribeBizService;
import com.fuze.bcp.bean.ResultBean;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by CJ on 2017/8/24.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class BizMessageServiceTest {

    @Autowired
    IMessageBizService iMessageBizService;

    @Autowired
    ISubscribeBizService iSubscribeBizService;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testSaveSubScribeSource() {

        SubSribeSourceBean subSribeSourceBean = new SubSribeSourceBean();
        subSribeSourceBean.setName("刘福强");
        subSribeSourceBean.setEmail("415554457@QQ.com");
        subSribeSourceBean.setPhoneNo("18811167384");
        subSribeSourceBean.setVcharNo("abc");
        iSubscribeBizService.actSaveSubScribeSource(subSribeSourceBean);

    }

    @Test
    public void testGetByGroup() {
        Object obj = iSubscribeBizService.actGetSourceByGroup("123");
        System.out.println(obj);
    }

    @Test
    public void testGetGroup() {
        Object obj = iSubscribeBizService.actLookupSubSribeSourcesGruop();
        System.out.println(obj);
    }

    @Test
    public void testA() throws ClassNotFoundException {
        ReferenceBean referenceBean = new ReferenceBean();
        referenceBean.setInterface(com.fuze.bcp.api.msg.service.IMessageBizService.class);
        referenceBean.setApplicationContext(applicationContext);

        try {
            referenceBean.afterPropertiesSet();
            IMessageBizService demoService = (IMessageBizService) referenceBean.get();
            ResultBean resultBean = demoService.actGetMessageTemplates(0);
            System.out.print(resultBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, TemplateException {
        StringTemplateLoader stl = new StringTemplateLoader();
        String template = "业务通知：客户[${customer.name}]的征信查询\n" +
                "<#if (approveStatus==2)>\n" +
                "  已通过，请尽快进行下一步审核。\n" +
                "<#elseif (approveStatus==8)>\n" +
                "  已驳回，请重新提交！\n" +
                "<#elseif (approveStatus==9)>\n" +
                "  未通过审核！\n" +
                "</#if>";
        stl.putTemplate("hello", template);
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(stl);
        Template temp = configuration.getTemplate("hello");
        StringWriter writer = new StringWriter();
        Map map = new HashMap();
        Map map1 = new HashMap();
        map1.put("name", "lili");
        map.put("customer", map1);

        map.put("approveStatus", 2);
        temp.process(map, writer);
        System.out.println(writer.toString());

    }

    @Test
    public void testSaveFeedback() {

        FeedbackBean feedbackBean = new FeedbackBean();
        feedbackBean.setTitle("title2");
        feedbackBean.setContent("content2");
//        feedbackBean.setLaunch(0);
//        feedbackBean.setLoginUserId("loginUserId2");
        iMessageBizService.actSaveFeedBack(feedbackBean);

    }

    @Test
    public void testGetFeedback() {

        FeedbackBean feedbackBean = new FeedbackBean();
        feedbackBean.setTitle("title");
        feedbackBean.setContent("content");
//        feedbackBean.setLaunch(0);
//        feedbackBean.setLoginUserId("loginUserId");
        ResultBean resultBean = iMessageBizService.actGetFeedBacks(0);
        System.out.println(resultBean);

    }

    @Test
    public void testFeedback() {
        FeedbackBean feedbackBean = new FeedbackBean();
        feedbackBean.setContent("feedback132324667");
//        feedbackBean.setLaunch(1);
//        feedbackBean.setLoginUserId("loginUserId");
//        feedbackBean.setRootId("59ed8a323a67c22e68cde101");
//        feedbackBean.setParentId("59ed8c883a67c22e68cde108");
        iMessageBizService.actSaveFeedBack(feedbackBean);

    }

    @Test
    public void testDetail() {

        NoticeBean notice = new NoticeBean();
        notice.setTitle("测试数据标题");
        notice.setContent("测试数据内容");
        notice.setSendType(NoticeBean.PAD);
        Set<String> loginUserIds = new HashSet<>();
        loginUserIds.add("15010779296");
        notice.setFromGroup(1);
        notice.setLoginUserNames(loginUserIds);
        iMessageBizService.actSaveNotice(notice);
//        List feedBack = iMessageBizService.actDetailFeedback("59ed8a323a67c22e68cde101").getD();
//        System.out.println(feedBack);

    }

    @Test
    public void testGetMyNitice() {

        ResultBean resultBean = iMessageBizService.actGetMyNoticeForPad(0, 20,NoticeBean.TYPE_1, Channels.PAD, "13521872164");
        System.out.println(resultBean);

    }

}
