package bcp.wechat.service;

import com.fuze.bcp.api.wechat.bean.LoanQueryBean;
import com.fuze.bcp.api.wechat.service.ILoanQueryBizService;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

/**
 * Created by ${Liu} on 2018/4/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class loanQueryServiceTest {

    @Autowired
    ILoanQueryBizService iLoanQueryBizService;

    @Test
    public void test() throws IOException {
        LoanQueryBean loanQueryBean = new LoanQueryBean();
        loanQueryBean.setCell("1355218982");
        loanQueryBean.setCustomerName("测试1");
        loanQueryBean.setWorkSalary(10000);
        loanQueryBean.setApoci(0);
        loanQueryBean.setMonthlyAmount(3000);
        loanQueryBean.setExpectedLoanAmount(200000.0);
        loanQueryBean.setCompanyType("ICS");
        loanQueryBean.setShareOpenId("omWUM0wal8bnY6CvzLfwFjf28TF4");
        ResultBean r = iLoanQueryBizService.actSaveLoanQuery(loanQueryBean);
        System.out.println(r);
    }

    @Test
    public void test1(){
        List<LoanQueryBean> d = iLoanQueryBizService.actGetAllLoanQuery().getD();
        for (LoanQueryBean l:d) {
            System.out.println(l);
        }
    }

}
