package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.customerfeebill.CustomerFeeBillBean;
import com.fuze.bcp.bean.ResultBean;

/**客户汇款信息接口
 * Created by ${Liu} on 2017/9/27.
 */
public interface ICustomerFeeBizService {

    /**
     *根据交易ID获取客户汇款信息
     */
    ResultBean<CustomerFeeBillBean> actGetCustomerFeeBill(String customerTransactionId);

}
