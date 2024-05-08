package statistics;

import com.fuze.bcp.api.statistics.bean.BalaceAccountBean;
import com.fuze.bcp.api.statistics.bean.BalanceAccountDetailBean;
import com.fuze.bcp.api.statistics.bean.BalanceAccountExport;
import com.fuze.bcp.api.statistics.bean.ChargeFeePlanDetailBean;
import com.fuze.bcp.api.statistics.service.IBalanceAccountBizService;
import com.fuze.bcp.api.statistics.service.IChargeFeePlanBizService;
import com.fuze.bcp.api.statistics.service.IDailyReportBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.utils.ExcelUtil;
import com.fuze.bcp.utils.SimpleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by CJ on 2017/6/9.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class ChargeFeePlanImplTest {

    private static final Logger logger = LoggerFactory.getLogger(ChargeFeePlanImplTest.class);

    @Autowired
    IChargeFeePlanBizService iChargeFeePlanBizService;

    @Autowired
    IBalanceAccountBizService iBalanceAccountService;

    @Autowired
    IDailyReportBizService iDailyReportBizService;




    @Test
    public void testSyncAllOrders() {
        iChargeFeePlanBizService.actSyncAllOrders();
        // iChargeFeePlanBizService.actCreatePlan("5a0062a9557f711758d1443f");


    }
    @Test
    public void testCreateChargePlan(){
          String[] strMonths = new String[]{"2017-03","2017-04","2017-05","2017-06","2017-07","2017-08","2017-09","2017-10","2017-11","2017-12"};
          for(int i=0; i<strMonths.length; i++){
              //iChargeFeePlanBizService.actSyncSwingCard(strMonths[i]);
          }
    }

    @Test
    public void test(){
        BalaceAccountBean ba = new BalaceAccountBean();
        ba.setYear("2017");
        ba.setMonth("09");
        ChargeFeePlanDetailBean cha = new ChargeFeePlanDetailBean();
        cha.setChargeFeePlanId("5a02da3c14629daa4ed834f0");

        ResultBean<BalanceAccountDetailBean> balanceAccountDetailBeanResultBean = iBalanceAccountService.actCalculateOneChargeFeePlan(cha, ba);

    }

    @Test
    public void testCreateBalanceByMonth(){
        BalaceAccountBean ba = new BalaceAccountBean();
        ba.setYear("2017");
        ba.setMonth("12");
        ResultBean<BalaceAccountBean> res = iBalanceAccountService.actCalculateBalanceAccount(ba);

        if(res.failed()){
            logger.error(res.getM());
        }else{
            logger.info(res.getM());
        }
    }



    @Test
    public void testAll(){
        Integer status= -1;
        Integer currentPage= 0;
        ResultBean<DataPageBean<BalaceAccountBean>> allBalanceAccount = iBalanceAccountService.actGetAllBalanceAccounts(status, currentPage);
        DataPageBean<BalaceAccountBean> d = allBalanceAccount.getD();

        System.out.print(d);


    }


    @Test
    public void testExportExcel() {
//        testExportExcelByMonth("2017","09");
        testExportExcelByMonth("2017","10");
        testExportExcelByMonth("2017","11");
        testExportExcelByMonth("2017","12");
    }

    @Test
    public void testExportExcelByMonth(String year,String month) {

        OutputStream outputStream = null;
        try {

            String fileName = String.format("/Volumes/APP/temp/%s年%s月银行结费明细单-%s.xls", year, month, SimpleUtils.getTodayStr());
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            String sheetName = year + "年" + month + "月" + "银行对账总表";
            String titleName = year + "年" + month + "月" + "银行对账总表";
            String[] headers = {"客户", "身份证", "刷卡金额", "刷卡日期", "业务类型", "手续费缴纳方式", "贷款金额", "贷款期限", "银行手续费金额", "银行手续费率", "结算费用金额"};
            String pattern = "yyyy-MM-dd";
            List<BalanceAccountExport> balanceAccountExports = iBalanceAccountService.actExportExcel(year, month).getD();
            ExcelUtil.exportExcel(sheetName, titleName, headers, balanceAccountExports, pattern, outputStream);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }finally {
            try{
                if(outputStream != null) {
                    outputStream.close();
                }
            }catch(Exception ee){

            }
        }

    }
}
