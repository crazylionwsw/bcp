package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardBean;
import com.fuze.bcp.api.creditcar.service.ISwipingCardBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.SwipingCard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ${Liu} on 2017/9/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class SwipingCardTest {

    @Autowired
    ISwipingCardBizService iSwipingCardBizService;

    @Test
    public void testSwiping(){

    }
}
