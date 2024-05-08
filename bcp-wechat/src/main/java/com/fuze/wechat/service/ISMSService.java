package com.fuze.wechat.service;

import com.fuze.wechat.base.ResultBean;

import java.util.List;

public interface ISMSService {
    /**
     * 发送多条短信
     *
     * @param cells
     * @param content
     * @return
     */
    String send(String cells, String content);

    String send(List<String> cells, String content);

    ResultBean<Boolean> sendCode(String cell);

    ResultBean<String> getCode(String cell);
}
