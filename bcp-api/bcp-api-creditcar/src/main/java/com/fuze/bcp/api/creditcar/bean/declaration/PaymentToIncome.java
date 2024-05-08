package com.fuze.bcp.api.creditcar.bean.declaration;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqw on 2017-8-5 17:24:03
 */
//收入还贷比
@Data
public class PaymentToIncome implements Serializable {

    /**
     * 收入来源资料证明类型
     * 申请表？收入证明？社保卡？银行流水？
     */
    private List<String>  sourceIncomes ;

    // 账户一收入流水（万元）
    private List<IncomeAccount> accountList = new ArrayList<IncomeAccount>();

    // 账户二收入流水（万元）
    private List<IncomeAccount> accountList2 = new ArrayList<IncomeAccount>();

    //账户一情况说明
    private String accountOneDescription;

    //  账户一账户类型
    private String accountOneType;

    //账户二情况说明
    private String accountTwoDescription;

    //  账户二账户类型
    private String accountTwoType;

}