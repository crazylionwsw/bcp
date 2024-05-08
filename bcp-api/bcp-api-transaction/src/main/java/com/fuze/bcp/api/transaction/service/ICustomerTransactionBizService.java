package com.fuze.bcp.api.transaction.service;

import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.TransactionStageBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 客户交易服务接口
 * Created by Lily on 2017/8/8.
 */
public interface ICustomerTransactionBizService {

    /**
     * 获取所有的交易
     * @return
     */
    ResultBean<List<CustomerTransactionBean>> actFindAllTransactions();

    /**
     * 根据IDS查询
     * @param ids
     * @return
     */
    ResultBean<List<CustomerTransactionBean>> actGetTransactions(List<String> ids);

    /**
     * 根据Id查询交易信息
     * @param id
     * @return
     */
    ResultBean<CustomerTransactionBean> actFindCustomerTransactionById(@NotNull String id);

    /**
     * 根据Id查询可用交易信息
     * @param id
     * @return
     */
    ResultBean<CustomerTransactionBean> actFindAvailableCustomerTransactionById(@NotNull String id);

    /**
     *      保存  客户交易数据
     * @param customerTransactionBean
     * @return
     */
    ResultBean<CustomerTransactionBean> actSaveCustomerTransaction(CustomerTransactionBean customerTransactionBean);

    /**
     * 获取交易的阶段
     * @param tid
     * @return
     */
    ResultBean<TransactionStageBean> actGetTransactionStage(String tid);

    /**
     *      查询客户的 交易信息      模糊查询
     * @param searchBean
     * @return
     */
    ResultBean<CustomerTransactionBean> actSearchCustomerTransactions(SearchBean searchBean);

    /**
     * 获取客户的在办业务数
     * @param customerId
     * @return
     */
    ResultBean<Integer> actCountTransactionsByCustomerId(String customerId);

    /**
     * 获取经销商在办业务数
     * @param cardealerId
     * @return
     */
    ResultBean<Integer> actCountTransactionByCardealerId(String cardealerId);

    ResultBean<TransactionStageBean> actGetTransactionStage(String tid, String bizCode, String paramCode);

    ResultBean<String> actCreateCustomerNumber(String ts,String businessTypeCode);

    ResultBean<List<CustomerTransactionBean>> actGetAllBySearchBean(SearchBean searchBean);

    ResultBean<CustomerTransactionBean> actGetEditableTransaction(String tid);

    ResultBean<List<String>> actGetTransactionIds(String loginUserId, Boolean isPass);

    ResultBean<DataPageBean<CustomerTransactionBean>> actGetPagesBySomeConditions(String loginUserId, String selectTime, List<String> customerIds, List<String> employeeIds, List<String> carDealerIds, List<Integer> statusList, Integer pageIndex, Integer pageSize, String sortName, Boolean sortDesc);

    ResultBean<List<CustomerTransactionBean>> actGetListsBySomeConditions(String loginUserId, String selectTime, List<String> customerIds, List<String> employeeIds, List<String> carDealerIds, List<Integer> statusList, String sortName, Boolean sortDesc);

    ResultBean<List<String>> actGetAllUnFinishTransactionIds();

    ResultBean<List<String>> actGetTransactionIdsOnDecompress(String userId, Boolean isPass);
}
