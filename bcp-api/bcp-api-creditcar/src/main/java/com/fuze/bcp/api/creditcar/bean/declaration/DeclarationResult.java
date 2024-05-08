package com.fuze.bcp.api.creditcar.bean.declaration;


import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 报批结果
 */
@Data
public class DeclarationResult extends APIBaseBean {

    //  初始化 未报批反馈
    public final static int RESULT_INIT = 0;

    /**
     * 报批通过
     */
    public final static int RESULT_PASS = 2;

    /**
     * 报批被驳回 、调整
     */
    public final static int RESULT_REAPPLY = 8;

    /**
     * 报批被拒绝
     */
    public final static int RESULT_REJECT = 9;


    //  银行批付额度
    private Double approvedCreditAmount = 0.0;

    /**
     * 报批状态（通过、驳回或拒绝）
     */
    private Integer  result = RESULT_INIT;

}
