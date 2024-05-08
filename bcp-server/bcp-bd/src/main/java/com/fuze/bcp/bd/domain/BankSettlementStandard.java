package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by zqw on 2017/6/30.
 *      支行间结算标准表
 */
@Document(collection="bd_banksettlementstandard")
@Data
public class BankSettlementStandard extends BaseDataEntity {

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
