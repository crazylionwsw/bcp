package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.bd.bean.FeeItemBean;
import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.creditcar.bean.CustomerDemandBean;
import com.fuze.bcp.api.creditcar.bean.OrderSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistryBean;
import com.fuze.bcp.api.creditcar.service.IAppointPaymentBizService;
import com.fuze.bcp.api.creditcar.service.ICarRegistryBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerDemandBizService;
import com.fuze.bcp.api.creditcar.service.IOrderBizService;
import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.utils.SimpleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Lily on 2017/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class OrderServiceTest {

    @Autowired
    IOrderBizService iOrderBizService;

    @Autowired
    ICustomerDemandBizService iCustomerDemandBizService;

    @Autowired
    IAppointPaymentBizService iAppointPaymentBizService;

    @Autowired
    ICarRegistryBizService iCarRegistryBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;



    @Test
    public void actSubmitOrder(){
        OrderSubmissionBean orderSubmissionBean = iOrderBizService.actGetTransactionOrder("5a0cfd38ba2a5d1e0067d7bc").getD();

        iOrderBizService.actSaveOrder(orderSubmissionBean);
        //ResultBean<PurchaseCarOrderBean> purchaseCarOrderBeanResultBean = iOrderBizService.actSubmitOrder("5a0819cbba2a5d177d5ace94","提交");
    }
    @Test
    public void actYesOrNo(){

    }

    @Test
    public void actTestOrderCharts(){
        String loginUserId = "59b896121580a73b1c39cbaf";
        PurchaseCarOrderBean purchaseCarOrder ;
        ResultBean<Map<Object, Object>> employeeReport = iOrderBizService.getEmployeeReport(null, new PurchaseCarOrderBean(), loginUserId);
        Map<Object, Object> d = employeeReport.getD();
        String m = employeeReport.getM();
        System.out.println(d);
    }

    @Test
    public void actTestCardemandCharts(){
        String loginUserId = "59b896121580a73b1c39cbaf";
        Map<Object, Object> employeeReport = iCustomerDemandBizService.getEmployeeReport(null, new CustomerDemandBean(), loginUserId).getD();
        System.out.println(employeeReport);
    }

    @Test
    public void actTestpaymentCharts(){
        String loginUserId = "59bc8363e4b097a70da53fc1";
        Map<Object, Object> employeeReport = iAppointPaymentBizService.getChannelReport(null, new AppointPaymentBean(), loginUserId).getD();
        System.out.println(employeeReport);
    }

    @Test
    public void actTestpaymentChannelCharts(){
        String loginUserId = "59008dd6e4b02c15d7e4f6fa";
        Map<Object, Object> employeeReport = iAppointPaymentBizService.getChannelReport(null, new AppointPaymentBean(), loginUserId).getD();
        System.out.println(employeeReport);
    }

    @Test
    public void actTestCarregistryCharts() throws ParseException {
// 获取Calendar
        String date= "2017-07-19";
        DateFormat formatter = SimpleUtils.getOnlySimpleDateFormat();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formatter.parse(date));
// 设置Calendar月份数为下一个月
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
// 设置Calendar日期为下一个月一号
        calendar.set(Calendar.DATE, 1);
// 设置Calendar日期减一,为本月末
        calendar.set(Calendar.DATE, -1);

// 打印
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(calendar.getTime()));
        //  System.out.print(SimpleUtils.getOffsetDaysTodayStrOf("2017-07-16",2));
//        String loginUserId = "59bc8363e4b097a70da53fc1";
//        Map<Object, Object> employeeReport = iCarRegistryBizService.getChannelReport(null, new CarRegistryBean(), loginUserId).getD();
//        System.out.println(employeeReport);
    }

    @Test
    public void actTestCardealerOrderCharts(){
       // {dmvpCount=2, carRegistryCount=1, dmvpServiceFee=40968.0, dmvpledgeCharge=72096.0, carRegistryCharge=2460.0, carRegistryAmount=1000000.0, dmvpAmount=2000000.0, carRegistryServiceFee=6150.0, pickupCount=0}
        String loginUserId = "598c105cba2a5d38afe0f3c8";
        Map<Object, Object> employeeReport = iOrderBizService.getChannelReport(null, new PurchaseCarOrderBean(), loginUserId).getD();
        System.out.println(employeeReport);
    }

    @Test
    public void actTestCardealerDemandCharts(){
        String loginUserId = "599e8ec0ba2a5d34d6cb9990";
        Map<Object, Object> employeeReport = iCustomerDemandBizService.getChannelReport(null, new CustomerDemandBean(), loginUserId).getD();
        System.out.println(employeeReport);
    }


    @Test
    public void actTestSaveOrder(){
        OrderSubmissionBean orderSubmissionBean = new OrderSubmissionBean();
        /**
         * 客户工作信息
         */
        CustomerJobBean customerJobBean = new CustomerJobBean();
        /**
         * 反担保人
         */
        CustomerBean counterGuarantor = new CustomerBean();
        /**
         * 贷款信息
         */
        CustomerLoanBean customerLoanBean = new CustomerLoanBean();
        /**
         * 车辆信息
         */
        PadCustomerCarBean customerCarBean = new PadCustomerCarBean();

        CustomerBean customerBean = new CustomerBean();
        customerBean.setId("59b3c9ab0e4aa834b49b8ef1");
        //客户ID
        orderSubmissionBean.setCreditMaster(customerBean);

        //业务ID
        orderSubmissionBean.setCustomerTransactionId("59b3c9ab0e4aa834b49b8ef2");
        //提车日期
        orderSubmissionBean.setPickCarDate("2017-08-22 16:54");
        //业务类型
        orderSubmissionBean.setBusinessTypeCode("NC");
        //提交人
        orderSubmissionBean.setLoginUserId("599e8ec0ba2a5d34d6cb9990");
        //客户工作信息
        //反担保人信息
        //orderSubmissionBean.setCounterGuarantor(counterGuarantor);
        //贷款信息
        //orderSubmissionBean.setCustomerLoan(customerLoanBean);
        //车辆信息
        orderSubmissionBean.setCustomerCar(customerCarBean);

        /*DemandSubmissionBean demandSubmissionBean = new DemandSubmissionBean();
        *//**
         * 指标人
         *//*
        CustomerBean pledgecustomer = new CustomerBean();
        pledgecustomer.setIdentifyNo("340825199002281234");
        pledgecustomer.setName("王莉莉测试数据");
        *//**
         * 贷款主体
         *//*
        CustomerBean creditMaster = new CustomerBean();
        creditMaster.setIdentifyNo("340825199002281234");
        creditMaster.setName("王莉莉测试数据");
        *//**
         * 选购车辆信息
         *//*
        CustomerCarBean customercar = new CustomerCarBean();
        *//**
         * 抵押贷款信息
         *//*
        CustomerLoanBean loan = new CustomerLoanBean();

        //指标人信息
        demandSubmissionBean.setPledgeCustomer(pledgecustomer);
        demandSubmissionBean.setCreditMaster(creditMaster);
        demandSubmissionBean.setCustomerLoan(loan);
        //指标人关系为本人
        demandSubmissionBean.setRelation("1");
        //车辆信息
        demandSubmissionBean.setCustomerCar(customercar);
        //来源经销商
        demandSubmissionBean.setCarDealerId("59880ccdc6e32d2f30c1e029");
        //业务类型
        demandSubmissionBean.setBusinessTypeCode("NC");
        //提交人
        demandSubmissionBean.setLoginUserId("5985640438edd4cb482ac36c");
        //提车日期
        demandSubmissionBean.setPickCarDate("2017-08-22 16:54");
        //demandSubmissionBean.setFirstPurchaseDate();二手车需要
        //指标状态
        demandSubmissionBean.setIndicatorStatus(0);
        //外迁日期
        demandSubmissionBean.setMoveoutDate("2017-09-20 16:54");
        //预计指标获取日期
        demandSubmissionBean.setRetrieveDate("2017-08-20 16:54");
        //情况说明
        demandSubmissionBean.setIndicatorComment("测试2");
        //4S店销售员
        demandSubmissionBean.setDealerEmployeeId("58b5168fe4b02916e6276e38");
        //备注信息
        demandSubmissionBean.setComment("提交2");*/
        //orderSubmissionBean.setId("59b103de0820151c3caac791");
//        orderSubmissionBean.setApproveStatus(8);
        ResultBean<OrderSubmissionBean> submissionBean = iOrderBizService.actSaveOrder(orderSubmissionBean);
        if (submissionBean.failed()){
            System.out.print("save"+submissionBean.getM());
        }
        ResultBean<PurchaseCarOrderBean> purchaseCarOrderBeanResultBean = iOrderBizService.actSubmitOrder(submissionBean.getD().getId(),"提交");
        if(purchaseCarOrderBeanResultBean.failed()){
            System.out.print("submit"+purchaseCarOrderBeanResultBean.getM());
        }
    }

    @Test
    public void actTestReport() {
        ResultBean resultBean = iOrderBizService.getDailyReport(null,null,new PurchaseCarOrderBean());
        System.out.println(resultBean.getD());
    }

    @Test
    public void actTestList() {
        List<FeeValueBean> feeValues = new ArrayList<FeeValueBean>();
        PurchaseCarOrderBean purchaseCarOrderBean = iOrderBizService.actGetOrderByTransactionId("5ab369d8f98b5a2f24f95c81").getD();
        List<FeeValueBean> feeItemList = purchaseCarOrderBean.getFeeItemList();
        System.out.println("签约的收费项"+feeItemList);
        List<FeeItemBean> feeItemBeen = iBaseDataBizService.actGetFeeItems().getD();
        System.out.println("所有收费项"+feeItemBeen);
        for (FeeValueBean fee:feeItemList) {
            for (FeeItemBean feeitem:feeItemBeen) {
                if(fee.getCode().equals(feeitem.getCode())){
                    fee.setName(feeitem.getName());
                }
            }
        }
        System.out.println("*-*-*-*-*-*-*-更改后的收费项*-*-*-*-*-*-*-*-");
        for (FeeValueBean fee:feeItemList) {
            System.out.println(fee+fee.getName());
        }

    }

}
