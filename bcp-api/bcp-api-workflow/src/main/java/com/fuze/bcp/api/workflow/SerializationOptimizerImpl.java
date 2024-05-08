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
package com.fuze.bcp.api.workflow;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.fuze.bcp.BaseSerializationOptimizerImplr;
import com.fuze.bcp.api.workflow.bean.*;

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
        classes.add(BillSignInfo.class);
        classes.add(ExecutionBean.class);
        classes.add(GroupBean.class);
        classes.add(SignCondition.class);
        classes.add(SignInfo.class);
        classes.add(SignInfoBean.class);
        classes.add(TaskBean.class);
        classes.add(TEMSignInfo.class);
        classes.add(WorkFlowBillBean.class);
        classes.addAll(BaseSerializationOptimizerImplr.getSerializableClasses());
        return classes;
    }
}
