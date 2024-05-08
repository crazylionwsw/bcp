package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

/**
 * 贴息政策
 * Created by sean on 2016/10/20.
 */
@Document(collection="bd_compensatory_policy")
@Data
public class CompensatoryPolicy extends BaseDataEntity {

    /**
     * 车辆品牌
     */
    private String carBrandId;

    /**
     * 规则公式
     */
    private String formula = null;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 截止日期
     */
    private String endDate;

    private String templateId;

    private Map templateMap;

}
