package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 渠道返佣
 */
@Document(collection = "so_cardealer_sharing")
@Data
public class DealerSharing extends MongoBaseEntity {

    /**
     * 经销商`
     */
    private String carDealerId;

    /**
     * 经销商名称
     */
    private String dealerName;

    /**
     * 月份
     */
    private String month;

    /**
     * 借款合计
     */
    private Double totalCredit = 0.0;

    /**
     * 交易数量
     */
    private Integer totalCount = 0;

    /**
     * 分成合计
     */
    private Double totalSharing = 0.0;

    /**
     * 状态 0 初始化 1 修正 2 核对 3 复核 4 结算 所有单据都结算达到状态才算到达状态
     */
    private Integer status = 0;

    /**
     * 详情IDs
     */
    private List<String> sharingDetailIds = new ArrayList<>();

    /**
     * 渠道经理
     */
    private String channelId;

}
