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
package com.fuze.bcp.api.customer;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.fuze.bcp.BaseSerializationOptimizerImplr;
import com.fuze.bcp.api.customer.bean.*;

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
        classes.add(BusinessDataBean.class);
        classes.add(CategoryQuestionResultBean.class);
        classes.add(CorporateActionBean.class);
        classes.add(CustomerBean.class);
        classes.add(CustomerCarBean.class);
        classes.add(CustomerCardBean.class);
        classes.add(CustomerCarExchangeBean.class);
        classes.add(CustomerCheckBean.class);
        classes.add(CustomerInstantaneBean.class);
        classes.add(CustomerJobBean.class);
        classes.add(CustomerLoanBean.class);
        classes.add(CustomerLoanExchangeBean.class);
        classes.add(CustomerPackageBean.class);
        classes.add(CustomerRelation.class);
        classes.add(CustomerRepaymentBean.class);
        classes.add(CustomerSurveyBean.class);
        classes.add(LoanSubmissionBean.class);
        classes.add(Options.class);
        classes.add(PadCustomerCarBean.class);
        classes.add(PersonalActionBean.class);
        classes.add(PersonalInvestmentBean.class);
        classes.add(QuestionCategoryBean.class);
        classes.add(QuestionResultBean.class);
        classes.add(QuestionsBean.class);
        classes.add(SurveyOption.class);
        classes.addAll(BaseSerializationOptimizerImplr.getSerializableClasses());
        return classes;
    }
}
