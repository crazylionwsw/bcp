package com.fuze.bcp.msg.service;

import com.fuze.bcp.bean.ResultBean;

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

}
