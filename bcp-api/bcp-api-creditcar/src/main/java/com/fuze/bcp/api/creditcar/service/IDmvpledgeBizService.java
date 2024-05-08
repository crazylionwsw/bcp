package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.DMVPledgeBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.Map;

/**
 * 车辆抵押的接口
 */
public interface IDmvpledgeBizService {

    /**
     * 获取日报数据信息
     */
    ResultBean<Map<Object,Object>> getDailyReport(String orgid,String date, DMVPledgeBean t);

    ResultBean<Map<Object, Object>> getEmployeeReport(String employeeId ,String date, DMVPledgeBean t);

    /**
     * 创建车辆抵押信息(监控上牌通过事件)
     * @param carRegistryId
     */
    void actCreateDMVPledge(String carRegistryId);

    /**
     * 创建车辆抵押信息(监控转移过户通过事件)
     * @param carTransferId
     */
    void actCreateDMVPledgeByCarTransfer(String carTransferId);

    /**
     * 完成审批流任务
     * @param dmvPledge
     * @return
     */
    ResultBean<DMVPledgeBean> actApprovedDmvpledge(DMVPledgeBean dmvPledge,String loginUserId);

    //模糊查询(升级)
    ResultBean<DMVPledgeBean> actSearchPromoteDMVPledges(SearchBean searchBean);

    /**
     * 车辆抵押查询列表
     * @param currentPage
     * @param status
     * @return
     */
    ResultBean<DMVPledgeBean> actGetDMVPledges(int currentPage,Integer status);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ResultBean<DMVPledgeBean> actGetDmvpledge(String id);

    /**
     * 通过交易ID查询车辆抵押数据
     * @param transactionId
     * @return
     */
    ResultBean<DMVPledgeBean> actGetDmvpledgeByCustomerTransactionId(String transactionId);

    /**
     *  保存
     * @param dmvPledgeBean
     * @return
     */
    ResultBean<DMVPledgeBean> actSaveDMVPledge(DMVPledgeBean dmvPledgeBean);

    //保存抵押信息(只保存)
    ResultBean<DMVPledgeBean> actSaveDMVPledgeInfo(DMVPledgeBean dmvPledgeBean);
}
