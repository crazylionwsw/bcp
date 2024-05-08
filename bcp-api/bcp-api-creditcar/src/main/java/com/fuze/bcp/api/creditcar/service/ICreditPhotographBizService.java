package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.CreditPhotographBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 征信拍照上传接口
 * Created by sean on 2016/10/20.
 */
public interface ICreditPhotographBizService {

    /**
     * 根据支行查询未完成的征信报告
     * @return
     */
    ResultBean<List<CreditPhotographBean>> actFindAllUnFinished(String bankId);

    /**
     * 根据支行查询已经完成的征信报告
     * @return
     */
    ResultBean<DataPageBean<CreditPhotographBean>> actFindAllFinished(String bankid, Integer pageIndex, Integer pageSize);

    /**
     * 完成一个客户的征信报告上传
     * @param customerId
     * @return
     */
    ResultBean<CreditPhotographBean> actFinishOneCustomer(String customerId);

    /**
     * 创建征信查询
     * @param id
     * @return
     */
    void actCreateCreditPhotograph(String id);

    /**
     * 保存征信拍照
     * @return
     */
    ResultBean<CreditPhotographBean> actSave(CreditPhotographBean creditPhotographBean);

    /**
     * 查询所有的征信报告(带分页)
     * @param uploadFinish
     * @return
     */
    ResultBean<CreditPhotographBean> actFindCreditPhotographs(Integer currentPage,Boolean uploadFinish);

    /**
     * 根据id获取征信报告信息
     * @param id
     * @return
     */
    ResultBean<CreditPhotographBean> actFindCreditPhotographById(String id);

    /**
     * 生成pdf
     * @param creditPhotographBean
     * @return
     */
    ResultBean<CreditPhotographBean> actCreateSavePdf(CreditPhotographBean creditPhotographBean);

    /**
     * 后台手动生成征信报告
     * @param creditPhotographBean
     * @return
     */
    ResultBean<CreditPhotographBean> actCreateCreditReportManually(CreditPhotographBean creditPhotographBean);

    /**
     * 使用mq生成pdf
     * @param id
     * @return
     */
    ResultBean<CreditPhotographBean> actMqCreateSavePdf(String id);

    /**
     * 查询用户信息(带分页)
     * @param uploadFinish
     * @param customerIds
     * @return
     */
    ResultBean<CreditPhotographBean> actFindAllByuploadFinishAndCustomerIds(Integer currentPage,Boolean uploadFinish, List<String> customerIds);

    /**
     * 根据客户ID查询上传的征信报告原文件
     * @param customerId
     * @return
     */
    ResultBean<CreditPhotographBean>  actFindCreditPhotographByCustomerId(String customerId);


}
