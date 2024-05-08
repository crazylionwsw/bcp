package com.fuze.bcp.api.drools.service;

import com.fuze.bcp.api.drools.bean.DroolsBaseBean;
import com.fuze.bcp.bean.APIBaseBean;

import java.io.IOException;
import java.util.List;

/**
 * Created by CJ on 2017/7/3.
 */
public interface IDroolsBizService {

    DroolsBaseBean doDrools(APIBaseBean apiBaseBean, String kBaseName, List arr);

    Object doCheckByRulStr(String rules, String interfacePath, Object obj) throws IOException;

}
