package com.fuze.bcp.mq;

import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.utils.EncryUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lily on 2017/8/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class MqServiceTest {

    @Autowired
    IAmqpBizService bizAmqpService;

    @Test
    public void testAmqp() {
        try {
            bizAmqpService.actSendBusinessMsg("NC_A001_CustomerDemand_Submit", new Object[]{"58b92599e4b0b55390c49da7"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAmqp2() {
        ResultBean data = null;
        try {
            data = bizAmqpService.actGetTaskDescribeByType("NC_A001_SubmitCardemand");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(data);
    }

    @Test
    public void testSendEmail() {
        MsgRecordBean msgRecordBean = new MsgRecordBean();
        bizAmqpService.actSendMessageMsg(msgRecordBean);


    }

    @Test
    public void testPush() {
        MsgRecordBean msgRecordBean = new MsgRecordBean();
        Map map = new HashMap<>();
        map.put("afterOpenAction", "1");
        map.put("extraFields", new HashMap<>());
        msgRecordBean.setPushCtrlMap(map);
        Map templateMap = new HashMap<>();
        msgRecordBean.setTemplateData(templateMap);
        msgRecordBean.setTaskType("NC_A001_CustomerDemand_Submit");
        msgRecordBean.setTransactionId("59e0944f20e5994a7051bbf7");
        bizAmqpService.actSendMessageMsg(msgRecordBean);
    }

    @Test
    public void testSendNotice(){
        bizAmqpService.actSendNotice("59f7d6853a67c232909407ae");
    }

    @Test
    public void testSendOrderweb3j(){
        //签约通过后将交易信息添加到区块链中
        Map map = new HashMap<String,Object>();
        String hash = EncryUtil.MD5("小艳452102233652145210");
        map.put("eventType","BankCard_Swiping");
        map.put("Hash",hash);
        map.put("customerName","小艳");
        map.put("identifyNo","452102233652145210");
        map.put("creditAmount",111111.00);
        map.put("loanDate","2018-04-28 17:32:00");
        System.out.println("发送的hash值为"+hash);
        bizAmqpService.actSendWeb3jMsg(map);
    }

    @Test
    public void testSendDmvpweb3j(){
        //签约通过后将交易信息添加到区块链中
        Map map = new HashMap<String,Object>();
        String hash = EncryUtil.MD5("小艳452102233652145210");
        map.put("eventType","DMVP_PledgeEnd");
        map.put("Hash",hash);
        map.put("customerName","小艳");
        map.put("identifyNo","452102233652145210");
        map.put("creditAmount",111111.00);
        map.put("loanDate","2018-04-28 17:32:00");
        map.put("dmvpledgeDate","2018-04-28 17:42:00");
        System.out.println("发送的hash值为"+hash);
        bizAmqpService.actSendWeb3jMsg(map);
    }

}
