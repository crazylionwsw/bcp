package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.api.bd.bean.HistoryRecord;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户的文档（合同）
 */
@Data
public class CustomerContractBean extends APIBaseBean {

    /**
     * 关联的客户
     */
    private String customerId;

    /**
     * 客户交易ID
     */
    private String customerTransactionId;

    /**
     *  合同类型
     */
    private String documentId = null;

//    /**
//     * 关联的模板
//     */
//    private String contractTplId;

    /**
     * 合同编号
     */
    private String contractNo;


    /**
     * 合同文件 (当fileId为空时，需要调用合同套打工具类生成合同，并把生成的PDF文件保存到MongoDB)
     */
    private String fileId;

    /**
     * 合同下载日志
     */
    private List<HistoryRecord> downloadRecords = new ArrayList<HistoryRecord>();

    /**
     * 最后一次下载时间
     */
    private String latestDownloadTime;

}
