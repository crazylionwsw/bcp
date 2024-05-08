package com.fuze.bcp.cardealer.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * 渠道分成比例
 */
@Document(collection = "bd_cardealer_sharing_ratio")
@Data
public class DealerSharingRatio extends MongoBaseEntity {

    private String carDealerId;

    private Map<Integer, Double> sharingRatio = null;

}
