package com.fuze.bcp.api.creditcar.bean.declaration;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqw on 2017/12/8.
 */
@Data
public class DeclarationHistorysBean extends APIBaseBean {

    private String customerTransactionId;

    // 报批反馈历史记录
    private List<DeclarationRecord> historyRecords = new ArrayList<DeclarationRecord>();
}
