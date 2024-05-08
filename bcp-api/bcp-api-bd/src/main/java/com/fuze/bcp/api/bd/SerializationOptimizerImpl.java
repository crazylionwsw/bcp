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
package com.fuze.bcp.api.bd;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.fuze.bcp.BaseSerializationOptimizerImplr;
import com.fuze.bcp.api.bd.bean.*;

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
        classes.add(BillTypeBean.class);
        classes.add(BankSettlementStandardBean.class);
        classes.add(BusinessEventTypeBean.class);
        classes.add(BusinessTypeBean.class);
        classes.add(BusinessTypeRateLookupBean.class);
        classes.add(CarBrandBean.class);
        classes.add(CarModelBean.class);
        classes.add(CarTypeBean.class);
        classes.add(CarTypeLookupBean.class);
        classes.add(CashSourceBean.class);
        classes.add(CashSourceEmployeeBean.class);
        classes.add(CompensatoryPolicyBean.class);
        classes.add(CompensatoryPolicyFormulaBean.class);
        classes.add(CompensatoryPolicyListBean.class);
        classes.add(CompensatoryRate.class);
        classes.add(CreditProductBean.class);
        classes.add(CustomerImageTypeBean.class);
        classes.add(DealerEmployeeBean.class);
        classes.add(EmployeeBean.class);
        classes.add(EmployeeLookupBean.class);
        classes.add(EmployeeUserBean.class);
        classes.add(FeeItemBean.class);
        classes.add(FeeValueBean.class);
        classes.add(GuaranteeWayBean.class);
        classes.add(HistoryRecord.class);
        classes.add(OptionsBean.class);
        classes.add(OrgBean.class);
        classes.add(PadCarDealerBean.class);
        classes.add(PadSalesPolicyBean.class);
        classes.add(PayAccountBean.class);
        classes.add(PaymentPolicyBean.class);
        classes.add(PromotionPolicyBean.class);
        classes.add(ProvinceBean.class);
        classes.add(RabbitmqRegister.class);
        classes.add(RepaymentWayBean.class);
        classes.add(RoleControl.class);
        classes.add(RoleImageControl.class);
        classes.add(SalesPolicyBean.class);
        classes.add(SourceRateBean.class);
        classes.add(SourceRateLookupBean.class);
        classes.add(UserProfileBean.class);
        classes.addAll(BaseSerializationOptimizerImplr.getSerializableClasses());
        return classes;
    }
}
