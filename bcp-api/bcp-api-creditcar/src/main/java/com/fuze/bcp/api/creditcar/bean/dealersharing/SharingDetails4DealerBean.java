package com.fuze.bcp.api.creditcar.bean.dealersharing;

import lombok.Data;

/**
 * Created by ${Liu} on 2018/1/5.
 */
@Data
public class SharingDetails4DealerBean extends SharingDetailsBean {

    public SharingDetails4DealerBean() {
    }

    public SharingDetails4DealerBean(SharingDetailsBean sharingDetailsBean, String cardealerId,String channelManagerId) {
        super(sharingDetailsBean.getPledgeDateReceiveTime(), sharingDetailsBean.getCompensatoryFlag(), sharingDetailsBean.getChargePaymentWay(), sharingDetailsBean.getEmployeeId(), sharingDetailsBean.getTransactionId(),
                sharingDetailsBean.getCustomerId(), sharingDetailsBean.getCreditAmount(), sharingDetailsBean.getBankFeeAmount(),
                sharingDetailsBean.getLoanServiceFee(), sharingDetailsBean.getMonths(), sharingDetailsBean.getSharingRatio(),
                sharingDetailsBean.getSharingAmount(), sharingDetailsBean.getSharingType(), sharingDetailsBean.getStatus(), sharingDetailsBean.getMainPartType());
        this.cardealerId = cardealerId;
        this.channelManagerId = channelManagerId;
    }

    private String dealerSharingId;

    private String cardealerId;

    private String cardealerName;

    private String channelManagerId;

    private Integer employeeRowspan;

    private Integer cardealerRowspan;

    private Integer dealerSharingStatus;

    private Double totalSharing;

}
