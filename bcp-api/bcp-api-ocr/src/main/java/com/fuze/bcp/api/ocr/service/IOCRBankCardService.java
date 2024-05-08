package com.fuze.bcp.api.ocr.service;

import com.fuze.bcp.api.ocr.bean.IdentifyCardInfo;
import com.fuze.bcp.bean.ResultBean;

/**
 * 银行卡识别服务接口
 * Created by sean on 2017/6/8.
 */
public interface IOCRBankCardService {
    /**
     * 识别银行卡信息
     * @param frontImage  银行卡正面
     * @param reverseImage   银行卡背面
     * @return
     */
    ResultBean<IdentifyCardInfo> recognize(byte[] frontImage, byte[] reverseImage);

}
