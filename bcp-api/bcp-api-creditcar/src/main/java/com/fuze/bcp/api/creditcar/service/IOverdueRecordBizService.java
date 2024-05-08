package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.overdueRecord.OverdueRecordBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

/**
 * Created by ${Liu} on 2018/3/10.
 * 逾期记录接口
 */
public interface IOverdueRecordBizService {

    /**
     *逾期记录列表(h含查询)
     */
    ResultBean<OverdueRecordBean> actSearchOverdueRecord(String userId, SearchBean searchBean);

    /**
     *查询逾期记录
     */
    ResultBean<OverdueRecordBean> actGetOverdueRecord(String overdueRecordId ,Integer dataStatus);

    /**
     * 查询次数
     * @param customertransactionId
     * @return
     */
    ResultBean<OverdueRecordBean> actGetOverdueRecordCount(String customertransactionId);

    /**
     *保存逾期记录
     */
    ResultBean<OverdueRecordBean> actSaveOverdueRecord(OverdueRecordBean overdueRecordBean,String userId);

    /**
     *发送逾期提醒
     */
    ResultBean<OverdueRecordBean> actSendRemindMsg(String overdueRecordId);

    /**
     *判断当月是否已有记录
     */
    ResultBean<OverdueRecordBean> actGetOverdueRecordByMonth(String transactionId,String month);

    /**
     *创建逾期记录
     */
    ResultBean<OverdueRecordBean> actCreateOverdueRecord(String transactionId,String userId);

}
