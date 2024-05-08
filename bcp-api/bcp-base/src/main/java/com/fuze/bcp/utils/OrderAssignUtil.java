package com.fuze.bcp.utils;

/**
 * Created by sean on 2017/7/3.
 * 订单分配算法实现
 */
public class OrderAssignUtil {


    /**
     * 默认A是渠道行，B是报单行
     * 支行A 和 支行B 进行合作， A的目前手续费总收入是totalA ,B的目前手续费总收入是totalB ，按照分成标准是A占比是pa， B占比是pb
     * ()，已分配的手续费总额（totalA,totalB），手续费所占百分比(pa,pb)，要求pa+pb = 1
     *
     * @param chargeFee 当前订单手续费金额
     * @param totalA  A支行已分配的手续费总额
     * @param totalB  B支行已分配的手续费总额
     * @param PA  A支行手续费所占比率
     * @param PB  B支行手续费所占比率
     * @return 返回A，则订单分配给A，返回B，则订单分配给B
     */
    public static String assign(double chargeFee,double totalA,double totalB, double PA,double PB){
        String res = "A";
        if((totalA == 0.0) && ( totalB==0.0)){
            //第一种情况，totalA和totalB都是0，判断pa 和pb的大小，谁大分给谁
            if(PA>=PB){
                res = "A";
            }else{
                res = "B";
            }
        }else{
            //第二种情况, totalA和totalB计算比率，已经存在的比率还没有达到比率标准
            double oldPA = totalA/(totalA+totalB);
            double oldPB = 1 - oldPA;
            if(oldPA>PA){
                //A支行已经超过比率
                res = "B";
            }else if(oldPA < PA){
                //A支行小于比率
                res = "A";
            }else{
                //AB支行比率相等
                if(PA>=PB){
                    res = "A";
                }else{
                    res = "B";
                }
            }
        }

        return res;
    }

    public static void main(String[] strs){
        String res = OrderAssignUtil.assign(1000.00,0.0,0.0,0.7,0.3);
        System.out.println(res);
        res = OrderAssignUtil.assign(1000.00,0.0,0.0,0.7,0.3);
        System.out.println(res);
        res = OrderAssignUtil.assign(3000.00,15000,6000.0,0.6,0.4);
        System.out.println(res);
        res = OrderAssignUtil.assign(5000.00,50000,12000.0,0.7,0.3);
        System.out.println(res);
    }


}
