package com.fuze.bcp.drools.test.business;

import com.fuze.bcp.api.drools.bean.AccrualSubsidiesBean;
import com.fuze.bcp.api.drools.service.IDroolsBizService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by CJ on 2017/8/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class DroolsServiceTest {

    @Autowired
    IDroolsBizService iDroolsBizService;

    @Test
    public void testDrools() throws IOException {
        AccrualSubsidiesBean accrualSubsidiesBean = new AccrualSubsidiesBean();
        accrualSubsidiesBean.setCreditAmount(100.0);

        accrualSubsidiesBean.setCarTypeId("58fa078477c82cd3aa0df4e3");
        accrualSubsidiesBean.setMonths(18);
        accrualSubsidiesBean.setRatio(0.06);
        accrualSubsidiesBean.setDownPaymentRatio(0.48);
        String rule = "package com.fuze.bcp.server.drools;\n" +
                "\n" +
                "import com.fuze.bcp.api.drools.bean.AccrualSubsidiesBean\n" +
                "import java.util.Arrays\n" +
                "import java.util.List;\n" +
                "\n" +
                "rule \"东风本田\"\n" +
                "    dialect \"java\"\n" +
                "    lock-on-active true\n" +
                "when\n" +
                "    $accrualSubsidiesBean : AccrualSubsidiesBean();\n" +
                "then\n" +
                "    Integer[] months = new Integer[]{12, 18, 24, 36};\n" +
                "    List arr = Arrays.asList(months);\n" +
                "    if(arr.contains($accrualSubsidiesBean.getMonths())){\n" +
                "        if(\"58fa078477c82cd3aa0df4e3\".equals($accrualSubsidiesBean.getCarTypeId())){ //2016 款 2.0L CVT 两驱经典版\n" +
                "            if($accrualSubsidiesBean.getDownPaymentRatio() >= 0.5){\n" +
                "                $accrualSubsidiesBean.setCompensatoryRatio(0.04);\n" +
                "                $accrualSubsidiesBean.setCompensatoryAmount($accrualSubsidiesBean.getCompensatoryRatio() * $accrualSubsidiesBean.getCreditAmount());\n" +
                "            } else if ($accrualSubsidiesBean.getDownPaymentRatio() < 0.5 && $accrualSubsidiesBean.getDownPaymentRatio() >= 0.2 ){\n" +
                "                $accrualSubsidiesBean.setCompensatoryRatio(0.03);\n" +
                "                $accrualSubsidiesBean.setCompensatoryAmount($accrualSubsidiesBean.getCompensatoryRatio() * $accrualSubsidiesBean.getCreditAmount());\n" +
                "            }\n" +
                "        }\n" +
                "        if(\"58fa078477c82cd3aa0df4e4\".equals($accrualSubsidiesBean.getCarTypeId())){ //2016 款 2.0L CVT 两驱都市版\n" +
                "            if($accrualSubsidiesBean.getDownPaymentRatio() >= 0.2){\n" +
                "                $accrualSubsidiesBean.setCompensatoryRatio(0.04);\n" +
                "                $accrualSubsidiesBean.setCompensatoryAmount($accrualSubsidiesBean.getCompensatoryRatio() * $accrualSubsidiesBean.getCreditAmount());\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    update($accrualSubsidiesBean);\n" +
                "end";
        String interfacePath = "com.fuze.bcp.drools";
        accrualSubsidiesBean = (AccrualSubsidiesBean) iDroolsBizService.doCheckByRulStr(rule, interfacePath, accrualSubsidiesBean);
        System.out.println(accrualSubsidiesBean);
    }

}
