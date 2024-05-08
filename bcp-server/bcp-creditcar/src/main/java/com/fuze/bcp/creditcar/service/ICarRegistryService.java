package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.creditcar.domain.CarRegistry;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by GQR on 2017/8/19.
 */
public interface ICarRegistryService extends IBaseBillService<CarRegistry> {

    CarRegistry findByCustomer(CustomerBean customerBean);

    List<ObjectId> getRegistryFinishTransactionObjectIds(String date);

}
