package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.businessbook.BusinessBookExcelBean;
import com.fuze.bcp.api.creditcar.service.IBusinessBookBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by ${Liu} on 2017/12/18.
 */
@RestController
@RequestMapping(value = "/json")
public class BusinessBookController {

    Logger logger = LoggerFactory.getLogger(BusinessBookController.class);

    @Autowired
    IBusinessBookBizService iBusinessBookBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;


    //获取列表数据
    @RequestMapping(value = "/businessbooks",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getBusinesssBooks(@RequestParam(value = "selectTime",required = false,defaultValue = "") String selectTime){
        return iBusinessBookBizService.actGetBusinessBooks(selectTime);
    }

    //导出Excel表格
    @RequestMapping(value = "/businessbook/export",method = RequestMethod.GET)
    @ResponseBody
    public void exportBusinessBook(@RequestParam(value = "selectTime",required = false,defaultValue = "") String selectTime,HttpServletResponse response) throws Exception {
        try{
            List<BusinessBookExcelBean> dataSet = iBusinessBookBizService.actExportBusinessBook(selectTime).getD();
            String filename = selectTime+"业务台账.xlsx";
            response.setContentType("application/octet-stream");
            String sheetName = selectTime+"业务台账";
            String titleName = selectTime+"业务台账";
            response.setHeader("Content-disposition", "filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
            int[] widths = {20,20,20,65,20,30,20,20,20,20,20,20,20,25,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20};
            int[] cells = {6,14,15,16};
            String[] headers = {"序号","姓名", "身份证号", "联系方式", "拟购车型", "车价(元)", "4S店", "首付比例", "新/二手车", "征信查询时间",
                    "客户不做原因", "报审时间", "批复时间", "申请金额(元)", "批复金额(含手续费)(元)", "实际贷款(元)", "期限", "手续费分期", "首次还款日",
                    "是否贴息", "手续费(元)", "领卡时间", "卡号", "垫款时间", "垫款金额(元)", "刷卡时间", "刷卡金额(元)", "刷卡地点", "抵押登记时间", "抵押人",
                    "与申请人关系", "车架号", "发动机号","车牌", "车辆登记证编号","银行归档时间", "客户交接时间", "渠道经理", "分期经理", "审查人", "备注"};
            String pattern = "yyyy-MM-dd";

            ExcelUtil.exportExcelBusinessBook(sheetName, titleName, headers,widths,dataSet, pattern, response.getOutputStream());

            response.flushBuffer();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("生成业务台账失败", e);
        }

    }

}
