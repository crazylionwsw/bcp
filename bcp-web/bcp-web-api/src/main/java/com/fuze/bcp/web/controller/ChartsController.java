package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.CustomerDemandBean;
import com.fuze.bcp.api.creditcar.bean.DMVPledgeBean;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistryBean;
import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferBean;
import com.fuze.bcp.api.creditcar.service.*;
import com.fuze.bcp.api.statistics.service.IDailyReportBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.utils.SimpleUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by GQR on 2017/11/16.
 */
@RestController
@RequestMapping(value = "/json")
public class ChartsController {

    @Autowired
    IDailyReportBizService iDailyReportBizService;

    @Autowired
    ICustomerDemandBizService iCustomerDemandBizService;

    @Autowired
    IOrderBizService iOrderBizService;

    @Autowired
    IAppointPaymentBizService iAppointPaymentBizService;

    @Autowired
    ICarRegistryBizService iCarRegistryBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    IDmvpledgeBizService iDmvpledgeBizService;

    @Autowired
    private ICarTransferBizService iCarTransferBizService;

    //  得到某部门的数据统计
    @RequestMapping(value = "/orginfo/{orginfoid}/charts",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOrgCharts(@PathVariable(value = "orginfoid") String orginfoid,
                                @RequestParam(value="searchdate",required = false) String searchDate,
                                @RequestParam(value="billTypeCode",required = true) String billTypeCode) {
        if(StringUtils.isEmpty(searchDate)){
            searchDate = SimpleUtils.getShortDate().substring(0,7);
        }
        switch (billTypeCode){
            case "A001":
                return iCustomerDemandBizService.getDailyReport(orginfoid, searchDate, new CustomerDemandBean());
            case "A002":
                return iOrderBizService.getDailyReport(orginfoid, searchDate);
            case "A004":
                return iAppointPaymentBizService.getDailyReport(orginfoid, searchDate, new AppointPaymentBean());
            case "A005":
                return iCarRegistryBizService.getDailyReport(orginfoid, searchDate, new CarRegistryBean());
            case "A008":
                return iDmvpledgeBizService.getDailyReport(orginfoid, searchDate, new DMVPledgeBean());
            case "A023":
                return iCarTransferBizService.getDailyReport(orginfoid, searchDate, new CarTransferBean());
        }
        return ResultBean.getFailed();
    };

    //  得到分期、渠道经理的数据统计
    @RequestMapping(value = "/manager/{employeeId}/charts",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getStageManagerCharts(@PathVariable(value = "employeeId", required = false) String employeeId,
                                        @RequestParam(value="searchdate",required = false) String searchDate,
                                        @RequestParam(value="billTypeCode",required = true) String billTypeCode) {
        if(StringUtils.isEmpty(searchDate)){
            searchDate = SimpleUtils.getShortDate().substring(0,7);
        }
        switch (billTypeCode){
            case "A001":
                return iCustomerDemandBizService.getEmployeeReport(searchDate, new CustomerDemandBean(), employeeId);
            case "A002":
                return iOrderBizService.getEmployeeReport(searchDate, new PurchaseCarOrderBean(), employeeId);
            case "A004":
                return iAppointPaymentBizService.getEmployeeReport(searchDate, new AppointPaymentBean(), employeeId);
            case "A005":
                return iCarRegistryBizService.getEmployeeReport(employeeId,searchDate, new CarRegistryBean());
            case "A008":
                return iDmvpledgeBizService.getEmployeeReport(employeeId, searchDate, new DMVPledgeBean());
            case "A023":
                return iCarTransferBizService.getEmployeeReport(employeeId, searchDate, new CarTransferBean());
        }
        return ResultBean.getFailed();
    };

    //  某月某部门渠道数量统计
    @RequestMapping(value = "/orginfo/{orginfoid}/cardealer/count",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOrgDealerCount(@PathVariable(value = "orginfoid") String orginfoid,
                                         @RequestParam(value="searchdate",required = false) String searchDate) {
        return iCarDealerBizService.actGetChannelCount(searchDate, orginfoid, null);
    }

    //  某月某渠道经理渠道数量统计
    @RequestMapping(value = "/manager/{employeeId}/cardealer/count",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getManagerDealerCount(@PathVariable(value = "employeeId") String employeeId,
                                        @RequestParam(value="searchdate",required = false) String searchDate) {
        return iCarDealerBizService.actGetChannelCount(searchDate,null, employeeId);
    }

}
