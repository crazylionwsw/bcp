package com.fuze.bcp.credithome.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.credithome.bean.DomesticOutfitBean;
import com.fuze.bcp.api.credithome.bean.DomesticOutfitSubmissionBean;
import com.fuze.bcp.api.credithome.service.IDomesticOutfitBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.bean.CustomerJobBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.credithome.domain.DomesticOutfit;
import com.fuze.bcp.credithome.service.IDomesticOutfitService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.SimpleUtils;
import com.fuze.bcp.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 家装分期微服务
 * Created by ZQW on 2018/3/19.
 */
@Service
public class BizDomesticOutfitService implements IDomesticOutfitBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizDomesticOutfitService.class);

    @Autowired
    IDomesticOutfitService iDomesticOutfitService;

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

    @Override
    public ResultBean<DomesticOutfitBean> actSubmitDomesticOutfit(DomesticOutfitSubmissionBean domesticOutfitSubmissionBean) {

        String today = SimpleUtils.getShortDate();
        String identifyNo = domesticOutfitSubmissionBean.getIdentifyNo();
        String channelId = domesticOutfitSubmissionBean.getChannelId();
        String loginUserId = domesticOutfitSubmissionBean.getLoginUserId();
        CustomerBean customerBean = iCustomerBizService.actGetCustomerByIdentifyNo(identifyNo).getD();
        if (customerBean != null) {
            List customerIds = new ArrayList<>();
            customerIds.add(customerBean.getId());
            List<CustomerTransactionBean> transactions = (List<CustomerTransactionBean>) iCustomerTransactionBizService.actGetListsBySomeConditions(loginUserId,null,customerIds,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<Integer>(),"ts", true).getD();
            for (CustomerTransactionBean transaction : transactions) {
                CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarByTransactionId(transaction.getId()).getD();
                if (customerCarBean != null && transaction.getTs().indexOf(today) != -1) {
                    if (channelId.equals(transaction.getChannelId())) {
                        return ResultBean.getFailed().setM("当天禁止提交重复数据");
                    }
                }
            }
        }
        ResultBean result;
        //保存贷款主体信息
        CustomerBean creditMaster = new CustomerBean();
        DomesticOutfit domesticOutfit = new DomesticOutfit();
        CustomerTransactionBean customerTransaction = null;
        if (StringHelper.isBlock(domesticOutfitSubmissionBean.getId())) {
            //  添加客户信息
            creditMaster.setName(domesticOutfitSubmissionBean.getCustomerName());
            creditMaster.setGender(domesticOutfitSubmissionBean.getGender());
            creditMaster.setIdentifyNo(domesticOutfitSubmissionBean.getIdentifyNo());
            List<String> list = new ArrayList<String>();
            list.add(domesticOutfitSubmissionBean.getCell());
            creditMaster.setCells(list);
            CustomerJobBean customerJobBean = new CustomerJobBean();
            customerJobBean.setCompanyName(domesticOutfitSubmissionBean.getCompanyName());
            creditMaster.setCustomerJob(customerJobBean);
            creditMaster.setHouseAddress(domesticOutfitSubmissionBean.getHouseAddress());
            creditMaster.setHouseDetailAddress(domesticOutfitSubmissionBean.getHouseDetailAddress());
            creditMaster.setComment(domesticOutfitSubmissionBean.getCustomerComment());
            creditMaster = iCustomerBizService.actSubmitCustomer(creditMaster).getD();
            //  保存客户交易信息
            customerTransaction.setCustomerId(creditMaster.getId());
            customerTransaction = iCustomerTransactionBizService.actSaveCustomerTransaction(customerTransaction).getD();
            domesticOutfit = mappingService.map(domesticOutfitSubmissionBean, DomesticOutfit.class);
            domesticOutfit.setCashSourceId(domesticOutfitSubmissionBean.getCashSourceIds().size() > 0 ? domesticOutfitSubmissionBean.getCashSourceIds().get(0) : "");
            domesticOutfit.setChannelId(domesticOutfitSubmissionBean.getChannelIds().size() > 0 ? domesticOutfitSubmissionBean.getChannelIds().get(0) : "");
            domesticOutfit.setEmployeeId(domesticOutfitSubmissionBean.getEmployeeIds().size() > 0 ? domesticOutfitSubmissionBean.getEmployeeIds().get(0) : "");

            domesticOutfit.setCustomerId(creditMaster.getId());
            domesticOutfit.setCustomerTransactionId(customerTransaction.getId());
            domesticOutfit = iDomesticOutfitService.save(domesticOutfit);
        } else {
            //  修改
            //  修改客户信息
            creditMaster = iCustomerBizService.actGetCustomerById(domesticOutfitSubmissionBean.getCustomerId()).getD();
            creditMaster.setName(domesticOutfitSubmissionBean.getCustomerName());
            creditMaster.setGender(domesticOutfitSubmissionBean.getGender());
            creditMaster.setIdentifyNo(domesticOutfitSubmissionBean.getIdentifyNo());
            List<String> list = new ArrayList<String>();
            list.add(domesticOutfitSubmissionBean.getCell());
            creditMaster.setCells(list);
            CustomerJobBean customerJobBean = new CustomerJobBean();
            customerJobBean.setCompanyName(domesticOutfitSubmissionBean.getCompanyName());
            creditMaster.setCustomerJob(customerJobBean);
            creditMaster.setHouseAddress(domesticOutfitSubmissionBean.getHouseAddress());
            creditMaster.setHouseDetailAddress(domesticOutfitSubmissionBean.getHouseDetailAddress());
            creditMaster.setComment(domesticOutfitSubmissionBean.getCustomerComment());
            creditMaster = iCustomerBizService.actSubmitCustomer(creditMaster).getD();

            //  修改家装数据
            domesticOutfit = mappingService.map(domesticOutfitSubmissionBean, DomesticOutfit.class);
            domesticOutfit.setCashSourceId(domesticOutfitSubmissionBean.getCashSourceIds().size() > 0 ? domesticOutfitSubmissionBean.getCashSourceIds().get(0) : "");
            domesticOutfit.setChannelId(domesticOutfitSubmissionBean.getChannelIds().size() > 0 ? domesticOutfitSubmissionBean.getChannelIds().get(0) : "");
            domesticOutfit.setEmployeeId(domesticOutfitSubmissionBean.getEmployeeIds().size() > 0 ? domesticOutfitSubmissionBean.getEmployeeIds().get(0) : "");
            domesticOutfit = iDomesticOutfitService.save(domesticOutfit);
        }
        return ResultBean.getSucceed().setD(mappingService.map(domesticOutfit,DomesticOutfitBean.class));
    }

    @Override
    public ResultBean<DomesticOutfitSubmissionBean> actRetrieveDomesticOutfitById(String id) {
        DomesticOutfit domesticOutfit = iDomesticOutfitService.getOne(id);
        if (domesticOutfit == null){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
        DomesticOutfitSubmissionBean domesticOutfitSubmissionBean = mappingService.map(domesticOutfit, DomesticOutfitSubmissionBean.class);
        if (!StringUtils.isEmpty(domesticOutfit.getCustomerId())){
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(domesticOutfit.getCustomerId()).getD();
            if ( customerBean == null){
                return ResultBean.getFailed().setD(messageService.getMessage("MSG_CUSTOMER_NULL_ID"));
            }
            domesticOutfitSubmissionBean.setCustomerName(customerBean.getName());
            domesticOutfitSubmissionBean.setGender(customerBean.getGender());
            domesticOutfitSubmissionBean.setIdentifyNo(customerBean.getIdentifyNo());
            domesticOutfitSubmissionBean.setCell(customerBean.getCells().size() > 0 ? customerBean.getCells().get(0) : "");
            domesticOutfitSubmissionBean.setCompanyName(customerBean.getCustomerJob() != null ? customerBean.getCustomerJob().getCompanyName() : "");
            domesticOutfitSubmissionBean.setHouseAddress(customerBean.getHouseAddress());
            domesticOutfitSubmissionBean.setHouseDetailAddress(customerBean.getHouseDetailAddress());
            domesticOutfitSubmissionBean.setCustomerComment(customerBean.getComment());
            List<String> list = new ArrayList<>();
            list.add(domesticOutfit.getCashSourceId());
            domesticOutfitSubmissionBean.setCashSourceIds(list);
            list = new ArrayList<>();
            list.add(domesticOutfit.getChannelId());
            domesticOutfitSubmissionBean.setChannelIds(list);
            list = new ArrayList<>();
            list.add(domesticOutfit.getEmployeeId());
            domesticOutfitSubmissionBean.setEmployeeIds(list);
        }
        return ResultBean.getSucceed().setD(domesticOutfitSubmissionBean);
    }

    @Override
    public ResultBean<DomesticOutfitSubmissionBean> actRetrieveDomesticOutfitByTransactionId(String id) {
        DomesticOutfit domesticOutfit = iDomesticOutfitService.findByCustomerTransactionId(id);
        return this.actRetrieveDomesticOutfitById(domesticOutfit.getId());
    }

    public ResultBean<List<DomesticOutfitSubmissionBean>> actGetDomesticOutfits(String loginUserId, Integer currentPage, Integer pageSize, Boolean isPass){
        Page<DomesticOutfit> domesticOutfits = null;
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_APPOINTPAYMENT_LOGINUSERID_ID_NULL"), loginUserId));
        }
        domesticOutfits = iDomesticOutfitService.findAllByLoginUser(loginUserId, currentPage, pageSize, isPass);
        DataPageBean<DomesticOutfitSubmissionBean> destination = new DataPageBean<DomesticOutfitSubmissionBean>();
        destination.setPageSize(domesticOutfits.getSize());
        destination.setTotalCount(domesticOutfits.getTotalElements());
        destination.setTotalPages(domesticOutfits.getTotalPages());
        destination.setCurrentPage(domesticOutfits.getNumber());
        destination.setResult(this.dealWithDomesticOutfitList(domesticOutfits.getContent()));
        return ResultBean.getSucceed().setD(destination);
    }

    private List<DomesticOutfitSubmissionBean> dealWithDomesticOutfitList(List<DomesticOutfit> domesticOutfits){
        List<DomesticOutfitSubmissionBean> domesticOutfitSubmissionBeans = new ArrayList<DomesticOutfitSubmissionBean>();
        for (DomesticOutfit domesticOutfit : domesticOutfits){
            DomesticOutfitSubmissionBean domesticOutfitSubmissionBean = mappingService.map(domesticOutfit, DomesticOutfitSubmissionBean.class);

            //  客户信息
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(domesticOutfit.getCustomerId()).getD();
            if ( customerBean == null){
                logger.error(messageService.getMessage("MSG_CUSTOMER_NULL_ID"));
                return null;
            }
            domesticOutfitSubmissionBean.setCustomerName(customerBean.getName());
            domesticOutfitSubmissionBean.setGender(customerBean.getGender());
            domesticOutfitSubmissionBean.setIdentifyNo(customerBean.getIdentifyNo());
            domesticOutfitSubmissionBean.setCell(customerBean.getCells().size() > 0 ? customerBean.getCells().get(0) : "");
            domesticOutfitSubmissionBean.setCompanyName(customerBean.getCustomerJob() != null ? customerBean.getCustomerJob().getCompanyName() : "");
            domesticOutfitSubmissionBean.setHouseAddress(customerBean.getHouseAddress());
            domesticOutfitSubmissionBean.setHouseDetailAddress(customerBean.getHouseDetailAddress());
            domesticOutfitSubmissionBean.setCustomerComment(customerBean.getComment());

            List<String> list = new ArrayList<>();
            list.add(domesticOutfit.getCashSourceId());
            domesticOutfitSubmissionBean.setCashSourceIds(list);
            list = new ArrayList<>();
            list.add(domesticOutfit.getChannelId());
            domesticOutfitSubmissionBean.setChannelIds(list);
            list = new ArrayList<>();
            list.add(domesticOutfit.getEmployeeId());
            domesticOutfitSubmissionBean.setEmployeeIds(list);

            domesticOutfitSubmissionBeans.add(domesticOutfitSubmissionBean);
        }
        return domesticOutfitSubmissionBeans;
    }


    @Override
    public ResultBean<DomesticOutfitBean> actFindDomesticOutfitById(String id) {
        DomesticOutfit domesticOutfit = iDomesticOutfitService.getOne(id);
        String billTypeCode = domesticOutfit.getBillTypeCode();
        //通过编码获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType(billTypeCode).getD();
        DomesticOutfitBean domesticOutfitBean = mappingService.map(domesticOutfit, DomesticOutfitBean.class);
        domesticOutfitBean.setBillType(billType);
        return ResultBean.getSucceed().setD(domesticOutfitBean);
    }

    @Override
    public ResultBean<DomesticOutfitBean> actFindDomesticOutfitByTransactionId(String id) {
        DomesticOutfit domesticOutfit = iDomesticOutfitService.findByCustomerTransactionId(id);
        String billTypeCode = domesticOutfit.getBillTypeCode();
        //通过编码获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType(billTypeCode).getD();
        DomesticOutfitBean domesticOutfitBean = mappingService.map(domesticOutfit, DomesticOutfitBean.class);
        domesticOutfitBean.setBillType(billType);
        return ResultBean.getSucceed().setD(domesticOutfitBean);
    }
}
