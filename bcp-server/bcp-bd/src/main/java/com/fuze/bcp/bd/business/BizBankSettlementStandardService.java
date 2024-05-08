package com.fuze.bcp.bd.business;

import com.fuze.bcp.api.bd.bean.BankSettlementStandardBean;
import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.service.IBankSettlementStandardBizService;
import com.fuze.bcp.api.bd.service.ICashSourceBizService;
import com.fuze.bcp.bd.domain.BankSettlementStandard;
import com.fuze.bcp.bd.service.IBankSettlementStandardService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by zqw on 2017/8/11.
 */
@Service
public class BizBankSettlementStandardService implements IBankSettlementStandardBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizBankSettlementStandardService.class);

    @Autowired
    MappingService mappingService;

    @Autowired
    private IBankSettlementStandardService iBankSettlementStandardService;

    @Autowired
    ICashSourceBizService iCashSourceBizService;

    @Override
    public ResultBean<List<BankSettlementStandardBean>> actFindBankSettlementStandards() {
        List<BankSettlementStandard> bankSettlementStandardList = iBankSettlementStandardService.getAll();
        if (bankSettlementStandardList != null){
            return ResultBean.getSucceed().setD(mappingService.map(bankSettlementStandardList,BankSettlementStandardBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<BankSettlementStandardBean> actFindBankSettlementStandardById(@NotNull String id) {
        BankSettlementStandard bankSettlementStandard = iBankSettlementStandardService.getAvailableOne(id);
        if (bankSettlementStandard!=null){
            return ResultBean.getSucceed().setD(mappingService.map(bankSettlementStandard,BankSettlementStandardBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<BankSettlementStandardBean> actFindBankSettlementStandardByChannelIdAndDeclarationId(String channelId,String declarationId) {
        BankSettlementStandard bankSettlementStandard = iBankSettlementStandardService.getOneByChannelIdAndDeclarationId(channelId,declarationId);
        if (bankSettlementStandard!=null){
            return ResultBean.getSucceed().setD(mappingService.map(bankSettlementStandard,BankSettlementStandardBean.class));
        }
        return ResultBean.getFailed();
    }

    /**
     *      添加、保存  手续费分成标准
     * @param bankSettlementStandardBean
     * @return
     */
    @Override
    public ResultBean<BankSettlementStandardBean> actSaveBankSettlementStandard(BankSettlementStandardBean bankSettlementStandardBean) {

        BankSettlementStandard bankSettlementStandard = mappingService.map(bankSettlementStandardBean, BankSettlementStandard.class);
        BankSettlementStandard oneByChannelIdAndDeclarationId = iBankSettlementStandardService.getOneByChannelIdAndDeclarationId(bankSettlementStandardBean.getChannelId(), bankSettlementStandardBean.getDeclarationId());

        // 报单行与渠道行相同时
        if (bankSettlementStandardBean.getId() == null && oneByChannelIdAndDeclarationId != null){
            return ResultBean.getSucceed().setM("对不起！该渠道行与报单行的分成比例已经存在！请修改！");
        }else if (bankSettlementStandardBean.getId() == null && oneByChannelIdAndDeclarationId == null){ //报单行与渠道行不相同
            bankSettlementStandard = iBankSettlementStandardService.save(bankSettlementStandard);
            ResultBean<CashSourceBean> cashSourceBeanResultBean = iCashSourceBizService.actGetCashSource(bankSettlementStandard.getChannelId());
            String channelName = cashSourceBeanResultBean.getD().getShortName();
            ResultBean<CashSourceBean> cashSourceBeanResultBean1 = iCashSourceBizService.actGetCashSource(bankSettlementStandard.getDeclarationId());
            String declarationName = cashSourceBeanResultBean1.getD().getShortName();
            logger.info("新增分成标准：报单行为："+declarationName+",分成标准为："+bankSettlementStandard.getDeclarationProportion()+";渠道行为："+channelName+",分成标准为："+bankSettlementStandard.getChannelProportion());
        }else if (bankSettlementStandardBean.getId() != null){//修改


            //查询修改前的信息
            BankSettlementStandard one = iBankSettlementStandardService.getOne(bankSettlementStandardBean.getId());
            ResultBean<CashSourceBean> cashSourceBeanResultBean = iCashSourceBizService.actGetCashSource(one.getChannelId());
            String channelName = cashSourceBeanResultBean.getD().getShortName();
            ResultBean<CashSourceBean> cashSourceBeanResultBean1 = iCashSourceBizService.actGetCashSource(one.getDeclarationId());
            String declarationName = cashSourceBeanResultBean1.getD().getShortName();

            bankSettlementStandard = iBankSettlementStandardService.save(bankSettlementStandard);
            //查询更改后的信息
            ResultBean<CashSourceBean> newCashSourceBeanResultBean = iCashSourceBizService.actGetCashSource(bankSettlementStandard.getChannelId());
            String newChannelName = newCashSourceBeanResultBean.getD().getShortName();
            ResultBean<CashSourceBean> newCcashSourceBeanResultBean1 = iCashSourceBizService.actGetCashSource(bankSettlementStandard.getDeclarationId());
            String newDeclarationName = newCcashSourceBeanResultBean1.getD().getShortName();

            logger.info("修改分成标准：原报单行为："+declarationName+",分成标准为："+one.getDeclarationProportion()+";原渠道行为："+channelName+",分成标准为："+one.getChannelProportion()
            +"。修改后的报单行为："+newDeclarationName+",分成标准为："+bankSettlementStandard.getDeclarationProportion()+"修改后的渠道行为："+newChannelName+",分成标准为："+bankSettlementStandard.getChannelProportion());

        }
        if (bankSettlementStandard != null){
            return ResultBean.getSucceed().setD(mappingService.map(bankSettlementStandard,BankSettlementStandardBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<BankSettlementStandardBean> actDeleteBankSettlementStandard(@NotNull String id) {
        BankSettlementStandard bankSettlementStandard = iBankSettlementStandardService.getOne(id);
        if (bankSettlementStandard!=null){
            bankSettlementStandard = iBankSettlementStandardService.delete(id);
            return ResultBean.getSucceed().setD(mappingService.map(bankSettlementStandard,BankSettlementStandardBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<Boolean> actCheckBankSettlementStandardByChannelIdAndDeclarationId(String channelId, String declarationId) {
        BankSettlementStandard oneByChannelIdAndDeclarationId = iBankSettlementStandardService.getOneByChannelIdAndDeclarationId(channelId, declarationId);
        if (oneByChannelIdAndDeclarationId == null){
            return ResultBean.getSucceed().setD(false);
        }
        return ResultBean.getSucceed().setD(true);
    }
}
