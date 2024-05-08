package bcp.wechat.service;

import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.wechat.bean.LoanQueryBean;
import com.fuze.bcp.api.wechat.service.IOfficialAccountsService;
import com.fuze.bcp.api.wechat.service.IWechatBizService;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2018/2/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class WechatServiceTest {

    @Autowired
    IOfficialAccountsService iOfficialAccountsService;

    @Autowired
    IWechatBizService iWechatBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Test
    public void testAA() throws IOException {
        ResultBean r = iOfficialAccountsService.oath2AccessToken("081UTGFk0L3RMk1dVWHk0iQRFk0UTGFp");
        System.out.println(r.getD());
    }

    @Test
    public void addPicMedia() throws Exception {
        File file = new File("e:\\abc.jpg");

        ResultBean resultBean = iOfficialAccountsService.addMaterialEver(file, "image");

        System.out.println(resultBean);

    }


    @Test
    public void addMedia() throws IOException {
        Map article = new HashMap<>();
        article.put("title", "我是一个标题");
        article.put("thumb_media_id", "fZhNOsQjlPhO1oSgtd4WLL3QxYrHkIOycB1rbnnW-FI");
        article.put("author", "liufuqiang");
        article.put("digest", "这是一条测试的文章");
        article.put("show_cover_pic", "1");
        article.put("content", "<H1>我是内容第一行</H1><H2>我是内容第二行</H2><H3>我是内容第三行</H3><H4>我是内容第四行</H4><H5>我是内容第五行</H5>");
        article.put("content_source_url", "https://mvnrepository.com/artifact/junit/junit/4.11");
        List<Map> articles = new ArrayList<Map>();
        articles.add(article);
        Map params = new HashMap<>();
        params.put("articles", articles);
        ResultBean r = iOfficialAccountsService.addMedia(params);
        System.out.println("resultbean:------" + r);
        System.out.println("d--------" + r.getD());
    }


    @Test
    public void testAAA() throws IOException {
        ResultBean r = iOfficialAccountsService.baseAccessToken(false);
        System.out.println(r.getD());
    }

    @Test
    public void testAb() {
        /*ResultBean r = iWechatBizService.sendCardMessage("5938b536e4b04384cc3767a9");
        System.out.println(r);*/
    }


    @Test
    public void test2() throws Exception {
        ResultBean org = iWechatBizService.getOrg("", null);
        System.out.println(org.getD());
    }

    @Test
    public void test3() throws Exception {
        ResultBean resultBean = iWechatBizService.syncOrg();
    }

    @Test
    public void test4() throws Exception {
        ResultBean resultBean = iWechatBizService.deleteOrg("24");
        System.out.println(resultBean.getD());
    }

    @Test
    public void test5() throws Exception {
        Map orgMap = new HashMap<>();
        orgMap.put("id", "2");
        orgMap.put("name", "管理部");
        orgMap.put("parentid", "1");
        orgMap.put("order", "2");
        ResultBean resultBean = iWechatBizService.updateOrg(orgMap);
        System.out.println(resultBean);
    }

    @Test
    public void test6() throws Exception {
        ResultBean employee = iWechatBizService.createEmployee();
        System.out.println(employee);
    }

    @Test
    public void test7() throws Exception {
        ResultBean employee = iWechatBizService.getEmployee("5938b536e4b04384cc3767a9");
        System.out.println(employee);
    }

    @Test
    public void test8() throws Exception {
        ResultBean resultBean = iWechatBizService.deleteEmployee("5ad58ce9cd7d570eb5196398");
        System.out.println(resultBean);
    }

    @Test
    public void test9() throws Exception {
        ResultBean resultBean = iWechatBizService.getEmployeeByOrg("2", 1);
        System.out.println(resultBean.getD());
        System.out.println("-------------输出数据:" + resultBean.getD());
    }

    @Test
    public void test10() throws Exception {
        ResultBean resultBean = iWechatBizService.createOrg("589d8298e4b043db09873dc8");
    }

    @Test
    public void test11() throws Exception {
        List userList = new ArrayList<>();
//        List<EmployeeBean> employeeBeen = iOrgBizService.actGetOrgEmployees("58a6b358e4b06e6e32453417").getD();
//        for (EmployeeBean em:employeeBeen) {
//            userList.add(em.getId());
//        }
        userList.add("ZhouQingWen");
        userList.add("AnJiaShu");
        userList.add("WangLiLi");
//        userList.add("58ac08929509f254e5a9c559");
//        userList.add("LiShuQi");
//        userList.add("XingXiaoLe");
        ResultBean resultBean = iWechatBizService.deleteEmployees(userList);
        System.out.println("-*-*-*-*-*-*-*-*-*输出数据:" + resultBean.getD());
    }

    @Test
    public void test12() throws Exception {
        ResultBean resultBean = iWechatBizService.deleteAllInfos();
    }

    @Test
    public void test13() throws Exception {
        ResultBean resultBean = iWechatBizService.updateEmployee("WangShaoWei");
    }

    @Test
    public void test14(){
        LoanQueryBean loanQueryBean = new LoanQueryBean();
        loanQueryBean.setCustomerName("萨科齐");
        loanQueryBean.setWorkSalary(1000);
        loanQueryBean.setGender(1);
        iWechatBizService.sendCardMessage("58ca3a08e4b061fe297d7c12",loanQueryBean);
        loanQueryBean.setExpectedLoanAmount(80000.0);
        loanQueryBean.setGender(1);
        iWechatBizService.sendCardMessage("5a7d0783c4ddc63638145e8d",loanQueryBean);
    }

    @Test
    public void test15() throws Exception{
        iWechatBizService.getAngent();
    }


}
