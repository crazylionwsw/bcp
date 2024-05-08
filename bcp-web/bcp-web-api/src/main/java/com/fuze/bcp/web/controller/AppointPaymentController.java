package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentExcelBean;
import com.fuze.bcp.api.creditcar.service.IAppointPaymentBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 预约垫资
 * Created by zqw on 2017/8/16.
 */
@RestController
@RequestMapping(value = "/json")
public class AppointPaymentController {

    @Autowired
    private IAppointPaymentBizService iAppointPaymentBizService;

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/appointpayment/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<AppointPaymentBean> getOne(@PathVariable("id") String id) {
        return iAppointPaymentBizService.actGetAppointPayment(id);
    }

    /**
     * 签批
     */
    @RequestMapping(value = "/appointpayment/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<AppointPaymentBean> signAppointPayment(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iAppointPaymentBizService.actSignAppointPayment(id, signInfo);
    }

    /**
     * 模糊查询
     * @param searchBean
     * @return
     */
    @RequestMapping(value = "/appointpayments/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<AppointPaymentBean> searchAppointPayments(@RequestBody SearchBean searchBean) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iAppointPaymentBizService.actSearchAppointPayments(userId, searchBean);
    }

    @RequestMapping(value = "/appointpayment/status", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<AppointPaymentBean> saveStatus(@RequestParam(value = "id", required = false)  String id,
                                                     @RequestParam(value = "status", required = false)  Integer status) {
        return iAppointPaymentBizService.actUpdateStatus(id,status);
    }

    /**
     *根据交易Id获取预约垫资信息
     */
    @RequestMapping(value = "/appointpayment/transaction/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAppointPaymentByCustomerTransactionId(@PathVariable("id") String customerTransactionId){
        return iAppointPaymentBizService.actGetAppointPaymentByCustomerTransactionId(customerTransactionId);
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/appointpayments/{carDealerId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<AppointPaymentBean>> getAllCarDealerIds(@PathVariable("carDealerId") String carDealerId) {
        return iAppointPaymentBizService.actGetCustomerTransactionsByCarDealerId(carDealerId);
    }


    @RequestMapping(value = "/appointpayments/export",method = RequestMethod.GET)
    @ResponseBody
    public void exportAppointPayBusinessBook(@RequestParam(value = "selectTime",required = false) String selectTime,HttpServletResponse response) throws Exception {
        List<AppointPaymentExcelBean> paymentExcelBeen = iAppointPaymentBizService.actExportAppointPayBusinessBook(selectTime).getD();
        String filename = selectTime+"财务台账.xlsx";
        response.setContentType("application/octet-stream");
        String sheetName = selectTime+"财务台账";
        String titleName = selectTime+"财务台账";
        response.setHeader("Content-disposition", "filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
        int[] widths = {20,20,20,30,30,20,20,20,20,20,20,20,30,30,16,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20};  //29:应收超基准银行手续费（二手车含1、1.5、2年）

        //分类列
        String[] header_cate = {"系统信息","收款信息","刷卡信息","趸交、分期还款金额","分期银行手续费还款","代存信息（财务填写）"};
        //分类列行数（即合并的行数）
        int[] cate_num = {14,3,4,4,4,2};
        String[] headers = {"序号","报单支行", "渠道支行", "业务类型", "客户姓名【身份证号码】", "分期经理【手机号码】", "还款账号", "垫资金额(元)", "垫资时间", "贷款金额",
                "期限", "点位", "是否分期", "应收基准银行手续费", "应收超基准银行手续费（二手车含1、1.5、2年）", "实收手续费（含代收）", "实收贷款服务费", "分期贷款服务费", "刷卡金额",
                "刷卡点位", "刷卡时间", "刷卡期数", "应还银行手续费", "客户首次还款额", "客户每期还款额", "客户每期还款额（保留整数）", "应还银行手续费-分期", "客户首次还款额", "客户每期还款额", "客户每期还款额（保留整数）",
                "代客户存手续费时间", "代付手续费金额（刷卡）"};
        String pattern = "yyyy-MM-dd";

//        ExcelUtil.exportExcelAppointpayments(sheetName, titleName, headers, widths, paymentExcelBeen, pattern, response.getOutputStream());
        ExcelUtil.exportExcelAppointpaymentsDoubleTitle(sheetName, titleName, headers,header_cate,cate_num, widths, paymentExcelBeen, pattern, response.getOutputStream());

        response.flushBuffer();
    }

}
