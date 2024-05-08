package com.fuze.bcp.push.service;

import com.fuze.bcp.push.domain.PushData;
import com.fuze.bcp.service.IBaseService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/9/28.
 */
public interface IPushDataServie extends IBaseService<PushData> {

    Map pushData(String token, PushData pushData) throws Exception;

    Map pushDatas(List<String> tokens, PushData pushData) throws Exception;

    Map pushAllData(PushData pushData) throws Exception;
}
