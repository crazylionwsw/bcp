package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.bd.bean.FeeBillBean;
import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.creditcar.bean.businessexchange.BusinessExchangeBean;
import com.fuze.bcp.api.creditcar.bean.businessexchange.BusinessExchangeListBean;
import com.fuze.bcp.api.creditcar.bean.businessexchange.BusinessExchangeSubmitBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;
import java.util.Map;

/**
 * Created by ${Liu} on 2018/3/1.
 */
public interface IBusinessExchangeBizService {

    ResultBean<BusinessExchangeBean> actGetBusinessExchangeByTransaction(String transactionId);

    /**
     *提交业务调整单(PAD端)
     */
    ResultBean<BusinessExchangeSubmitBean> actSubmitBusinessExchange(BusinessExchangeSubmitBean businessExchangeSubmitBean);

    /**
     *审核业务调整单
     */
    ResultBean<BusinessExchangeBean> actSignBusinessExchange(String  businessExchangeId,SignInfo signInfo);

    /**
     * 获取分期经理业务调整列表(PAD端)
     */
    ResultBean<List<BusinessExchangeListBean>> actGetBusinessExchanges(Boolean isPass,String loginUserId, Integer pageIndex, Integer pageSize);

    /**
     *获取业务调整单详情(PAD端)
     */
    ResultBean<BusinessExchangeSubmitBean> actGetBusinessExchangeInfo(String businessExchangeId);

    /**
     *获取业务调整单列表(查询)
     */
    ResultBean<BusinessExchangeBean> actSearchBusinessExchange(String userId, SearchBean searchBean);

    /**
     *获取单条业务调整单数据
     */
    ResultBean<BusinessExchangeBean> actGetBusinessExchange(String businessExchangeId);

    /**
     *对比签约数据
     */
    ResultBean<Map> actCompareData(String propname, String transactionId,String businessExchangeId);

    /**
     *获取签约以及收费项里的所有收费项目
     */
    ResultBean<List<FeeValueBean>> actGetALLFeesOnBusinessExchange(String transactionId);


}
