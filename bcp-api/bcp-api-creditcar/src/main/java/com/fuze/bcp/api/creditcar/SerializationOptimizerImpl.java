/**
 * Copyright 1999-2014 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fuze.bcp.api.creditcar;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.fuze.bcp.BaseSerializationOptimizerImplr;
import com.fuze.bcp.api.creditcar.bean.CancelOrderBean;
import com.fuze.bcp.api.creditcar.bean.*;
import com.fuze.bcp.api.creditcar.bean.appointpayment.*;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.*;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.*;
import com.fuze.bcp.api.creditcar.bean.businessbook.*;
import com.fuze.bcp.api.creditcar.bean.businessexchange.*;
import com.fuze.bcp.api.creditcar.bean.carregistry.*;
import com.fuze.bcp.api.creditcar.bean.cartransfer.*;
import com.fuze.bcp.api.creditcar.bean.customerfeebill.*;
import com.fuze.bcp.api.creditcar.bean.dealerrepayment.*;
import com.fuze.bcp.api.creditcar.bean.dealersharing.DealerSharingBean;
import com.fuze.bcp.api.creditcar.bean.dealersharing.GroupSharingBean;
import com.fuze.bcp.api.creditcar.bean.dealersharing.SharingDetails4DealerBean;
import com.fuze.bcp.api.creditcar.bean.dealersharing.SharingDetailsBean;
import com.fuze.bcp.api.creditcar.bean.declaration.*;
import com.fuze.bcp.api.creditcar.bean.enhancement.EnhancementListBean;
import com.fuze.bcp.api.creditcar.bean.enhancement.EnhancementSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.fileexpress.FileExpressListBean;
import com.fuze.bcp.api.creditcar.bean.fileexpress.FileExpressSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardListBean;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardSubmissionBean;
import com.fuze.bcp.bean.ImageTypeFileBean;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This class must be accessible from both the provider and consumer
 *
 * @author sean
 */
public class SerializationOptimizerImpl implements SerializationOptimizer {

    public Collection<Class> getSerializableClasses() {
        List<Class> classes = new LinkedList<Class>();
        classes.add(AppointPaymentBean.class);
        classes.add(AppointPaymentExcelBean.class);
        classes.add(AppointPaymentListBean.class);
        classes.add(AppointPaymentSubmissionBean.class);
        classes.add(AppAppointSwipingCardBean.class);
        classes.add(AppointSwipingCardBean.class);
        classes.add(AppointSwipingCardListBean.class);
        classes.add(AppointSwipingCardSubmissionBean.class);
        classes.add(BankCardApplyBean.class);
        classes.add(BankCardApplyListBean.class);
        classes.add(BankCardBean.class);
        classes.add(BankCardListBean.class);
        classes.add(BusinessBookBean.class);
        classes.add(BusinessBookExcelBean.class);
        classes.add(BusinessExchangeBean.class);
        classes.add(BusinessExchangeListBean.class);
        classes.add(BusinessExchangeSubmitBean.class);
        classes.add(CarRegistryBean.class);
        classes.add(CarRegistryListBean.class);
        classes.add(CarRegistrySubmissionBean.class);
        classes.add(CarTransferBean.class);
        classes.add(CarTransferListBean.class);
        classes.add(CarTransferSubmissionBean.class);
        classes.add(CustomerFeeBean.class);
        classes.add(CustomerFeeBillBean.class);
        classes.add(DealerRepaymentBean.class);
        classes.add(DealerRepaymentListBean.class);
        classes.add(DealerRepaymentSubmissionBean.class);
        classes.add(DealerSharingBean.class);
        classes.add(GroupSharingBean.class);
        classes.add(SharingDetails4DealerBean.class);
        classes.add(SharingDetailsBean.class);
        classes.add(DeclarationBean.class);
        classes.add(DeclarationHistorysBean.class);
        classes.add(DeclarationRecord.class);
        classes.add(DeclarationResult.class);
        classes.add(IncomeAccount.class);
        classes.add(PaymentToIncome.class);
        classes.add(EnhancementListBean.class);
        classes.add(EnhancementSubmissionBean.class);
        classes.add(FileExpressBean.class);
        classes.add(FileExpressListBean.class);
        classes.add(FileExpressSubmissionBean.class);
        classes.add(SwipingCardBean.class);
        classes.add(SwipingCardListBean.class);
        classes.add(SwipingCardSubmissionBean.class);
        classes.add(APIBillListBean.class);
        classes.add(APICarBillBean.class);
        classes.add(BillSubmissionBean.class);
        classes.add(BusinessExcelBean.class);
        classes.add(CancelOrderBean.class);
        classes.add(CardActionRecord.class);
        classes.add(CarIndicator.class);
        classes.add(CarPaymentBillBean.class);
        classes.add(CarValuationBean.class);
        classes.add(CompensatoryBean.class);
        classes.add(CompensatoryExcelBean.class);
        classes.add(CreditCustomerBean.class);
        classes.add(CreditPhotographBean.class);
        classes.add(CreditReportQueryBean.class);
        classes.add(CustomerContractBean.class);
        classes.add(CustomerDemandBean.class);
        classes.add(CustomerImageFileBean.class);
        classes.add(CustomerSurveyResultBean.class);
        classes.add(CustomerSurveyTemplateBean.class);
        classes.add(DemandListBean.class);
        classes.add(DemandSubmissionBean.class);
        classes.add(DMVPledgeBean.class);
        classes.add(EnhancementBean.class);
        classes.add(FileExpressBean.class);
        classes.add(ImageTypeFileBean.class);
        classes.add(OrderListBean.class);
        classes.add(OrderSubmissionBean.class);
        classes.add(OrderSubmissionCarBean.class);
        classes.add(OrderSubmissionLoanBean.class);
        classes.add(OrderSubmissionPeopleBean.class);
        classes.add(PickupCarBean.class);
        classes.add(PoundageSettlementBean.class);
        classes.add(PurchaseCarOrderBean.class);
        classes.add(ReceptFileBean.class);
        classes.add(RejectCustomerBean.class);
        classes.add(ResetOrderBean.class);
        classes.add(ReturnInfoBean.class);
        classes.add(SwipingCardTrusteeBean.class);
        classes.add(ValuationInfo.class);
        classes.add(ValuationListBean.class);
        classes.add(ValuationSubmissionBean.class);
        classes.addAll(BaseSerializationOptimizerImplr.getSerializableClasses());
        return classes;
    }
}
