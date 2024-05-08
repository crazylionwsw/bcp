package com.fuze.bcp.api.transaction.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 交易阶段
 */
@Data
public class TransactionStageBean implements Serializable {

    /**
     * 业务阶段（列表）
     */
    private List<Map<String, String>> definedStages;

    /**
     * 当前业务阶段
     */
    private String currentStage;

}
