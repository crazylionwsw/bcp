package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.ReceptFileBean;
import com.fuze.bcp.bean.ResultBean;

/**
 * Created by ${Liu} on 2017/9/19.
 * 客户资料交接
 */
public interface IReceptFileBizService {
    /**
     * 通过交易ID获取客户交接资料
     */
    ResultBean<ReceptFileBean> actGetReceptFile(String customerTransactionId);

    /**
     *保存客户交接资料
     */
    ResultBean<ReceptFileBean> actSaveReceptFile(ReceptFileBean receptFileBean);

}
