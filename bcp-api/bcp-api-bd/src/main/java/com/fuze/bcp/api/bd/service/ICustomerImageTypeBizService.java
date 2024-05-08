package com.fuze.bcp.api.bd.service;

import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * 客户资料类型服务接口
 * Created by Lily on 2017/7/20.
 */
public interface ICustomerImageTypeBizService {

    /**
     * 根据图片编码
     * @param code
     * @return
     */
    ResultBean<CustomerImageTypeBean> actFindCustomerImageTypeByCode(String code);

    /**
     * 根据ID获取客户资料
     * @param id
     * @return
     */
    ResultBean<CustomerImageTypeBean> actFindCustomerImageTypeById(String id);

    /**
     * 根据ids获取资料类型
     * @return
     */
    ResultBean <List<CustomerImageTypeBean>> actFindCustomerImageTypesByIds(List<String> ids);

    /**
     * 根据  codes  获取资料类型
     * @return
     */
    ResultBean <List<CustomerImageTypeBean>> actFindCustomerImageTypesByCodes(List<String> codes);

    /**
     * 根据ID删除单个样例图片
     * @return
     */
    ResultBean<CustomerImageTypeBean> actDeleteSingleImage(String id,String imageId);

    /**
     *
     * @param bizCode
     * @param billTypeCode
     * @return
     */
    ResultBean<List<CustomerImageTypeBean>> actGetCustomerImageTypesByBillType(String bizCode, String billTypeCode);

    /**
     *  从参配项中取出某业务某单据的档案资料
     * @param businessTypeCode
     * @param billTypeCode
     * @return
     */
    ResultBean<String> actFindCustomerImageTypeCodesByParam(String businessTypeCode, String billTypeCode);

    //  获取银行报批材料清单
    ResultBean<CustomerImageTypeBean> actFindCustomerImageTypeCodesByDeclarationParam();

    ResultBean<DataPageBean<CustomerImageTypeBean>> actGetCustomerImageTypes(String businessTypeCode, Integer pageIndex, Integer pageSize);
}
