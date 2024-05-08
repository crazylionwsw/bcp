package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.CreditReportQueryBean;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * 征信查询接口
 * Created by Lily on 2017/7/19.
 */
public interface ICreditReportQueryBizService {
    /**
     * 创建征信查询
     * @param id
     * @param content
     * @return
     */
    ResultBean<CreditReportQueryBean> actCreateCreditReportQuery(String id, String content);

    /**
     * 保存征信数据
     * @return
     */
    ResultBean<CreditReportQueryBean> actSave(CreditReportQueryBean creditReportQueryBean);

    /**
     * 根据客户ID查询征信报告
     * @param customerId
     * @return
     */
    ResultBean<CreditReportQueryBean> actFindByCustomerId(String customerId);

    /**
     * 解析图像数据，这个部分暂时不处理，需要调用服务接口才能处理，不再Java进行数据处理
     * @param customerImageFileBean
     * @return
     */
    ResultBean<CreditReportQueryBean> actParseReportImage(CustomerImageFileBean customerImageFileBean);

    /**
     * 根据单据状态查询征信报告单据
     * @param currentPage
     * @param approveStatus
     * @return
     */
    ResultBean<CreditReportQueryBean> actFindCreditReportQueries(int currentPage,Integer approveStatus);

    /**
     * 根据id获取CreditReportQuery对象
     * @param id
     * @return
     */
    ResultBean<CreditReportQueryBean> actFindCreditReportQueryById(String id);

    /**
     * 根据ids获取CreditReportQuery对象
     * @param ids
     * @return
     */
    ResultBean<CreditReportQueryBean> actFindCreditReportQueriesByCustomerIds(int currentPage,List<String> ids);

    /**
     * 审批
     * @param creditReportQuery
     * @return
     */
    ResultBean<CreditReportQueryBean> actSign(CreditReportQueryBean creditReportQuery);

    /**
     * 删除CreditReportQuery对象
     * @param id
     */
    ResultBean deleteCreditReportQuery(String id);

}
