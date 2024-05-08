package com.fuze.bcp.push.business;

import com.fuze.bcp.api.push.bean.PushDataBean;
import com.fuze.bcp.api.push.bean.PushTokenBindBean;
import com.fuze.bcp.api.push.service.IPushDataBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.push.domain.PushData;
import com.fuze.bcp.push.domain.PushTokenBind;
import com.fuze.bcp.push.service.IPushDataServie;
import com.fuze.bcp.push.service.IPushTokenBindService;
import com.fuze.bcp.service.MappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/10/11.
 */
@Service
public class BizPushDataService implements IPushDataBizService {

    Logger logger = LoggerFactory.getLogger(BizPushDataService.class);

    @Autowired
    IPushTokenBindService iPushTokenBindService;

    @Autowired
    IPushDataServie iPushDataServie;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<PushTokenBindBean> actGetPushTokenBind(String id) {
        PushTokenBind pushTokenBind = iPushTokenBindService.getAvailableOne(id);
        if (pushTokenBind != null) {
            return ResultBean.getSucceed().setD(mappingService.map(pushTokenBind, PushTokenBindBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PushTokenBindBean> actSavePushTokenBind(PushTokenBindBean pushTokenBindBean) {
        PushTokenBind pushTokenBind = mappingService.map(pushTokenBindBean, PushTokenBind.class);
        pushTokenBind = iPushTokenBindService.save(pushTokenBind);
        if (pushTokenBind != null) {
            return ResultBean.getSucceed().setD(mappingService.map(pushTokenBind, PushTokenBindBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PushTokenBindBean> actGetToken(String loginUserName) {
        PushTokenBind pushTokenBind = iPushTokenBindService.getByLoginUserName(loginUserName);
        if (pushTokenBind != null) {
            return ResultBean.getSucceed().setD(mappingService.map(pushTokenBind, PushTokenBindBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PushTokenBindBean> actGetTokenByUserToken(String userToken) {
        PushTokenBind pushTokenBind = iPushTokenBindService.getByUserToken(userToken);
        if (pushTokenBind != null) {
            return ResultBean.getSucceed().setD(mappingService.map(pushTokenBind, PushTokenBindBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<PushTokenBindBean>> actGetToken(List<String> loginUserNames) {
        List<PushTokenBind> pushTokenBinds = iPushTokenBindService.getByLoginUserName(loginUserNames);
        if (pushTokenBinds != null && pushTokenBinds.size() > 0) {
            return ResultBean.getSucceed().setD(mappingService.map(pushTokenBinds, PushTokenBindBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<PushTokenBindBean>> actGetTokenByUserTokenOrLoginUserName(String userToken, String loginUserName) {
        List<PushTokenBind> pushTokenBinds = iPushTokenBindService.getByUserTokenOrLoginUserName(userToken, loginUserName);
        if (pushTokenBinds != null && pushTokenBinds.size() > 0) {
            return ResultBean.getSucceed().setD(mappingService.map(pushTokenBinds, PushTokenBindBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actDeleteRealByIds(List<String> ids) {
        iPushTokenBindService.deleteRealByIds(ids);
        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean actPushData(String loginUserName, PushDataBean pushDataBean) {
        PushTokenBind pushTokenBind = iPushTokenBindService.getByLoginUserName(loginUserName);
        if (pushTokenBind != null) {
            String token = pushTokenBind.getUserToken();
            PushData pushData = mappingService.map(pushDataBean, PushData.class);
            try {
                Map map = iPushDataServie.pushData(token, pushData);
                if ("SUCCESS".equals(map.get("ret"))) {
                    return ResultBean.getSucceed();
                } else {
                    throw new Exception((String) map.get("result"));
                }
            } catch (Exception e) {
                logger.error("推送消息失败", e);
                return ResultBean.getFailed();
            }
        }
        logger.error("推送消息失败,未找到设备绑定信息");
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actPushDatas(List<String> loginUserNames, PushDataBean pushDataBean) {
        List<String> tokens = new ArrayList<>();
        List<PushTokenBind> pushTokenBinds = iPushTokenBindService.getByLoginUserName(loginUserNames);
        pushTokenBinds.forEach(pushTokenBind -> tokens.add(pushTokenBind.getUserToken()));
        if (!tokens.isEmpty()) {
            PushData pushData = mappingService.map(pushDataBean, PushData.class);
            try {
                Map map = iPushDataServie.pushDatas(tokens, pushData);
                if ("SUCCESS".equals(map.get("ret"))) {
                    return ResultBean.getSucceed();
                } else {
                    throw new Exception(map.get("result").toString());
                }
            } catch (Exception e) {
                logger.error("推送消息失败", e);
                return ResultBean.getFailed();
            }
        }
        logger.error("推送消息失败", new Exception("无效的发送用户列表"));
        return ResultBean.getFailed().setM("无效的发送用户列表");
    }

    @Override
    public ResultBean actPushAllData(PushDataBean pushDataBean) {
        PushData pushData = mappingService.map(pushDataBean, PushData.class);
        try {
            Map map = iPushDataServie.pushAllData(pushData);
            if ("SUCCESS".equals(map.get("ret"))) {
                return ResultBean.getSucceed();
            } else {
                throw new Exception((String) map.get("result"));
            }
        } catch (Exception e) {
            logger.error("推送消息失败", e);
            return ResultBean.getFailed();
        }
    }
}
