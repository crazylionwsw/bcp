package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.creditcar.domain.RejectCustomer;
import com.fuze.bcp.service.IBaseService;
import org.springframework.data.domain.Page;

/**
 * Created by zqw on 2017/6/2.
 */

public interface IRejectCustomerService extends IBaseService<RejectCustomer> {


    Page<RejectCustomer> findAllBySearchBean(SearchBean searchBean);
}

