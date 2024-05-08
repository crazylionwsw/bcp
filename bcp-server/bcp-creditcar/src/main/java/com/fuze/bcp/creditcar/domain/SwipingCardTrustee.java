package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 受托支付（代刷卡）
 * Created by Lily on 2017/9/21.
 */
@Document(collection="so_swipingcard_trustee")
@Data
public class SwipingCardTrustee extends SwipingCard {
    /**
     * 单据类型
     * @return
     */
    public String getBillTypeCode() {
        return "A027";
    }
}
