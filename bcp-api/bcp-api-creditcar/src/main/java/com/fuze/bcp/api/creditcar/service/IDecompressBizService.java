package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.decompressBill.DecompressBillBean;
import com.fuze.bcp.api.creditcar.bean.decompressBill.DecompressBillListBean;
import com.fuze.bcp.api.creditcar.bean.decompressBill.DecompressBillSubmissionBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

/**
 * Created by ${Liu} on 2018/3/8.
 * 解押管理服务接口
 */
public interface IDecompressBizService {
    /**
     *创建解押单(PAD端)
     */
    ResultBean<DecompressBillSubmissionBean> actCreateDecompress(String transactionId);

    /**
     *提交解押单(PAD端)
     */
    ResultBean<DecompressBillSubmissionBean> actSubmitDecompress(DecompressBillSubmissionBean decompressBillSubmissionBean);

    /**
     *获取解押单详情(PAD端)
     */
    ResultBean<DecompressBillListBean> actGetDecompressInfo(String decompressId);

    /**
     *获取解押单列表(PAD端)
     */
    ResultBean<DecompressBillListBean> actGetDecomresss(Boolean isPass,String loginUserId, Integer pageIndex, Integer pageSize);


    /**
     *获取解押单列表(含查询)
     */
    ResultBean<DecompressBillBean> actSearchDecomresss(String userId,SearchBean searchBean);

    /**
     *获取单条解押数据
     */
    ResultBean<DecompressBillBean> actGetDecompress(String decompressId,Integer dataStatus);

    /**
     *保存解押单数据
     */
    ResultBean<DecompressBillBean> actSaveDecompress(DecompressBillBean decompressBillBean,String userId);

    /**
     *审核解押单
     */
    ResultBean<DecompressBillBean> actSignDecompress(String decompressId, SignInfo signInfo);

    /**
     * 通过交易获取解压单
     * @param customerTransactionId
     * @return
     */
    ResultBean<DecompressBillBean> actGetDecompressByTransactionId(String customerTransactionId);


    /**
     *PC发起解押单
     */
    ResultBean<DecompressBillBean> actCreateDecompressOnPc(String transactionId,String userId);

}
