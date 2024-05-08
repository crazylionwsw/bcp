package com.fuze.bcp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuze.bcp.service.IJingZhenGuService;
import com.fuze.bcp.callback.ISyncDataCallback;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.EncryUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;


/****
 * 精真估评估接口
 * 获取车型库
 * 获取参配项
 * 提交评估请求,获取评估报告
 **/
@Service
public class JingZhenGuServiceImpl implements IJingZhenGuService {

    private static Logger logger = LoggerFactory.getLogger(JingZhenGuServiceImpl.class);

    @Value("${jingzhengu.server}")
    private String jzgServer;

    @Value("${jingzhengu.key}")
    private String jzgKey;

    @Value("${jingzhengu.partnerId}")
    private String jzgPartnerId;

    /**
     * 同步车辆品牌数据
     */
    public JSONArray getMakeData(ISyncDataCallback iSyncDataCallback) {
        //获取品牌
        JSONArray makeArray = this.parseBodyAsJsonArray(this.requestJingZhenGuDataAPI("Make", ""));
        if(iSyncDataCallback != null) {
            iSyncDataCallback.callBack(makeArray);
        }
        return makeArray;
    }

    /**
     * 获取车系数据
     */
    public JSONArray getModelData(String makeId, ISyncDataCallback iSyncDataCallback) {
        //获取品牌
        JSONArray modelArray = this.parseBodyAsJsonArray(this.requestJingZhenGuDataAPI("Model", "{\"MakeId\":" + makeId + "}"));
        if(iSyncDataCallback!= null) {
            iSyncDataCallback.callBack(modelArray);
        }
        return modelArray;
    }

    /**
     * 获取车型数据
     * @param modelId
     * @param iSyncDataCallback
     */
    public JSONArray getTypeData(String modelId, ISyncDataCallback iSyncDataCallback){
        JSONArray styleArray = this.parseBodyAsJsonArray(this.requestJingZhenGuDataAPI("Style", "{\"ModelId\":" + modelId + "}"));
        if(iSyncDataCallback != null){
            iSyncDataCallback.callBack(styleArray);
        }
        return styleArray;
    }

    /**
     * 获取省份数据
     * @param iSyncDataCallback
     * @return
     */
    public JSONArray getProvinceData(ISyncDataCallback iSyncDataCallback){
        JSONArray styleArray = this.parseBodyAsJsonArray(this.requestJingZhenGuDataAPI("Province", ""));
        if(iSyncDataCallback != null){
            iSyncDataCallback.callBack(styleArray);
        }
        return styleArray;
    }
    /**
     * 获取城市数据
     * @param iSyncDataCallback
     * @return
     */
    public JSONArray getCityData(String provinceId, ISyncDataCallback iSyncDataCallback){
        try {
            JSONObject json = new JSONObject();
            json.put("ProvinceId", provinceId);
            JSONArray styleArray = this.parseBodyAsJsonArray(this.requestJingZhenGuDataAPI("City", json.toString()));
            if (iSyncDataCallback != null) {
                iSyncDataCallback.callBack(styleArray);
            }
            return styleArray;
        }catch(Exception  ex){
            ex.printStackTrace();
            logger.error("同步城市数据出错！",ex);
        }
        return null;
    }

    /**
     * 获取旧车使用成本
     * @param trimId
     * @param buyCarDate
     * @param mileage
     * @param iSyncDataCallback
     * @return
     * TODO  暂无权限
     */
    private JSONObject getOldCarCost(String trimId, String buyCarDate, String mileage, String cityId, ISyncDataCallback iSyncDataCallback){
        try {
            JSONObject jsonP = new JSONObject();
            jsonP.put("TrimId", trimId);
            jsonP.put("BuyCarDate",buyCarDate);
            jsonP.put("Mileage",mileage);
            jsonP.put("CityId",cityId);

            JSONObject styleArray = this.parseBodyAsJsonObject(this.requestJingZhenGuValuateAPI("OldCarCost",jsonP.toString() ));
            if (iSyncDataCallback != null) {
                iSyncDataCallback.callBack(styleArray);
            }
            return styleArray;
        }catch (JSONException ex){
            ex.printStackTrace();
            logger.error("获取新车残值接口出现错误！",ex);
        }
        return null;
    }


    /**
     *
     * 根据车型ID，上牌日期，行驶公里数，城市ID，未来年数获取新车的厂商指导价和残值率
     * @param trimId
     * @param buyCarDate
     * @param mileage
     * @param cityId
     * @param nextYears
     * @param iSyncDataCallback
     * TODO  暂无权限
     * @return
     */
    private JSONObject getNewCarPrice(String trimId, String buyCarDate, String mileage, String cityId, String nextYears, ISyncDataCallback iSyncDataCallback){
        try {
            JSONObject jsonP = new JSONObject();
            jsonP.put("TrimId", trimId);
            jsonP.put("BuyCarDate",buyCarDate);
            jsonP.put("Mileage",mileage);
            jsonP.put("CityId",cityId);
            jsonP.put("Year",nextYears);

            JSONObject styleArray = this.parseBodyAsJsonObject(this.requestJingZhenGuValuateAPI("NewCarScrapValue",jsonP.toString() ));
            if (iSyncDataCallback != null) {
                iSyncDataCallback.callBack(styleArray);
            }
            return styleArray;
        }catch (JSONException ex){
            ex.printStackTrace();
            logger.error("获取新车残值接口出现错误！",ex);
        }
        return null;
    }

    /**
     * 根据车型获取新车指导价
     * @param trimId
     * @return
     */
    public Double getMsrp(String trimId){
        try {
            JSONObject jp = getValuationInfo(trimId, null, null, null, null);
            if(jp != null){
                String msrp= (String)jp.get("Msrp");
                double dl = Double.parseDouble(msrp);
                if(dl<10000){
                    return dl*10000;
                }
                return dl;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取车型官方指导价出错！",ex);
        }
        return 0.0;
    }

        /**
         * 获取车辆评估价格
         * @param trimId
         * @param buyCarDate
         * @param mileage
         * @param cityId
         * @param iSyncDataCallback
         * @return
         */
    public JSONObject getValuationInfo(String trimId, String buyCarDate, String mileage, String cityId, ISyncDataCallback iSyncDataCallback){
        try {
            JSONObject jsonP = new JSONObject();
            jsonP.put("TrimId", trimId);
            jsonP.put("BuyCarDate",buyCarDate!=null? buyCarDate : DateTimeUtils.getOffsetMonthTodayStrOf("YYYY-MM-dd",-3));
            jsonP.put("Mileage",mileage==null?"10000":mileage);
            jsonP.put("CityId",cityId==null?"2":cityId);
            //System.out.println(jsonP.toString());
            JSONObject styleArray = this.parseBodyAsJsonObject(this.requestJingZhenGuDataAPI("Estimate",jsonP.toString() ));
            if (iSyncDataCallback != null) {
                iSyncDataCallback.callBack(styleArray);
            }
           // System.out.println("官方指导价："+styleArray.get("Msrp")+"万元");
            return styleArray;
        }catch (JSONException ex){
            ex.printStackTrace();
            logger.error("获取车辆评估价格接口出现错误！",ex);
        }
        return null;
    }

    /**
     * 根据VIN，车型国标，年份 获取车型的参数配置
     * @param vin
     * @param NoticeNo
     * @param year
     * @param callback
     * @return
     */
    public JSONObject getConfigData(String vin, String NoticeNo, String year, ISyncDataCallback callback){
        RestTemplate restTemplate = new RestTemplate();
        String sequenceId = UUID.randomUUID().toString();
        String data = null;
        String operate = "propdiffer";
        try {
            JSONObject jsonData = new JSONObject();
            jsonData.put("VIN",vin);
            jsonData.put("NoticeNo",NoticeNo);
            jsonData.put("ProductionYear",year);

            return parseBodyAsJsonObject(requestJingZhenGuValuateAPI(operate,jsonData.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("调用精真估接口出错！", ex);
        }
        return null;
    }
    /**
     * 根据VIN 获取车型的信息
     * @param vin
     * @return
     * [{"MakeId":"18","MakeName":"马自达","ModelId":"4711","ModelName":"马自达CX-4","StyleId":"119691","StyleName":"2.0L 自动 两驱蓝天领先版","StyleYear":"2016","Emission":"2.0","EnvironmentStandard":"国5"},
     * {"MakeId":"18","MakeName":"马自达","ModelId":"4711","ModelName":"马自达CX-4","StyleId":"119692","StyleName":"2.0L 自动 两驱蓝天品位版","StyleYear":"2016","Emission":"2.0","EnvironmentStandard":"国5"},
     * {"MakeId":"18","MakeName":"马自达","ModelId":"4711","ModelName":"马自达CX-4","StyleId":"119693","StyleName":"2.0L 自动 两驱蓝天活力真皮版","StyleYear":"2016","Emission":"2.0","EnvironmentStandard":"国5"},
     * {"MakeId":"18","MakeName":"马自达","ModelId":"4711","ModelName":"马自达CX-4","StyleId":"119694","StyleName":"2.0L 自动 两驱蓝天活力版","StyleYear":"2016","Emission":"2.0","EnvironmentStandard":"国5"}]

     */
    public JSONArray getCarTypesByVin(String vin, ISyncDataCallback callback){
        RestTemplate restTemplate = new RestTemplate();
        String data = null;
        String operate = "VIN";
        try {
            JSONObject jsonData = new JSONObject();
            jsonData.put("VIN",vin);
            return parseBodyAsJsonArray(requestJingZhenGuVINAPI(operate,jsonData.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("调用精真估接口出错！", ex);
        }
        return null;
    }


    /**
     * 调用精真估VIN解析接口
     *
     * @param operate
     * @param jsonDataStr
     * @return
     */
    private String requestJingZhenGuVINAPI(String operate, String jsonDataStr) {
        RestTemplate restTemplate = new RestTemplate();
        String data = null;
        try {
            data = restTemplate.postForObject(jzgServer + "/VINService.ashx", getRequestBody(operate,jsonDataStr), String.class);
            System.out.println("精真估返回原始消息："+data);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("调用精真估VIN解析接口出错！", ex);
        }
        return data;
    }

    private String getRequestBody(String operate, String jsonDataStr){
        try {
            String sequenceId = UUID.randomUUID().toString();
            byte[] bodyEncrypt = EncryUtil.DES3Encrypt(jzgKey, jsonDataStr);
            String bodyBase64 = EncryUtil.BASE64Encrypt(bodyEncrypt);
            String sign = EncryUtil.BASE64Encrypt(EncryUtil.MD5Bytes(sequenceId + jzgPartnerId + operate + bodyBase64 + jzgKey));
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SequenceId", sequenceId);
            jsonBody.put("PartnerId", jzgPartnerId);
            jsonBody.put("Operate", operate);
            jsonBody.put("Sign", sign);
            jsonBody.put("Body", bodyBase64);
            return jsonBody.toString();
        }catch(Exception ex ){
            logger.error("构造精真估接口请求数据出错！",ex);
        }
        return null;
    }
    /**
     * 调用精真估车型库API
     *
     * @param operate
     * @param jsonDataStr
     * @return
     */
    private String requestJingZhenGuDataAPI(String operate, String jsonDataStr) {
        RestTemplate restTemplate = new RestTemplate();
        String data = null;
            try {
                data = restTemplate.postForObject(jzgServer + "/VehicleDataService.ashx", getRequestBody(operate,jsonDataStr), String.class);
                //System.out.println("精真估返回原始消息："+data);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("调用精真估接口出错！", ex);
            }
        return data;
    }


    /**
     * 调用精真估估值服务接口，url有变化
     *
     * @param operate
     * @param jsonDataStr
     * @return
     */
    private String requestJingZhenGuValuateAPI(String operate, String jsonDataStr) {
        RestTemplate restTemplate = new RestTemplate();
        String data = null;
        try {
            //模拟FORM提交
            MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
            mvm.add("Data", getRequestBody(operate,jsonDataStr));
            data = restTemplate.postForObject(jzgServer + "/FinanceDataService.asmx/Operate",mvm, String.class);
            //System.out.println("精真估返回原始消息："+data);
            return data;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("调用精真估接口出错！", ex);
        }
        return data;
    }

    /**
     * 解析返回结果
     * @param json
     * @return
     */
    private JSONArray parseBodyAsJsonArray(String json) {
        JSONArray array = null;
        try {
            if(!json.startsWith("{") || !json.endsWith("}")){
                logger.error("返回的JSON格式串不正确！需要自己处理!\n"+json);
                json = json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);
                System.out.println(json);
            }
            ObjectMapper mapper = new ObjectMapper();
            Map<String,String> bb = (Map) mapper.readValue(json, Map.class);

            //解密后得到的品牌
            String bodyDecrypt = EncryUtil.DES3Decrypt(EncryUtil.BASE64Decrypt((String) bb.get("Body")), jzgKey);
            if(bodyDecrypt != null && !"".equals(bodyDecrypt)){
                array = new JSONArray(bodyDecrypt);
            }
            //System.out.println("解密后的精真估数据返回结果为：\n"+bodyDecrypt);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("调用精真估车型品牌接口出错！", ex);
        }

        return array;
    }

    private JSONObject parseBodyAsJsonObject(String json) {
        JSONObject jsonData = null;
        String bodyDecrypt ="";
        if(json != null && !"".equals(json)){
            try {
                if(!json.startsWith("{") || !json.endsWith("}")){
                    logger.error("返回的JSON格式串不正确！需要自己处理!\n"+json);
                    json = json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);
                    System.out.println(json);
                }
                ObjectMapper mapper = new ObjectMapper();
                Map<String,String> bb = (Map) mapper.readValue(json, Map.class);
                //解密后得到的品牌
                bodyDecrypt = EncryUtil.DES3Decrypt(EncryUtil.BASE64Decrypt((String) bb.get("Body")), jzgKey);
                if (bodyDecrypt != null && !"".equals(bodyDecrypt)) {
                    jsonData = new JSONObject(bodyDecrypt);
                }
                //System.out.println("解密后的精真估数据返回结果为：\n"+bodyDecrypt);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("调用精真估车型品牌接口出错！获取的解密后数据是："+bodyDecrypt, ex);
            }
        }
        return jsonData;
    }

    /**
     * 精真估接口测试
     * @param strs
     */
    public static void main(String[] strs){
        System.out.println(new JingZhenGuServiceImpl().getConfigData("LFPM4APP8H1A09262","CA7202ATE5","2017",null));
        //new JingZhenGuAPI().getMakeData(null);
        //new JingZhenGuAPI().getProvinceData(null);
        //new JingZhenGuServiceImp().getCityData("2",null);
       //new JingZhenGuAPI().getNewCarPrice("100053","2016-05-12","30000","901","3",null);
        //System.out.println(new JingZhenGuServiceImp().getValuationInfo("15238","2016-05-12","30000","2",null));
        //new JingZhenGuAPI().getOldCarCost("15238","2016-05-12","30000","2",null);
       //System.out.println("官方指导价："+new JingZhenGuAPI().getMsrp("15238"));
        //new JingZhenGuServiceImp().getCarTypesByVin("LFPM4APP8H1A09262",null);
    }
}
