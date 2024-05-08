package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.List;

/** 客户资料交接
 * Created by ${Liu} on 2017/9/19.
 */
@Data
public class ReceptFileBean extends APICarBillBean{

    /**
     * 交接资料
     */
    private List<String> fileNames;

    public String getBillTypeCode() {
        return "A030";
    }

}
