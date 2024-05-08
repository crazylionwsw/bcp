package com.fuze.bcp.bd.business;

import com.alibaba.dubbo.rpc.RpcContext;
import com.fuze.bcp.api.bd.bean.CarBrandBean;
import com.fuze.bcp.api.bd.bean.CarModelBean;
import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.api.bd.bean.CarTypeLookupBean;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.bd.domain.CarBrand;
import com.fuze.bcp.bd.domain.CarModel;
import com.fuze.bcp.bd.domain.CarType;
import com.fuze.bcp.bd.service.ICarBrandService;
import com.fuze.bcp.bd.service.ICarModelService;
import com.fuze.bcp.bd.service.ICarTypeService;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.IJingZhenGuService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.impl.JingZhenGuServiceImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CJ on 2017/6/12.
 */
@Service
public class BizCarTypeService implements ICarTypeBizService {

    private static Logger logger = LoggerFactory.getLogger(JingZhenGuServiceImpl.class);

    @Autowired
    ICarBrandService iCarBrandService;

    @Autowired
    ICarModelService iCarModelService;

    @Autowired
    ICarTypeService iCarTypeService;

    @Autowired
    IJingZhenGuService iJingZhenGuService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<DataPageBean<CarBrandBean>> actGetCarBrands(Integer currentPage) {
        return ResultBean.getSucceed().setD(mappingService.map(iCarBrandService.getAll(currentPage), CarBrandBean.class));
    }

    @Override
    public ResultBean<List<CarBrandBean>> actGetCarBrands() {
        return ResultBean.getSucceed().setD(mappingService.map(iCarBrandService.getAll(CarBrand.getSortASC("name")), CarBrandBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupCarBrands() {
        return ResultBean.getSucceed().setD(mappingService.map(iCarBrandService.getAvaliableAll(CarBrand.getSortASC("name")), APILookupBean.class));
    }

    @Override
    public ResultBean<CarBrandBean> actSaveCarBrand(CarBrandBean carBrandBean) {
        CarBrand carBrand = iCarBrandService.save(mappingService.map(carBrandBean, CarBrand.class));
        return ResultBean.getSucceed().setD(mappingService.map(carBrand, CarBrandBean.class));
    }

    @Override
    public ResultBean<CarBrandBean> actDeleteCarBrand(String carBrandId) {

        CarBrand carBrand = iCarBrandService.getOne(carBrandId);
        if (carBrand != null) {
            if (carBrand.getDataStatus() == DataStatus.SAVE) {
                carBrand = iCarBrandService.delete(carBrandId);
            } else if (carBrand.getDataStatus() == DataStatus.DISCARD) {
                //删除当前品牌
                carBrand = iCarBrandService.delete(carBrandId);
                //获取该品牌下所有车系
                List<CarModel> carModels = iCarModelService.getAllByCarBrand(carBrandId, CarModel.getSortASC("name"));
                if (carModels != null) {
                    List<String> models = new ArrayList<String>();
                    for (CarModel model : carModels) {
                        models.add(model.getId());
                        List<CarType> carTypes = iCarTypeService.getAllByCarModel(model.getId(), CarType.getSortASC("name"));
                        if (carTypes != null) {
                            List<String> types = new ArrayList<String>();
                            for (CarType type : carTypes) {
                                types.add(type.getId());
                            }
                            iCarTypeService.deleteRealByIds(types);
                        }
                    }
                    iCarModelService.deleteRealByIds(models);
                }

            }
            return ResultBean.getSucceed().setD(mappingService.map(carBrand, CarModelBean.class));
        }

        return ResultBean.getFailed();
    }


    @Override
    public ResultBean<CarBrandBean> actGetCarBrandById(String id) {
        CarBrand carBrand = iCarBrandService.getOne(id);
        if (carBrand != null) {
            return ResultBean.getSucceed().setD(mappingService.map(carBrand, CarBrandBean.class));
        }
        return ResultBean.getFailed();

    }

    @Override
    public ResultBean<DataPageBean<CarModelBean>> actGetCarModels(Integer currentPage) {
        PageRequest page = new PageRequest(currentPage, 100, CarType.getSortASC("name"));
        Page<CarModel> carModels = iCarModelService.getAll(page);
        return ResultBean.getSucceed().setD(mappingService.map(carModels, CarModelBean.class));
    }

    @Override
    public ResultBean<DataPageBean<CarModelBean>> actGetCarModels(Integer currentPage, String carBrandId) {
        CarBrand carBrand = iCarBrandService.getOne(carBrandId);

        if (carBrand != null) {
            Page<CarModel> carModels = iCarModelService.getAllByCarBrand(carBrandId, currentPage);

            return ResultBean.getSucceed().setD(mappingService.map(carModels, CarModelBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<CarModelBean>> actGetCarModels(String carBrandId) {
        CarBrand carBrand = iCarBrandService.getOne(carBrandId);

        if (carBrand != null) {
            List<CarModel> carModels = iCarModelService.getAllByCarBrand(carBrandId, CarModel.getSortASC("name"));
            return ResultBean.getSucceed().setD(mappingService.map(carModels, CarModelBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupCarModels() {
        return ResultBean.getSucceed().setD(mappingService.map(iCarModelService.getAvaliableAll(), APILookupBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupCarModels(String carBrandId) {
        CarBrand carBrand = iCarBrandService.getOne(carBrandId);
        if (carBrand != null) {
            List<CarModel> carModels = iCarModelService.getLookupsByCarBrand(carBrandId);
            return ResultBean.getSucceed().setD(mappingService.map(carModels, APILookupBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CarModelBean> actSaveCarModel(CarModelBean carModelBean) {

        CarModel carModel = mappingService.map(carModelBean, CarModel.class);

        CarBrand carBrand = iCarBrandService.getOne(carModel.getCarBrandId());
        carModel.setFullName(String.format("%s %s", carBrand.getName(), carModel.getName()));
        carModel = iCarModelService.save(carModel);
        if (carModel != null) {
            carModelBean = mappingService.map(carModel, CarModelBean.class);

            return ResultBean.getSucceed().setD(carModelBean);
        }
        return ResultBean.getFailed();

    }

    @Override
    public ResultBean<CarModelBean> actDeleteCarModel(String carModelId) {

        CarModel carModel = iCarModelService.getOne(carModelId);

        if (carModel != null) {
            if (carModel.getDataStatus() == DataStatus.SAVE) {
                carModel = iCarModelService.delete(carModelId);
            } else if (carModel.getDataStatus() == DataStatus.DISCARD) {
                carModel = iCarModelService.delete(carModelId);
                List<CarType> carTypes = iCarTypeService.getAllByCarModel(carModelId, CarType.getSortASC("name"));
                if (carTypes != null) {
                    List<String> typeIds = new ArrayList<String>();
                    for (CarType type : carTypes) {
                        typeIds.add(type.getId());
                    }
                    iCarTypeService.deleteRealByIds(typeIds);
                }
            }
            return ResultBean.getSucceed().setD(mappingService.map(carModel, CarModelBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CarModelBean> actGetCarModelById(String id) {
        CarModel carModel = iCarModelService.getOne(id);
        if (carModel != null) {
            CarModelBean carModelBean = mappingService.map(carModel, CarModelBean.class);
            return ResultBean.getSucceed().setD(carModelBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<CarTypeBean>> actGetCarTypes(Integer currentPage) {
        PageRequest page = new PageRequest(currentPage, 100, CarType.getSortASC("name"));
        Page<CarType> carTypes = iCarTypeService.getAll(page);
        return ResultBean.getSucceed().setD(mappingService.map(carTypes, CarTypeBean.class));
    }

    @Override
    public ResultBean<DataPageBean<CarTypeBean>> actGetCarTypes(Integer currentPage, String carModelId) {

        CarModel carModel = iCarModelService.getOne(carModelId);

        if (carModel != null) {
            Page<CarType> carTypes = iCarTypeService.getAllByCarModel(carModelId, currentPage);
            return ResultBean.getSucceed().setD(mappingService.map(carTypes, CarTypeBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<CarTypeBean>> actGetCarTypes(String carModelId) {

        CarModel carModel = iCarModelService.getOne(carModelId);

        if (carModel != null) {
            List<CarType> carTypes = iCarTypeService.getAllByCarModel(carModelId, CarType.getSortASC("name"));
            return ResultBean.getSucceed().setD(mappingService.map(carTypes, CarTypeBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupCarTypes() {
        List<CarType> carTypes = iCarTypeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(carTypes, APILookupBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupCarTypes(String carModelId) {
        CarModel carModel = iCarModelService.getOne(carModelId);
        if (carModel != null) {
            List<CarType> carTypes = iCarTypeService.getAllByCarModel(carModelId, CarType.getSortASC("name"));
            return ResultBean.getSucceed().setD(mappingService.map(carTypes, APILookupBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<CarTypeLookupBean>> actLookupCarTypes(List<String> carModelIds) {
        //获取车系数据
        List<CarModel> carModels = iCarModelService.getAvaliableList(carModelIds);

        //获取车型数据
        List<CarType> carTypes = iCarTypeService.getAllByCarModels(carModelIds);

        List<CarTypeLookupBean> returnCarTypes = new ArrayList<CarTypeLookupBean>();
        for (CarModel carModel : carModels) {
            for (CarType carType : carTypes) {
                if (carType.getCarModelId().equals(carModel.getId())) {

                    CarTypeLookupBean lookupBean = mappingService.map(carType, CarTypeLookupBean.class);
                    lookupBean.setCarModel(carModel.getName());
                    returnCarTypes.add(lookupBean);
                }
            }
        }

        return ResultBean.getSucceed().setD(returnCarTypes);
    }

    @Override
    public ResultBean<CarTypeBean> actSaveCarType(CarTypeBean carTypeBean) {
        CarType carType = mappingService.map(carTypeBean, CarType.class);

        //获取车系
        CarModel carModel = iCarModelService.getOne(carType.getCarModelId());
        //设置全称
        carType.setFullName(String.format("%s %s", carModel.getFullName(), carType.getName()));
        carType = iCarTypeService.save(carType);
        if (carType != null) {
            carTypeBean = mappingService.map(carType, CarTypeBean.class);
            return ResultBean.getSucceed().setD(carTypeBean);
        }
        return ResultBean.getFailed();

    }

    @Override
    public ResultBean<CarTypeBean> actDeleteCarType(String carTypeId) {

        CarType carType = iCarTypeService.getOne(carTypeId);

        if (carType != null) {
            carType = iCarTypeService.delete(carTypeId);
            return ResultBean.getSucceed().setD(mappingService.map(carType, CarTypeBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CarTypeBean> actGetCarTypeById(String id) {
        CarType carType = iCarTypeService.getOne(id);
        if (carType != null) {
            CarTypeBean carTypeBean = mappingService.map(carType, CarTypeBean.class);
            return ResultBean.getSucceed().setD(carTypeBean);
        }
        return ResultBean.getFailed();

    }

    @Override
    public ResultBean<List<CarTypeBean>> actGetCarTypeByCarBrand(String carBrandId) {
        CarBrand carBrand = iCarBrandService.getOne(carBrandId);
        if (carBrand != null) {
            List<CarType> carTypes = iCarTypeService.getAllByCarBrand(carBrandId);
            return ResultBean.getSucceed().setD(mappingService.map(carTypes, CarTypeBean.class));
        }
        return ResultBean.getFailed();
    }

    /**
     * 定时更新车型库
     * 时间设置为23:00
     */
    @Scheduled(cron = "0 00 20 * * ?")
    public void startTimeTask() {
        System.out.println("执行更新车型库时间：" + new Date(System.currentTimeMillis()));
        logger.info("执行更新车型库时间：" + new Date(System.currentTimeMillis()));
        List<CarBrand> brandList = iCarBrandService.getAvaliableAll();
        for (CarBrand cd : brandList) {
            try {
                if (cd.getId() != null) {
                    this.actSyncCarBrand(cd.getId(), "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public ResultBean<String> actSyncCarBrand(String carBrandId, String userId) throws Exception {
        String operatorId = RpcContext.getContext().getAttachment("OperatorId");
        CarBrand carBrand = iCarBrandService.getOne(carBrandId);
        try {
            JSONArray modelArray = iJingZhenGuService.getModelData(carBrand.getRefMakeId(), null);
            List<CarModel> carModels = compareCarModel(modelArray, carBrand);
            carModels = iCarModelService.save(carModels);
            List<CarModelBean> cModel = mappingService.map(carModels, CarModelBean.class);
            if (carModels != null && carModels.size() > 0) {
                int carModelsCount = carModels.size();
                Long carTypesCount = syncCarType(carModels);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("models", carModelsCount);
                map.put("types", carTypesCount);
                map.put("carModelList", cModel);
                logger.info("此次操作同步成功车系--" + carModelsCount + "条.");
            }
        } catch (Exception ex) {
            logger.error("同步车系出错！", ex);
            throw ex;
        }
        return ResultBean.getSucceed().setD(operatorId);
    }

    @Override
    public ResultBean<CarTypeBean> actGetCarTypeByRefStyleId(String refStyleId) {
        CarType carType = iCarTypeService.getOneByRefId(refStyleId);
        return ResultBean.getSucceed().setD(mappingService.map(carType, CarTypeBean.class));
    }

    @Override
    public ResultBean<CarBrandBean> actGetCarBrandByRefMakeId(String refMakeId) {
        CarBrand carBrand = iCarBrandService.getOneByRefId(refMakeId);
        return ResultBean.getSucceed().setD(mappingService.map(carBrand, CarBrandBean.class));
    }

    @Override
    public ResultBean<CarModelBean> actGetCarModelByRefModelId(String refModelId) {
        CarModel carModel = iCarModelService.getOneByRefId(refModelId);
        return ResultBean.getSucceed().setD(mappingService.map(carModel, CarModelBean.class));
    }

    @Override
    public ResultBean<List<CarBrandBean>> actGetCarBrandAll() {
        List<CarBrand> list = iCarBrandService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(list, CarBrandBean.class));
    }

    @Override
    public ResultBean<List<CarBrandBean>> actGetCarBrandList(List<String> carBrandIds) {
        List<CarBrand> list = iCarBrandService.getAvaliableList(carBrandIds);
        return ResultBean.getSucceed().setD(mappingService.map(list, CarBrandBean.class));
    }

    /**
     * 同步车型
     *
     * @return
     */
    public Long syncCarType(List<CarModel> carModels) {
        /**ljq
         */
        long countA = 0;
        long countB = 0;
        //SyncLog syncLog = new SyncLog();
        //syncLog.setDataType("车型");
        //syncLog.setLocalCount(iCarTypeService.countCarTypes().getD());
        List<String> list = new ArrayList<String>();
        ArrayList modeIdList = new ArrayList();
        /***/
        long sysnCount = 0;
        List<CarType> carTypes = new ArrayList<CarType>();
        if (carModels == null)
            carModels = iCarModelService.getAll();

        for (CarModel carModel : carModels) {
            try {
                JSONArray styleArray = iJingZhenGuService.getTypeData(carModel.getRefModelId(), null);
                carTypes.addAll(compareCarTypes(styleArray, carModel));
            } catch (Exception ex) {
                logger.error("获取车型信息出错！", ex);
                modeIdList.add(carModel.getRefModelId());
            }
            if (carTypes.size() >= 1000) {
                countA = sysnCount + (long) carModels.size();
                iCarTypeService.save(carTypes);
                carTypes.clear();
            }
        }
        if (carTypes.size() > 0) {
            countB = sysnCount + (long) carTypes.size();
            iCarTypeService.save(carTypes);
            logger.info("此次同步操作共同步成功车型--" + carTypes.size() + "条.");
        }
        /**ljq
         */
/*        sysnCount = countA + countB;
        syncLog.setFailedIDs(modeIdList);
        syncLog.setRemoteCount(sysnCount);
        if (syncLog.getFailedIDs().size() != 0) {
            syncLog.setResult(ResultCode.FAILED);
        } else {
            syncLog.setResult(ResultCode.SUCCEED);
        }
        syncRepository.save(syncLog);*/
        /***/
        return sysnCount;
    }

    /**
     * 比较现有车型
     *
     * @param styleArray
     * @param carModel
     * @return
     * @throws JSONException
     */
    private List<CarType> compareCarTypes(JSONArray styleArray, CarModel carModel) throws JSONException {
        List<CarType> carTypes = new ArrayList<CarType>();
        if (styleArray != null) {
            for (int k = 0; k < styleArray.length(); k++) {
                JSONObject jsonObjectStyle = (JSONObject) styleArray.get(k);
                String styleId = jsonObjectStyle.getString("Value");
                try {
                    //检查车系是否存在
                    CarType carType = iCarTypeService.getOneByRefId(styleId);
                    if (carType == null) {
                        carType = new CarType();
                    }
                    String styleName = jsonObjectStyle.getString("Text");
                    String groupName = jsonObjectStyle.getString("GroupName");
                    carType.setRefStyleId(styleId);

                    String name = String.format("%s %s", groupName, styleName);
                    carType.setName(name);
                    carType.setFullName(String.format("%s %s", carModel.getFullName(), name)); //保存车型全称（品牌名+车系名+车型名)
                    carType.setGroupName(groupName);
                        /*通过正则表达式解析车型名称*/
                    //取得排量
                    Pattern regex = Pattern.compile("\\d\\.\\d[TL]", Pattern.MULTILINE);
                    Matcher regexMatcher = regex.matcher(jsonObjectStyle.getString("Text"));
                    String s = regexMatcher.replaceAll("");
                    String ml = jsonObjectStyle.getString("Text").replace(s, "");
                    carType.setMl(ml);

                    //取得动车类型
                    String regex1 = "柴油";
                    String regex2 = "电动";
                    Pattern compile1 = Pattern.compile(regex1, Pattern.MULTILINE);
                    Pattern compile2 = Pattern.compile(regex2, Pattern.MULTILINE);
                    boolean b1 = compile1.matcher(jsonObjectStyle.getString("Text")).find();
                    boolean b2 = compile2.matcher(jsonObjectStyle.getString("Text")).find();
                    if (b1) {
                        carType.setMotiveType(CarTypeBean.MOTIVE_TYPE_DIESEL);
                    }
                    if (b2) {
                        carType.setMotiveType(CarTypeBean.MOTIVE_TYPE_POWER);
                    }
                    if (!b1 && !b2) {
                        carType.setMotiveType(CarTypeBean.MOTIVE_TYPE_FUEL);
                    }
                    carType.setCarModelId(carModel.getId());
                    carType.setCarBrandId(carModel.getCarBrandId());
                    carType.setDataStatus(DataStatus.SAVE);
                    //获取新车价格
                    carType.setPrice(iJingZhenGuService.getMsrp(carType.getRefStyleId()));
                    carTypes.add(carType);
                } catch (Exception dataex) {
                    /**保存异常数据ID*/
                    dataex.printStackTrace();
                    logger.error("获取车型数据出现异常！", dataex);
                }
            }
        }
        return carTypes;
    }


    /**
     * 根据JSONARRAY比较库里的车系
     *
     * @param
     * @return
     */
    private List<CarModel> compareCarModel(JSONArray modelArray, CarBrand carBrand) throws JSONException {
        List<CarModel> carModels = new ArrayList<CarModel>();
        if (modelArray != null) {
            for (int j = 0; j < modelArray.length(); j++) {
                JSONObject jsonObjectModel = (JSONObject) modelArray.get(j);
                String modelId = jsonObjectModel.getString("Value");
                //检查车系是否存在
                CarModel carModel = iCarModelService.getOneByRefId(modelId);
                try {
                    if (carModel == null) { //不存在，创建新车系
                        carModel = new CarModel();
                    }
                    carModel.setDataStatus(DataStatus.SAVE);
                    carModel.setGroupName(jsonObjectModel.getString("GroupName"));
                    carModel.setName(jsonObjectModel.getString("Text"));
                    carModel.setFullName(String.format("%s %s", carBrand.getName(), carModel.getName()));
                    carModel.setRefModelId(modelId);
                    carModel.setCarBrandId(carBrand.getId());
                } catch (Exception ex) {
                    logger.error("该车系同步出错:" + jsonObjectModel.getString("GroupName") + "--RefModelId为:" + carModel.getRefModelId(), ex);
                }
                carModels.add(carModel);
            }
        }
        return carModels;
    }
}
