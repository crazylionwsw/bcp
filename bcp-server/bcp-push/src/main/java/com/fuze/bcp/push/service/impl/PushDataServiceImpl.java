package com.fuze.bcp.push.service.impl;

import com.fuze.bcp.api.push.bean.PushDataBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.push.domain.PushData;
import com.fuze.bcp.push.repository.PushDataRepository;
import com.fuze.bcp.push.service.IPushDataServie;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import push.AndroidNotification;
import push.PushClient;
import push.android.AndroidBroadcast;
import push.android.AndroidListcast;
import push.android.AndroidUnicast;

import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/9/28.
 */
@Service
public class PushDataServiceImpl extends BaseServiceImpl<PushData, PushDataRepository> implements IPushDataServie {

    Logger logger = LoggerFactory.getLogger(PushDataServiceImpl.class);

    @Value("${umeng.appkey}")
    private String appkey = null;

    @Value("${umeng.appMasterSecret}")
    private String appMasterSecret = null;
//
//    public void sendAndroidBroadcast(String ticker, String title, String text) throws Exception {
//        PushClient client = new PushClient();
//        AndroidBroadcast broadcast = new AndroidBroadcast(appkey, appMasterSecret);
//        broadcast.setTicker(ticker);
//        broadcast.setTitle(title);
//        broadcast.setText(text);
//        broadcast.goAppAfterOpen();
//        broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//        // TODO Set 'production_mode' to 'false' if it's a test device.
//        // For how to register a test device, please see the developer doc.
//        broadcast.setProductionMode();
//        // Set customized fields
//        broadcast.setExtraField("test", "helloworld");
//        client.send(broadcast);
//    }

//    public void sendAndroidUnicast(String ticker, String title, String text, String token) throws Exception {
//        PushClient client = new PushClient();
//        AndroidUnicast unicast = new AndroidUnicast(appkey, appMasterSecret);
//        // TODO Set your device token
//        unicast.setDeviceToken(token);
//        unicast.setTicker(ticker); //  "Ak9Wpi9gyKzRL5KwbsEwg5ApXoIcAPWb4TJmnvMeVmmu"
//        unicast.setTitle(title);
//        unicast.setText(text);
//        unicast.goAppAfterOpen();
//        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//        // TODO Set 'production_mode' to 'false' if it's a test device.
//        // For how to register a test device, please see the developer doc.
//        unicast.setProductionMode();
//        // Set customized fields
//        unicast.setExtraField("test", "helloworld");
//        client.send(unicast);
//    }


    @Override
    public Map pushData(String token, PushData pushData) throws Exception {
        PushClient client = new PushClient();
        AndroidUnicast unicast = new AndroidUnicast(appkey, appMasterSecret);
        unicast.setDeviceToken(token);
        putPublicAndroidField(unicast, pushData);
        return client.send(unicast);
    }

    @Override
    public Map pushDatas(List<String> tokens, PushData pushData) throws Exception {
        PushClient client = new PushClient();
        AndroidListcast listcast = new AndroidListcast(appkey, appMasterSecret);
        listcast.setDeviceToken(tokens);
        putPublicAndroidField(listcast, pushData);
        return client.send(listcast);
    }

    @Override
    public Map pushAllData(PushData pushData) throws Exception {
        PushClient client = new PushClient();
        AndroidBroadcast broadcast = new AndroidBroadcast(appkey, appMasterSecret);
        putPublicAndroidField(broadcast, pushData);
        return client.send(broadcast);
    }

    private void putPublicAndroidField(AndroidNotification androidNotification, PushData pushData) throws Exception {
        androidNotification.setTicker(pushData.getTicker());
        androidNotification.setTitle(pushData.getTitle());
        androidNotification.setText(pushData.getText());
        if (pushData.getAfterOpenAction() == PushDataBean.go_app) {
            androidNotification.goAppAfterOpen();
        } else if (pushData.getAfterOpenAction() == PushDataBean.go_url) {
            androidNotification.goUrlAfterOpen(pushData.getUrl());
        } else if (pushData.getAfterOpenAction() == PushDataBean.go_activity) {
            androidNotification.goActivityAfterOpen(pushData.getActivity());
        }
        else if (pushData.getAfterOpenAction() == PushDataBean.go_activity) {
            androidNotification.goActivityAfterOpen(pushData.getActivity());
        }
        androidNotification.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        androidNotification.setProductionMode();
        // Set customized fields
        if (pushData.getExtraFields() != null && !pushData.getExtraFields().isEmpty()) {
            pushData.getExtraFields().forEach((k, v) -> {
                try {
                    androidNotification.setExtraField(k, v);
                } catch (Exception e) {
                    logger.error("设置自定义参数错误", e);
                }
            });
        }
    }
}
