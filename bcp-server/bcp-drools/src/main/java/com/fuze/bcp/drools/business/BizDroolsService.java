package com.fuze.bcp.drools.business;

import com.fuze.bcp.api.drools.bean.DroolsBaseBean;
import com.fuze.bcp.api.drools.service.IDroolsBizService;
import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.drools.filter.BcpAgendaFilter;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by CJ on 2017/6/29.
 */
@Service
public class BizDroolsService implements IDroolsBizService {

//    @Autowired
    ReleaseId fuzRelease;

    public KieSession getKieSession(String kBase) {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.newKieContainer(fuzRelease);
        return kContainer.getKieBase(kBase).newKieSession();
    }

    @Override
    public DroolsBaseBean doDrools(APIBaseBean apiBaseBean, String kBaseName, List arr) {
        DroolsBaseBean droolsBaseBean = new DroolsBaseBean();
        droolsBaseBean.setApiBaseBean(apiBaseBean);
        KieSession kieSession = getKieSession(kBaseName);
        kieSession.insert(droolsBaseBean);
        if (arr != null && arr.size() > 0) {
            AgendaFilter agendaFilter = new BcpAgendaFilter(arr);
            kieSession.fireAllRules(agendaFilter);
            kieSession.destroy();
        } else {
            kieSession.fireAllRules();
            kieSession.destroy();
        }
        return droolsBaseBean;
    }

    private static final String PATH = "src/main/resources/";

    @Override
    public Object doCheckByRulStr(String rules, String interfacePath, Object obj) throws IOException {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write(PATH + interfacePath.replaceAll("\\.", "/") + ".drl", rules.getBytes());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new RuntimeException("ERROR:创建drools rule失败，文件路径错误或语法错误");
        }
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KieBase kieBase = kieContainer.getKieBase();
        KieSession kieSession = kieBase.newKieSession();
        if (kieSession == null) {
            throw new RuntimeException("ERROR:获取KieSession失败");
        }
        kieSession.insert(obj);
        kieSession.fireAllRules();
        kieSession.destroy();
        return obj;
    }
}
