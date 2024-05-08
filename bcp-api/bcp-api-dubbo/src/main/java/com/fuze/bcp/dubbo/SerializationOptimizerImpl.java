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
package com.fuze.bcp.dubbo;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.bean.PadLoginBean;
import com.fuze.bcp.api.bd.bean.PadCarDealerBean;
import com.fuze.bcp.api.creditcar.bean.OrderSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentListBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardListBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardSubmissionBean;
import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;

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
        classes.add(LoginUserBean.class);
        classes.add(OrderSubmissionBean.class);
        classes.add(PurchaseCarOrderBean.class);
        classes.add(PadCustomerCarBean.class);
        classes.add(PadCarDealerBean.class);
        classes.add(PadLoginBean.class);
        classes.add(CustomerTransactionBean.class);
        classes.add(CustomerCarBean.class);
        classes.add(CustomerJobBean.class);
        classes.add(CustomerLoanBean.class);
        classes.add(CustomerSurveyBean.class);
        //预约刷卡
        classes.add(AppointSwipingCardSubmissionBean.class);
        classes.add(AppointSwipingCardListBean.class);
        classes.add(AppointSwipingCardBean.class);
        //预约
        classes.add(AppointPaymentSubmissionBean.class);
        classes.add(AppointPaymentListBean.class);
        classes.add(AppointPaymentBean.class);
        return classes;
    }
}
