package com.fuze.bcp.api.customer.service;

import com.fuze.bcp.api.customer.bean.CustomerCheckBean;
import com.fuze.bcp.bean.ResultBean;

/**
 * Created by Lily on 2017/8/3.
 */
public interface ICustomerCheckBizService {

    /**
     * 通过第三方接口查询数据
     * @param id
     * @param customerId
     * @return
     */
    ResultBean<CustomerCheckBean> actFindCustomerCheck(String id,String customerId);

    /**
     * 通过客户id从数据库查询数据
     * @param customerId
     * @return
     */
    ResultBean<CustomerCheckBean> actFindCustomerCheckByCustomerId(String customerId);

    /**
     * 统计当前客户数量
     * @param customerId
     * @return
     */
    ResultBean<Integer> actFindCustomerCheckCountByCustomerId(String customerId);
}
