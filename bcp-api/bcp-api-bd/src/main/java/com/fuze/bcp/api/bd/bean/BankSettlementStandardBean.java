package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

/**
 * Created by zqw on 2017/6/30.
 *      支行间结算标准表
 */
@Data
public class BankSettlementStandardBean extends APIBaseDataBean {

    /**
     *      渠道行
     */
    private String channelId = null;
    /**
     *      报单行
     */
    private String declarationId = null;

    /**
     *      渠道行分成比例
     */
    private Double channelProportion = 0.0;

    /**
     *      报单支行分成比例
     */
    private Double declarationProportion = 0.0;

    /**
     *      开始时间
     */
    private String startTime = null;

    /**
     *        结束时间      ---     如果为空 ，则为长期
     */
    private String endTime = null;

}
