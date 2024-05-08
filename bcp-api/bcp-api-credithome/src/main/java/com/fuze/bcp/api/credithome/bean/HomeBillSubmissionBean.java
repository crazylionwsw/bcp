package com.fuze.bcp.api.credithome.bean;

import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.ImageTypeFileBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 2017/7/28.
 */
@Data
public class HomeBillSubmissionBean extends APIBaseBean {

    /**
     * 审核状态
     */
    private Integer approveStatus;

    /**
     * 提交人
     */
    private String loginUserId = null;

    /**
     * 来源经销商
     */
    private String carDealerId = null;

    /**
     * 渠道
     */
    private String channelId = null;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 员工ID
     */
    private String employeeId;

    /**
     * 客户交易ID
     */
    private String customerTransactionId;

    /**
     * 业务类型
     */
    private String businessTypeCode = null;


    //档案资料
    private List<ImageTypeFileBean> customerImages = new ArrayList<ImageTypeFileBean>();

}
