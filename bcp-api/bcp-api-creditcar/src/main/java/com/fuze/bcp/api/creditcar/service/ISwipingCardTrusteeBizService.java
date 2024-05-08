package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.SwipingCardTrusteeBean;
import com.fuze.bcp.bean.ResultBean;

/**
 * 受托支付(代刷卡)服务接口
 * Created by Lily on 2017/10/16.
 */
public interface ISwipingCardTrusteeBizService {

    /**
     * 初始化代刷卡信息
     * @param bankCardApplyId
     * @return
     */
    ResultBean<SwipingCardTrusteeBean> actCreateSwipingCardTrustee(String bankCardApplyId);

    /**
     * 完成交易流程的代刷卡任务
     */
}
