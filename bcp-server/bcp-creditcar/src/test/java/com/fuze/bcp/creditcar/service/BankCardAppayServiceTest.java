package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardListBean;
import com.fuze.bcp.api.creditcar.service.IBankCardApplyBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Lily on 2017/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class BankCardAppayServiceTest {

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    @Test
    public void commitBanCardApply(){

        iBankCardApplyBizService.actCreateBankCardApply("5a56fe8d617db11f9840a6ff");
    }

    @Test
    public void test1(){
        /*List l = iBankCardApplyBizService.actGetBankCardApplyByCardStep("BankCard_Apply").getD();
        System.out.println(l);*/
        List<BankCardListBean> bankCard_apply = iBankCardApplyBizService.actGetBankCardApplyByTaskDefinitionKey("BankCard_Take","58ad2da2e4b000431c11e925").getD();
        for (BankCardListBean list:bankCard_apply) {
            System.out.println(list);
        }
    }

    @Test
    public void updateBanCardApply(){
        /*List<BankCardApply> bankCardApplies = iBankCardApplyService.getAll();
        for (BankCardApply bankCard:bankCardApplies) {
            BankCardApply bankCardApply = iBankCardApplyService.getOne(bankCard.getId());
            if(bankCard.getCashSourceId() == null){
                CustomerTransaction customerTransaction = iCustomerTransactionService.getOne(bankCard.getCustomerTransactionId());
                if(customerTransaction != null){
                    bankCardApply.setCashSourceId(customerTransaction.getCashSourceId());
                    bankCardApply = iBankCardApplyService.save(bankCardApply);
                    System.out.print("更新卡业务信息成功！");
                }
            }
        }*/
    }

    @Test
    public void testSelectList(){
        SearchBean searchBean = new SearchBean();
        searchBean.setCashSourceId("58ad2da2e4b000431c11e925");
        searchBean.setCurrentPage(0);
        searchBean.setPageSize(10);
        searchBean.setStatusName("status");
        searchBean.setStatusValue(-1);
        searchBean.setSortWay("DESC");
        ResultBean<DataPageBean<BankCardListBean>> dataPageBeanResultBean = iBankCardApplyBizService.actGetBankCardApplyListByCashSourceId(searchBean);
        for (BankCardListBean bankCard:dataPageBeanResultBean.getD().getResult()) {
            System.out.print("卡业务信息信息！"+bankCard);
        }
    }

    @Test
    public void testSelectOne(){
        BankCardBean bankCardBean = iBankCardApplyBizService.actGetBankCardApplyById("5a41f9c4ba2a5d0802a083aa").getD();
        System.out.print(bankCardBean);
    }

    @Test
    public void testclear(){
        List<BankCardApplyBean> blist = iBankCardApplyBizService.actGetByStatus().getD();
        System.out.println("共计"+blist.size()+"条刷卡完成和销卡数据");
        if(blist.size() > 0 && blist != null){
            for (int i = 0; i <blist.size() ; i++) {
                iBankCardApplyBizService.actDeleteReport(blist.get(i), "admin").getD();
            }
        }

//        if(blist.size() > 0 && blist != null){
//            for (BankCardApplyBean b:blist) {
//                Object info = iBankCardApplyBizService.actDeleteReport(b, "admin").getD();
//            }
//
//        }

    }

}
