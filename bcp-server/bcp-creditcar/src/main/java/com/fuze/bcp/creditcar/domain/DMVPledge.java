package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.creditcar.bean.CardActionRecord;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 抵押登记
 * Created by sean on 2016/11/27.
 */
@Document(collection = "so_dmvpledge")
@Data
public class DMVPledge extends BaseBillEntity {


    /**
     *  抵押状态
     */
    private Integer status = 0;

    /**
     * 抵押人Id
     */
    private String pledgeCustomerId = null;

    /**
     * 客户抵押资料签收时间
     */
    private String pledgeDateReceiveTime = null;

    /**
     * 银行抵押合同打印时间
     */
    private String contractStartTime =null;

    /**
     * 银行抵押合同盖章时间
     */
    private String takeContractTime =null;

    /**
     * 车管所抵押开始时间
     */
    private String pledgeStartTime = null;

    /**
     * 抵押完成时间
     */
    private String pledgeEndTime = null;

    /**
     * 存储完成的动作
     */
    private List<CardActionRecord> actionRecords = new ArrayList<CardActionRecord>();


    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A008";
    }

    public void updateStatus(){
        setStatus(0);
        if(getPledgeDateReceiveTime() != null){
            setStatus(1);
        }
        if(getContractStartTime() != null){
            setStatus(2);
        }
        if(getTakeContractTime() != null){
            setStatus(3);
        }
        if(getPledgeStartTime() != null){
            setStatus(4);
        }
        if(getPledgeEndTime() != null){
            setStatus(5);
        }
    }
}
