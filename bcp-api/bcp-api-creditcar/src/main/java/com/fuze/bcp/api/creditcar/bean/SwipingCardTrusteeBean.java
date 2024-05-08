package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardBean;

/**
 * 受托支付（代刷卡）
 * Created by Lily on 2017/9/21.
 */
public class SwipingCardTrusteeBean extends SwipingCardBean {
    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A027";
    }

}
