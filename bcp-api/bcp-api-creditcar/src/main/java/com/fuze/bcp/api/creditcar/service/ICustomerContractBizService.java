package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.CustomerContractBean;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * 客户合同接口类
 * Created by zqw on 2017/8/10.
 */
public interface ICustomerContractBizService {

    /**
     * 保存档案信息
     * @param customerContractBean
     * @return
     */
    ResultBean<CustomerContractBean> actSaveCustomerContract(CustomerContractBean customerContractBean);

    /**
     * 获取某交易的所有客户合同
     * @param customerId
     * @param customerTransactionId
     * @return
     */
    ResultBean<List<CustomerContractBean>> actGetTransactionContracts(String customerId, String customerTransactionId);

    /**
     *     获取某交易的某几种客户合同
     * @param customerId
     * @param customerTransactionId
     * @param documentIdsList               该单据需要的合同IDs
     * @return
     */
    ResultBean<List<CustomerContractBean>> actGetTransactionContracts(String customerId, String customerTransactionId, List<String> documentIdsList);


    /**
     *      下载合同
     * @param customerContractBean
     * @return
     */
    ResultBean<CustomerContractBean> actCreateCustomerContract(Boolean force,CustomerContractBean customerContractBean);

    //  获取某交易的某种客户合同
    ResultBean<CustomerContractBean> actGetTransactionContract(String customerId, String customerTransactionId, String documentId);

    ResultBean<CustomerContractBean> actDeleteCustomerContractById(String id);

    ResultBean actGetMergedCustomerContract(CustomerImageFileBean customerImageFileBean);


}
