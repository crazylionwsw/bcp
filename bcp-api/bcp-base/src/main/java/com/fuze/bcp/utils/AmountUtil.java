package com.fuze.bcp.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by ${Liu} on 2018/3/22.
 * 关于金额小数点后的转换保留位数
 */
public class AmountUtil {

    /**
     *金额保留两位小数
     */
    public static String getMoneyTwo(String money){
        if (money == null){
            return null;
        }
        BigDecimal moneyString = new BigDecimal(money);
        moneyString = moneyString.setScale(2,BigDecimal.ROUND_HALF_UP);
        return moneyString.toString();
    }

    /**
     * 取整
     */
    public static String getMoneyNumber(String money){
        if (money == null){
            return null;
        }
        BigDecimal moneyString = new BigDecimal(money);
        moneyString = moneyString.setScale(0,BigDecimal.ROUND_HALF_UP);
        return moneyString.toString();
    }

    /**
     *  保留两位小数(Double类型)
     */
    public static Double getAmountTwo(Double money){
        if (money == null){
            return null;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String amount = df.format(money);
        return Double.valueOf(amount);
    }

    public static Double getAmountNumber(Double money){
        if (money == null){
            return null;
        }
        DecimalFormat df = new DecimalFormat("#");
        String amount = df.format(money);
        return Double.valueOf(amount);
    }
}
