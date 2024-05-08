package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.creditcar.bean.ResetOrderBean;
import com.fuze.bcp.api.creditcar.service.IResetOrderBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.ResetOrder;
import com.fuze.bcp.creditcar.service.IResetOrderService;
import com.fuze.bcp.service.MappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/23.
 */
@Service
public class BizResetOrderService implements IResetOrderBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizResetOrderService.class);

    @Autowired
    MappingService mappingService;

    @Autowired
    IResetOrderService iResetOrderService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Override
    public ResultBean<ResetOrderBean> actSaveResetOrder(ResetOrderBean resetOrderBean) {
        return null;
    }

    @Override
    public ResultBean<ResetOrderBean> actSubmitResetOrder(ResetOrderBean resetOrderBean) {
        return null;
    }

    /**
     * 根据id右边框显示
     * @param id
     * @return
     */
    @Override
    public ResultBean<ResetOrderBean> actGetResetOrder(String id) {
        ResetOrder resetOrder = iResetOrderService.getOne(id);
        if (resetOrder == null ){
            return ResultBean.getFailed();
        }
        String code = resetOrder.getBillTypeCode();
        BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
        ResetOrderBean resetOrderBean = mappingService.map(resetOrder, ResetOrderBean.class);
        resetOrderBean.setBillType(billType);
        return ResultBean.getSucceed().setD(resetOrderBean);
    }

    public ResultBean<ResetOrderBean> actGetResetOrderByTransactionId(String transactionId){
        ResetOrder resetOrder = iResetOrderService.findByCustomerTransactionId(transactionId);
        if (resetOrder == null ){
            return ResultBean.getFailed();
        }
        String code = resetOrder.getBillTypeCode();
        BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
        ResetOrderBean resetOrderBean = mappingService.map(resetOrder, ResetOrderBean.class);
        resetOrderBean.setBillType(billType);
        return ResultBean.getSucceed().setD(resetOrderBean);
    }

    /**
     * 列表带分页
     * @param currentPage
     * @param approveStatus
     * @return
     */
    @Override
    public ResultBean<ResetOrderBean> actGetResetOrders(int currentPage, int approveStatus) {
        Page<ResetOrder> resetOrders;
        if(approveStatus == -1){
            resetOrders = iResetOrderService.findAllByOrderByTsDesc(currentPage);
        }else {
            resetOrders = iResetOrderService.findAllByApproveStatusOrderByTsDesc(approveStatus,currentPage);
        }
        return ResultBean.getSucceed().setD(mappingService.map(resetOrders,ResetOrderBean.class));
    }

    /**
     * 模糊查询 带分页
     * @param currentPage
     * @param approveStatus
     * @param customerBean
     * @return
     */
    @Override
    public ResultBean<ResetOrderBean> actSearchResetOrders(int currentPage, int approveStatus, CustomerBean customerBean) {
        List<CustomerBean> customerBeanList = iCustomerBizService.actSearchCustomer(customerBean).getD();
        List<String> customerIds = new ArrayList<String>();
        for (CustomerBean cb : customerBeanList) {
            customerIds.add(cb.getId());
        }
        Page<ResetOrder> resetOrders;
        if(approveStatus == -1){
            resetOrders = iResetOrderService.findAllByCustomerIds(customerIds,currentPage);
        }else{
            resetOrders = iResetOrderService.findAllByApproveStatusAndCustomerIds(customerIds,approveStatus,currentPage);
        }
        return ResultBean.getSucceed().setD(mappingService.map(resetOrders,ResetOrderBean.class));
    }

    @Override
    public ResultBean<ResetOrderBean> actSignResetOrder(String id, SignInfo signInfo) {
        return null;
    }

    @Override
    public ResultBean<List<ResetOrderBean>> actGetResetOrders(String loginUserId, Integer currentPage) {
        return null;
    }
}
