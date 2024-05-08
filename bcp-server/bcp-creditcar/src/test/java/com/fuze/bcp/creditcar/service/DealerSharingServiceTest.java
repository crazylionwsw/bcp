package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.dealersharing.GroupSharingBean;
import com.fuze.bcp.api.creditcar.service.IDealerSharingBizService;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by CJ on 2017/9/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class DealerSharingServiceTest {

    @Autowired
    IDealerSharingBizService iDealerSharingBizService;

    /**
     * 重新生成特定月份的分成详细
     */
    @Test
    public void test() {
        iDealerSharingBizService.actResetSharingDetails("2018-01", 0,  null, null);
    }

    /**
     * 创建当月特定集团的集团分成
     */
    @Test
    public void test2() {
        iDealerSharingBizService.actCreateGroupSharing("5a33a9cec4ddc6427045cbf9", "2017-11");
    }

    /**
     * 创建特定渠道指定月份的渠道分成
     */
    @Test
    public void test3() {
        iDealerSharingBizService.actCreateDealerSharing("5a4c8280c20fa14bd4a8bcbc", "2018-01");
    }

    /**
     * 更改渠道详细的状态
     */
    @Test
    public void test4() {
        try {
            iDealerSharingBizService.actConfirmStatus("5a275ac5ba2a5d49c0717b2e","5a275ac5ba2a5d49c0717b2e", "5a44c44c3a67c206c0beccba", 1, 0.0, 0.0,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分页获取分成详细
     */
    @Test
    public void test5() {
        ResultBean resultBean = iDealerSharingBizService.actGetSharingDetails(0);
        System.out.println(resultBean.getD());
    }

    /**
     * 分页获取集团分成
     */
    @Test
    public void test6() {
//        ResultBean resultBean = iDealerSharingBizService.actGetGroupSharings(0);
//        System.out.println(resultBean.getD());
    }

    /**
     * 分页获取渠道分成
     */
    @Test
    public void test7() {
        //ResultBean resultBean = iDealerSharingBizService.actGetDealerSharings(0);
        //System.out.println(resultBean.getD());
    }

    /**
     * 生成全部渠道分成数据
     */
    @Test
    public void test8() {
        ResultBean resultBean = iDealerSharingBizService.actCreateDealerSharings("2018-01", "");
    }

    /**
     * 获取特定集团的集团 分成详细
     */
    @Test
    public void test9() {
        ResultBean resultBean = iDealerSharingBizService.actLookupGroupDetail("5a4c457b9ef15534780260e6");
        System.out.println(resultBean.getD());
    }

    /**
     * 获取全部渠道分成信息
     */
    @Test
    public void test10 () {
        ResultBean resultBean = iDealerSharingBizService.actLookupDealerDetail("");
        System.out.println(resultBean.getD());
    }

    @Test
    public void test11(){
        ResultBean<GroupSharingBean> resultBean = iDealerSharingBizService.actGetGroupDetail("5a33a9cec4ddc6427045cbf9","2017-12");
        System.out.println(resultBean);
    }


}
