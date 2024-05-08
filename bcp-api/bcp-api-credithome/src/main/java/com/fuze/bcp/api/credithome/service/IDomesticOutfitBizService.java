package com.fuze.bcp.api.credithome.service;

import com.fuze.bcp.api.credithome.bean.DomesticOutfitBean;
import com.fuze.bcp.api.credithome.bean.DomesticOutfitSubmissionBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * 家装分期服务
 */
public interface IDomesticOutfitBizService {

    /**
     *  【Enterprise WX - API】提交 客户家装分期数据
     * @param domesticOutfitSubmissionBean
     * @return
     */
    ResultBean<DomesticOutfitBean> actSubmitDomesticOutfit(DomesticOutfitSubmissionBean domesticOutfitSubmissionBean);

    /**
     *  【Enterprise WX - API】根据ID查询家装分期数据
     * @param id
     * @return
     */
    ResultBean<DomesticOutfitSubmissionBean> actRetrieveDomesticOutfitById(String id);

    /**
     *  【Enterprise WX - API】根据交易ID查询家装分期数据
     * @param id
     * @return
     */
    ResultBean<DomesticOutfitSubmissionBean> actRetrieveDomesticOutfitByTransactionId(String id);

    /**
     * 【Enterprise WX - API】 获取家装分期分页数据
     * @param loginUserId   当前登录人的ID
     * @param currentPage   当前页
     * @param pageSize      分页单位
     * @param isPass       是否通过
     * @return
     */
    ResultBean<List<DomesticOutfitSubmissionBean>> actGetDomesticOutfits(String loginUserId, Integer currentPage, Integer pageSize, Boolean isPass);

    /**
     *  WEB端
     *  根据ID获取家装分期数据
     * @param id
     * @return
     */
    ResultBean<DomesticOutfitBean> actFindDomesticOutfitById(String id);

    /**
     *  根据交易ID查询家装分期数据
     * @param id
     * @return
     */
    ResultBean<DomesticOutfitBean> actFindDomesticOutfitByTransactionId(String id);
}
