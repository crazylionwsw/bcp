package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.BusinessExcelBean;
import com.fuze.bcp.api.creditcar.bean.CompensatoryExcelBean;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.customer.bean.SurveyOption;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerSurveyTemplateBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zqw on 2017/8/11.
 */
@RestController
@RequestMapping(value = "/json")
public class CustomerTransactionController {

    @Autowired
    private ICustomerTransactionBizService iCustomerTransactionBizService;
    @Autowired
    private ICustomerSurveyTemplateBizService iCustomerSurveyTemplateBizService;
    @Autowired
    private ICustomerBizService iCustomerBizService;
    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    /**
     * 保存客户交易信息
     *
     * @param customerTransactionBean
     * @return
     */
    @RequestMapping(value = "/customertransaction", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCustomerTransaction(@RequestBody CustomerTransactionBean customerTransactionBean) {
        return iCustomerTransactionBizService.actSaveCustomerTransaction(customerTransactionBean);
    }

    /**
     * 查询单条客户交易信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/customertransaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerTransactionById(@PathVariable("id") String id) {
        return iCustomerTransactionBizService.actFindCustomerTransactionById(id);
    }

    /**
     * 获取调查问卷
     * @param id
     * @return
     */
    @RequestMapping(value = "/customertransaction/{id}/survey", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTransactionSurvey(@PathVariable("id") String id) {
        return iCustomerSurveyTemplateBizService.actGetTransactionSurvey(id);
    }

    /**
     * 获取调查问卷结果
     * @param id
     * @return
     */
    @RequestMapping(value = "/customertransaction/{id}/survey/result", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTransactionSurveyResult(@PathVariable("id") String id) {
        return iCustomerSurveyTemplateBizService.actGetTransactionSurveyResult(id);
    }

    /**
     * 保存调查问卷
     * @param id
     * @return
     */
    @RequestMapping(value = "/customertransaction/{id}/survey", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean postTransactionSurvey(@PathVariable("id") String id, @RequestBody List<SurveyOption> results) {
        return iCustomerSurveyTemplateBizService.actSaveTransactionSurvey(id, results);
    }

    /**
     * 获取阶段
     * @param id
     * @return
     */
    @RequestMapping(value = "/customertransaction/{id}/stage", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTransactionStage(@PathVariable("id") String id) {
        return iCustomerTransactionBizService.actGetTransactionStage(id);
    }

    @RequestMapping(value = "/customertransactions/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean searchCustomerTransactions(@RequestBody SearchBean searchBean) {
        return iCustomerTransactionBizService.actSearchCustomerTransactions(searchBean);
    }

    @RequestMapping(value = "/customertransactions/list", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getTransactionsList(
            @RequestBody List<String> ids) {
        return iCustomerTransactionBizService.actGetTransactions(ids);
    }

    /**
     * 获取车
     * @param id
     * @return
     */
    @RequestMapping(value = "/customertransaction/{id}/car", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTransactionCar(@PathVariable("id") String id) {
        return iCustomerBizService.actGetCustomerCarByTransactionId(id);
    }

    /**
     * 获取贷款信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/customertransaction/{id}/loan", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTransactionLoan(@PathVariable("id") String id) {
        return iCustomerBizService.actGetCustomerLoanByTransactionId(id);
    }

    /**
     * TODO:修改实现的位置
     * @param id
     * @return
     */
    @RequestMapping(value = "/customertransaction/{id}/createFileNumer", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean createFileNumber(@PathVariable("id") String id) {
        return iCarTransactionBizService.actCreateFileNumberByTransactionId(id);
    }

    /**
     * 根据条件导出交易数据
     */
    @RequestMapping(value = "/compensatorytransactions/export",method = RequestMethod.GET)
    @ResponseBody
    public void exportCompensatoryTransactionBook(@RequestParam Boolean compensatory,@RequestParam Boolean business,@RequestParam  Boolean nc,@RequestParam  Boolean oc,@RequestParam String swipingCardTime,HttpServletResponse response) throws Exception {
        List<CompensatoryExcelBean> dataSet = iCarTransactionBizService.actExportCompensatoryTransactions(compensatory,business,nc,oc,swipingCardTime).getD();
        String filename = swipingCardTime+"贴息客户刷卡统计表.xlsx";
        response.setContentType("application/octet-stream");
        String sheetName = swipingCardTime+"贴息客户刷卡统计表";
        String titleName = swipingCardTime+"贴息客户刷卡统计表";
        response.setHeader("Content-disposition", "filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
        String[] headers = {"经销商名称","序号","姓名(指标人)", "申请人", "证件类型", "证件号码(指标人)", "交易卡号", "交易检索号", "产品名称", "车架号", "DLR代码",
                "申请日期(批贷日期)", "汽车型号", "汽车销售价格(元)", "首付金额(元)", "分期金额(元)", "分期期数", "备注"};
        String pattern = "yyyy-MM-dd";
        ExcelUtil.exportTransactionExcel(sheetName, titleName, headers,dataSet, pattern, response.getOutputStream(),business,compensatory);
        response.flushBuffer();
    }

    @RequestMapping(value = "/businesstransactions/export",method = RequestMethod.GET)
    @ResponseBody
    public void exportBusinessTransactionBook(@RequestParam Boolean compensatory,@RequestParam Boolean business,@RequestParam  Boolean nc,@RequestParam  Boolean oc,@RequestParam String swipingCardTime,HttpServletResponse response) throws Exception {
        List<BusinessExcelBean> dataSet = iCarTransactionBizService.actExportBusinessTransactions(compensatory,business,nc,oc,swipingCardTime).getD();
        String filename = swipingCardTime+"垫资支付统计表.xlsx";
        response.setContentType("application/octet-stream");
        String sheetName = swipingCardTime+"垫资支付统计表";
        String titleName = swipingCardTime+"垫资支付统计表";
        response.setHeader("Content-disposition", "filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
        String[] headers = {"经销商名称","借款人姓名", "证件号码(借款人)", "批贷日期", "分期金额(元)", "分期期数", "垫资支付日期", "刷卡日期", "汽车型号", "汽车销售价格(元)",
                "贷款服务费(元)", "垫资服务费(元)", "交易卡号", "渠道经理", "分期经理", "业务类型"};
        String pattern = "yyyy-MM-dd";
        ExcelUtil.exportTransactionExcel(sheetName, titleName, headers,dataSet, pattern, response.getOutputStream(),business,compensatory);
        response.flushBuffer();
    }

    @RequestMapping(value = "/customertransaction/{id}/{billTypeCode}/checkimage", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<String> checkTransactionBillImageTypeFileExited(@PathVariable("billTypeCode") String billTypeCode, @PathVariable("id") String id){
        return iCarTransactionBizService.actCheckTransactionBillImageTypeFileExited(billTypeCode, id);
    }


}
