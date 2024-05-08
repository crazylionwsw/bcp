package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.bd.bean.DealerEmployeeBean;
import com.fuze.bcp.api.bd.bean.PayAccountBean;
import com.fuze.bcp.api.cardealer.bean.*;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.bean.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/json")
public class CarDealerController {

    @Autowired
    private ICarDealerBizService iCarDealerBizService;

    @Autowired
    private IOrgBizService iOrgBizService;

    /**
     * 【PAD API】--获取渠道经理下分期经理信息接口
     *
     * @param loginUserId
     * @return
     */
    @RequestMapping(value = "/user/{loginUserId}/employees", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetDealerEmpyees(@PathVariable("loginUserId") String loginUserId) {
        return iOrgBizService.actGetEmployeesList(loginUserId);
    }

    @RequestMapping(value = "/cardealer/query", method = RequestMethod.GET)
    public ResultBean<List<CarDealerListBean>> actGetDealerQuery(@RequestParam("inputstr") String inputStr) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        return iCarDealerBizService.actGetCarDealerQuery(jwtUser.getId(), inputStr);
    }

    /**
     * 【PAD - API】--保存渠道员工信息接口
     * 获取指定4S店的员工列表
     *
     * @param cardealerid
     * @return
     */
    @RequestMapping(value = "/cardealer/{cardealerid}/employee", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean actGetDealerEmployees(@PathVariable("cardealerid") String cardealerid, @RequestBody DealerEmployeeBean dealerEmployeeBean) {
        if (dealerEmployeeBean == null) {
            return ResultBean.getFailed().setM("员工数据为空");
        }
        if (StringUtils.isBlank(dealerEmployeeBean.getId())) {
            dealerEmployeeBean.setId(null);
        }
        dealerEmployeeBean.setCarDealerId(cardealerid);
        ResultBean resultBean = iCarDealerBizService.actSaveDealerEmployee(dealerEmployeeBean);
        if (resultBean.isSucceed()) {
            dealerEmployeeBean = (DealerEmployeeBean) resultBean.getD();
            if (dealerEmployeeBean != null) {
                return ResultBean.getSucceed().setD(dealerEmployeeBean).setM("渠道员工信息保存成功！");
            }
        }
        return resultBean;
    }


    /**
     * 【PAD API】--根据渠道Id得到销售人员列表
     * 获取指定4S店的员工列表
     *
     * @param cardealerid
     * @return
     */
    @RequestMapping(value = "/cardealer/{cardealerid}/employees", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetDealerEmployees(@PathVariable("cardealerid") String cardealerid) {
        return iCarDealerBizService.actGetAllDealerEmployees(cardealerid);
    }

    /**
     * 【PAD - API】--保存渠道信息接口
     *
     * @return
     */
    @RequestMapping(value = "/cardealer", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCardealer(@RequestBody CarDealerSubmissionBean submissionBean) {
        if (submissionBean == null) {
            return ResultBean.getFailed().setM("保存渠道错误，请重试!");
        }
        //TODO MESSAGESERVICE
        ResultBean resultBean = iCarDealerBizService.actSaveCarDealerSubmission(submissionBean);
        if (resultBean.isSucceed()) {
            return resultBean.setM("保存渠道成功！");
        } else {
            return resultBean;
        }
    }


    /**
     * 【PAD API】-- 分页获取渠道接口
     *
     * @return
     */
    @RequestMapping(value = "/user/{loginuserid}/cardealers", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DataPageBean<CarDealerListBean>> getCarDealers(@PathVariable("loginuserid") String loginUserId, @RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pageSize) {
        return iCarDealerBizService.actGetCarDealersPageByLoginUserId(pageIndex, pageSize, loginUserId);
    }

    /**
     * 【PAD API】-- 获取我的可用渠道列表接口
     *
     * @param loginUserId
     * @param ctype
     * @return
     */
    @RequestMapping(value = "/user/{loginuserid}/mycardealers", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<CarDealerListBean>> getMyCarDealers(@PathVariable("loginuserid") String loginUserId, @RequestParam(value = "ctype") String ctype) {
        return iCarDealerBizService.actGetMyCarDealers(loginUserId, ctype);
    }


    /**
     * 【PAD API】-- 获取单个渠道接口
     *
     * @param cardealerId 渠道id
     * @return
     */
    @RequestMapping(value = "/cardealer/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOneCarDealer(@PathVariable("id") String cardealerId) {
        return iCarDealerBizService.actGetOneCarDealer(cardealerId);
    }

    /**
     * 【PAD - API】--获取渠道销售政策
     *
     * @param cardealerId 渠道id
     * @return
     */
    //移到service里
    @RequestMapping(value = "/cardealer/{cardealerid}/salepolicy/{stype}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarDealerRateTypes(@PathVariable("cardealerid") String cardealerId, @PathVariable(value = "stype") String stype) {
        stype = stype.toUpperCase();
        CarDealerBean bean = iCarDealerBizService.actGetOneCarDealer(cardealerId).getD();
        if (bean == null) {
            return ResultBean.getFailed().setM("渠道信息为空，请重试！");
        }
        if (bean.getDealerRateTypes() == null) {
            return ResultBean.getFailed().setM("未设置渠道销售政策为，请重试！");
        }


        CarDealerRateBean carDealerRate = new CarDealerRateBean();
        for (SalesRate sr : bean.getDealerRateTypes()) {
            if (stype.equals(sr.getBusinessTypeCode())) {
                List<RateType> rateTypes = sr.getRateTypeList().get(0).getRateTypeList();
                carDealerRate.setBankRates(rateTypes);
                break;
            }
        }
        if (bean.getServiceFeeEntityList() != null) {
            for (ServiceFee sr : bean.getServiceFeeEntityList()) {
                if (stype.equals(sr.getBusinessType())) {
                    carDealerRate.setServiceRates(sr.getRateTypeList());
                    break;
                }
            }
        }

        return ResultBean.getSucceed().setD(carDealerRate);

    }

    /**
     * 【PAD - API】--保存新增渠道中账户信息接口
     *
     * @param cardealerId
     * @param pindex      序号，-1为新建，否则为编辑
     * @param payAccount  账号信息
     * @return
     */
    @RequestMapping(value = "/cardealer/{cardealerid}/payaccount/{pindex}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCarDealerPayAccount(@PathVariable("cardealerid") String cardealerId, @PathVariable("pindex") Integer pindex, @RequestBody PayAccountBean payAccount) {
        return iCarDealerBizService.actSaveCarDealerPayAccount(cardealerId, pindex, payAccount);
    }


    /**
     * 【PAD - API】-更新渠道分期经理
     *
     * @param cardealerId
     * @param employeeids
     * @returnr
     */
    @RequestMapping(value = "/cardealer/{cardealerid}/businessman", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setDealerBuzinessMan(@PathVariable("cardealerid") String cardealerId,@RequestBody List<String> employeeids) {
        return iCarDealerBizService.actSetCarDealerBusinessMans(cardealerId, employeeids);
    }

    @RequestMapping(value = "/cardealer/{cardealerid}/{cartypeid}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean checkDealerCarType(@PathVariable("cardealerid") String cardealerId, @PathVariable("cartypeid") String cartypeid) {
        return iCarDealerBizService.actCheckDealerCarType(cardealerId, cartypeid);
    }


    /**
     * 【PAD - API】--获取渠道数据里面的账户列表
     *
     * @param cardealerId
     * @returnr
     */
    @RequestMapping(value = "/cardealer/{cardealerid}/payaccounts", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getPayAccounts(@PathVariable("cardealerid") String cardealerId) {
        ResultBean resultBean = iCarDealerBizService.actGetOneCarDealer(cardealerId);
        if (resultBean.isSucceed()) {
            CarDealerBean bean = (CarDealerBean) resultBean.getD();
            if (bean != null) {
                return ResultBean.getSucceed().setD(bean.getPayAccounts());
            } else {
                return ResultBean.getFailed().setM("获取账号数据失败！");
            }
        }
        return resultBean;
    }


    /**
     * 【PAD - API】--获取渠道分组
     * @returnr
     */
    @RequestMapping(value = "/cardealer/groups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getGroups() {
        return iCarDealerBizService.actLookupDealerGroup();
    }


    /**
     * 【PAD - API】--删除渠道所属员工接口  逻辑删除
     *
     * @param employeeId
     * @returnr
     */
    @RequestMapping(value = "/cardealer/{employeeid}/dealeremployee", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteDealerEmployee(@PathVariable("employeeid") String employeeId) {
        return iCarDealerBizService.actDeleteDealerEmployee(employeeId);
    }

    /**
     * 【PAD - API】--删除渠道账号接口  逻辑删除
     *
     * @param dealerId
     * @param pIndex
     * @returnr
     */
    @RequestMapping(value = "/cardealer/{dealerid}/payaccount/{pindex}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteDealerEmployee(@PathVariable("dealerid") String dealerId, @PathVariable("pindex") Integer pIndex) {
        return iCarDealerBizService.actDeleteCarDealerPayAccount(dealerId, pIndex);
    }


    /**
     * 【PAD API】--根据渠道Id得到销售人员列表
     * 获取指定4S店的员工列表
     *
     * @param employeeId
     * @return
     */
    @RequestMapping(value = "/dealeremployee/{employeeid}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetDealerEmployeeById(@PathVariable("employeeid") String employeeId) {
        return iCarDealerBizService.actGetDealerEmployeeById(employeeId);
    }



}
