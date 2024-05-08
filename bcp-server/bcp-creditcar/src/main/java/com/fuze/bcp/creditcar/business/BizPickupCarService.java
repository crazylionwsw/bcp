package com.fuze.bcp.creditcar.business;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.creditcar.bean.PickupCarBean;
import com.fuze.bcp.api.creditcar.service.IPickupCarBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.PickupCar;
import com.fuze.bcp.creditcar.service.IPickupCarService;
import com.fuze.bcp.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by GQR on 2017/8/19.
 */
@Service
public class BizPickupCarService implements IPickupCarBizService {
    @Autowired

    private IPickupCarService iPickupCarService;
    @Autowired

    private IBaseDataBizService iBaseDataBizService;
    @Autowired

    private ICustomerBizService iCustomerBizService;

    @Autowired
    MappingService mappingService;



    @Override
    public ResultBean<PickupCarBean> actGetPickupCars(int currentPage) {
        Page<PickupCar> pickupCarPage = iPickupCarService.findAllByPickupCarByTsDesc(currentPage);
        if ( pickupCarPage != null ){
            return ResultBean.getSucceed().setD(mappingService.map(pickupCarPage,PickupCarBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PickupCarBean> getPickupCarsByCustomer(CustomerBean customer, Integer status) {
        List<CustomerBean> customers = iCustomerBizService.actSearchCustomer(customer).getD();
        List<String> ids = new ArrayList<String>();
        for (CustomerBean customer1 : customers){
            ids.add(customer1.getId());
        }
        List<PickupCar> pickupCars = iPickupCarService.getPickupCarsByCustomer(DataStatus.SAVE,ids);
        if(pickupCars != null){
            return  ResultBean.getSucceed().setD(mappingService.map(pickupCars,PickupCarBean.class));
        }
        return ResultBean.getFailed().setM("客户的提车业务");
    }

    @Override
    public ResultBean<PickupCarBean> actGetPickupCar(String id) {
        PickupCar pickupCar = iPickupCarService.getOne(id);
        if ( pickupCar != null ){
            String billTypeCode = pickupCar.getBillTypeCode();
            BillTypeBean billTypeBean = iBaseDataBizService.actGetBillType(billTypeCode).getD();
            PickupCarBean pickupCarBean = mappingService.map(pickupCar, PickupCarBean.class);
            pickupCarBean.setBillType(billTypeBean);
            return ResultBean.getSucceed().setD(pickupCarBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PickupCarBean> actSearchPickupCars(int currentPage, CustomerBean customerBean) {
        List<CustomerBean> customerBeanList = iCustomerBizService.actSearchCustomer(customerBean).getD();
        List<String> customerIds = new ArrayList<String>();
        for (CustomerBean cb : customerBeanList) {
            customerIds.add(cb.getId());
        }
        Page<PickupCar> pickupCarPage =  iPickupCarService.findAllByCustomerIds(customerIds,currentPage);

        if (pickupCarPage!=null){
            return ResultBean.getSucceed().setD(mappingService.map(pickupCarPage,PickupCarBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PickupCarBean> actSavePickupCar(PickupCarBean pickupCarBean) {
        PickupCar pickupCar = mappingService.map(pickupCarBean, PickupCar.class);
        pickupCar = iPickupCarService.save(pickupCar);
        return ResultBean.getSucceed().setD(mappingService.map(pickupCar,PickupCarBean.class));
    }

    @Override
    public ResultBean<PickupCarBean> actSignPickupCar(String id, SignInfo signInfo) {
        return null;
    }

    @Override
    public ResultBean<PickupCarBean> actGetByCustomerTransactionId(String customerTransactionId) {
        PickupCar pickupCar = iPickupCarService.findPickupCarByCustomerTransactionId(customerTransactionId);
        if ( pickupCar != null ){
            return ResultBean.getSucceed().setD(mappingService.map(pickupCar,PickupCarBean.class));
        }
        return ResultBean.getFailed();
    }

    /**
     * 日报
     * @param date
     * @param t
     * @return
     */
    @Override
    public ResultBean<Map<Object, Object>> getDailyReport(String orgid,String date, PickupCarBean t) {
        Map<Object, Object> dailyReport = iPickupCarService.getDailyReport(orgid,date, mappingService.map(t, PickupCar.class));
        if(dailyReport != null){
            return  ResultBean.getSucceed().setD(dailyReport);
        }else {
            return ResultBean.getFailed();
        }
    }
}
