package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.bean.DealerEmployeeBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.bean.DealerGroupBean;
import com.fuze.bcp.api.cardealer.bean.DealerSharingRatioBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.statistics.service.IBalanceAccountBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/json")
public class CarDealerController {

    @Autowired
    private ICarDealerBizService iCarDealerBizService;

    @Autowired
    private IOrgBizService iOrgBizService;

    @Autowired
    private ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    private IBalanceAccountBizService iBalanceAccountBizService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    /**
     * 保存4S店经销商信息
     *
     * @param carDealerBean
     * @return
     */
    @RequestMapping(value = "/cardealer", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCarDealer(@RequestBody CarDealerBean carDealerBean) {
        return iCarDealerBizService.actSaveCarDealer(carDealerBean);
    }

    /**
     * 删除4S店经销商信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cardealer/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCarDealer(@PathVariable("id") String id) {
        return iCarDealerBizService.actDeleteCarDealer(id);
    }


    /**
     * 获取一条4S店经销商信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cardealer/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarDealer(@PathVariable("id") String id) {
        return iCarDealerBizService.actGetOneCarDealer(id);
    }

    /**
     *      审查、审批
     */
    @RequestMapping(value = "/cardealer/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CarDealerBean> sign(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iCarDealerBizService.actSignCarDealer(id, signInfo);
    }

    /**
     *  通过  经销商ID  查询 经销商的渠道报单行
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cardealer/{id}/cashsource", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CashSourceBean> getCashSource(@PathVariable("id") String id) {
        return iCarDealerBizService.actFindCashSource(id);
    }

    /**
     *  通过  经销商ID  查询 经销商的渠道合作行
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cardealer/{id}/cooperationCashsource", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CashSourceBean> getCooperationCashSource(@PathVariable("id") String id) {
        return iCarDealerBizService.actFindCooperationCashSource(id);
    }

    /**
     * 获取4S店经销商信息(带分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/cardealers/page", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarDealers(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iCarDealerBizService.actGetCarDealers(currentPage);
    }

    /**
     * 获取经销商列表
     * @return
     */
    @RequestMapping(value = "/cardealers", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<CarDealerBean>> getAllCarDealers() {
        return iCarDealerBizService.actGetCarDealers();
    }


    /**
     * 保存4S店经销商员工信息
     *
     * @param dealerEmployeeBean
     * @return
     */
    @RequestMapping(value = "/cardealer/employee", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean actSaveDealerEmployee(@RequestBody DealerEmployeeBean dealerEmployeeBean) {
        return iCarDealerBizService.actSaveDealerEmployee(dealerEmployeeBean);
    }


    /**
     * 删除4S店经销商员工信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cardealer/employee/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean actDeleteDealerEmployee(@PathVariable("id") String id) {
        return iCarDealerBizService.actDeleteDealerEmployee(id);
    }


    /**
     * 获取单条4S店经销商员工信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cardealer/employee/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetOneDealerEmployee(@PathVariable("id") String id) {
        return iCarDealerBizService.actGetOneDealerEmployee(id);
    }


    /**
     * 获取指定4S店的员工列表
     *
     * @param currentPageCarDealer
     * @param id
     * @return
     */
    @RequestMapping(value = "/cardealer/{id}/employees", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetDealerEmployees(@RequestParam(value = "currentPageCarDealer", defaultValue = "0") Integer currentPageCarDealer, @PathVariable("id") String id) {
        return iCarDealerBizService.actGetDealerEmployees(currentPageCarDealer, id);
    }

    /**
     * 获取4S店经销商员工信息(仅可用)
     *
     * @return
     */
    @RequestMapping(value = "/cardealer/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupCarDealers() {
        return iCarDealerBizService.actLookupCarDealer();
    }


    @RequestMapping(value = "/cardealers/all",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<CarDealerBean>> getCarDealers(){
        return iCarDealerBizService.actCarDealers();
    }



    @RequestMapping(value = "/cardealer/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean searchCarDealer(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,@RequestBody CarDealerBean carDealerBean){
        return iCarDealerBizService.actSearchCarDealers(currentPage,carDealerBean);
    }

    @RequestMapping(value = "/cardealer",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean checkAuditPermission(@RequestParam(value = "orginfoId", defaultValue = "0") String orginfoId,@RequestParam(value = "loginEmployeeId", defaultValue = "0") String loginEmployeeId){
        return iCarDealerBizService.actCheckAuditPermission(orginfoId,loginEmployeeId);
    }

    /**
     *经销商的在办业务数
     */
    @RequestMapping(value = "/cardealer/transaction/{id}",method = RequestMethod.GET)
    @ResponseBody
    public  ResultBean getCountTransactionByCardealerId(@PathVariable("id") String cardealerId){
        return iCustomerTransactionBizService.actCountTransactionByCardealerId(cardealerId);
    }

    @RequestMapping(value = "/cardealer/sharingratio", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveSharingRatio(@RequestBody DealerSharingRatioBean dealerSharingRatio) {
        ResultBean<DealerSharingRatioBean> result = iCarDealerBizService.actSaveDealerSharingRatio(dealerSharingRatio);
        return result;
    }

    @RequestMapping(value = "/cardealer/{id}/sharingratio",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DealerSharingRatioBean> getSharingRatio(@PathVariable("id") String id){
        ResultBean<DealerSharingRatioBean> result = iCarDealerBizService.actGetDealerSharingRatio(id);
        return result;
    }


    @RequestMapping(value = "/cardealer/groups",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDealerGroups(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage){
        return iCarDealerBizService.actGetDealerGroups(currentPage);
    }

    @RequestMapping(value = "/cardealer/group", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveDealerGroup(@RequestBody DealerGroupBean dealerGroupBean) {
        ResultBean<DealerGroupBean> result = iCarDealerBizService.actSaveDealerGroup(dealerGroupBean);
        return result;
    }

    /**
     * 删除渠道分组
     * @param id
     * @return
     */
    @RequestMapping(value = "/cardealer/group/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteDealerGroup(@PathVariable("id") String id) {
        return iCarDealerBizService.actDeleteDealerGroup(id);
    }


    @RequestMapping(value = "/cardealer/group/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDealerGroup(@PathVariable("id") String id) {
        return iCarDealerBizService.actGetDealerGroup(id);
    }

    /**
     * 获取渠道分组(仅可用)
     * @return
     */
    @RequestMapping(value = "/cardealer/groups/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupDealerGroups() {
        return iCarDealerBizService.actLookupDealerGroup();
    }


    //获取所有经销商员工
    @RequestMapping(value = "/cardealer/employee/avaliable",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actAvaliableEmployees(){
        return  iCarDealerBizService.actGetAvaliableDealerEmployee();
    }

    @RequestMapping(value = "/cardealer/employeesub/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOneCardealerEmployee(@PathVariable("id") String id){
        return iCarDealerBizService.actGetOneDealerEmployeeById(id);
    }

    //获取多个经销商
    @RequestMapping(value = "/cardealer/more",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getCarDealers(@RequestBody List<String> tids){
        return iCarDealerBizService.actGetCarDealer(tids);
    }

    //渠道业务转移
    @RequestMapping(value = "/cardealer/transfer",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getCardealerTransfer(@RequestParam String bid,@RequestBody List<String> tids){
        ResultBean<CarDealerBean> carDealerBeanResultBean = iCarTransactionBizService.actGetCardealerTransfer(bid, tids);
        iBalanceAccountBizService.actCardealerTransferWithStatistics(bid, tids);
        return carDealerBeanResultBean;
    }

}
