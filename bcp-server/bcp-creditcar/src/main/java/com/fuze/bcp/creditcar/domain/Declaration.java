package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.creditcar.bean.declaration.DeclarationBean;
import com.fuze.bcp.api.creditcar.bean.declaration.PaymentToIncome;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 银行报批
 */
@Document(collection = "so_declaration")
@Data
public class Declaration extends BaseBillEntity {

    // 报批状态--默认未报批
    private Integer status = DeclarationBean.STATUS_NONE_DECLARATION;

    // 客户分类
    private Integer customerClass;

    //报批合同ID
    private List<String> documentIds = new ArrayList<String>();

    //  能否继续报批
    private Boolean canContinueDeclaration = true;

    //   征信查询结果
    private String creditReportComment;

    //  社保查询结果
    private String socialSecurityComment;

    // 工商查询结果
    private String icbcQueryComment;

    //  法院执行查询结果
    private String fayuanQueryComment;

    //  收入还贷比信息
    private PaymentToIncome paymentToIncome = new PaymentToIncome();

    //社保卡信息------单位是否一致
    private Boolean companyIsSame = false;

    // 社保卡信息------是否正常缴费
    private Boolean normalPayment = false;

    //社保缴费基数
    private Double insuranceBase = 0.0;

    //  专项分期申请情况 --用于发邮件（作废），2017年12月28日11:47:50 修改 用于银行报批附件发送
    private String stageApplication;

    // 还款能力审查情况
    private String repaymentAbility;

    //  需要发送给银行的材料
    private List<String> imageTypeCodes = new ArrayList<String>();

    //  共同申请人存在时，可以选择信用卡分期业务审核表模板中，添加共同申请人信息
    private Boolean needCommon = false;

    public String getBillTypeCode() {
        return "A015";
    }

}