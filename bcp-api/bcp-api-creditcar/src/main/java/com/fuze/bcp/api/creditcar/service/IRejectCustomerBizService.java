package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.RejectCustomerBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

/**
 * Created by zqw on 2017/12/18.
 */
public interface IRejectCustomerBizService {

    /**
     *  资质审查任何一步被审批拒绝，经过部门审批，判断是否被拒绝
     *      是，就创建被拒绝客户信息
     *      否，不做任何操作
     * @param demandId
     * @return
     */
    ResultBean<RejectCustomerBean> actCreateRejectCustomerByDemandId(String demandId);

    /**
     *  客户签约任何一步被审批拒绝，就判断是否被拒绝
     *      是，就创建被拒绝客户信息
     *      否，不做任何操作
     * @param orderId
     * @return
     */
    ResultBean<RejectCustomerBean> actCreateRejectCustomerByOrderId(String orderId);

    /**
     *  模糊查询
     * @param searchBean
     * @return
     */
    ResultBean<RejectCustomerBean> actSearchRejectCustomers(SearchBean searchBean);
}
