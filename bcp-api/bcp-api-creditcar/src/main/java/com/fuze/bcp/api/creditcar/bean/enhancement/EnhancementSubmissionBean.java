package com.fuze.bcp.api.creditcar.bean.enhancement;

import com.fuze.bcp.api.creditcar.bean.BillSubmissionBean;
import lombok.Data;

/**
 * Created by Lily on 2017/9/14.
 */
@Data
public class EnhancementSubmissionBean extends BillSubmissionBean {

    //  是否允许分期经理删除已有档案，默认不可以删除  0 : 不能删除  1： 可以删除
    private int allowDeleteCustomerImage = 0;
}
