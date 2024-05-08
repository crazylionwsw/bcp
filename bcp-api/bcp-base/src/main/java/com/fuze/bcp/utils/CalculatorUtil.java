package com.fuze.bcp.utils;

/**
 * 计算器功能
 * <p>
 * Created by zqw on 2017/4/10.
 */
public class CalculatorUtil {

    /**
     * 计算  月还款(不保留小数点后面的数字)
     * 计算表达式：月还款 = 贷款额度 / 贷款期限
     *
     * @param creditAmount 贷款额度
     * @param creditMonths 贷款期限
     * @return
     */
    public static Double calculatorMonthlyPayments(Double creditAmount, Integer creditMonths) {
        return Math.floor(creditAmount / Double.parseDouble(String.valueOf(creditMonths)));
    }

    public static Double calculatorMonthlyPaymentsNew(Double creditAmount, Integer creditMonths) {

        return Math.floor((creditAmount-calculatorFirstPayment(creditAmount,creditMonths)) / (creditMonths-1));
    }

    /***
     * 		计算  差额
     * 		计算公式：差额 = 借款额-月还款*月份
     *
     * @param creditAmount      贷款额度
     * @param creditMonths      贷款期限
     * @return
     */
    public static Double calculatorBalance(Double creditAmount, Integer creditMonths) {
        return creditAmount - calculatorMonthlyPayments(creditAmount, creditMonths) * creditMonths;
    }

    /**
     * 计算  首期还款
     * 计算公式：	首期还款  = 	月还款 + 差额
     *
     * @param creditAmount 贷款额度
     * @param creditMonths 贷款期限
     * @return
     */
    public static Double calculatorFirstPayment(Double creditAmount, Integer creditMonths) {

        return calculatorMonthlyPayments(creditAmount, creditMonths) + calculatorBalance(creditAmount, creditMonths);
    }

    /**
     * 计算  月还款(不保留小数点后面的数字)  PAD 使用
     * 计算公式：
     * 趸交  =    贷款额度 / 期数
     * 分期  =    (贷款额度 + 银行手续费） / 期数
     *
     * @param creditAmount 贷款额度
     * @param creditMonths 贷款期限
     * @param charge       银行手续费
     * @return
     */
    public static Double calculatorMonthlyPayments(Double creditAmount, Double charge, Integer creditMonths, String chargePaymentWay) {
        if ("WHOLE".equals(chargePaymentWay)) {
            return Math.floor(creditAmount / Double.parseDouble(String.valueOf(creditMonths)));
        } else if ("STAGES".equals(chargePaymentWay)) {
            return Math.floor((creditAmount + charge) / Double.parseDouble(String.valueOf(creditMonths)));
        }
        return 0.0;
    }

    /**
     * 计算  首期还款  PAD 使用
     * 计算公式：
     * 趸交  =    贷款额度  -  （贷款额度 / 期数）* （贷款期限 - 1）+ 银行手续费
     * 分期  =    贷款额度  -  （贷款额度 / 期数）* （贷款期限 - 1）
     *
     * @param creditAmount 贷款额度
     * @param creditMonths 贷款期限
     * @param charge       银行手续费
     * @return
     */
    public static Double calculatorFirstPayment(Double creditAmount, Double charge, Integer creditMonths, String chargePaymentWay) {
        if ("WHOLE".equals(chargePaymentWay)) {
            return Math.floor((creditAmount - calculatorMonthlyPayments(creditAmount, charge, creditMonths, chargePaymentWay) * (creditMonths - 1)) + charge);
        } else if ("STAGES".equals(chargePaymentWay)) {
            return Math.floor((creditAmount + charge) - calculatorMonthlyPayments(creditAmount, charge, creditMonths, chargePaymentWay) * (creditMonths - 1));
        }
        return 0.0;
    }
}