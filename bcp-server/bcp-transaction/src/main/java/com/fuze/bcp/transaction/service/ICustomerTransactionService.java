package com.fuze.bcp.transaction.service;

import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.service.IBaseService;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Lily on 2017/7/31.
 */
public interface ICustomerTransactionService extends IBaseService<CustomerTransaction> {

    List<CustomerTransaction> getListsBySomeConditions(String loginUserId, String selectTime, List<String> customerIds, List<String> employeeIds, List<String> carDealerIds, List<Integer> statusList, String sortName, Boolean sortDesc);

    Page<CustomerTransaction> getPagesBySomeConditions(String loginUserId, String selectTime, List<String> customerIds, List<String> employeeIds, List<String> carDealerIds, List<Integer> statusList, Integer pageIndex, Integer pageSize, String sortName, Boolean sortDesc);

    Page<CustomerTransaction> findAllPagesBySearchBean(SearchBean searchBean);

    List<CustomerTransaction> getAllBySearchBean(SearchBean searchBean);

    Integer countTransactionsByCustomerId(String customerId);

    List<CustomerTransaction> getPendingTransactionsByLoginUserId(String loginUserId);

    List<CustomerTransaction> getAllTransactionsByLoginUserId(String loginUserId);

    ResultBean<CustomerTransaction> getEditableTransaction(String tid);

    String createCustomerNumber(String ts,String businessTypeCode);

    List<String> getTransactionIds(String userId, Boolean status);

    List<String>  getAllUnFinishTransactionIds();

    List<String> getTransactionIdsOnDecompress(String userId, Boolean status);

}
/*
*
* List<CustomerTransaction> getBusinessBooksByTime(String selectTime);
* */