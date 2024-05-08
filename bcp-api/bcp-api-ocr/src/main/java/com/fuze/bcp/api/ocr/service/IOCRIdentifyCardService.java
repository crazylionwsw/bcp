package com.fuze.bcp.api.ocr.service;

import com.fuze.bcp.api.ocr.bean.IdentifyCardInfo;
import com.fuze.bcp.bean.ResultBean;

/**
 * Created by sean on 2017/6/8.
 * 身份证识别服务接口
 */
public interface IOCRIdentifyCardService {

    /**
     * 识别身份证号码
     * @param frontImage  身份证正面图片
     * @param reverseImage   身份证背面图片
     * @return
     */
    ResultBean<IdentifyCardInfo>    recognize(byte[] frontImage, byte[] reverseImage);


    /**
     * 身份证号码识别
     * @param frontUrl  身份证正面图片url
     * @param reverseUrl 身份证背面图片url
     * @return
     */
    ResultBean<IdentifyCardInfo>    recognize(String frontUrl, String reverseUrl);
}
