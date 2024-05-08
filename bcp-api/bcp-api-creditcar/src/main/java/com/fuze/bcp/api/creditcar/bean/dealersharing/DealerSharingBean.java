package com.fuze.bcp.api.creditcar.bean.dealersharing;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 渠道返佣
 */
@Data
public class DealerSharingBean extends APIBaseBean {

    /**
     * 经销商
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
