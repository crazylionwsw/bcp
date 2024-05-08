package com.fuze.bcp.api.creditcar.bean.decompressBill;

import com.fuze.bcp.api.creditcar.bean.APIBillListBean;
import lombok.Data;

/**
 * Created by ${Liu} on 2018/3/7.
 */
@Data
public class DecompressBillListBean extends APIBillListBean{

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 身份证号
     */
    private String identifyNo;

    /**
     * 手机号
     */
    private String cell;

    /**
     * 经销商名称
     */
    private String cardealerName;

    /**
     * 经销商地址
     */
    private String carDealerAddress = null;

    /**
     * 车型
     */
    private String fullName;

    /**
     * 颜色
     */
    private String carColor = null;

    /**
     * 车排量
     */
    private String carEmissions = null;

    /**
     * 审核状态
     */
    private Integer approveStatus;

    /**
     * 业务类型 (新车/二手车）
     */
    private String businessTypeCode;

    /**
     * 交易Id
     */
    private String customerTransactionId;

    /**
     * 解押总额
     */
    private Double decompressAmount = 0.0;

    /**
     * 解押时间
     */
    private String decompressTime;

    /**
     * 解押类型
     * NORMALPAYMENT:正常解押
     * BEFOREPAYMENT:提前还款
     */
    private String decompressType;

    /**
     * 是否贴息
     * 0：否   1：是
     */
    private Integer compensatoryInterest;

}
