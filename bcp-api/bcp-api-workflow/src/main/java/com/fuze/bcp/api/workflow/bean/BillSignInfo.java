package com.fuze.bcp.api.workflow.bean;

import com.fuze.bcp.utils.DateTimeUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/17.
 */
@Data
public class BillSignInfo  implements Serializable {

    private String billTypeCode;

    private List<SignInfoBean> signInfos = new ArrayList<SignInfoBean>();
}
