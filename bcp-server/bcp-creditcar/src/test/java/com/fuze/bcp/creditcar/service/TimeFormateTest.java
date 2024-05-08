package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.creditcar.service.IBusinessExchangeBizService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Lily on 2017/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class TimeFormateTest {

//    @Autowired
//    ICustomerDemandService iCustomerDemandService;
//
//    @Autowired
//    IBankCardApplyBizService iBankCardApplyBizService;

//    @Autowired
//    IBusinessExchangeBizService iBusinessExchangeBizService;



//    @Test
//    public void Test(){
//        List<CustomerDemand> customerDemands = iCustomerDemandService.findAllByTsDesc(CustomerDemand.getTsSort());
//    }

//
//    @Test
//    public void getTimeTrans(){
//        String data = "2017-12-12 17:17:15.226";
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateString = f.format(data);
//        System.out.println(dateString);
//    }
//
//    @Test
//    public void getLength(){
//        String text = "宝马 宝马X6 2017 款 3.0T 自动 xDrive35i M运动豪华型";
//        int len = text.toString().length();
//        System.out.println(len);
////        if(len > 18){
////
////        }else {
////
////            String apace = " ";
////            String text1 = text.concat(apace);
////            int len1 = text1.toString().length();
////
////            System.out.println("----------------->len1:"+len1);
////        }
//
//    }
//
//    @Test
//    public void getFontSize(){
//        Font f = new Font("宋体", Font.BOLD, 12);
//        FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
//        // 高度
//        System.out.println(fm.getHeight());
//        // 单个字符宽度
//        System.out.println(fm.charWidth('A'));
//        // 整个字符串的宽度
//        System.out.println(fm.stringWidth("宋A"));
//    }
    private String str = "helloWorld";

    @Test
    public void getFees(){
        Jedis jedis = new Jedis("127.0.0.1");
        System.out.println("redis数据库"+jedis.ping());

    }


}
