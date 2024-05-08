package com.fuze.bcp.api.statistics.service;

import com.fuze.bcp.api.statistics.bean.BalaceAccountBean;
import com.fuze.bcp.api.statistics.bean.BalanceAccountDetailBean;
import com.fuze.bcp.api.statistics.bean.BalanceAccountExport;
import com.fuze.bcp.api.statistics.bean.ChargeFeePlanDetailBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import java.io.OutputStream;
import java.util.List;


/**
 * Created by GQR on 2017/11/7.
 */
public interface IBalanceAccountBizService {

    /**
     * 查询列表页面
     * @param status
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<BalaceAccountBean>> actGetAllBalanceAccounts(Integer status, Integer currentPage);

    /**
     * 结费详情
     * @param id
     * @return
     */
    ResultBean<List<BalanceAccountDetailBean>> actGetOneDetailById(String id);

    /**
     * 根据ID查询结费信息
     * @param id
     * @return
     */
    ResultBean<BalaceAccountBean> actGetOneById(String id);

    /**
     * 保存结费信息
     * @param balanceAccount
     * @return
     */
    ResultBean<BalaceAccountBean> actSaveBill(BalaceAccountBean balanceAccount);

    /**
     * 计算指定年月的银行费用结费表
     * @param year
     * @param month
     * @return
     */
    ResultBean<BalaceAccountBean> actCalculateBalanceAccounts(String year,String month,String loginUserId);

    /**
     * 根据还款计划表计算这个月的手续费收入
     * @param feePlanDetail
     * @param balanceAccount
     * @return
     */
    ResultBean<BalanceAccountDetailBean>  actCalculateOneChargeFeePlan(ChargeFeePlanDetailBean feePlanDetail, BalaceAccountBean balanceAccount);

    /**
     * 计算指定月份的用户结费表
     * @return
     */
    ResultBean<BalaceAccountBean> actCalculateBalanceAccount(BalaceAccountBean balanceAccount);

    /**
     * 导出结费信息
     * @param year
     * @param month
     * @return
     */
    ResultBean<List<BalanceAccountExport>>  actExportExcel(String year, String month);

    /**
     * 核对对账总表
     */
    ResultBean<BalaceAccountBean> actChecked(String loginUserId, String id, SignInfo signInfo);

    /**
     * 取消核对对账总表
     */
    ResultBean<BalaceAccountBean> actUnChecked(String loginUserId, String id, SignInfo signInfo);

    /**
     * 开始支付
     * @param loginUserId
     * @param id
     * @return
     */
    ResultBean<BalaceAccountBean> actStartPay(String loginUserId , String id);

    /**
     * 支付完成
     * @param loginUserId
     * @param id
     * @return
     */
    ResultBean<BalaceAccountBean> actOverPay(String loginUserId , String id);

    //根据交易Id查询收款计划
    ResultBean<BalanceAccountDetailBean> actGetBalanceAccountDetail(String transactionId);

    //保存
    ResultBean<BalanceAccountDetailBean> actSaveBalanceAccountDetail(BalanceAccountDetailBean balanceAccountDetailBean);

    //业务转移修改替换统计数据
    void actCardealerTransferWithStatistics(String bid,List<String> tids);
}
