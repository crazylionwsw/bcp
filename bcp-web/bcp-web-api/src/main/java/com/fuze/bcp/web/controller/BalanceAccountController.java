package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.statistics.bean.BalaceAccountBean;
import com.fuze.bcp.api.statistics.bean.BalanceAccountExport;
import com.fuze.bcp.api.statistics.bean.ChargeFeePlanErrorExport;
import com.fuze.bcp.api.statistics.service.IBalanceAccountBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by GQR on 2017/11/7.
 */
@RestController
@RequestMapping(value = "/json")
public class BalanceAccountController {


    @Autowired
    IBalanceAccountBizService iBalanceAccountBizService;

    /**
     * 查找列表
     * @param currentPage
     * @param status
     * @return
     */
    @RequestMapping(value = "/balanceaccounts",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DataPageBean<BalaceAccountBean>> getAll(
            @RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
            @RequestParam(value = "status", required = false, defaultValue = "1") int status){
        return iBalanceAccountBizService.actGetAllBalanceAccounts(status,currentPage);
    }

    /**
     * 根据id回显
     *
     */
    @RequestMapping(value = "/balanceaccount/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getBalanceAccountById(@PathVariable("id") String id){
        return iBalanceAccountBizService.actGetOneById(id);
    }

    // 查询详细账单
    @RequestMapping(value = "/balanceaccount/{id}/details",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getBalanceAccountDetailById(@PathVariable("id") String id){
        return iBalanceAccountBizService.actGetOneDetailById(id);
    }


    @RequestMapping(value = "/balanceaccounts/count",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCalculateBalance(@RequestParam(value = "year",required = false) String year,
                                                 @RequestParam(value = "month",required = false) String month ,
                                                 @RequestParam(value = "loginUserId",required = false) String loginUserId ){
        BalaceAccountBean balanceAccount = new BalaceAccountBean();
        balanceAccount.setYear(year);
        balanceAccount.setMonth(month);
        balanceAccount.setLoginUserId(loginUserId);
        return iBalanceAccountBizService.actCalculateBalanceAccount(balanceAccount);
    }

    /**
     * 重算 --根据年月计算某月的结费
     *
     * @return
     */
    @RequestMapping(value = "/balanceaccount/count",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCalculateBalanceAccount(@RequestParam(value = "year",required = false) String year,
                                                @RequestParam(value = "month",required = false) String month ,
                                                 @RequestParam(value = "loginUserId",required = false) String loginUserId ){
        BalaceAccountBean balanceAccount = new BalaceAccountBean();
        balanceAccount.setYear(year);
        balanceAccount.setMonth(month);
         balanceAccount.setLoginUserId(loginUserId);
        return iBalanceAccountBizService.actCalculateBalanceAccounts(year,month,loginUserId);
    }

    /**
     * 后台，保存订单档案和审核信息
     * @param
     * @return
     */
    @RequestMapping(value = "/balanceaccount/info",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveOrder(@RequestBody BalaceAccountBean BalanceAccount, HttpServletRequest request) throws Exception {
        return iBalanceAccountBizService.actSaveBill(BalanceAccount);
    }


    /**
     * 核对信息
     * @param loginUserId
     * @param id
     * @param signInfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/balanceaccount/check",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean check(@RequestParam(value = "loginUserId",required = false) String loginUserId,
                            @RequestParam(value = "id",required = false) String id,
                            @RequestBody SignInfo signInfo) throws Exception {
        return iBalanceAccountBizService.actChecked(loginUserId,id,signInfo);
    }

    /**
     * 取消核对信息
     * @param loginUserId
     * @param id
     * @param signInfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/balanceaccount/uncheck",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean unCheck(@RequestParam(value = "loginUserId",required = false) String loginUserId,
                            @RequestParam(value = "id",required = false) String id,
                            @RequestBody SignInfo signInfo) throws Exception {
        return iBalanceAccountBizService.actUnChecked(loginUserId,id,signInfo);
    }

    /**
     * 导出错误数据
     * @param
     * @param
     * @param response
     * @return
     */
    @RequestMapping(value = "/balanceaccount/export/",method = RequestMethod.GET)
    @ResponseBody
    public void exportError(
            @RequestParam(value = "year") String year,
            @RequestParam(value = "month") String month,
            HttpServletResponse response) throws Exception {
        String fileName = String.format("%s年%s月银行对账总表.xls",year,month);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));

        //调用excel的工具类
        String sheetName = year+"年"+month+"月"+"银行对账总表";
        String titleName = year+"年"+month+"月"+"银行对账总表";
        String[] headers = {"代码行","渠道名称","业务类型","客户姓名","身份证号码","贷款金额","贷款期限","是否贴息","手续费缴纳方式","手续费率","手续费","刷卡日期","返佣比例","本月结费金额"};
        String pattern="yyyy-MM-dd";
        int[]  widths = {15,50,15,25,30,15,10,10,15,20,20,20,20};

        List balanceAccountExports = iBalanceAccountBizService.actExportExcel(year,month).getD();
        if(balanceAccountExports != null && balanceAccountExports.size() > 0){
            ExcelUtil.doExportExcel(sheetName, titleName, headers, widths,balanceAccountExports, response.getOutputStream());
        }
        //ExcelUtil.doExportExcel(sheetName, titleName, headers, widths,chargefeeplan, response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * 开始付款
     * @param loginUserId
     * @param id
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/balanceaccount/pay",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean pay(@RequestParam(value = "loginUserId",required = false) String loginUserId,
                              @RequestParam(value = "id",required = false) String id) throws Exception {
        return iBalanceAccountBizService.actStartPay(loginUserId,id);
    }

    /**
     * 付款完成
     * @param loginUserId
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/balanceaccount/payOver",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean payOver(@RequestParam(value = "loginUserId",required = false) String loginUserId,
                              @RequestParam(value = "id",required = false) String id) throws Exception {
        return iBalanceAccountBizService.actOverPay(loginUserId,id);
    }







}
