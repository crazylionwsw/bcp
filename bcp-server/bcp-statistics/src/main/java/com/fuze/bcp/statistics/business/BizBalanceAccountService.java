package com.fuze.bcp.statistics.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICashSourceBizService;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.creditcar.bean.PoundageSettlementBean;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.service.IBankCardApplyBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.creditcar.service.IOrderBizService;
import com.fuze.bcp.api.creditcar.service.IPoundageSettlementBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.statistics.bean.*;
import com.fuze.bcp.api.statistics.service.IBalanceAccountBizService;
import com.fuze.bcp.api.statistics.service.IChargeFeePlanBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.statistics.domain.BalanceAccount;
import com.fuze.bcp.statistics.domain.BalanceAccountDetail;
import com.fuze.bcp.statistics.domain.ChargeFeePlan;
import com.fuze.bcp.statistics.domain.ChargeFeePlanDetail;
import com.fuze.bcp.statistics.service.IBalanceAccountDetailService;
import com.fuze.bcp.statistics.service.IBalanceAccountService;
import com.fuze.bcp.statistics.service.IChareFeePlanService;
import com.fuze.bcp.utils.SimpleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GQR on 2017/11/7.
 */
@Service
public class BizBalanceAccountService implements IBalanceAccountBizService {

    /**
     * 日志序列化
     */
    private static final Logger logger = LoggerFactory.getLogger(BizBalanceAccountService.class);

    @Autowired
    IBalanceAccountService iBalanceAccountService;

    @Autowired
    IBalanceAccountDetailService iBalanceAccountDetailService;

    @Autowired
    MappingService mappingService;

    @Autowired
    IChareFeePlanService iChareFeePlanService;

    @Autowired
    IChargeFeePlanBizService iChargeFeePlanBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    ICashSourceBizService iCashSourceBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    IOrderBizService iOrderBizService;

    @Autowired
    IPoundageSettlementBizService iPoundageSettlementBizService;

    /**
     * 获取所有的银行结费列表
     *
     * @param status
     * @param currentPage
     * @return
     */
    @Override
    public ResultBean<DataPageBean<BalaceAccountBean>> actGetAllBalanceAccounts(Integer status, Integer currentPage) {

        Page<BalanceAccount> demands;
        if (status < 0) {
            demands = iBalanceAccountService.getAll(currentPage);
        } else {
            demands = iBalanceAccountService.findAllByCheckStatus(status, currentPage);
        }
        return ResultBean.getSucceed().setD(mappingService.map(demands, BalaceAccountBean.class));

    }

    /**
     * 开始付款
     *
     * @param loginUserId
     * @param id
     * @return
     */
    @Override
    public ResultBean<BalaceAccountBean> actStartPay(String loginUserId, String id) {
        BalanceAccount one = iBalanceAccountService.getOne(id);
        if (one != null) {
            one.setSettleUserId(loginUserId);
            one.setPaymentApplyDate(SimpleUtils.getCreateTime());
            one.setCheckStatus(BalaceAccountBean.CHECKSTATUS_PAY);
        }
        return ResultBean.getSucceed().setD(mappingService.map(iBalanceAccountService.save(one), BalaceAccountBean.class));
    }

    /**
     * 付款完成
     *
     * @param loginUserId
     * @param id
     * @return
     */
    @Override
    public ResultBean<BalaceAccountBean> actOverPay(String loginUserId, String id) {
        BalanceAccount one = iBalanceAccountService.getOne(id);
        if (one != null) {
            one.setSettleUserId(loginUserId);
            one.setPaymentFinishDate(SimpleUtils.getCreateTime());
            one.setCheckStatus(BalaceAccountBean.CHECKSTATUS_FINISH);
        }

        return ResultBean.getSucceed().setD(mappingService.map(iBalanceAccountService.save(one), BalaceAccountBean.class));
    }

    @Override
    public ResultBean<BalanceAccountDetailBean> actGetBalanceAccountDetail(String transactionId) {
        BalanceAccountDetail balanceAccountDetail = iBalanceAccountDetailService.getBalanceAccountDetail(transactionId);
        if (balanceAccountDetail != null) {
            return ResultBean.getSucceed().setD(mappingService.map(balanceAccountDetail, BalanceAccountDetailBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<BalanceAccountDetailBean> actSaveBalanceAccountDetail(BalanceAccountDetailBean balanceAccountDetailBean) {
        BalanceAccountDetail balanceAccountDetail = iBalanceAccountDetailService.save(mappingService.map(balanceAccountDetailBean, BalanceAccountDetail.class));
        return ResultBean.getSucceed().setD(mappingService.map(balanceAccountDetail, BalanceAccountDetailBean.class));
    }


    /*@Override
    public ResultBean<BalanceAccountDetailBean> calculateOneOrder(String orderId, BalaceAccountBean balanceAccount) {
        return null;
    }*/

    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    @Override
    public ResultBean<List<BalanceAccountDetailBean>> actGetOneDetailById(String id) {
        List<BalanceAccountDetail> oneDetail = iBalanceAccountDetailService.getOneDetail(id);
        if (oneDetail.size() > 0) {
            return ResultBean.getSucceed().setD(oneDetail);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<BalaceAccountBean> actGetOneById(String id) {
        BalanceAccount availableOne = iBalanceAccountService.getAvailableOne(id);
        if (availableOne != null) {
            String code = availableOne.getBillTypeCode();
            //通过编码获取单据类型
            BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
            BalaceAccountBean balanceAccountBean = mappingService.map(availableOne, BalaceAccountBean.class);
            balanceAccountBean.setBillType(billType);
            return ResultBean.getSucceed().setD(balanceAccountBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<BalaceAccountBean> actSaveBill(BalaceAccountBean balanceAccount) {
        BalanceAccount balanceAccount1 = iBalanceAccountService.save(mappingService.map(balanceAccount, BalanceAccount.class));
        if (balanceAccount1 != null) {
            return ResultBean.getSucceed().setD(balanceAccount1);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<BalaceAccountBean> actCalculateBalanceAccounts(String year, String month, String loginUserId) {
        BalanceAccount oldBan = iBalanceAccountService.findOneByYearAndMonth(year, month);
        BalaceAccountBean balaceAccountBean = new BalaceAccountBean();
        if (oldBan.getSettleUserId() != null) {
            balaceAccountBean.setSettleUserId(oldBan.getSettleUserId());
        } else {
            balaceAccountBean.setSettleUserId(loginUserId);
        }

        balaceAccountBean.setYear(year);
        balaceAccountBean.setMonth(month);

        return actCalculateBalanceAccount(balaceAccountBean);
    }

    /**
     * 根据还款计划表计算这个月的手续费收入，如果手续费计算为0，并且状态是正常，需要删除数据
     *
     * @param feePlanDetail
     * @param balanceAccount
     * @return
     */
    @Override
    public ResultBean<BalanceAccountDetailBean> actCalculateOneChargeFeePlan(ChargeFeePlanDetailBean feePlanDetail, BalaceAccountBean balanceAccount) {
        String chargeFeePlanId = feePlanDetail.getChargeFeePlanId();
        if (chargeFeePlanId != null) {
            ChargeFeePlan feePlan = iChareFeePlanService.findOneByIdAndStatus(feePlanDetail.getChargeFeePlanId(), ChargeFeePlanBean.STATUS_CHEKCED_PASS);
            if (feePlan != null) {
                feePlan.addOneDetail(mappingService.map(feePlanDetail, ChargeFeePlanDetail.class));
                ResultBean<BankCardApplyBean> bankCardApplyBeanResultBean = iBankCardApplyBizService.actFindBankCardApplyByTransactionId(feePlan.getCustomerTransactionId());
                Double swipingMoney = bankCardApplyBeanResultBean.getD().getSwipingMoney();

                BalanceAccountDetail detail = iBalanceAccountDetailService.findOneByCustomerTransactionIdAndBalanceAccountId(feePlan.getCustomerTransactionId(), balanceAccount.getId());
                if (detail == null) {
                    detail = new BalanceAccountDetail();
                    detail.setStatus(BalanceAccountDetail.STATUS_FAILED);
                }
                detail.setBalanceAccountId(balanceAccount.getId());
                detail.setCustomerTransactionId(feePlan.getCustomerTransactionId());
                detail.setCreditAmount(feePlan.getLimitAmount());
                detail.setChargeFee(feePlan.getChargeAmount());
                detail.setSwingAmount(swipingMoney);
                detail.setSwingCardDate(feePlan.getSwingCardDate());
                detail.setCardealerId(feePlan.getCardealerId());
                detail.setChargePaymentWay(feePlan.getChargePaymentWay());
                detail.setCustomerId(feePlan.getCustomerId());
                detail.setCreditMonths(feePlan.getCreditMonths());
                detail.setChargeFeeRatio(feePlan.getBankChargeRatio());
                detail.setCashSourceId(feePlan.getCashSourceId());

                if (feePlan.getStatus() != ChargeFeePlan.STATUS_CHEKCED_PASS) {
                    String msg = String.format(messageService.getMessage("MSG_BALANCEACCOUNT_NOCHECK"));
                    return saveFailed(mappingService.map(detail, BalanceAccountDetailBean.class), msg);
                }

                ResultBean<CustomerTransactionBean> customerTransactionBeanResultBean = iCustomerTransactionBizService.actFindCustomerTransactionById(feePlan.getCustomerTransactionId());
                if (customerTransactionBeanResultBean.failed()) {
                    return ResultBean.getFailed().setM(customerTransactionBeanResultBean.getM());
                }
                CustomerTransactionBean transaction = customerTransactionBeanResultBean.getD();
                //更新订单部分的数据
                detail.setBusinessTypeCode(transaction.getBusinessTypeCode());
                // BalanceAccountDetailBean map = mappingService.map(detail, BalanceAccountDetailBean.class);
                return updateDetailCheckFee(detail, feePlan);
            }
        }
        return ResultBean.getFailed();
    }

    /**
     * 根据年月和还款计划表，计算银行本月需要计算给公司的费用
     *
     * @param rp
     * @return
     */
    private ResultBean<BalanceAccountDetailBean> updateDetailCheckFee(BalanceAccountDetail detail, ChargeFeePlan rp) {
        if (rp.getDetailList().size() != 1) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BALANCEACCOUNT_NODATAIL"), rp.getId()));
        }
        Double baseMonthlyFee = rp.getDetailList().get(0).getChargeAmount();
        //年月都相等的情况下，计算本月需要向银行收取的手续费，此处计算只能依据手续费收入进行等比例计算
        detail.setCheckFee(SimpleUtils.formatDigit(baseMonthlyFee * rp.getChargeCheckRatio(), 2));
        detail.setChargeCheckRatio(rp.getChargeCheckRatio());
        detail.setStatus(BalanceAccountDetail.STATUS_SUCCEED);
        detail.setComment(messageService.getMessage("MSG_BALANCEACCOUNT_CREATEFINISH"));
        detail = iBalanceAccountDetailService.saveOneBalanceAccountDetail(detail);
        BalanceAccountDetailBean map = mappingService.map(detail, BalanceAccountDetailBean.class);
        logger.info(String.format(messageService.getMessage("MSG_BALANCEACCOUNT_CREATECOUNTANDAMOUNT"), detail.getBalanceAccountId(), rp.getCustomerId(), detail.getCheckFee()));
        return ResultBean.getSucceed().setD(map);
    }


    /**
     * 保存失败的费用结算单
     *
     * @param detail
     * @param failedMsg
     * @return
     */
    private ResultBean<BalanceAccountDetailBean> saveFailed(BalanceAccountDetailBean detail, String failedMsg) {
        detail.setStatus(BalanceAccountDetailBean.STATUS_FAILED);
        detail.setComment(failedMsg);
        detail.updateTs();
        BalanceAccountDetail balanceAccountDetail = iBalanceAccountDetailService.saveOneBalanceAccountDetail(mappingService.map(detail, BalanceAccountDetail.class));
        return ResultBean.getSucceed().setD(mappingService.map(balanceAccountDetail, BalanceAccountDetailBean.class)).setM(failedMsg);

    }

    /**
     * 计算指定月份的用户结费表
     *
     * @return
     */
    @Override
    public ResultBean<BalaceAccountBean> actCalculateBalanceAccount(BalaceAccountBean balanceAccount) {
       /* String str = validBalanceAccount(balanceAccount);
        if (!StringUtils.isEmpty(str) || str == null || str=="") {
            return ResultBean.getFailed().setM(str);
        }*/
        BalanceAccount oldBan = iBalanceAccountService.findOneByYearAndMonth(balanceAccount.getYear(), balanceAccount.getMonth());
        String settleUserId = balanceAccount.getSettleUserId();

        if (oldBan != null) {
            if (oldBan.getCheckStatus() == BalaceAccountBean.CHECKSTATUS_INIT) {
                BalaceAccountBean map = mappingService.map(oldBan, BalaceAccountBean.class);
                balanceAccount = map;
                balanceAccount.setTotalCount(0);
                balanceAccount.setTotalCredit(0.0);
                balanceAccount.setTotalPaymentAmount(0.0);
            } else {
                return ResultBean.getFailed().setD(ResultBean.getFailed().setM(messageService.getMessage("MSG_BALANCEACCOUNT_NOCALCULATE")));
            }

        }
        balanceAccount.updateTs();
        BalanceAccount account = iBalanceAccountService.save(mappingService.map(balanceAccount, BalanceAccount.class));
        balanceAccount = mappingService.map(account, BalaceAccountBean.class);
        balanceAccount = clearOldData(balanceAccount);

        List<ChargeFeePlanDetailBean> rpDetailList = mappingService.map(iChareFeePlanService.queryAllDetaillByYearAndMonthAnd(balanceAccount.getYear(), balanceAccount.getMonth()), ChargeFeePlanDetailBean.class);
        for (int i = 0; i < rpDetailList.size(); i++) {
            ResultBean<BalanceAccountDetailBean> balanceAccountDetailBeanResultBean = actCalculateOneChargeFeePlan(rpDetailList.get(i), balanceAccount);
            if (balanceAccountDetailBeanResultBean.isSucceed()) {
                if (balanceAccountDetailBeanResultBean.getD() != null) {
                    BalanceAccountDetailBean bdetail = balanceAccountDetailBeanResultBean.getD();
                    balanceAccount.setTotalCredit(bdetail.getCreditAmount() + balanceAccount.getTotalCredit());//贷款金额
                    balanceAccount.setTotalPaymentAmount(balanceAccount.getTotalPaymentAmount() + bdetail.getCheckFee());//手续费总额
                    balanceAccount.setTotalCount(balanceAccount.getTotalCount() + 1);
                }
            }
            //balanceAccount.setBusinessTypeCode("NC");
            //总业务笔数
        }
        balanceAccount.setSettleUserId(settleUserId);

        balanceAccount = mappingService.map(iBalanceAccountService.save(mappingService.map(balanceAccount, BalanceAccount.class)), BalaceAccountBean.class);
        return ResultBean.getSucceed().setD(balanceAccount).setM(String.format(messageService.getMessage("MSG_BALANCEACCOUNT_COUNT"), balanceAccount.getYear(), balanceAccount.getMonth(), rpDetailList.size(), balanceAccount.getTotalPaymentAmount()));
    }

    /**
     * 清除旧数据
     *
     * @param balanceAccount
     */
    private BalaceAccountBean clearOldData(BalaceAccountBean balanceAccount) {
        if (balanceAccount.getId() != null) {
            iBalanceAccountService.delete(balanceAccount.getId());
            balanceAccount.setTotalCredit(0.0);
            balanceAccount.setTotalCount(0);
            balanceAccount.setTotalPaymentAmount(0.0);
        }
        return balanceAccount;
    }

    @Override
    public ResultBean<List<BalanceAccountExport>> actExportExcel(String year, String month) {

        BalanceAccount balanceAccount = iBalanceAccountService.findOneByYearAndMonth(year, month);
        List<BalanceAccountDetail> detail = iBalanceAccountDetailService.getOneDetail(balanceAccount.getId());
        List<List<Object>> dataset = new ArrayList<List<Object>>();
        for (BalanceAccountDetail bad : detail) {
            List<Object> rowData = new ArrayList<Object>();
            if (bad.getCustomerTransactionId() != null) {
                rowData.add(getCashSourceName(bad.getCustomerTransactionId()));
            } else {
                rowData.add("空");
            }
            rowData.add(iCarDealerBizService.actGetCarDealer(bad.getCardealerId()).getD().getName());
            CustomerBean customer = iCustomerBizService.actGetCustomerById(bad.getCustomerId()).getD();
            rowData.add("NC".equals(bad.getBusinessTypeCode()) ? "新车" : "二手车");
            rowData.add(customer.getName());
            rowData.add(customer.getIdentifyNo());
            rowData.add(SimpleUtils.formatDigit(bad.getCreditAmount(), 0));
            rowData.add(String.valueOf(bad.getCreditMonths()));
            rowData.add(getCompen(bad.getCustomerTransactionId()));
            rowData.add("WHOLE".equals(bad.getChargePaymentWay()) ? "趸交" : "分期");
            rowData.add(String.valueOf(SimpleUtils.formatDigit(bad.getChargeFeeRatio() * 100, 2) + "%"));
            rowData.add(SimpleUtils.formatDigit(bad.getChargeFee(), 2));
            rowData.add(bad.getSwingCardDate().substring(0, 10));
            rowData.add(String.valueOf(SimpleUtils.formatDigit(bad.getChargeCheckRatio() * 100, 2) + "%"));
            rowData.add(SimpleUtils.formatDigit(bad.getCheckFee(), 2));
            dataset.add(rowData);
        }
        //增加合计行
        List<Object> rowData = new ArrayList<Object>();
        rowData.add("总计");
        rowData.add((dataset.size()) + "条");
        rowData.add("");
        rowData.add("");
        rowData.add("合计贷款额");
        rowData.add(SimpleUtils.formatDigit(balanceAccount.getTotalCredit(), 0));
        rowData.add("");
        rowData.add("");
        rowData.add("");
        rowData.add("");
        rowData.add("");
        rowData.add("");
        rowData.add("结算费用合计：");
        rowData.add(SimpleUtils.formatDigit(balanceAccount.getTotalPaymentAmount(), 2));
        dataset.add(rowData);

        return ResultBean.getSucceed().setD(dataset);
    }

    /**
     * 根据交易id在手续费分成表里找代码行id
     *
     * @param transactionId
     * @return
     */
    private String getCashSourceName(String transactionId) {
        PoundageSettlementBean poundageSettlementBean = iPoundageSettlementBizService.actGetOneByCustomerTransactionId(transactionId).getD();
        if (poundageSettlementBean != null && poundageSettlementBean.getSettlementCashSourceId() != null) {
            String name = iCashSourceBizService.actGetCashSource(poundageSettlementBean.getSettlementCashSourceId()).getD().getShortName();
            return name;
        }
        return "";
    }

    /**
     * 根据TransactionId获取最后的CustomerLoanBean
     *
     * @param transactionId
     * @return
     */
    private CustomerLoanBean getCustomerLoadByTransaction(String transactionId) {
        PurchaseCarOrderBean orderBean = iOrderBizService.actGetOrderByTransactionId(transactionId).getD();
        if (orderBean == null) return null;
        return iCustomerBizService.actGetCustomerLoanById(orderBean.getCustomerLoanId()).getD();
    }


    private String getCompen(String transactionId) {
        CustomerLoanBean customerLoan = iCustomerBizService.actGetCustomerLoan(transactionId).getD();
        if (customerLoan.getCompensatoryInterest() == 1) {
            return "是";
        } else {
            return "否";
        }
    }


    private String validBalanceAccount(BalaceAccountBean balanceAccount) {
        StringBuffer msg = null;
        if (StringUtils.isEmpty(balanceAccount.getMonth())) {
            msg.append(messageService.getMessage("MSG_BALANCEACCOUNT_MONTHNONULL"));
        }
        if (StringUtils.isEmpty(balanceAccount.getYear())) {
            msg.append(messageService.getMessage("MSG_BALANCEACCOUNT_YEARNONULL"));
        }
        return msg.toString();
    }

    /**
     * 核对与取消核对
     *
     * @param loginUserId
     * @param id
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<BalaceAccountBean> actChecked(String loginUserId, String id, SignInfo signInfo) {
        BalanceAccount one = iBalanceAccountService.getOne(id);
        if (one != null) {
            if (one.getCheckStatus() == BalaceAccountBean.CHECKSTATUS_CHECKED) {
                return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BALANCEACCOUNT_ALREADYCHECK")));
            }
            if (one.getCheckStatus() == BalaceAccountBean.CHECKSTATUS_FINISH || one.getCheckStatus() == BalaceAccountBean.CHECKSTATUS_PAY) {
                return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BALANCEACCOUNT_ALREADYPAY")));
            }
            one.addSignInfo(signInfo);
            one.setCheckStatus(BalaceAccountBean.CHECKSTATUS_CHECKED);
            one.setCheckUserId(loginUserId);
            one.setCheckDate(SimpleUtils.getCreateTime());
            one.setComment(signInfo.getComment());
            return ResultBean.getSucceed().setD(mappingService.map(iBalanceAccountService.save(one), BalaceAccountBean.class)).setM(String.format("对账总表核对成功！"));
        } else {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BALANCEACCOUNT_NODATE")));
        }
    }

    @Override
    public ResultBean<BalaceAccountBean> actUnChecked(String loginUserId, String id, SignInfo signInfo) {
        BalanceAccount one = iBalanceAccountService.getOne(id);
        if (one != null) {
            if (one.getCheckStatus() == BalaceAccountBean.CHECKSTATUS_CHECKED) {
                one.setCheckStatus(BalaceAccountBean.CHECKSTATUS_INIT);
                one.addSignInfo(signInfo);
                return ResultBean.getSucceed().setD(mappingService.map(one, BalaceAccountBean.class)).setM(String.format(messageService.getMessage("MSG_BALANCEACCOUNT_CANCELSUCCESS")));
            }
        } else {
            ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BALANCEACCOUNT_NODATE")));
        }
        return null;
    }

    @Override
    public void actCardealerTransferWithStatistics(String bid, List<String> tids) {
        List<CustomerTransactionBean> transactionBeanList = iCustomerTransactionBizService.actGetTransactions(tids).getD();
        if (transactionBeanList.size() > 0) {
            for (CustomerTransactionBean transaction : transactionBeanList) {
                BalanceAccount balanceAccount = iBalanceAccountService.findByCustomerTransactionId(transaction.getId());
                if (balanceAccount != null) {
                    balanceAccount.setCarDealerId(bid);
                    iBalanceAccountService.save(balanceAccount);
                }
                ChargeFeePlan chargeFeePlan = iChareFeePlanService.findByCustomerTransactionId(transaction.getId());
                if (chargeFeePlan != null) {
                    chargeFeePlan.setCardealerId(bid);
                    iChareFeePlanService.save(chargeFeePlan);
                }
            }
        }

    }
}
