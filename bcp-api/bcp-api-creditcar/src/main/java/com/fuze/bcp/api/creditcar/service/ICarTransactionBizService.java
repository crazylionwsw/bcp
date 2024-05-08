package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.creditcar.bean.BusinessExcelBean;
import com.fuze.bcp.api.creditcar.bean.CompensatoryExcelBean;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.PadCustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.TransactionSummaryBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * 处理车交易服务
 * Created by ZQW on 2018/3/17.
 */
public interface ICarTransactionBizService {

    /**
     * 获取我的待办的客户业务
     *
     * @param
     * @return
     */
    ResultBean<DataPageBean<PadCustomerTransactionBean>> actGetTransactionsByLoginUserId(String loginUserId, List<Integer> statusList, Integer pageIndex, Integer pageSize);

    /**
     *导出贴息交易数据
     */
    ResultBean<List<CompensatoryExcelBean>> actExportCompensatoryTransactions(Boolean compensatory, Boolean business, Boolean nc, Boolean oc, String swipingCardTime);

    /**
     *导出商贷交易数据
     */
    ResultBean<List<BusinessExcelBean>> actExportBusinessTransactions(Boolean compensatory, Boolean business, Boolean nc, Boolean oc, String swipingCardTime);

    /**
     * 通过交易Id查询渠道垫资策略是否需要垫资
     * @param customerTransactionId
     * @return
     */
    ResultBean<Integer> actGetPaymentPolicy(String customerTransactionId);

    /**
     *  通过交易ID，重新生成客户文档编号
     * @param id
     * @return
     */
    ResultBean<CustomerTransactionBean> actCreateFileNumberByTransactionId(String id);

    //渠道中的业务转移
    ResultBean<CarDealerBean> actGetCardealerTransfer(String bid, List<String> tids);

    /**
     * 获取交易的概览信息
     * @param tid
     * @return
     */
    ResultBean<TransactionSummaryBean> actGetTransactionSummary(String tid);

    /**
     * 获取交易列表
     * @param loginUserId
     * @param statusList
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean<DataPageBean<TransactionSummaryBean>> actGetTransactions(String loginUserId, List<Integer> statusList, Integer pageIndex, Integer pageSize);

    /**
     * 获取交易概览
     * @param customerTransaction
     * @param customerBean
     * @param customerCarBean
     * @param customerLoanBean
     * @return
     */
    TransactionSummaryBean actGetTransactionSummary(CustomerTransactionBean customerTransaction, CustomerBean customerBean, CustomerCarBean customerCarBean, CustomerLoanBean customerLoanBean, CarDealerBean carDealerBean);

    /**
     *  判断某单据必须的档案资料是否上传
     * @param billTypeCode                  单据编码
     * @param customerTransactionId         交易ID
     * @return
     */
    ResultBean<String> actCheckTransactionBillImageTypeFileExited(String billTypeCode, String customerTransactionId);

    /**
     * 获取我的全部交易 分页显示
     * @param employeeId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean<DataPageBean<PadCustomerTransactionBean>> actGetAllTransactionsByLoginUserId(String employeeId, Integer pageIndex, Integer pageSize);

    ResultBean<List<PadCustomerTransactionBean>> actGetTransactionQuery(String loginUserId, String inputStr);

    /**
     * 查询交易（通过姓名、手机号）
     * @param customerBean
     * @param loginUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean<DataPageBean<TransactionSummaryBean>> actSearchTransactions(CustomerBean customerBean, String loginUserId, int pageIndex, int pageSize );
}
