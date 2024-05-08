package com.fuze.bcp.api.sys.service;

import com.fuze.bcp.api.sys.bean.LoginLogBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 日志管理
 */
public interface ILogBizService {

    /**************************日志操作***************************/

    /**
     * 获取登录日志信息(带分页,返回所有数据)
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<LoginLogBean>> actGetLoginLogs(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取登录日志信息列表
     * @return
     */
    ResultBean<List<LoginLogBean>> actGetLoginLogs();

    /**
     * 获取单条登录日志信息
     * @param id
     * @return
     */
    ResultBean<LoginLogBean> actGetLoginLog(@NotNull String id);


    /**
     * 保存登录日志信息
     * @param loginLogBean
     * @return
     */
    ResultBean<LoginLogBean> actSaveLoginLogs(LoginLogBean loginLogBean);


    /**
     * 删除登录日志信息
     * @param id
     * @return
     */
    ResultBean<LoginLogBean> actDeleteLoginLogs(@NotNull String id);

    /**
     * 获取登录日志信息列表(仅可用信息)
     * @return
     */
    ResultBean<List<LoginLogBean>> actLookupLoginLogs();

}
