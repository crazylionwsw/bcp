package com.fuze.bcp.api.statistics.service;

import com.fuze.bcp.bean.ResultBean;

import java.io.OutputStream;
import java.util.Map;

/**
 * Created by GQR on 2017/10/30.
 */
public interface IDailyReportBizService {

    ResultBean createSummaryReport(String date);


    ResultBean createEmployeeSummartReport(String date, String employeeId);

    ResultBean createLoginUserSummartReport(String date, String loginUserId);


    ResultBean createOrginfoSummaryReport(String date, String orginfoId);

    ResultBean<String> updateAllBillApproveData();

    ResultBean<Map<String, Map<String, Object>>> summaryTotalByChannelCashSourceId(String cashSourceId, String settleCashSourceId, String startDate, String endDate);

    ResultBean<String> createExcelSummaryTotalByChannelCashSourceId(String cashSourceId, String settleCashSourceId, String startDate, String endDate);

    ResultBean<String> createSummaryReportPdf(OutputStream outputStream, String date);

}
