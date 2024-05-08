package com.fuze.bcp.api.sys.service;

import com.fuze.bcp.api.sys.bean.SysParamBean;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 系统参数管理
 */
public interface IParamBizService {

    /**
     * 获取单个系统参数
     *
     * @param paramId
     * @return
     */
    ResultBean<SysParamBean> actGetSysParam(@NotNull String paramId);

    /**
     * 保存系统参数
     *
     * @param sysParam
     * @return
     */
    ResultBean<SysParamBean> actSaveSysParam(SysParamBean sysParam);

    /**
     * 获取参数列表（有分页）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<SysParamBean>> actGetSysParams(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取参数列表
     *
     * @return
     */
    ResultBean<List<SysParamBean>> actGetSysParams();

    /**
     * 获取参数列表 (仅可用的参数）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupParams();

    /**
     * 删除系统参数
     *
     * @param sysParamId
     * @return
     */
    ResultBean<SysParamBean> actDeleteSysParam(@NotNull String sysParamId);

    //TODO：添加其它系统参数相关的接口

    /** 根据code获取，如果数据状态为作废，提示用户该参数已经作废。如果不存在，需要提示。
     * 根据编码获取String类型参数值
     * @param code
     * @return
     */
    ResultBean<String> actGetString(String code);

    /**
     * 根据编码获取Integer参数类型值
     * @param code
     * @return
     */
    ResultBean<Integer> actGetInteger(String code);

    /**
     * 根据参数编码获取Double参数类型值
     * @param code
     * @return
     */
    ResultBean<Double> actGetDouble(String code);

    /**
     * 根据参数编码获取Boolean参数类型值
     * @param code
     * @return
     */
    ResultBean<Boolean> actGetBoolean(String code);

    /**
     * 根据参数编码获取参数值List
     * @param code
     * @return
     */
    ResultBean<List<?>> actGetList(String code);

    /**
     * 根据参数编码获取参数的值Map
     * @param code
     * @return
     */
    ResultBean<Map<?,?>> actGetMap(String code);

    ResultBean<String> actGetWebServerUrl();

    ResultBean<List<String>> actGetParamByCode (String code) throws Exception;
}
