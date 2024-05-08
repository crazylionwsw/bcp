package com.fuze.bcp.web.controller;

/**
 * Created by GQR on 2017/9/20.
 */

import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.statistics.bean.ChargeFeePlanBean;
import com.fuze.bcp.api.statistics.bean.ChargeFeePlanErrorExport;
import com.fuze.bcp.api.statistics.bean.QueryFilter;
import com.fuze.bcp.api.statistics.service.IChargeFeePlanBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.utils.ExcelUtil;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 收款计划表
 */
@RestController
@RequestMapping(value = "/json")
public class ChargeFeePlanController {


    private static final Logger logger = LoggerFactory.getLogger(ChargeFeePlanController.class);


    @Autowired
    IChargeFeePlanBizService iChargeFeePlanBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;




   /**
     * 根据ID回显
     * @param
     * @return
     */
    @RequestMapping(value = "/chargefeeplan/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOne(@PathVariable("id") String id){
        return iChargeFeePlanBizService.actGetChargeFeePlanById(id);
    }

    @RequestMapping(value = "/chargefeeplanDetail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOneById(@PathVariable("id") String id){
        return iChargeFeePlanBizService.actFindOneDetailById(id);
    }


    /**
     * 模糊查询
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/chargefeeplans/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean search(@RequestBody SearchBean searchBean){
        ResultBean<ChargeFeePlanBean> chargeFeePlanBeanResultBean = iChargeFeePlanBizService.actSearchChargeFeePlans(searchBean);
        return chargeFeePlanBeanResultBean;
    }

    /**
     * 计算银行结费信息
     * @param year
     * @param month
     * @return
     */
    @RequestMapping(value = "/chargefeeplan/count",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getCalculateBalance(@RequestParam(value = "year",required = false) String year,
                                          @RequestParam(value = "month",required = false) String month,
                                          @RequestBody SignInfo signInfo){
        return iChargeFeePlanBizService.actSyncSwingCard(year+"-"+month,signInfo);
    }

    /**
     * 重算
     * @param id
     * @return
     */
    @RequestMapping(value = "/chargefeeplans/{id}/reset", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean refreshOne(@PathVariable("id") String id,
                                  @RequestBody SignInfo signInfo){
        return iChargeFeePlanBizService.actRecreateChargeFeePlan(id,signInfo);
    }


    /**
     * 核对多条信息
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/chargefeeplan/check", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<List> check(@RequestParam(value = "choseArr", required = false) List<String> choseArr,
                                  @RequestParam(value = "loginUserId", required = false) String loginUserId,
                                  @RequestBody SignInfo signInfo){
        return ResultBean.getSucceed().setD(iChargeFeePlanBizService.actCheck(choseArr,loginUserId,signInfo));
    }
    /**
     * 核对一条信息
     * @param
     * @param signinfo
     * @return
     */
    @RequestMapping(value = "/chargefeeplan/checkOne", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean checkOne(@RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "loginUserId", required = false) String loginUserId,
                                             @RequestBody SignInfo signinfo){
        return ResultBean.getSucceed().setD(iChargeFeePlanBizService.actCheckOne(id,loginUserId,signinfo));
    }


    /**
     * 取消核对一条信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/chargefeeplan/uncheckOne", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean unCheckOne(@RequestParam(value = "id", required = false)  String id,
                                               @RequestBody SignInfo signinfo){
        return iChargeFeePlanBizService.actUncheckOne(id,signinfo);

    }

    /**
     * 取消核对多条信息
     * @param
     * @return
     */
    @RequestMapping(value = "/chargefeeplan/uncheck", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<List> unCheck(@RequestParam(value = "choseArr", required = false)  List<String> choseArr,
                                   @RequestBody SignInfo signinfo){
        return ResultBean.getSucceed().setD(iChargeFeePlanBizService.actUncheck(choseArr,signinfo));

    }

    /**
     *重新生成按照日期
     * @param beforeDate
     * @return
     */
    @RequestMapping(value = "/chargefeeplans/count", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<String> getBefore(@PathVariable("beforeDate") String beforeDate){
        /*ResultBean<String> result = iChargeFeePlanBizService.resyncFailedChargeFeePlan(beforeDate);
        return result;*/
        return  null;
    }
    /**
     *查找错误状态
     * @param status
     * @return
     */
    @RequestMapping(value = "/chargefeeplan/status", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<ChargeFeePlanBean>> getBeforeDate(@RequestParam(value = "status") Integer status){
        ResultBean<List<ChargeFeePlanBean>> result = iChargeFeePlanBizService.actFindErrorStatus(status);
        return  result;
    }

    /**
     * 导出错误数据
     * @param
     * @param response
     * @return
     */
    @RequestMapping(value = "/chargefeeplan/export",method = RequestMethod.GET)
    @ResponseBody
    public void exportError(HttpServletResponse response) throws Exception {
        String filename = "收款计划错误列表.xls";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));

        //调用excel的工具类
        int[] widths = {15, 15, 20, 15, 15, 30, 15, 15, 100};
        String sheetName = "收款计划错误列表";
        String titleName = "收款计划错误列表";
        String[] headers = {"业务类型", "分期经理", "分期经理联系方式", "贴息/商贷", "客户姓名", "身份证号码", "客户联系方式", "签约日期", "数据错误原因"};
        String pattern="yyyy-MM-dd";

        List chargefeeplan  = iChargeFeePlanBizService.actExportFailedExcel().getD();
            if(chargefeeplan.size() > 0){
                //ExcelUtil.exportExcel(sheetName, titleName, headers, chargefeeplan, pattern, response.getOutputStream());
                ExcelUtil.doExportExcel(sheetName, titleName, headers, widths,chargefeeplan, response.getOutputStream());
                response.flushBuffer();
            }
    }

    /**
     * 查找错误数据
     * @return
     */
    @RequestMapping(value = "/chargefeeplan/find",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actFindErrorReport(){
        return iChargeFeePlanBizService.actFindFailedExcel();
    }











}
