package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.creditcar.bean.overdueRecord.OverdueRecordBean;
import com.fuze.bcp.api.creditcar.service.IOverdueRecordBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.msg.bean.NoticeBean;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.creditcar.domain.OverdueRecord;
import com.fuze.bcp.creditcar.service.IOverdueRecordService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ${Liu} on 2018/3/10.
 * 逾期记录服务
 */
@Service
public class BizOverdueRecordService implements IOverdueRecordBizService{

    private static final Logger logger = LoggerFactory.getLogger(BizOverdueRecordService.class);

    @Autowired
    IOverdueRecordService iOverdueRecordService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    IMessageBizService iMessageBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<OverdueRecordBean> actSearchOverdueRecord(String userId, SearchBean searchBean) {
        Page<OverdueRecord> overdueRecords = iOverdueRecordService.findAllBySearchBean(OverdueRecord.class, searchBean, SearchBean.STAGE_ORDER, userId);
        return ResultBean.getSucceed().setD(mappingService.map(overdueRecords,OverdueRecordBean.class));
    }

    @Override
    public ResultBean<OverdueRecordBean> actGetOverdueRecord(String overdueRecordId ,Integer dataStatus) {
        OverdueRecord overdueRecord;
        if(dataStatus == 0){
            overdueRecord = iOverdueRecordService.getOne(overdueRecordId);
        } else{
            overdueRecord = iOverdueRecordService.getAvailableOne(overdueRecordId);
        }
        if(overdueRecord != null){
            BillTypeBean billTypeBean = iBaseDataBizService.actGetBillType(overdueRecord.getBillTypeCode()).getD();
            OverdueRecordBean overdueRecordBean = mappingService.map(overdueRecord, OverdueRecordBean.class);
            overdueRecordBean.setBillType(billTypeBean);
            return ResultBean.getSucceed().setD(overdueRecordBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<OverdueRecordBean> actGetOverdueRecordCount(String customertransactionId) {
        List<OverdueRecord> overdues  = iOverdueRecordService.findByCustomerTransactionIdAndDataStatus(customertransactionId,DataStatus.SAVE);
        return ResultBean.getSucceed().setD(overdues.size());
    }

    @Override
    public ResultBean<OverdueRecordBean> actSaveOverdueRecord(OverdueRecordBean overdueRecordBean,String userId) {
        if(overdueRecordBean.getDataStatus() == 0){
            overdueRecordBean.setDataStatus(1);
        }
        overdueRecordBean.setApproveUserId(userId);
        overdueRecordBean.setApproveDate(DateTimeUtils.getCreateTime());
        OverdueRecord overdueRecord = iOverdueRecordService.save(mappingService.map(overdueRecordBean, OverdueRecord.class));
        this.actSendRemindMsg(overdueRecord.getId());
        return ResultBean.getSucceed().setD(mappingService.map(overdueRecord,OverdueRecordBean.class));
    }

    @Override
    public ResultBean<OverdueRecordBean> actSendRemindMsg(String overdueRecordId) {
        OverdueRecord overdueRecord = iOverdueRecordService.getAvailableOne(overdueRecordId);
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(overdueRecord.getCustomerTransactionId()).getD();
        CustomerBean customerBean = iCustomerBizService.actGetCustomerById(overdueRecord.getCustomerId()).getD();
        String customerName = null;
        if(customerBean != null){
            customerName = customerBean.getName();
        }
        if(overdueRecord != null){
            NoticeBean noticeBean = new NoticeBean();
            noticeBean.setType("type_1");
            noticeBean.setSendType("pad");
            noticeBean.setContent("客户"+customerName+""+overdueRecord.getOverdueTime()+"还款已经逾期，请提醒客户及时还款!");
            noticeBean.setTitle("业务通知");
            noticeBean.setFromGroup(1);
            Set set = new HashSet<>();
            set.add(customerTransaction.getEmployeeId());
            noticeBean.setLoginUserNames(set);
            NoticeBean noticeBean1 = iMessageBizService.actSaveNotice(noticeBean).getD();
            if(noticeBean1 != null){
                NoticeBean bean = iAmqpBizService.actSendNotice(noticeBean1.getId()).getD();
                if(bean != null){
                    logger.info(String.format(messageService.getMessage("MSG_OVERDUERECORDSEND_COMPLETE"), "交易为:"+customerTransaction.getId()));
                    return ResultBean.getSucceed().setD(mappingService.map(overdueRecord,OverdueRecordBean.class));
                }
            }
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<OverdueRecordBean> actGetOverdueRecordByMonth(String transactionId, String month) {
        OverdueRecord overdueRecord = iOverdueRecordService.getOverdueRecordByMonth(transactionId, month);
        if(overdueRecord != null){
            return ResultBean.getSucceed().setD(mappingService.map(overdueRecord,OverdueRecordBean.class));
        }
        return null;
    }

    @Override
    public ResultBean<OverdueRecordBean> actCreateOverdueRecord(String transactionId,String userId) {
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(transactionId).getD();
        if(customerTransaction == null){
            return ResultBean.getFailed();
        }
        iOverdueRecordService.delOverdueRecordByTransactionId(transactionId);
        OverdueRecordBean overdueRecordBean = this.initOverdueRecord(customerTransaction);
        overdueRecordBean.setLoginUserId(userId);
        OverdueRecord overdueRecord = iOverdueRecordService.tempSave(mappingService.map(overdueRecordBean, OverdueRecord.class));
        if(overdueRecord != null){
            return ResultBean.getSucceed().setD(mappingService.map(overdueRecord,OverdueRecordBean.class));
        }
        return ResultBean.getFailed();
    }

    public OverdueRecordBean initOverdueRecord(CustomerTransactionBean customerTransaction){
        OverdueRecordBean overdueRecordBean = new OverdueRecordBean();
        overdueRecordBean.setCustomerId(customerTransaction.getCustomerId());
        overdueRecordBean.setCustomerTransactionId(customerTransaction.getId());
        overdueRecordBean.setCarDealerId(customerTransaction.getCarDealerId());
        overdueRecordBean.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
        overdueRecordBean.setEmployeeId(customerTransaction.getEmployeeId());
        return overdueRecordBean;
    }
}
