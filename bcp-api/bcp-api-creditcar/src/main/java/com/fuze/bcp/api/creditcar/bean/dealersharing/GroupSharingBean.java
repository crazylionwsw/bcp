package com.fuze.bcp.api.creditcar.bean.dealersharing;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 渠道返佣
 */
@Data
public class GroupSharingBean extends APIBaseBean {

    public GroupSharingBean() {
    }

    public GroupSharingBean(String id, String ts, Integer status, Integer dataStatus, Map<String, List<String>> groupSharingDetails, Map<String, Integer> statuses, Double totalSharing, Integer totalCount, Double totalCredit, String month, String dealerGroupId,String groupName) {
        this.setId(id);
        this.setTs(ts);
        this.status = status;
        this.setDataStatus(dataStatus);
        this.groupSharingDetails = groupSharingDetails;
        this.statuses = statuses;
        this.totalSharing = totalSharing;
        this.totalCount = totalCount;
        this.totalCredit = totalCredit;
        this.month = month;
        this.dealerGroupId = dealerGroupId;
        this.groupName = groupName;
    }

    /**
     * 集团id
     */
    private String dealerGroupId;

    /**
     * 集团名称
     */
    private String groupName;

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
     * 集团下渠道分成状态 0 初始化 1 修正 2 核对 3 复核 4 结算 所有单据都结算达到状态才算到达状态
     */
    private Map<String, Integer> statuses = new HashMap(); // key 渠道ID value status

    /**
     * 详情IDs
     */
    private Map<String, List<String>> groupSharingDetails = new HashMap<>();

    /**
     * 集团分成状态，需要所有渠道状态达到特定状态
     */
    private Integer status;

}
