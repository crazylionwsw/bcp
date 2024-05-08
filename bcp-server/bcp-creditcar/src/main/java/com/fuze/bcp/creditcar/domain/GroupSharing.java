package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.creditcar.bean.dealersharing.GroupSharingBean;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 集团返佣
 */
@Document(collection = "so_group_sharing")
@Data
public class GroupSharing extends MongoBaseEntity {

    public GroupSharing() {
    }

    public GroupSharing(GroupSharingBean g) {
        this.setId(g.getId());
        this.setDataStatus(g.getDataStatus());
        this.setTs(g.getTs());
        this.dealerGroupId = g.getDealerGroupId();
        this.groupName = g.getGroupName();
        this.month = g.getMonth();
        this.totalCredit = g.getTotalCredit();
        this.totalCount = g.getTotalCount();
        this.totalSharing = g.getTotalSharing();
        this.statuses = g.getStatuses();
        this.groupSharingDetails = g.getGroupSharingDetails();
    }

    /**
     * 集团
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
     * 状态 0 初始化 1 修正 2 核对 3 复核 4 结算 所有单据都结算达到状态才算到达状态
     */
    private Map<String, Integer> statuses = new HashMap();


    private Map<String, List<String>> groupSharingDetails = new HashMap<>();

    /**
     * 集团分成状态  0 初始化 1 修正 2 核对 3 复核 4 结算
     */
    private Integer status;


}
