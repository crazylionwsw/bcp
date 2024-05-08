package com.fuze.bcp.api.bd.service;

import com.fuze.bcp.api.bd.bean.BankSettlementStandardBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 支行分成标准接口
 * Created by zqw on 2017/8/11.
 */
public interface IBankSettlementStandardBizService {

    /**
     *      获取所有的支行手续费分成标准
     * @return
     */
    ResultBean<List<BankSettlementStandardBean>> actFindBankSettlementStandards();

    /**
     *      根据ID 回显
     * @param id
     * @return
     */
    ResultBean<BankSettlementStandardBean> actFindBankSettlementStandardById(@NotNull String id);

    /**
     *      根据 保单行  渠道行  查询支行手续费分成标准
     * @param channelId
     * @param declarationId
     * @return
     */
    ResultBean<BankSettlementStandardBean> actFindBankSettlementStandardByChannelIdAndDeclarationId(String channelId,String declarationId);

    /**
     *          保存  手续费分成标准
     * @param bankSettlementStandardBean
     * @return
     */
    ResultBean<BankSettlementStandardBean> actSaveBankSettlementStandard(BankSettlementStandardBean bankSettlementStandardBean);

    /**
     *      根据ID 作废、删除数据
     * @param id
     * @return
     */
    ResultBean<BankSettlementStandardBean> actDeleteBankSettlementStandard(@NotNull String id);

    /**
     *      判断      支行手续费分成标准是否存在
     * @param channelId
     * @param declarationId
     * @return
     */
    ResultBean<Boolean> actCheckBankSettlementStandardByChannelIdAndDeclarationId(String channelId,String declarationId);
}
