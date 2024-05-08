package com.fuze.bcp.cardealer.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * 集团
 */
@Document(collection = "bd_cardealer_group")
@Data
public class DealerGroup extends BaseDataEntity {

    /**
     * 分成比例
     */
    private Map<Integer, Double> sharingRatio = null;


}