package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.dealersharing.DealerSharingBean;
import com.fuze.bcp.api.creditcar.bean.dealersharing.GroupSharingBean;
import com.fuze.bcp.api.creditcar.bean.dealersharing.SharingDetailsBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;
import java.util.Map;

/**
 * 渠道分成
 */
public interface IDealerSharingBizService {

    /**
     * 计算一笔业务的分成数据
     *
     * @param customerTransactionId
     * @param scope 0全部 1渠道 2集团
     * @return
     */
    ResultBean<SharingDetailsBean> actSaveSharingDetails(String customerTransactionId, String date, Integer scope);


    ResultBean actResetDealerSharingDetails(String saleMonth, String carDealerId);

    /**
     * 重新计算当月的详细分成数据
     *
     * @param saleMonth
     * @param scope 0全部 1渠道 2集团
     * @return
     */
    ResultBean actResetSharingDetails(String saleMonth, Integer scope, String groupId, String orgId);

    /**
     * 创建当月集团分成
     *
     * @param groupId
     * @param saleMonth
     * @return
     */
    ResultBean<GroupSharingBean> actCreateGroupSharing(String groupId, String saleMonth);

    /**
     * 创建当月渠道分成
     *
     * @param dealerId
     * @param saleMonth
     * @return
     */
    ResultBean<DealerSharingBean> actCreateDealerSharing(String dealerId, String saleMonth);

    ResultBean actCreateDealerSharings(String saleMonth, String orgId);
    /**
     * 确认状态
     *
     * @return
     */
    ResultBean<SharingDetailsBean> actConfirmStatus(String id,String carDealerId, String sharingDetailId, Integer status, Double sharingRatio, Double sharingAmount,Integer mainPartType) throws Exception;

    /**
     * 获取分成详细列表数据
     */
    ResultBean<DataPageBean<SharingDetailsBean>> actGetSharingDetails(Integer currentPage);

    /**
     * 获取渠道分成列表
     */
    ResultBean<DataPageBean<DealerSharingBean>> actGetDealerSharings(Integer currentPage, String month, String orgId);

    /**
     * 获取集团分成列表
     */
    ResultBean<DataPageBean<GroupSharingBean>> actGetGroupSharings(Integer currentPage, String month, String groupId);

    ResultBean<List<SharingDetailsBean>> actLookupDealerDetail(String carDealerSharingId);

    ResultBean<Map<String, SharingDetailsBean>> actLookupGroupDetail(String groupSharingId);

    ResultBean<GroupSharingBean> actGetGroupDetail(String groupId,String saleMonth);

    ResultBean<DealerSharingBean> actGetDealerSharing(String saleMonth,String carDealerId);

    ResultBean<DealerSharingBean> actGetDealerSharing(String dealerSharingId);

    ResultBean<GroupSharingBean> actGetGroupSharing(String groupSharingId);

    ResultBean actDoBalanceSharing(Integer type, String id) throws Exception;

}
