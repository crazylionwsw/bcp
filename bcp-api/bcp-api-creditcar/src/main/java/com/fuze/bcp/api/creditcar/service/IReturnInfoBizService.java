package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.ReturnInfoBean;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.bean.ResultBean;

/**
 * 资料归还的查询接口
 */
public interface IReturnInfoBizService {

    /**
     * 资料归还查询列表
     * @param currentPage
     * @return
     */
    ResultBean<ReturnInfoBean> actGetReturnInfos(int currentPage);

    /**
     * 模糊查询接口
     * @param currentPage
     * @param customerBean
     * @return
     */
    ResultBean<ReturnInfoBean> actSearchReturnInfos(int currentPage, CustomerBean customerBean);

    /**
     * 根据id回显
     * @param id
     * @return
     */
    ResultBean<ReturnInfoBean> actGetReturnInfo(String id);

    /**
     * 保存资料归还单
     * @param returnInfo
     * @return
     */
    ResultBean<ReturnInfoBean> actSaveReturnInfo(ReturnInfoBean returnInfo);

}
