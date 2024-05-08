package com.fuze.bcp.customer.service;


import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.IdcardUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by zqw on 2017/9/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class CustomerTest {

    /*@Autowired
    TemplateService templateService;*/

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void parseCustomerIdentifyNo() throws ParseException {

        CustomerLoanBean loan = iCustomerBizService.actGetCustomerLoanByTransactionId("59f855cfba2a5d7a5643b11e").getD();

        CustomerCarBean car = iCustomerBizService.actGetCustomerCarByTransactionId("59f855cfba2a5d7a5643b11e").getD();

        String idCardNo = "34122219810405683X";

        int ageByIdCard = IdcardUtils.getAgeByIdCard(idCardNo);
        String birthByIdCard = IdcardUtils.getBirthByIdCard(idCardNo);
        String pattern = DateTimeUtils.StringPattern(birthByIdCard, "yyyyMMdd", "yyyy-MM-dd");
        String provinceByIdCard = IdcardUtils.getProvinceByIdCard(idCardNo);
        String genderByIdCard = IdcardUtils.getGenderByIdCard(idCardNo);
        System.out.println(ageByIdCard+"   "+birthByIdCard + " "+ pattern +"   "+provinceByIdCard+"  性别："+genderByIdCard);
    }



    @Test
    public void testCompensatoryWay(){
        Double bankFeeAmount = 30000.0;
        Double compensatoryAmount = 20000.0;
        Integer months = 36;
        Map<String, Object> map = iCustomerBizService.actCalculateCompensatoryWay(bankFeeAmount, compensatoryAmount, months).getD();
        System.out.println("贴息方案：" + map.get("compensatoryWay"));
        System.out.println("贴息期限：" + map.get("compensatoryMonth"));
    }

    @Test
    public void testMetaData(){

        //templateService.getCustomerLoanMap("afawf4a5f4wf5af5wa4f54wf5");
    }


}
