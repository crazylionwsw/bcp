package com.fuze.bcp.web.controller;

/**
 * Created by GQR on 2017/8/19.
 */
import com.fuze.bcp.api.creditcar.bean.PickupCarBean;
import com.fuze.bcp.api.creditcar.service.IPickupCarBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 提车记录
 */

@RestController
@RequestMapping(value = "/json")
public class PickupCarController {

    private static final Logger logger = LoggerFactory.getLogger(PickupCarController.class);

    @Autowired
    private IPickupCarBizService iPickupCarBizService;

    /**
     * 分页列表
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/pickupcars", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PickupCarBean> getPageData(
            @RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage) {
        return iPickupCarBizService.actGetPickupCars(currentPage);
    }

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/pickupcar/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PickupCarBean> getOne(@PathVariable("id") String id) {
        return iPickupCarBizService.actGetPickupCar(id);
    }

    /**
     *      模糊  查询
     * @param currentPage
     * @param customerBean
     * @return
     */
    @RequestMapping(value = "/pickupcars/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<PickupCarBean> searchCarDemands(
            @RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
            @RequestBody CustomerBean customerBean) {
        return iPickupCarBizService.actSearchPickupCars(currentPage,customerBean);
    }

    /**
     * 保存 提车记录信息
     */
    @RequestMapping(value = "/pickupcar", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<PickupCarBean> save(@RequestBody PickupCarBean pickupCarBean) {
        return iPickupCarBizService.actSavePickupCar(pickupCarBean);
    }

    /**
     *      根据     客户交易ID       查询提车记录信息
     * @param id    客户交易ID
     * @return
     */
    @RequestMapping(value = "/pickupcar/transaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PickupCarBean> getOneByTransactionId(@PathVariable("id") String id) {
        return iPickupCarBizService.actGetByCustomerTransactionId(id);
    }
}
