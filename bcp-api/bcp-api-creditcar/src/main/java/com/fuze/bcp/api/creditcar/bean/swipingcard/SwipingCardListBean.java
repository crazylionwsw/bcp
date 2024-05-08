package com.fuze.bcp.api.creditcar.bean.swipingcard;

import com.fuze.bcp.api.creditcar.bean.APIBillListBean;
import lombok.Data;

/**
 * Created by Lily on 2017/9/14.
 */
@Data
public class SwipingCardListBean extends APIBillListBean {
    /**
     * 刷卡时间
     */
    private String payTime;

    /**
     * 刷卡金额
     */
    private Double payAmount;

    /**
     * 卡号
     */
    private String cardNumber;
}
