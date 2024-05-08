package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.DemandSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICustomerDemandBizService;
import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.bean.ResultBean;
import javafx.scene.control.Cell;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lily on 2017/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class CustomerDemandServiceDroolsTest {

    @Autowired
    ICustomerDemandBizService iCustomerDemandBizService;

    @Test
    public void commitCustomerDemand(){
        DemandSubmissionBean demandSubmissionBean = new DemandSubmissionBean();


        //4S店销售员(来源经销商)
        //业务类型
        demandSubmissionBean.setBusinessTypeCode("NC");
        demandSubmissionBean.setDealerEmployeeId("59649899e4b0c5aa17233dc0");
        /* *
         * 贷款主体
         */
        CustomerBean creditMaster = new CustomerBean();
        creditMaster.setName("测试用例");
//        creditMaster.setNationality("汉");//民族
//        creditMaster.setIdentifyNo("400521199007010014");//身份证号
//        creditMaster.setAddress("大邯郸");
//        creditMaster.setAuthorizeBy("公安局");//签发机关
//        creditMaster.setIdentifyValid("");//身份证有效期限

        CustomerJobBean jobBean = new CustomerJobBean();
        jobBean.setCompanyName("富择");//单位名称
        creditMaster.setCustomerJob(jobBean);
        demandSubmissionBean.setCreditMaster(creditMaster);
        /*
        * 选购车辆信息
        * */
        PadCustomerCarBean customerCar = new PadCustomerCarBean();
        customerCar.setCarColorName("blue");
        customerCar.setReceiptPrice(1.00); //车辆开票价
        customerCar.setRealPrice(1.00);//预计成交价
        customerCar.setCarTypeId("");
        demandSubmissionBean.setCustomerCar(customerCar);

        /**
         * 抵押贷款信息
         */
        LoanSubmissionBean customerLoanBean = new LoanSubmissionBean();
        customerLoanBean.setChargePaymentWay(""); //后续费缴纳方式
        customerLoanBean.setRealityBankFeeAmount(21.00);//贷款金额
        customerLoanBean.setDownPaymentRatio(25.00);//首付比例（新车大于20，二手车大于30）
        demandSubmissionBean.setCustomerLoan(customerLoanBean);


        demandSubmissionBean.setRelation("1");//与指标人关系（0为本人）
        /**
         * 检查抵押人
         */
        CustomerBean pledgecustomer = new CustomerBean();
        pledgecustomer.setIdentifyNo("400521199007010014");
        pledgecustomer.setName("姓名");
        demandSubmissionBean.setPledgeCustomer(pledgecustomer);

        /*//指标状态
        demandSubmissionBean.setIndicatorStatus(0);
        //预计指标获取日期
        demandSubmissionBean.setRetrieveDate("2017-08-20 16:54");
        //情况说明
        demandSubmissionBean.setIndicatorComment("测试2");
        //提车日期
        demandSubmissionBean.setPickCarDate("2017-08-22 16:54");
        //demandSubmissionBean.setFirstPurchaseDate();二手车需要
        //外迁日期
        demandSubmissionBean.setMoveOutDate("2017-09-20 16:54");*/

        ResultBean resultBean = iCustomerDemandBizService.actSubmitCustomerDemand(demandSubmissionBean);

    }

}
