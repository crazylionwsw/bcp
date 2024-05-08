package com.fuze.wechat.service;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.LoanQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ZQW on 2018/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LoanQueryServiceTest {

    @Autowired
    ILoanQueryService iLoanQueryService;

    @Test
    public void testSave(){
        LoanQuery loanQueryBean = new LoanQuery();
        loanQueryBean.setCell("17600530807");
        loanQueryBean.setCustomerName("166449498489");
        loanQueryBean.setWorkSalary(10000);
        loanQueryBean.setApoci(0);
        loanQueryBean.setMonthlyAmount(3000);
        loanQueryBean.setExpectedLoanAmount(0.0);
        loanQueryBean.setCompanyType("ICS");
        loanQueryBean.setShareOpenId("omWUM0wal8bnY6CvzLfwFjf28TF4");
        ResultBean r = iLoanQueryService.actSaveLoanQuery(loanQueryBean);
        System.out.println(r);
    }
}
