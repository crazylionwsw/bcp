package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.bean.ImageTypeFileBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * 客户资料服务接口
 * Created by Lily on 2017/7/19.
 */
public interface ICustomerImageFileBizService{
    /**
     * 保存档案信息
     * @param customerImageFileBean
     * @return
     */
    ResultBean<CustomerImageFileBean> actSaveCustomerImage(CustomerImageFileBean customerImageFileBean);

    /**
     * 保存档案信息（与客户ID和交易ID关联）
     * @param customerId
     * @param transactionId
     * @param customerImageFileBeans
     * @return 档案资料的列表
     */
    ResultBean<List<CustomerImageFileBean>> actSaveCustomerImages(String customerId, String transactionId, List<ImageTypeFileBean> customerImageFileBeans);

    /**
     * 保存档案信息
     * @param customerImageFileBeans
     * @return
     */
    ResultBean<List<CustomerImageFileBean>> actSaveCustomerImages(List<ImageTypeFileBean> customerImageFileBeans);

    /**
     * 删除档案信息
     * @return
     */
    ResultBean<CustomerImageFileBean> actDeleteCustomerImage(String id);

    /**
     * 通过ID获取详情
     * @param id
     * @return
     */
    ResultBean<CustomerImageFileBean> actFindCustomerImageById(String id);

    /**
     * 把旧交易的档案资料复制到新交易中
     * @param customerId
     * @param imageTypeCodes
     * @param fromTransId
     * @param toTransId
     * @return
     */
    ResultBean actCopyCustomerImages(String customerId, List<String> imageTypeCodes, String fromTransId, String toTransId);

    ResultBean actCopyCustomerImage(String customerId, String imageTypeCode, String fromTransId, String toTransId);

    /**
     *
     * @param customerId
     * @param customerImageTypeCode
     * @return
     */
    ResultBean<List<CustomerImageFileBean>> actFindByCustomerIdAndCustomerImageType(String customerId,String customerImageTypeCode);

    /**
     *
     * @param customerId
     * @param customerImageTypeCode
     * @param transactionId
     * @return
     */
    ResultBean<CustomerImageFileBean> actFindByCustomerIdAndCustomerImageTypeAndCustomerTransactionId(String customerId,String transactionId,String customerImageTypeCode);


    ResultBean<List<CustomerImageFileBean>> actFindByCustomerIdAndCustomerImageTypesAndCustomerTransactionId(String customerId,String transactionId,List<String> customerImageTypeCodes);

    /**
     * 获取某交易的所有客户档案
     * @param customerId
     * @param customerTransactionId
     * @return
     */
    ResultBean<List<CustomerImageFileBean>> actGetTransactionImages(String customerId, String customerTransactionId);

    /**
     * 获取某交易的某几种客户档案
     * @param customerId
     * @param customerTransactionId
     * @param imageTypeCodeList
     * @return
     */
    ResultBean<List<CustomerImageFileBean>> actGetTransactionImages(String customerId, String customerTransactionId, List<String> imageTypeCodeList);

    /**
     * 获取某交易的某几种客户档案
     * @param customerId
     * @param customerTransactionId
     * @param imageTypeCodeList
     * @return
     */
    ResultBean<List<ImageTypeFileBean>> actGetBillImageTypesWithFiles(String customerId, String customerTransactionId, List<String> imageTypeCodeList);

    //根据id和档案类型获取
    ResultBean<List<CustomerImageFileBean>> actGetImageByIdAndByImageTypeCodeList(String id,List<String> imageTypeCodeList);

    /**
     * 获取某交易的客户档案资料
     * @param customerId
     * @param customerTransactionId
     * @param bizCode
     * @param billTypeCode
     * @return
     */
    ResultBean<List<ImageTypeFileBean>> actGetBillImageTypesWithFiles(String customerId, String customerTransactionId, String bizCode, String billTypeCode);

    /**
     * 获取某交易的用户客户档案
     * @param customerId
     * @param customerTransactionId
     * @param billTypeCode
     * @return
     */
    ResultBean<List<ImageTypeFileBean>> actGetBillImageTypesWithBillTypeCode(String customerId, String customerTransactionId, String billTypeCode);

    /**
     * 根据ID列表获取客户档案
     * @param customerImageIds
     * @return
     */
    ResultBean<List<ImageTypeFileBean>> actGetBillImageTypesWithFiles(List<String> customerImageIds);


    /**
     * 根据id列表获取
     * @param ids
     * @return
     */
    ResultBean<List<CustomerImageFileBean>> actFindCustomerImagesByIds(List<String> ids);

    /**
     * 根据客户的id查找customerimage
     * @param id
     * @return
     */
    ResultBean<List<CustomerImageFileBean>> actFindCustomerImageByCustomer(String id);

    /**
     * 批量删除customerimage
     * @param customerImageIds
     */
    void actDeleteCustomerImages(List<String> customerImageIds);

    /**
     * 给一组档案资料设置交易ID
     * @param customerImageIds
     * @param customerId
     * @param transactionId
     */
    void actAssignImagesToTransaction(List<String> customerImageIds, String customerId, String transactionId);

    // 合并档案，输出PDF文档
    ResultBean actMergeImages(Boolean force,CustomerImageFileBean customerImageFileBean,String loginUserId);

    //
    ResultBean<CustomerImageFileBean> actGetCustomerImageFile(String customerId,String transactionId,String imgCode);

    /**
     * 【PAD】 获取某交易所有的档案资料
     * @param transactionId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean<DataPageBean<ImageTypeFileBean>> actGetCustomerImagesByTransactionId(String transactionId,Integer pageIndex,Integer pageSize);

}
