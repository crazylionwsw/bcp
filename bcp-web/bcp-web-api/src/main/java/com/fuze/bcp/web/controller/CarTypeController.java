package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.bd.bean.CarBrandBean;
import com.fuze.bcp.api.bd.bean.CarModelBean;
import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.web.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/json")
public class CarTypeController extends BaseController{

    @Autowired
    private ICarTypeBizService iCarTypeBizService;

    /*******************************************************品牌*******************************************************/

    /**
     * 获取车辆品牌信息列表(不带分页)
     *
     * @return
     */
    @RequestMapping(value = "/carbrands", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarBrands() {
        return iCarTypeBizService.actGetCarBrands();
    }


    /**
     * 获取车辆品牌信息(仅可用数据)
     *
     * @return
     */
    @RequestMapping(value = "/carbrands/lookups", method = RequestMethod.GET)
    public ResultBean lookupCarBrands() {
        return iCarTypeBizService.actLookupCarBrands();
    }


    /**
     * 保存车辆品牌信息
     *
     * @param carBrandBean
     * @return
     */
    @RequestMapping(value = "/carbrand", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCarBrand(@RequestBody CarBrandBean carBrandBean) {
        return iCarTypeBizService.actSaveCarBrand(carBrandBean);
    }


    /**
     * 删除车辆品牌信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/carbrand/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCarBrand(@PathVariable("id") String id) {
        return iCarTypeBizService.actDeleteCarBrand(id);
    }


    /**
     * 获取车辆品牌信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/carbrand/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarBrandById(@PathVariable("id") String id) {
        return iCarTypeBizService.actGetCarBrandById(id);
    }

    /**
     * 获取特定品牌车系列表
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/carbrand/{id}/carmodels", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarModels(@PathVariable("id") String id) {
        return iCarTypeBizService.actGetCarModels(id);
    }

    /**
     * 获取特定品牌车系列表(可用数据)
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/carbrand/{id}/carmodels/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupCarModels(@PathVariable("id") String id) {
        return iCarTypeBizService.actLookupCarModels(id);
    }


    /**
     * 获取车系列表，（只返回可用数据）
     *
     * @return
     */
    @RequestMapping(value = "/carmodels/lookups", method = RequestMethod.GET)
    public ResultBean lookupCarModels() {
        return iCarTypeBizService.actLookupCarModels();
    }


    /**
     * 保存车系信息
     *
     * @param carModelBean
     * @return
     */
    @RequestMapping(value = "/carmodel", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCarModel(@RequestBody CarModelBean carModelBean) {
        return iCarTypeBizService.actSaveCarModel(carModelBean);
    }


    /**
     * 删除车系信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/carmodel/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCarModel(@PathVariable("id") String id) {
        return iCarTypeBizService.actDeleteCarModel(id);
    }


    /**
     * 获取单条车系信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/carmodel/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarModelById(@PathVariable("id") String id) {
        return iCarTypeBizService.actGetCarModelById(id);
    }

    /**
     * 根据车系获取车型信息列表(带分页)
     *
     * @param currentPage
     * @param id
     * @return
     */
    @RequestMapping(value = "/carmodel/{id}/cartypes", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarTypes(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage, @PathVariable("id") String id) {
        return iCarTypeBizService.actGetCarTypes(currentPage, id);
    }

    /**
     * 根据车系获取车型信息列表(带分页)
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/carmodel/{id}/cartypes/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupCarTypes(@PathVariable("id") String id) {
        return iCarTypeBizService.actLookupCarTypes(id);
    }


    /**
     * 获取车型列表（只可用数据）
     *
     * @return
     */
    @RequestMapping(value = "/cartypes/lookups", method = RequestMethod.GET)
    public ResultBean lookupCarTypes() {
        return iCarTypeBizService.actLookupCarTypes();
    }

    /**
     * 获取车型列表（只可用数据）
     *
     * @return
     */
    @RequestMapping(value = "/cartypes/lookups", method = RequestMethod.POST)
    public ResultBean lookupCarTypesByCarModelIds(@RequestBody List<String> carModelIds) {
        return iCarTypeBizService.actLookupCarTypes(carModelIds);
    }

    /**
     * 根据品牌查车型
     *
     * @return
     */
    @RequestMapping(value = "/carbrand/{id}/cartypes", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarTypeByCarBrand(@PathVariable("id") String id) {
        return iCarTypeBizService.actGetCarTypeByCarBrand(id);
    }


    /**
     * 保存车型信息
     *
     * @param carTypeBean
     * @return
     */
    @RequestMapping(value = "/cartype", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCarType(@RequestBody CarTypeBean carTypeBean) {
        return iCarTypeBizService.actSaveCarType(carTypeBean);
    }


    /**
     * 删除车型信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cartype/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCarType(@PathVariable("id") String id) {
        return iCarTypeBizService.actDeleteCarType(id);
    }

    /**
     * 获取特定车型信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cartype/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarTypeById(@PathVariable("id") String id) {
        return iCarTypeBizService.actGetCarTypeById(id);
    }

    Logger logger = LoggerFactory.getLogger(CarTypeController.class);

    @Autowired
    IAmqpBizService iAmqpBizService;

    /**
     * 同步车型数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/carbrand/{id}/sync", method = RequestMethod.GET)
    public ResultBean syncCarbrand(@PathVariable("id") String id) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        String userId = jwtUser.getId();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    iCarTypeBizService.actSyncCarBrand(id, userId).getD();
                } catch (Exception e) {
                    logger.error("同步车型信息错误", e);
                } finally {
                    iAmqpBizService.actSendWebsocketMq(userId, "CARTYPE");
                }
            }
        });
        thread.start();
        return ResultBean.getSucceed();
    }

}
