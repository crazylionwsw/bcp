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
package com.fuze.bcp;

import com.fuze.bcp.bean.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This class must be accessible from both the provider and consumer
 *
 * @author sean
 */
public class BaseSerializationOptimizerImplr  {

    public static Collection<Class> getSerializableClasses() {
        List<Class> classes = new LinkedList<Class>();
        classes.add(APIBaseBean.class);
        classes.add(APIBaseBillBean.class);
        classes.add(APIBaseDataBean.class);
        classes.add(APIBillBean.class);
        classes.add(APILookupBean.class);
        classes.add(APITreeDataBean.class);
        classes.add(APITreeLookupBean.class);
        classes.add(BusinessRate.class);
        classes.add(DataPageBean.class);
        classes.add(DealerRate.class);
        classes.add(ImageTypeFileBean.class);
        classes.add(PadSalesRate.class);
        classes.add(PayAccount.class);
        classes.add(RateType.class);
        classes.add(ResultBean.class);
        classes.add(ResultCode.class);
        classes.add(SalesRate.class);
        classes.add(SearchBean.class);
        classes.add(ServiceFee.class);
        return classes;
    }
}
