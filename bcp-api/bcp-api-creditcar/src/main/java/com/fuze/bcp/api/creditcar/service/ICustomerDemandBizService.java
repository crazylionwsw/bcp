package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.CustomerDemandBean;
import com.fuze.bcp.api.creditcar.bean.DemandListBean;
import com.fuze.bcp.api.creditcar.bean.DemandSubmissionBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.TEMSignInfo;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;
import java.util.Map;

/**
 * 资质审查接口
 * Created by Lily on 2017/7/19.
 */
public interface ICustomerDemandBizService {

    /**
     * 获取日报数据信息
     */
    ResultBean<Map<Object,Object>> getDailyReport(String orgid,String date, CustomerDemandBean t);

    /**
     * 获取分期经理报表
     */
    ResultBean<Map<Object,Object>> getEmployeeReport(String date, CustomerDemandBean t,String employeeId);

    /**
     * 获取渠道经理报表
     */
    ResultBean<Map<Object,Object>> getChannelReport(String date, CustomerDemandBean t,String loginUserId);


    /**
     * 获取统计数据
     * @param as
     * @return
     */
    ResultBean<List<Map>> getAllCustomerByApproveStatus(Integer as);

    /**
     * 提交客户购车资质信息
     * @param demandSubmission
     * @return
     */
    ResultBean<CustomerDemandBean> actSubmitCustomerDemand(DemandSubmissionBean demandSubmission);

    /**
     * 获取购车资质
     * @param transactionId
     * @return
     */
    ResultBean<DemandSubmissionBean> actRetrieveCustomerDemand(String transactionId);

    /**
     * 审核
     * @param customerDemandId
     * @param signInfo
     * @return
     */
    ResultBean<CustomerDemandBean> actSignCustomerDemand(String customerDemandId, SignInfo signInfo);

//    /**
//     * 审核
//     * @param customerDemandId
//     * @param temSignInfo
//     * @return
//     */
//    ResultBean<CustomerDemandBean> actSignCustomerDemand(String customerDemandId, TEMSignInfo temSignInfo);

    /**
     * 根据配偶的ID查询客户的资质审核单据
     * @param customerId
     * @return
     */
    ResultBean<CustomerDemandBean> actFindCustomerDemandByMateCustomerId(String customerId);

    /**
     * 根据ID查询车辆资质信息
     * @param id
     * @return
     */
    ResultBean<CustomerDemandBean> actFindCustomerDemandById(String id);

    /**
     * 根据客户id查询资质信息
     * @param customerId
     * @return
     */
    ResultBean<CustomerDemandBean> actFindCustomerDemandByCustomerId(String customerId);

    /**
     *  模糊查询 资质列表
     * @param userId        当前登录用户的ID
     * @param searchBean    查询条件集合
     * @return
     */
    ResultBean<CustomerDemandBean> actSearchCustomerDemands(String userId, SearchBean searchBean);

    /**
     *      保存  资质信息
     * @param customerDemandBean
     * @return
     */
    ResultBean<CustomerDemandBean> actSaveCustomerDemand(CustomerDemandBean customerDemandBean);

    /**
     * 根据transactionid 获取资质信息
     * @param customerTransactionId
     * @return
     */
    ResultBean<CustomerDemandBean>  actGetByCustomerTransactionId(String customerTransactionId);


    /**
     *      保存资质信息,是否需要反担保人信息
     * @param customerDemandBean
     * @return
     */
    ResultBean<CustomerDemandBean> actSaveCustomerDemandNeedCounterGuarantor(CustomerDemandBean customerDemandBean);

    /**
     * 获取分期经理资质信息
     * @param isPass
     * @param loginUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean<DataPageBean<DemandListBean>> actGetCustomerDemands(Boolean isPass,String loginUserId, Integer pageIndex, Integer pageSize);


}
