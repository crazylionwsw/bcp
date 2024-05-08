package com.fuze.bcp.dubbo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.fuze.bcp.api.drools.bean.DroolsBaseBean;
import com.fuze.bcp.api.drools.service.IDroolsBizService;
import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.mqreceiver.service.SpringDynamicService;
import com.fuze.bcp.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Dubbox规则引擎过滤器
 */

@Activate
public class DroolsFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(DroolsFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        IDroolsBizService iDroolsService = SpringDynamicService.applicationContext.getBean(IDroolsBizService.class);
        MessageService messageService = SpringDynamicService.applicationContext.getBean(MessageService.class);
        String path = invocation.getAttachment("path");
        String kBase = path.substring(path.lastIndexOf(".") + 1);
        String methodName = invocation.getMethodName();
        List<String> ruName = new ArrayList<>();
        ruName.add(methodName);
        try {
            Object[] arguments = invocation.getArguments();
            for (Object object : arguments) {
                if (object instanceof APIBaseBean) {
                    DroolsBaseBean droolsBaseBean = iDroolsService.doDrools((APIBaseBean) object, kBase, Arrays.asList(ruName));
                    if (droolsBaseBean.getMessage().size() > 0) {
                        List<String> msg = new ArrayList<>();
                        for (int i = 0; i < droolsBaseBean.getMessage().size(); i++) {
                            msg.add(messageService.getMessage((String) droolsBaseBean.getMessage().get(i)));
                        }
                        RpcResult rpcResult = new RpcResult(ResultBean.getFailed().setD(msg));
                        rpcResult.setAttachments(invocation.getAttachments());
                        return rpcResult;
                    }
                }
            }
            // Do our business
            return invoker.invoke(invocation);
        } catch (RuntimeException e) {
            logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
                    + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                    + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
            throw e;
        }

    }

}
