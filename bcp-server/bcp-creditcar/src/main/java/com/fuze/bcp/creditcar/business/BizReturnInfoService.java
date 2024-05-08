package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.creditcar.bean.ReturnInfoBean;
import com.fuze.bcp.api.creditcar.service.IReturnInfoBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.ReturnInfo;
import com.fuze.bcp.creditcar.service.IReturnInfoService;
import com.fuze.bcp.service.MappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GQR on 2017/8/24.
 */
@Service
public class BizReturnInfoService implements IReturnInfoBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizReturnInfoService.class);

    @Autowired
    private IReturnInfoService iReturnInfoService;

    @Autowired
    MappingService mappingService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    /**
     * 分页列表查询
     * @param currentPage
     * @return
     */
    @Override
    public ResultBean<ReturnInfoBean> actGetReturnInfos(int currentPage) {
        Page<ReturnInfo> returnInfoPage;
            returnInfoPage = iReturnInfoService.findAllByOrderByTsDesc(currentPage);

        return ResultBean.getSucceed().setD(mappingService.map(returnInfoPage, ReturnInfoBean.class));
    }

    /**
     * 模糊查询
     * @param currentPage
     * @param customerBean
     * @return
     */
    @Override
    public ResultBean<ReturnInfoBean> actSearchReturnInfos(int currentPage, CustomerBean customerBean) {
        List<CustomerBean> customerBeanList = iCustomerBizService.actSearchCustomer(customerBean).getD();
        List<String> customerIds = new ArrayList<String>();
        for (CustomerBean cb : customerBeanList) {
            customerIds.add(cb.getId());
        }
        Page<ReturnInfo> returnInfos = iReturnInfoService.findAllByCustomerIds(customerIds, currentPage);

        return ResultBean.getSucceed().setD(mappingService.map(returnInfos, ReturnInfoBean.class));
    }

    /**
     * 根据ID回显
     * @param id
     * @return
     */
    @Override
    public ResultBean<ReturnInfoBean> actGetReturnInfo(String id) {
        ReturnInfo returnInfo=iReturnInfoService.getOne(id);
        String code=returnInfo.getBillTypeCode();
        BillTypeBean billType=iBaseDataBizService.actGetBillType(code).getD();
        ReturnInfoBean returnInfoBean=mappingService.map(returnInfo,ReturnInfoBean.class);
        returnInfoBean.setBillType(billType);
        return ResultBean.getSucceed().setD(returnInfoBean);
    }

    /**
     * 接收MQ消息，创建资料归还单
     * @param tid 交易ID
     * @return
     */
    public ResultBean<ReturnInfoBean> actCreateReturnInfo(String tid) {

        ReturnInfo returnInfo = iReturnInfoService.findByCustomerTransactionId(tid);
        if (returnInfo == null) {
            //TODO: 创建新的资料归还单
            returnInfo = new ReturnInfo();
            returnInfo.setCustomerTransactionId(tid);

            iReturnInfoService.save(returnInfo);
        }

        return ResultBean.getSucceed().setD(mappingService.map(returnInfo, ReturnInfoBean.class));
    }

    @Override
    public ResultBean<ReturnInfoBean> actSaveReturnInfo(ReturnInfoBean returnInfo) {

        ResultBean result = iCustomerTransactionBizService.actGetEditableTransaction(returnInfo.getCustomerTransactionId());
        if (result.failed()) return result;


        ReturnInfo returnInfo1 = mappingService.map(returnInfo, ReturnInfo.class);
        returnInfo1 = iReturnInfoService.save(returnInfo1);


        //TODO: 完成大工作流任务
        return null;
    }
}
