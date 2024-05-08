package com.fuze.bcp.bd.domain;
;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 促销政策
 */
@Document(collection="bd_promotion_policy")
@Data
public class PromotionPolicy extends BaseDataEntity {

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

}
