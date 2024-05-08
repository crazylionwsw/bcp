package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 贴息政策
 * 12期	首付超过50% 贴息手续费的50% 首付比例20%-50% 贴息手续费的30%
 */
@Data
@MongoEntity(entityName = "bd_compensatory_policy")
public class CompensatoryPolicyBean extends APIBaseDataBean {

    public static final String MONTHS = "months"; // 期数    12, 18, 24, 30, 36

    public static final String DOWNPAYMENTRATIO = "downPaymentRatio"; // 首付比例  区间的数组

    public static final String COMPENSATORYRATIO = "compensatoryRatio"; // 贴息占银行手续费比例

    public static final String SCOPE = "scope"; // 品牌、车系、车型

    public static final String CARBRANDID = "CarBrandId";

    public static final String CARMODELID = "CarModelId";

    public static final String CARTYPEID = "CarTypeId";

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
