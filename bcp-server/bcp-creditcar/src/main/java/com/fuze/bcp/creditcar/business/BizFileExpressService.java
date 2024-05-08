package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.creditcar.bean.FileExpressBean;
import com.fuze.bcp.api.creditcar.bean.fileexpress.FileExpressListBean;
import com.fuze.bcp.api.creditcar.bean.fileexpress.FileExpressSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IFileExpressBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.FileExpress;
import com.fuze.bcp.creditcar.service.IFileExpressService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 2017/8/17.
 */
@Service
public class BizFileExpressService implements IFileExpressBizService{

    @Autowired
    IFileExpressService iFileExpressService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    /**
     * 创建资料快递单
     * @return
     */
    public ResultBean<FileExpressBean> actCreateFileExpress() {

        return null;
    }


    @Override
    public ResultBean<FileExpressBean> actFileExpress(String orderId) {
        return null;
    }

    @Override
    public ResultBean<FileExpressSubmissionBean> actSaveFileExpress(FileExpressSubmissionBean fileExpressSubmissionBean) {
        return null;
    }

    @Override
    public ResultBean<FileExpressBean> actSubmitFileExpress(String id, String comment) {
        return null;
    }

    @Override
    public ResultBean<List<FileExpressListBean>> actGetFileExpress(String loginUserId, Integer currentPage, Integer currentSize) {
        return null;
    }

    @Override
    public ResultBean<FileExpressSubmissionBean> actInitFileExpressByTransactionId(String transactionId) {
        return null;
    }

    @Override
    public ResultBean<FileExpressBean> actGetFileExpress(String id) {
        FileExpress fileExpress = iFileExpressService.getOne(id);
        String code = fileExpress.getBillTypeCode();
        //通过编码获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();

        FileExpressBean fileExpressBean = mappingService.map(fileExpress, FileExpressBean.class);
        fileExpressBean.setBillType(billType);
        return ResultBean.getSucceed().setD(fileExpressBean);
    }

    @Override
    public ResultBean<FileExpressBean> actConfirmFileExpress(String id, SignInfo signInfo) {
        return null;
    }

    @Override
    public ResultBean<FileExpressBean> actGetFileExpresss(int currentPage) {
        Page<FileExpress> fileExpresses;
        fileExpresses = iFileExpressService.findAllByOrderByTsDesc(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(fileExpresses,FileExpressBean.class));
    }

    @Override
    public ResultBean<FileExpressBean> actSearchFileExpresss(int currentPage, CustomerBean customerBean) {
        List<CustomerBean> customerBeanList = iCustomerBizService.actSearchCustomer(customerBean).getD();
        List<String> customerIds = new ArrayList<String>();
        for (CustomerBean cb : customerBeanList) {
            customerIds.add(cb.getId());
        }
        Page<FileExpress> fileExpresses;
        fileExpresses = iFileExpressService.findAllByCustomerIds(customerIds, currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(fileExpresses,FileExpressBean.class));
    }
}
