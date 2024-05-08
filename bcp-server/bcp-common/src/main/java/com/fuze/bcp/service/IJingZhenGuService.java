package com.fuze.bcp.service;

import com.fuze.bcp.callback.ISyncDataCallback;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by sean on 2017/4/21.
 */
public interface IJingZhenGuService {

    /**
     * 获取厂商数据
     * @param iSyncDataCallback
     * @return
     */
    JSONArray getMakeData(ISyncDataCallback iSyncDataCallback);

    /**
     * 获取车系数据
     * @param makeId
     * @param iSyncDataCallback
     * @return
     */
    JSONArray getModelData(String makeId, ISyncDataCallback iSyncDataCallback);

    /**
     * 获取车型数据
     * @param modelId
     * @param iSyncDataCallback
     * @return
     */
    JSONArray getTypeData(String modelId, ISyncDataCallback iSyncDataCallback);

    /**
     * 获取省份数据
     * @param iSyncDataCallback
     * @return
     */
    JSONArray getProvinceData(ISyncDataCallback iSyncDataCallback);

    /**
     *  获取城市数据
     * @param provinceId
     * @param iSyncDataCallback
     * @return
     */
    JSONArray getCityData(String provinceId, ISyncDataCallback iSyncDataCallback);

    /**
     * 获取车型价格
     * @param trimId
     * @return
     */
    Double getMsrp(String trimId);

    /**
     * 获取估值信息
     * @param trimId
     * @param buyCarDate
     * @param mileage
     * @param cityId
     * @param iSyncDataCallback
     * @return
     * */
    JSONObject getValuationInfo(String trimId, String buyCarDate, String mileage, String cityId, ISyncDataCallback iSyncDataCallback);

    /**
     * 获取参配项数据
     * @param vin
     * @param NoticeNo
     * @param year
     * @param callback
     * @return
     */
    JSONObject getConfigData(String vin, String NoticeNo, String year, ISyncDataCallback callback);

    /**
     * 根据VIN 获取车型的信息
     * @param vin
     * @return
     * [{"MakeId":"18","MakeName":"马自达","ModelId":"4711","ModelName":"马自达CX-4","StyleId":"119691","StyleName":"2.0L 自动 两驱蓝天领先版","StyleYear":"2016","Emission":"2.0","EnvironmentStandard":"国5"},
     * {"MakeId":"18","MakeName":"马自达","ModelId":"4711","ModelName":"马自达CX-4","StyleId":"119692","StyleName":"2.0L 自动 两驱蓝天品位版","StyleYear":"2016","Emission":"2.0","EnvironmentStandard":"国5"},
     * {"MakeId":"18","MakeName":"马自达","ModelId":"4711","ModelName":"马自达CX-4","StyleId":"119693","StyleName":"2.0L 自动 两驱蓝天活力真皮版","StyleYear":"2016","Emission":"2.0","EnvironmentStandard":"国5"},
     * {"MakeId":"18","MakeName":"马自达","ModelId":"4711","ModelName":"马自达CX-4","StyleId":"119694","StyleName":"2.0L 自动 两驱蓝天活力版","StyleYear":"2016","Emission":"2.0","EnvironmentStandard":"国5"}]

     */
    JSONArray getCarTypesByVin(String vin, ISyncDataCallback callback);
}
