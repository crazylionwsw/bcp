package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.EnhancementBean;
import com.fuze.bcp.api.creditcar.bean.enhancement.EnhancementListBean;
import com.fuze.bcp.api.creditcar.bean.enhancement.EnhancementSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;

/**
 * 资料补全的服务接口
 */

public interface IEnhancementBizService {

    /**
     * 创建资料补全单 （暂存，不进审批流）
     * @return
     */
    ResultBean<EnhancementBean> actCreateEnhancement(EnhancementBean enhancementBean);

    /**
     *
     * @param submission
     * @return
     */
    ResultBean<EnhancementSubmissionBean> actSaveEnhancement(EnhancementSubmissionBean submission);

    /**
     * 提交资料补全单 （保存，并进审批流）
     * @return
     */
    ResultBean<EnhancementBean> actSubmitEnhancement(String id, String comment);

    /**
     * 获取资料补全单
     * @param id
     * @return
     */
    ResultBean<EnhancementBean> actGetEnhancement(String id);

    /**
     *  模糊查询 资料补全单
     * @param userId
     * @param searchBean
     * @return
     */
    ResultBean<EnhancementBean> actSearchEnhancements(String userId, SearchBean searchBean);

    /**
     * 获取某分期经理的资料补全单
     * @param loginUserId 分期经理的用户ID
     * @param currentPage
     * @return
     */
    ResultBean<List<EnhancementListBean>> actGetEnhancements(Boolean isPass, String loginUserId, Integer currentPage, Integer currentSize, String customerTransactionId);

    /**
     *      根据  客户交易ID      查询  资料补全信息
     * @param customerTransactionId
     * @return
     */
    ResultBean<EnhancementBean> actGetByCustomerTransactionId(String customerTransactionId);

    ResultBean<EnhancementSubmissionBean> actGetSubmissionById(String id);

    ResultBean<EnhancementBean> actSignEnhancement(String id, SignInfo signInfo);

    ResultBean<List<CustomerImageFileBean>> actGetEnhancementImages(String id);

    //  资料补全通过之后，同步档案资料
    void actFinnishEnhancement(String id);
}
