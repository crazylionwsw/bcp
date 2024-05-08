package com.fuze.bcp.bean;

/**
 * 审核任务
 * Created by Lily on 2017/8/9.
 */
public interface ApproveTask {

     /* 查询征信 */
    String A001_QUERY_CREDITREPORT = "QUERY_CREDITREPORT" ;

     /* 审查核对 */
    String A001_CHECK_REVIEW = "CHECK_REVIEW";

     /* 签约核对 */
    String A002_CHECK_ORDER = "CHECK_ORDER";

     /* 申请制卡 */
    String A011_APPLY_BK = "APPLY_BK";

     /* 取卡 */
    String A011_TAKE_BK = "TAKE_BK";

     /* 代启卡 */
    String A011_REPLACEACTIVE_BK = "REPLACEACTIVE_BK";

     /* 启卡 */
    String A011_ACTIVE_BK = "ACTIVE_BK";

     /* 调额 */
    String A011_CHANGEAMOUNT_BK = "CHANGEAMOUNT_BK";

     /* 代刷卡 */
    String A011_SWIPINGTRUSTEE_BK = "SWIPINGTRUSTEE_BK";

     /* 领卡 */
    String A011_RECEIVETRUSTEE_BK = "RECEIVETRUSTEE_BK";

    /* 贴息领卡 */
    String A011_RECEIVEDISCOUNT_BK = "RECEIVEDISCOUNT_BK";

    /* 4S店刷卡 */
    String A011_SWIPINGSHOP_BK = "SWIPINGSHOP_BK";


}
