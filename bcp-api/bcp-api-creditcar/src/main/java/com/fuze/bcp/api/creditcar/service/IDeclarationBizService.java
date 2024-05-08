package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.declaration.DeclarationBean;
import com.fuze.bcp.api.creditcar.bean.declaration.DeclarationHistorysBean;
import com.fuze.bcp.api.creditcar.bean.declaration.DeclarationResult;
import com.fuze.bcp.bean.ResultBean;

/**
 * 报批接口
 * Created by Lily on 2017/7/19.
 */
public interface IDeclarationBizService {

    /**
     * 列表分页
     * @param currentPage
     * @return
     */
    ResultBean<DeclarationBean> actGetDeclarations(Integer currentPage);
    /**
     *      通过  交易ID      查询 银行报批数据
     * @param transactionId
     * @return
     */
    ResultBean<DeclarationBean> actGetTransactionDeclaration(String transactionId);

    /**
     *      通过  银行报批 ID      查询 银行报批数据
     * @param id
     * @return
     */
    ResultBean<DeclarationBean> actGetDeclaration(String id);

    /**
     *      提交 银行报批数据
     * @return
     */
    ResultBean<DeclarationBean> actSubmitDeclaration(String id,String loginUserId);

    /**
     * 完善银行报批信息
     */
    ResultBean<DeclarationBean> actCompleteDeclaration(DeclarationBean declarationBean);

    //  保存银行报批反馈
    ResultBean<DeclarationBean>  actSaveDeclarationFeedBack(String id ,DeclarationResult declarationResult);

    /**
     *      保存 银行报批数据
     */
    ResultBean<DeclarationBean> actSaveDeclarationInfo(DeclarationBean declaration);


    /**
     * 创建电子报批信息 资质提交之后
     */
    void actCreateDeclaration(String id);

    /**
     * 报批数据 模糊查询
     */
    ResultBean<DeclarationBean> actSearchDeclaration(DeclarationBean declarationBean,String name,int currentPage);

    /**
     * 更新报批数据
     * @param orderId
     */
    void actUpdateDeclaration(String orderId);

    /**
     *      更新银行报批专项分期说明
     * @param id
     * @return
     */
    ResultBean<DeclarationBean> actUpdateDeclarationStageApplication(String id);

    //  报批反馈通过发送
    void actSendCreditAmountDocument(String transactionId);

    ResultBean<DeclarationHistorysBean> actGetDeclarationHistorys(String transactionId);
}
