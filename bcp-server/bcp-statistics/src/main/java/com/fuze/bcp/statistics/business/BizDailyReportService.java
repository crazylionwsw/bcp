package com.fuze.bcp.statistics.business;

import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.CustomerDemandBean;
import com.fuze.bcp.api.creditcar.bean.DMVPledgeBean;
import com.fuze.bcp.api.creditcar.bean.PickupCarBean;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistryBean;
import com.fuze.bcp.api.creditcar.service.*;
import com.fuze.bcp.api.statistics.service.IDailyReportBizService;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.pdf.PdfUtils;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.utils.ExcelUtil;
import com.fuze.bcp.utils.SimpleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by GQR on 2017/10/28.
 */

/**
 * 日报
 */
@Service
public class BizDailyReportService implements IDailyReportBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizDailyReportService.class);



    @Autowired
    ICustomerDemandBizService iCustomerDemandBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IPickupCarBizService iPickupCarBizService;

    @Autowired
    IOrderBizService iOrderBizService;

    @Autowired
    IAppointPaymentBizService iAppointPaymentBizService;

    @Autowired
    ICarRegistryBizService iCarRegistryBizService;

    @Autowired
    IDmvpledgeBizService iDmvpledgeBizService;

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    MappingService mappingService;


    /**
     * 创建公司当天的日报
     * @param date
     * @return
     */
    @Override
    public ResultBean createSummaryReport(String date) {
        if(date == null){
            date = SimpleUtils.getTodayStr();
        }
        Map map = new HashMap<>();
        //资质（总数，通过，驳回，拒绝，待处理），

       map.put("carDemand",iCustomerDemandBizService.getDailyReport(null,date, new CustomerDemandBean()).getD());

        //签约（总数，通过，驳回，拒绝，待处理）,申请额，批复额，银行手续费总额，贷款服务费总额
        map.put("order",iOrderBizService.getDailyReport(null,date, new PurchaseCarOrderBean()).getD());

        //提车（总数，通过，驳回，待处理）
        map.put("pickupCar",iPickupCarBizService.getDailyReport(null,date, new PickupCarBean()).getD());

        //垫资（总数，完成，驳回，待处理），垫资总额
       map.put("carPaymentBill",iAppointPaymentBizService.getDailyReport(null,date, new AppointPaymentBean()).getD());

        //上牌（总数）CarRegistry。待处理，通过
        map.put("carRegistry",iCarRegistryBizService.getDailyReport(null,date, new CarRegistryBean()).getD());

//        //抵押（待收取资料，登记证代收取，银行抵押合同打印，银行抵押合同盖章，已开始抵押，抵押完成）
        map.put("dmvPledge",iDmvpledgeBizService.getDailyReport(null,date, new DMVPledgeBean()).getD());

//        //卡业务处理（待制卡，制卡，取卡，启卡，调额，刷卡，领卡），刷卡总额
//        //  计算 刷卡总额
        map.put("bankCardApply",iBankCardApplyBizService.getDailyReport(null,date, new BankCardApplyBean()).getD());
//        *//**业务提示**//*
//        //资质通过，N天之内未签约的客户，按照资质通过日期排序
      //  map.put("Warning_CarDemand",iCustomerDemandBizService.getWarningData().getD());

//        //签约通过，N天之内未提车的客户，按照签约通过日期排序
//        //签约通过，N天之内未抵押的客户，按照签约通过日期排序
//        //签约通过，N天之内未刷卡的客户，按照签约通过日期排序
   //     map.put("Warning_Order",iOrderBizService.getWarningData().getD());

//        //垫资完成，N天之内未刷卡的客户，按照垫资支付日期排序
//        //垫资完成，N天之内未提车上牌的客户，按照垫资支付日期排序
  //     map.put("Warning_Payment",iAppointPaymentBizService.getWarningData().getD());


//        //提车通过，N天之内未上牌的客户，按照提车通过日期排序
   //     map.put("Warning_PickupCar",iPickupCarBizService.getWarningData().getD());
//
//        //资质驳回，未再次提交的客户，按照驳回日期排序
        map.put("Reapply_CarDemand",iCustomerDemandBizService.getAllCustomerByApproveStatus(ApproveStatus.APPROVE_REAPPLY).getD());
//        //签约驳回，未再次提交的客户，按照驳回日期排序
        map.put("Reapply_Order",iOrderBizService.getAllCustomerByApproveStatus(ApproveStatus.APPROVE_REAPPLY).getD());
//        //垫资驳回，未再次提交的客户，按照驳回日期排序
        map.put("Reapply_Payment",iAppointPaymentBizService.getAllCustomerByApproveStatus(ApproveStatus.APPROVE_REAPPLY).getD());
//        //提车驳回，未再次提交的客户，按照驳回日期排序
    //    map.put("Reapply_PickupCar",iPickupCarBizService.getAllCustomerByApproveStatus(ApproveStatus.APPROVE_REAPPLY).getD());


        return ResultBean.getSucceed().setD(map);
    }

    @Override
    public ResultBean createEmployeeSummartReport(String date, String employeeId) {
        return null;
    }



    @Override
    public ResultBean createOrginfoSummaryReport(String date, String orginfoId) {
        return null;
    }

    @Override
    public ResultBean<String> updateAllBillApproveData() {
        return null;
    }

    @Override
    public ResultBean<Map<String, Map<String, Object>>> summaryTotalByChannelCashSourceId(String cashSourceId, String settleCashSourceId, String startDate, String endDate) {
        return null;
    }

    /**
     * 根據渠道生成excel表格
     * @param startDate
     * @param endDate
     * @param cashSourceId
     * @return
     */
    public ResultBean<String> createExcelSummaryTotalByChannelCashSourceId(String cashSourceId,String settleCashSourceId,String startDate, String endDate){
        String[] headers = {"渠道", "申请笔数", "批复笔数", "放贷笔数", "申请额", "批复额", "放贷额", "申请手续费额", "批复手续费额", "放贷手续费额"};
        int[] widths = {50, 10, 10, 10, 20, 20, 20, 20, 20, 20};
        Map<String,Map<String, Object>> mapData  = summaryTotalByChannelCashSourceId(cashSourceId,settleCashSourceId,startDate,endDate).getD();

        logger.info(String.format("总计有【%s】个数据需要导出。", mapData.size()));

        Iterator<String> carDealerIds =  mapData.keySet().iterator();
        List<List<Object>> dataset = new ArrayList<List<Object>>();
        while (carDealerIds.hasNext()) {
            String carDealerId = carDealerIds.next();

            List<Object> rowData = new ArrayList<Object>();
            CarDealerBean carDealer =iCarDealerBizService.actGetOneCarDealer(carDealerId).getD();
            rowData.add(carDealer.getName());
            Map<String,Object>  rowMap = mapData.get(carDealer.getId());
            rowData.add(rowMap.get("totalCount"));
            rowData.add(rowMap.get("totalPassCount"));
            rowData.add(rowMap.get("totalCardCount"));

            rowData.add(rowMap.get("totalAmount"));
            rowData.add(rowMap.get("totalPassAmount"));
            rowData.add(rowMap.get("totalCardAmount"));

            rowData.add(rowMap.get("totalChargeAmount"));
            rowData.add(rowMap.get("totalPassChargeAmount"));
            rowData.add(rowMap.get("totalCardChargeAmount"));
            //增加行
            dataset.add(rowData);
        }
        String path = "/Volumes/APP/temp/支行渠道数据统计.xls";
        try {
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            ExcelUtil.doExportExcel("支行渠道数据统计", "支行渠道数据统计", headers, widths, dataset, new FileOutputStream(file));
        } catch (Exception ex) {
            logger.error("生成Excel出错！", ex);
        }
        return ResultBean.getSucceed().setD(path);
    }

    @Override
    public ResultBean<String> createSummaryReportPdf(OutputStream outputStream, String date) {
        Map map = (Map)createSummaryReport(date).getD();
        map.put("date",date);
        try {
            PdfUtils.generateToFile("dailyreport.ftl", map, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * \根据登录用户获取各自的交易信息
     * @param date
     * @param loginUserId
     * @return
     */
    @Override
    public ResultBean createLoginUserSummartReport(String date, String loginUserId) {
        return null;
    }

}
